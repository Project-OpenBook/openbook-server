package com.openbook.openbook.event.service;

import com.openbook.openbook.event.dto.LayoutAreaData;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.repository.EventLayoutAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventLayoutAreaService {

    private final EventLayoutAreaRepository layoutAreaRepository;

    public void createLayoutArea(EventLayout layout, LayoutAreaData areaLine) {
        String classification = areaLine.classification();
        int lineMax = areaLine.maxNumber();
        for(int number=1; number<=lineMax; number++) {
            EventLayoutArea area = EventLayoutArea.builder()
                    .linkedEventLayout(layout)
                    .type(classification)
                    .number(String.valueOf(number))
                    .build();
            layoutAreaRepository.save(area);
        }
    }

}
