package com.openbook.openbook.basicuser.service;

import com.openbook.openbook.basicuser.dto.LayoutAreaStatusData;
import com.openbook.openbook.basicuser.dto.LayoutAreaCreateData;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.event.dto.EventLayoutAreaStatus;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.repository.EventLayoutAreaRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.openbook.openbook.global.exception.OpenBookException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventLayoutAreaService {

    private final EventLayoutAreaRepository layoutAreaRepository;

    public void createLayoutArea(EventLayout layout, LayoutAreaCreateData areaLine) {
        String classification = areaLine.classification();
        int lineMax = areaLine.maxNumber();
        for(int number=1; number<=lineMax; number++) {
            EventLayoutArea area = EventLayoutArea.builder()
                    .linkedEventLayout(layout)
                    .classification(classification)
                    .number(String.valueOf(number))
                    .build();
            layoutAreaRepository.save(area);
        }
    }

    public Map<String, List<LayoutAreaStatusData>> getAreaStatus(EventLayout layout) {
        List<EventLayoutArea> areas = layoutAreaRepository.findAllByLinkedEventLayout(layout);
        return areas.stream()
                .collect(Collectors.groupingBy(
                        EventLayoutArea::getClassification,
                        Collectors.mapping(
                                e -> new LayoutAreaStatusData(e.getId(), e.getStatus(), e.getNumber()),
                                Collectors.toList()
                        )
                ));
    }

    public boolean hasReservationData(List<Long> eventLayoutAreaList){
        for(Long id : eventLayoutAreaList){
            EventLayoutArea eventLayoutArea = layoutAreaRepository.findById(id).get();
            if(eventLayoutArea.getLinkedBooth() != null){
                return true;
            }
        }
        return false;
    }

    public void boothLocationApplication(List<Long> locations, Booth booth){
        for(Long layoutId : locations){
            EventLayoutArea eventLayoutArea = layoutAreaRepository.findById(layoutId).get();
            if(eventLayoutArea.getStatus().equals(EventLayoutAreaStatus.EMPTY)){
                eventLayoutArea.updateStatus(EventLayoutAreaStatus.WAITING, booth);
            }else{
                throw new OpenBookException(HttpStatus.INTERNAL_SERVER_ERROR, "이미 예약 된 자리 입니다.");
            }
        }
    }

}
