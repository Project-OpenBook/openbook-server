package com.openbook.openbook.booth.service.core;


import com.openbook.openbook.booth.dto.BoothAreaStatusData;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.dto.BoothAreaStatus;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.booth.entity.BoothArea;
import com.openbook.openbook.booth.repository.BoothAreaRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothAreaService {

    private final BoothAreaRepository layoutAreaRepository;

    public BoothArea getAreaOrException(Long areaId){
        return layoutAreaRepository.findById(areaId).orElseThrow(() ->
                new OpenBookException(ErrorCode.AREA_NOT_FOUND)
        );
    }

    public void createLayoutArea(EventLayout layout, String classification, int lineMax) {
        for(int number=1; number<=lineMax; number++) {
            layoutAreaRepository.save(
                    BoothArea.builder()
                            .linkedEventLayout(layout)
                            .classification(classification)
                            .number(String.valueOf(number))
                            .build()
            );
        }
    }

    public void setBoothLocation(List<Long> layoutAreas, Booth booth){
        for(Long layoutAreaId : layoutAreas){
            BoothArea boothArea = getAreaOrException(layoutAreaId);
            boothArea.updateBooth(BoothAreaStatus.WAITING, booth);
        }
    }

    public List<BoothArea> getLayoutAreasByBoothId(Long boothId) {
        return layoutAreaRepository.findAllByLinkedBoothId(boothId);
    }

    public Map<String, List<BoothAreaStatusData>> getLayoutAreaProgress(EventLayout layout) {
        List<BoothArea> areas = layoutAreaRepository.findAllByLinkedEventLayout(layout);
        return areas.stream().collect(
                Collectors.groupingBy(
                        BoothArea::getClassification,
                        Collectors.mapping(
                                e -> new BoothAreaStatusData(e.getId(), e.getStatus(), e.getNumber()),
                                Collectors.toList()
                        )
                ));
    }

}
