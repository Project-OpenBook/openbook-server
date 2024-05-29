package com.openbook.openbook.eventmanager;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.repository.BoothRepository;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.repository.EventLayoutAreaRepository;
import com.openbook.openbook.event.repository.EventRepository;
import com.openbook.openbook.eventmanager.dto.BoothAreaData;
import com.openbook.openbook.eventmanager.dto.BoothManageData;
import com.openbook.openbook.global.exception.OpenBookException;
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

    private final BoothRepository boothRepository;
    private final EventLayoutAreaRepository eventLayoutAreaRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public Page<BoothManageData> getBoothManageData(String status, Long eventId, Pageable pageable, Long userId){
        if(!eventRepository.findById(eventId).get().getManager().getId().equals(userId)){
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "권한이 존재하지 않습니다.");
        }

        if(status.equals("all")) {
            return boothRepository.findAllBoothByEventId(pageable, eventId).map(this::convertToBoothManageData);
        }
        return boothRepository.findAllBoothByEventIdAndStatus(pageable, eventId, getBoothStatus(status)).map(this::convertToBoothManageData);
    }

    private BoothManageData convertToBoothManageData(Booth booth) {
        List<EventLayoutArea> eventLayoutAreas = eventLayoutAreaRepository.findAllByLinkedBoothId(booth.getId());
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
}
