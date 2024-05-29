package com.openbook.openbook.eventmanager;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.repository.BoothRepository;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.repository.EventLayoutAreaRepository;
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

    @Transactional(readOnly = true)
    public Page<BoothManageData> getBoothManageData(String status, Long eventId, Pageable pageable){

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
            default -> throw new OpenBookException(HttpStatus.BAD_REQUEST, "잘못된 요청 값 입니다.");
        };
    }
}
