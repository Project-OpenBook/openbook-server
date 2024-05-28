package com.openbook.openbook.eventmanager;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.repository.BoothRepository;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.repository.EventLayoutAreaRepository;
import com.openbook.openbook.eventmanager.dto.BoothLocationData;
import com.openbook.openbook.eventmanager.dto.BoothMangeData;
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
    public Page<BoothMangeData> getBoothMangeData(String status, Long eventId, Pageable pageable){

        if(status.equals("all")) {
            Page<Booth> booths = boothRepository.findAllBoothByEventId(pageable, eventId);

            return booths.map(this::convertToBoothMangeData);
        }

        Page<Booth> booths = boothRepository.findAllBoothByLinkedEventAndStatus(pageable, eventId, getBoothStatus(status));
        return booths.map(this::convertToBoothMangeData);
    }

    private BoothMangeData convertToBoothMangeData(Booth booth) {
        List<EventLayoutArea> eventLayoutAreas = eventLayoutAreaRepository.findAllByLinkedBoothId(booth.getId());
        List<BoothLocationData> locationData = eventLayoutAreas.stream()
                .map(BoothLocationData::of)
                .collect(Collectors.toList());

        return BoothMangeData.of(booth, locationData);
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
