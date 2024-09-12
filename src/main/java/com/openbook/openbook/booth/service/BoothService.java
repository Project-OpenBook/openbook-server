package com.openbook.openbook.booth.service;


import static com.openbook.openbook.global.util.Formatter.getDateTime;

import com.openbook.openbook.booth.controller.request.BoothRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothAreaData;
import com.openbook.openbook.booth.controller.response.BoothBasicData;
import com.openbook.openbook.booth.controller.response.BoothDetail;
import com.openbook.openbook.booth.controller.response.BoothManageData;
import com.openbook.openbook.booth.entity.BoothArea;
import com.openbook.openbook.booth.entity.dto.BoothAreaStatus;
import com.openbook.openbook.booth.service.dto.BoothDTO;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.repository.BoothRepository;
import com.openbook.openbook.booth.service.dto.BoothTagDTO;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.service.EventService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.global.util.TagUtil;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.entity.dto.AlarmType;
import com.openbook.openbook.user.service.AlarmService;
import com.openbook.openbook.user.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;

    public Booth getBoothOrException(Long boothId){
        return boothRepository.findById(boothId).orElseThrow(() ->
                new OpenBookException(ErrorCode.BOOTH_NOT_FOUND)
        );
    }
    private final BoothProductService boothProductService;
    private final BoothTagService boothTagService;
    private final EventService eventService;
    private final BoothAreaService boothAreaService;
    private final UserService userService;
    private final AlarmService alarmService;
    private final S3Service s3Service;

    @Transactional
    public void boothRegistration(Long userId, BoothRegistrationRequest request){
        User user = userService.getUserOrException(userId);
        Event event = eventService.getEventOrException(request.linkedEvent());
        LocalDateTime open = getDateTime(event.getOpenDate() + " " + request.openTime());
        LocalDateTime close = getDateTime(event.getCloseDate() + " " + request.closeTime());
        dateTimePeriodCheck(open, close, event);
        if(hasReservationData(request.requestAreas())){
            throw new OpenBookException(ErrorCode.ALREADY_RESERVED_AREA);
        }
        BoothDTO boothDTO = BoothDTO.builder()
                .linkedEvent(event)
                .manager(user)
                .name(request.name())
                .description(request.description())
                .mainImageUrl(s3Service.uploadFileAndGetUrl(request.mainImage()))
                .accountBankName(request.accountBankName())
                .accountNumber(request.accountNumber())
                .openTime(open)
                .closeTime(close)
                .build();

        Booth booth = createBooth(boothDTO);
        boothAreaService.setBoothToArea(request.requestAreas(), booth);
        if (request.tags() != null) {
            TagUtil.getValidTagsOrException(request.tags()).forEach(
                    tag ->  boothTagService.createBoothTag(BoothTagDTO.builder()
                            .content(tag)
                            .booth(booth)
                            .build())
            );
        }
        alarmService.createAlarm(user, event.getManager(), AlarmType.BOOTH_REQUEST, booth.getName());
    }


    @Transactional(readOnly = true)
    public Slice<BoothBasicData> getBoothBasicData(Pageable pageable) {
        return getBoothsByStatus(pageable, BoothStatus.APPROVE).map(
                booth -> BoothBasicData.of(
                        booth, booth.getLinkedEvent(), boothTagService.getBoothTag(booth.getId()))
        );
    }

    @Transactional(readOnly = true)
    public BoothDetail getBoothDetail(Long boothId){
        Booth booth = getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        List<BoothAreaData> boothAreaData = boothAreaService.getBoothAreasByBoothId(boothId)
                .stream()
                .map(BoothAreaData::of)
                .collect(Collectors.toList());
        return BoothDetail.of(booth, boothAreaData, boothTagService.getBoothTag(booth.getId()));
    }
    @Transactional(readOnly = true)
    public Page<BoothManageData> getBoothManageData(String status, Long eventId, Pageable pageable, Long userId){
        Event event = eventService.getEventOrException(eventId);
        User user = userService.getUserOrException(userId);

        if(!event.getManager().equals(user)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }

        if(status.equals("all")) {
            return getBoothsByEvent(pageable, eventId).map(this::convertToBoothManageData);
        }
        return getBoothsByEventAndStatus(pageable, eventId, getBoothStatus(status))
                .map(this::convertToBoothManageData);

    }



    @Transactional(readOnly = true)
    public Slice<BoothManageData> getManagedBoothList(Long managerId, Pageable pageable, String status){
        userService.getUserOrException(managerId);
        Slice<Booth> booths = (status.equals("ALL"))
                ? getAllManagedBooths(pageable, managerId)
                : getAllManagedBoothsByStatus(pageable, managerId, BoothStatus.valueOf(status));

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
        Booth booth = getBoothOrException(boothId);
        if(user != booth.getManager()){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if(booth.getStatus().equals(BoothStatus.APPROVE) && (booth.getLinkedEvent().getCloseDate().isAfter(LocalDate.now()))){
            throw new OpenBookException(ErrorCode.UNDELETABLE_PERIOD);
        }
        List<BoothArea> boothAreaList = boothAreaService.getBoothAreasByBoothId(boothId);
        deleteBooth(booth);
        boothAreaService.disconnectBooth(boothAreaList);
    }

    @Transactional
    public void changeBoothStatus(Long boothId, BoothStatus boothStatus, Long userId){
        Booth booth = getBoothOrException(boothId);
        User user = userService.getUserOrException(userId);

        if(!booth.getLinkedEvent().getManager().equals(user)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if(booth.getStatus().equals(boothStatus)){
            throw new OpenBookException(ErrorCode.ALREADY_PROCESSED);
        }

        booth.updateStatus(boothStatus);
        List<BoothArea> boothAreas = boothAreaService.getBoothAreasByBoothId(boothId);

        if(boothStatus.equals(BoothStatus.APPROVE)){
            changeAreaStatus(boothAreas, BoothAreaStatus.COMPLETE);
            boothProductService.createProductCategory("기본", "기본으로 생성되는 카테고리",booth);
            alarmService.createAlarm(user, booth.getManager(), AlarmType.BOOTH_APPROVED, booth.getName());
        } else if (boothStatus.equals(BoothStatus.REJECT)) {
            changeAreaStatus(boothAreas, BoothAreaStatus.EMPTY);
            alarmService.createAlarm(user, booth.getManager(), AlarmType.BOOTH_REJECTED, booth.getName());
        }
    }

    private BoothManageData convertToBoothManageData(Booth booth) {
        List<BoothArea> boothAreas = boothAreaService.getBoothAreasByBoothId(booth.getId());
        List<BoothAreaData> locationData = boothAreas.stream()
                .map(BoothAreaData::of)
                .collect(Collectors.toList());
        return BoothManageData.of(booth, locationData, boothTagService.getBoothTag(booth.getId()));
    }

    private BoothStatus getBoothStatus(String status){
        return switch (status){
            case "waiting" -> BoothStatus.WAITING;
            case "approved" -> BoothStatus.APPROVE;
            case "rejected" -> BoothStatus.REJECT;
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
    }

    private void changeAreaStatus(List<BoothArea> boothAreas, BoothAreaStatus boothAreaStatus){
        for(BoothArea boothArea : boothAreas){
            boothArea.updateStatus(boothAreaStatus);
        }
    }


    @Transactional(readOnly = true)
    public Slice<BoothBasicData> searchBoothBy(String searchType, String name, int page, String sort){
        PageRequest pageRequest = createBoothPageRequest(page, sort, searchType);

        Slice<Booth> booths = switch (searchType){
            case "boothName" ->
                    getBoothByName(pageRequest, name, BoothStatus.APPROVE);
            case "tagName" -> boothTagService.getBoothByTag(pageRequest, name, BoothStatus.APPROVE);
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
        return booths.map(
                booth -> BoothBasicData.of(booth, booth.getLinkedEvent(), boothTagService.getBoothTag(booth.getId()))
        );
    }

    private boolean hasReservationData(List<Long> eventLayoutAreaList){
        for(Long id : eventLayoutAreaList){
            BoothArea boothArea = boothAreaService.getBoothAreaOrException(id);
            if(!boothArea.getStatus().equals(BoothAreaStatus.EMPTY)){
                return true;
            }
        }
        return false;
    }

    private void dateTimePeriodCheck(LocalDateTime open, LocalDateTime close, Event event){
        if(open.isAfter(close)){
            throw new OpenBookException(ErrorCode.INVALID_DATE_RANGE);
        }
        LocalDate now = LocalDate.now();
        if(now.isBefore(event.getBoothRecruitmentStartDate()) || now.isAfter(event.getBoothRecruitmentEndDate())){
            throw new OpenBookException(ErrorCode.INACCESSIBLE_PERIOD);
        }
    }

    private PageRequest createBoothPageRequest(int page, String sort, String searchType) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProperty = searchType.equals("boothName") ? "registeredAt" : "linkedBooth.registeredAt";

        return PageRequest.of(page, 6, Sort.by(direction, sortProperty));
    }

    public Booth createBooth(BoothDTO booth) {
        return boothRepository.save(
          Booth.builder()
                  .linkedEvent(booth.linkedEvent())
                  .manager(booth.manager())
                  .name(booth.name())
                  .description(booth.description())
                  .mainImageUrl(booth.mainImageUrl())
                  .accountBankName(booth.accountBankName())
                  .accountNumber(booth.accountNumber())
                  .openTime(booth.openTime())
                  .closeTime(booth.closeTime())
                  .build()
        );
    }

    public void deleteBooth(Booth booth){boothRepository.delete(booth);}

    public Slice<Booth> getBoothsByStatus(Pageable pageable, BoothStatus status) {
        return boothRepository.findAllByStatus(pageable, status);
    }

    public Page<Booth> getBoothsByEvent(Pageable pageable, Long eventId) {
        return boothRepository.findAllBoothByEventId(pageable, eventId);
    }

    public Page<Booth> getBoothsByEventAndStatus(Pageable pageable, Long eventId, BoothStatus status) {
        return boothRepository.findAllBoothByEventIdAndStatus(pageable, eventId, status);
    }

    public Slice<Booth> getBoothByName(Pageable pageable, String boothName, BoothStatus status) {
        return boothRepository.findAllByNameAndStatus(pageable, boothName, status);
    }

    public Slice<Booth> getAllManagedBooths(Pageable pageable, Long managerId){
        return boothRepository.findAllByManagerId(pageable, managerId);
    }

    public Slice<Booth> getAllManagedBoothsByStatus(Pageable pageable, Long managerId, BoothStatus boothStatus){
        return boothRepository.findAllByManagerIdAndStatus(pageable, managerId, boothStatus);
    }
}
