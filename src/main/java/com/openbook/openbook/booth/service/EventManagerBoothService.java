package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.service.core.BoothAreaService;
import com.openbook.openbook.booth.service.core.BoothProductService;
import com.openbook.openbook.booth.service.core.BoothService;
import com.openbook.openbook.booth.service.core.BoothTagService;
import com.openbook.openbook.booth.entity.dto.BoothAreaStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.booth.entity.BoothArea;
import com.openbook.openbook.booth.controller.response.BoothAreaData;
import com.openbook.openbook.booth.controller.response.BoothManageData;
import com.openbook.openbook.event.service.core.EventService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.dto.AlarmType;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.core.AlarmService;
import com.openbook.openbook.user.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventManagerBoothService {

    private final UserService userService;
    private final EventService eventService;
    private final BoothAreaService boothAreaService;
    private final BoothService boothService;
    private final BoothTagService boothTagService;
    private final BoothProductService boothProductService;
    private final AlarmService alarmService;

    @Transactional(readOnly = true)
    public Page<BoothManageData> getBoothManageData(String status, Long eventId, Pageable pageable, Long userId){
        Event event = eventService.getEventOrException(eventId);
        User user = userService.getUserOrException(userId);

        if(!event.getManager().equals(user)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }

        if(status.equals("all")) {
            return boothService.getBoothsByEvent(pageable, eventId).map(this::convertToBoothManageData);
        }
        return boothService.getBoothsByEventAndStatus(pageable, eventId, getBoothStatus(status))
                .map(this::convertToBoothManageData);

    }

    @Transactional
    public void changeBoothStatus(Long boothId, BoothStatus boothStatus, Long userId){
        Booth booth = boothService.getBoothOrException(boothId);
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

}
