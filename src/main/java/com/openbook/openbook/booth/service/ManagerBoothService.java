package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.controller.request.ReservationRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothAreaData;
import com.openbook.openbook.booth.controller.response.BoothManageData;
import com.openbook.openbook.booth.dto.BoothReservationDTO;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothArea;
import com.openbook.openbook.booth.entity.BoothReservation;
import com.openbook.openbook.booth.entity.dto.BoothAreaStatus;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.service.core.*;
import com.openbook.openbook.booth.controller.request.ProductRegistrationRequest;

import com.openbook.openbook.booth.dto.BoothProductDto;
import com.openbook.openbook.booth.entity.BoothProduct;
import com.openbook.openbook.booth.service.core.BoothAreaService;
import com.openbook.openbook.booth.service.core.BoothProductService;
import com.openbook.openbook.booth.service.core.BoothService;
import com.openbook.openbook.booth.service.core.BoothTagService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerBoothService {
    private final S3Service s3Service;
    private final UserService userService;
    private final BoothService boothService;
    private final BoothTagService boothTagService;
    private final BoothAreaService boothAreaService;
    private final BoothReservationService boothReservationService;
    private final BoothReservationDetailService boothReservationDetailService;
    private final BoothProductService boothProductService;


    @Transactional(readOnly = true)
    public Slice<BoothManageData> getManagedBoothList(Long managerId, Pageable pageable, String status){
        userService.getUserOrException(managerId);
        Slice<Booth> booths = (status.equals("ALL"))
                ? boothService.getAllManagedBooths(pageable)
                : boothService.getAllManagedBoothsByStatus(pageable, managerId, BoothStatus.valueOf(status));

        return booths.map(booth -> {
            List<BoothAreaData> boothAreas = boothAreaService.getBoothAreasByBoothId(booth.getId())
                    .stream()
                    .map(BoothAreaData::of)
                    .collect(Collectors.toList());
            return BoothManageData.of(booth, boothAreas, boothTagService.getBoothTag(booth.getId()));
        });
    }

    @Transactional
    public void deleteBooth(Long userId, Long boothId){
        User user = userService.getUserOrException(userId);
        Booth booth = boothService.getBoothOrException(boothId);
        if(user != booth.getManager()){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if(booth.getStatus().equals(BoothStatus.APPROVE) && (booth.getLinkedEvent().getCloseDate().isAfter(LocalDate.now()))){
            throw new OpenBookException(ErrorCode.UNDELETABLE_PERIOD);
        }
        List<BoothArea> boothAreaList = boothAreaService.getBoothAreasByBoothId(boothId);
        boothService.deleteBooth(booth);
        boothAreaService.disconnectBooth(boothAreaList);
    }

    public void addBoothProduct(Long userId, Long boothId, ProductRegistrationRequest request) {
        User user = userService.getUserOrException(userId);
        Booth booth = boothService.getBoothOrException(boothId);
        if(user != booth.getManager()){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if(!booth.getStatus().equals(BoothStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }
        BoothProduct product = boothProductService.createBoothProduct(new BoothProductDto(
                request.name(), request.description(), request.stock(), request.price(), booth)
        );
        if(request.images() != null && !request.images().isEmpty()){
            request.images().forEach(image -> {
                boothProductService.createBoothProductImage(s3Service.uploadFileAndGetUrl(image), product);
            });
        }
    }

    @Transactional
    public void registerReservation(Long userId, ReservationRegistrationRequest request, Long boothId) {
        User user = userService.getUserOrException(userId);
        Booth booth = boothService.getBoothOrException(boothId);

        if (user != booth.getManager()) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }

        if (!booth.getStatus().equals(BoothStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }

        checkAvailableTime(request, booth);

        if (boothReservationService.isExistDate(request.date(), booth)) {
            BoothReservation boothReservation = boothReservationService.getReservationByBootAndDate(request.date(), booth);
            if(boothReservationDetailService.isExistTime(request.reservationDetailLists(), boothReservation)){
                throw new OpenBookException(ErrorCode.ALREADY_RESERVED_SERVICE);
            }
            boothReservationDetailService.createReservationDetail(request.reservationDetailLists(), boothReservation);

        } else {
            BoothReservationDTO boothReservationDTO = BoothReservationDTO.builder()
                    .content(request.content())
                    .date(request.date())
                    .build();
            BoothReservation boothReservation = boothReservationService.createBoothReservation(boothReservationDTO, booth);
            boothReservationDetailService.createReservationDetail(request.reservationDetailLists(), boothReservation);
        }
    }

    private void checkAvailableTime(ReservationRegistrationRequest request, Booth booth){
        for(String time : request.reservationDetailLists()){
            if(booth.getOpenTime().toLocalTime().isAfter(LocalTime.parse(time))
                    || booth.getCloseTime().toLocalTime().isBefore(LocalTime.parse(time))){
                throw new OpenBookException(ErrorCode.UNAVAILABLE_RESERVED_TIME);
            }
        }
    }
}
