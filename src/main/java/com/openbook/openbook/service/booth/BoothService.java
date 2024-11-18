package com.openbook.openbook.service.booth;


import static com.openbook.openbook.util.Formatter.getDateTime;

import com.openbook.openbook.api.booth.request.BoothModifyRequest;
import com.openbook.openbook.api.booth.request.BoothRegistrationRequest;
import com.openbook.openbook.domain.booth.dto.BoothAreaStatus;
import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.repository.booth.BoothRepository;
import com.openbook.openbook.service.booth.dto.BoothDto;
import com.openbook.openbook.domain.event.Event;
import com.openbook.openbook.service.booth.dto.BoothUpdateData;
import com.openbook.openbook.service.event.EventService;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.util.S3Service;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.domain.user.dto.AlarmType;
import com.openbook.openbook.service.user.AlarmService;
import com.openbook.openbook.service.user.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
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


    private final EventService eventService;
    private final UserService userService;
    private final AlarmService alarmService;
    private final S3Service s3Service;

    private final BoothAreaService boothAreaService;
    private final BoothTagService boothTagService;
    private final BoothProductCategoryService boothCategoryService;

    private final BoothRepository boothRepository;

    public Booth getBoothOrException(Long boothId){
        return boothRepository.findById(boothId).orElseThrow(() ->
                new OpenBookException(ErrorCode.BOOTH_NOT_FOUND)
        );
    }

    // TODO: 권한 확인 클래스 따로 설정
    private void VerifyUserIsManagerOfBooth(Booth booth, long userId) {
        if(!booth.getManager().getId().equals(userId)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    private void VerifyUserIsManagerOfEvent(Event event, long userId) {
        if(!event.getManager().getId().equals(userId)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    @Transactional
    public void boothRegistration(Long userId, BoothRegistrationRequest request){
        User user = userService.getUserOrException(userId);
        Event event = eventService.getEventOrException(request.linkedEvent());
        LocalDateTime open = getDateTime(event.getOpenDate() + " " + request.openTime());
        LocalDateTime close = getDateTime(event.getCloseDate() + " " + request.closeTime());
        dateTimePeriodCheck(open, close, event);
        if(boothAreaService.hasLinkedBooth(request.requestAreas())){
            throw new OpenBookException(ErrorCode.ALREADY_RESERVED_AREA);
        }
        Booth booth = boothRepository.save(
                Booth.builder()
                        .linkedEvent(event)
                        .manager(user)
                        .name(request.name())
                        .description(request.description())
                        .mainImageUrl(s3Service.uploadFileAndGetUrl(request.mainImage()))
                        .accountBankName(request.accountBankName())
                        .accountNumber(request.accountNumber())
                        .openTime(open)
                        .closeTime(close)
                        .build()
        );
        boothAreaService.setBoothToArea(request.requestAreas(), booth);
        if (request.tags() != null) {
            boothTagService.createBoothTags(request.tags(), booth);
        }
        alarmService.createAlarm(user, event.getManager(), AlarmType.BOOTH_REQUEST, booth.getName());
    }


    @Transactional(readOnly = true)
    public Slice<BoothDto> getBooths(Pageable pageable, String event) {
        if(event.equals("ALL")) {
            return boothRepository.findAllByStatus(pageable, BoothStatus.APPROVE).map(booth ->
                    BoothDto.of(booth, boothAreaService.getBoothAreasByBoothId(booth.getId()))
            );
        }
        return boothRepository.findAllByLinkedEventIdAndStatus(pageable, Long.parseLong(event), BoothStatus.APPROVE)
                .map(booth -> BoothDto.of(booth, boothAreaService.getBoothAreasByBoothId(booth.getId())));
    }

    @Transactional(readOnly = true)
    public BoothDto getBoothById(Long boothId){
        Booth booth = getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        return BoothDto.of(booth, boothAreaService.getBoothAreasByBoothId(boothId));
    }

    @Transactional(readOnly = true)
    public Page<BoothDto> getBoothsOfEvent(String status, Long eventId, Pageable pageable, Long userId){
        Event event = eventService.getEventOrException(eventId);
        VerifyUserIsManagerOfEvent(event, userId);
        Page<Booth> booths = (status.equals("all"))
                ? boothRepository.findAllBoothByEventId(pageable, eventId)
                : boothRepository.findAllBoothByEventIdAndStatus(pageable, eventId, getBoothStatus(status));
        return booths.map(booth -> BoothDto.of(booth, boothAreaService.getBoothAreasByBoothId(booth.getId())));
    }

    @Transactional(readOnly = true)
    public Slice<BoothDto> getBoothsByManager(Long managerId, Pageable pageable, String status){
        Slice<Booth> booths = (status.equals("ALL"))
                ? boothRepository.findAllByManagerId(pageable, managerId)
                : boothRepository.findAllByManagerIdAndStatus(pageable, managerId, BoothStatus.valueOf(status));
        return booths.map(booth ->
             BoothDto.of(booth, boothAreaService.getBoothAreasByBoothId(booth.getId())));
    }

    @Transactional
    public void modifyBooth(long userId, long boothId, BoothModifyRequest request){
        Booth booth = getBoothOrException(boothId);
        Event event = eventService.getEventOrException(booth.getLinkedEvent().getId());
        VerifyUserIsManagerOfBooth(booth, userId);

        LocalDateTime open = getDateTime(event.getOpenDate() + " " + request.openTime());
        LocalDateTime close = getDateTime(event.getCloseDate() + " " + request.closeTime());
        dateTimePeriodCheck(open, close, event);

        booth.updateBooth(BoothUpdateData.builder()
                .name(request.name())
                .description(request.description())
                .openTime(open)
                .closeTime(close)
                .mainImage(s3Service.uploadFileAndGetUrl(request.mainImage()))
                .accountBankName(request.accountBankName())
                .accountNumber(request.accountNumber())
                .build()
        );

        if(request.tagToAdd() != null || request.tagToDelete() != null){
            boothTagService.modifyBoothTag(request.tagToAdd(), request.tagToDelete(), booth);
        }
    }

    @Transactional
    public void deleteBooth(Long userId, Long boothId){
        Booth booth = getBoothOrException(boothId);
        VerifyUserIsManagerOfBooth(booth, userId);
        if(booth.getStatus().equals(BoothStatus.APPROVE) && (booth.getLinkedEvent().getCloseDate().isAfter(LocalDate.now()))){
            throw new OpenBookException(ErrorCode.UNDELETABLE_PERIOD);
        }
        boothAreaService.disconnectBooth(booth);
        boothRepository.delete(booth);
    }

    @Transactional
    public void changeBoothStatus(Long boothId, BoothStatus boothStatus, Long userId){
        Booth booth = getBoothOrException(boothId);
        User user = userService.getUserOrException(userId);
        VerifyUserIsManagerOfEvent(booth.getLinkedEvent(), userId);
        if(booth.getStatus().equals(boothStatus)){
            throw new OpenBookException(ErrorCode.ALREADY_PROCESSED);
        }
        booth.updateStatus(boothStatus);
        if(boothStatus.equals(BoothStatus.APPROVE)){
            boothAreaService.changeAreaStatusBy(booth, BoothAreaStatus.COMPLETE);
            boothCategoryService.createProductCategory("기본", "기본으로 생성되는 카테고리",booth);
            alarmService.createAlarm(user, booth.getManager(), AlarmType.BOOTH_APPROVED, booth.getName());
        } else if (boothStatus.equals(BoothStatus.REJECT)) {
            boothAreaService.changeAreaStatusBy(booth, BoothAreaStatus.EMPTY);
            alarmService.createAlarm(user, booth.getManager(), AlarmType.BOOTH_REJECTED, booth.getName());
        }
    }

    private BoothStatus getBoothStatus(String status){
        return switch (status){
            case "waiting" -> BoothStatus.WAITING;
            case "approved" -> BoothStatus.APPROVE;
            case "rejected" -> BoothStatus.REJECT;
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
    }

    @Transactional(readOnly = true)
    public Slice<BoothDto> searchBoothBy(String searchType, String name, int page, String sort){
        PageRequest pageRequest = createBoothPageRequest(page, sort, searchType);
        Slice<Booth> booths = switch (searchType){
            case "boothName" -> boothRepository.findAllByNameAndStatus(pageRequest, name, BoothStatus.APPROVE);
            case "tagName" -> boothTagService.getBoothByTag(pageRequest, name, BoothStatus.APPROVE);
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
        return booths.map(booth ->
                BoothDto.of(booth, boothAreaService.getBoothAreasByBoothId(booth.getId()))
        );
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

    //TODO: Utils 로 분리
    private PageRequest createBoothPageRequest(int page, String sort, String searchType) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProperty = searchType.equals("boothName") ? "registeredAt" : "linkedBooth.registeredAt";

        return PageRequest.of(page, 8, Sort.by(direction, sortProperty));
    }


}
