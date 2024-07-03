package com.openbook.openbook.eventmanager;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.service.BoothService;
import com.openbook.openbook.event.dto.EventLayoutAreaStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.service.EventService;
import com.openbook.openbook.event.service.LayoutAreaService;
import com.openbook.openbook.eventmanager.dto.BoothAreaData;
import com.openbook.openbook.eventmanager.dto.BoothManageData;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventManagerService {

    private final UserService userService;
    private final EventService eventService;
    private final LayoutAreaService layoutAreaService;
    private final BoothService boothService;

    @Transactional(readOnly = true)
    public Page<BoothManageData> getBoothManageData(String status, Long eventId, Pageable pageable, Long userId){
        Event event = eventService.getEventOrException(eventId);
        User user = userService.getUserOrException(userId);

        if(!event.getManager().equals(user)){
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "권한이 존재하지 않습니다.");
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
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "권한이 존재하지 않습니다.");
        }

        if(booth.getStatus().equals(boothStatus)){
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "이미 처리된 상태입니다.");
        }
        booth.updateStatus(boothStatus);
        List<EventLayoutArea> eventLayoutAreas = layoutAreaService.getLayoutAreasByBoothId(boothId);

        if(boothStatus.equals(BoothStatus.APPROVE)){
            changeAreaStatus(eventLayoutAreas, EventLayoutAreaStatus.COMPLETE);
        } else if (boothStatus.equals(BoothStatus.REJECT)) {
            changeAreaStatus(eventLayoutAreas, EventLayoutAreaStatus.EMPTY);
        }

    }

    private BoothManageData convertToBoothManageData(Booth booth) {
        List<EventLayoutArea> eventLayoutAreas = layoutAreaService.getLayoutAreasByBoothId(booth.getId());
        List<BoothAreaData> locationData = eventLayoutAreas.stream()
                .map(BoothAreaData::of)
                .collect(Collectors.toList());

        return BoothManageData.of(booth, locationData);
    }

    private BoothStatus getBoothStatus(String status){
        return switch (status){
            case "waiting" -> BoothStatus.WAITING;
            case "approved" -> BoothStatus.APPROVE;
            case "rejected" -> BoothStatus.REJECT;
            default -> throw new OpenBookException(HttpStatus.BAD_REQUEST, "요청 값이 잘못되었습니다.");
        };
    }

    private void changeAreaStatus(List<EventLayoutArea> eventLayoutAreas, EventLayoutAreaStatus eventLayoutAreaStatus){
        for(EventLayoutArea eventLayoutArea : eventLayoutAreas){
            eventLayoutArea.updateStatus(eventLayoutAreaStatus);
        }
    }

}
