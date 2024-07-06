package com.openbook.openbook.event.service;


import com.openbook.openbook.basicuser.dto.LayoutAreaStatusData;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.event.dto.EventLayoutAreaStatus;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.repository.EventLayoutAreaRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LayoutAreaService {

    private final EventLayoutAreaRepository layoutAreaRepository;

    public EventLayoutArea getAreaOrException(Long areaId){
        return layoutAreaRepository.findById(areaId).orElseThrow(() ->
                new OpenBookException(ErrorCode.AREA_NOT_FOUND)
        );
    }

    public void createLayoutArea(EventLayout layout, String classification, int lineMax) {
        for(int number=1; number<=lineMax; number++) {
            layoutAreaRepository.save(
                    EventLayoutArea.builder()
                            .linkedEventLayout(layout)
                            .classification(classification)
                            .number(String.valueOf(number))
                            .build()
            );
        }
    }

    public void setBoothLocation(List<Long> layoutAreas, Booth booth){
        for(Long layoutAreaId : layoutAreas){
            EventLayoutArea eventLayoutArea = getAreaOrException(layoutAreaId);
            eventLayoutArea.updateBooth(EventLayoutAreaStatus.WAITING, booth);
        }
    }

    public List<EventLayoutArea> getLayoutAreasByBoothId(Long boothId) {
        return layoutAreaRepository.findAllByLinkedBoothId(boothId);
    }

    public Map<String, List<LayoutAreaStatusData>> getLayoutAreaProgress(EventLayout layout) {
        List<EventLayoutArea> areas = layoutAreaRepository.findAllByLinkedEventLayout(layout);
        return areas.stream().collect(
                Collectors.groupingBy(
                        EventLayoutArea::getClassification,
                        Collectors.mapping(
                                e -> new LayoutAreaStatusData(e.getId(), e.getStatus(), e.getNumber()),
                                Collectors.toList()
                        )
                ));
    }

}
