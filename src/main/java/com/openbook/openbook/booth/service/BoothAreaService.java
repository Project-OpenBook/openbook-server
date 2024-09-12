package com.openbook.openbook.booth.service;


import com.openbook.openbook.booth.service.dto.BoothAreaStatusData;
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

    private final BoothAreaRepository boothAreaRepository;

    public BoothArea getBoothAreaOrException(Long areaId){
        return boothAreaRepository.findById(areaId).orElseThrow(() ->
                new OpenBookException(ErrorCode.AREA_NOT_FOUND)
        );
    }

    public void createBoothArea(EventLayout layout, String classification, int lineMax) {
        for(int number=1; number<=lineMax; number++) {
            boothAreaRepository.save(
                    BoothArea.builder()
                            .linkedEventLayout(layout)
                            .classification(classification)
                            .number(String.valueOf(number))
                            .build()
            );
        }
    }

    public void setBoothToArea(List<Long> boothAreas, Booth booth){
        for(Long areaId : boothAreas){
            BoothArea boothArea = getBoothAreaOrException(areaId);
            boothArea.updateBooth(BoothAreaStatus.WAITING, booth);
        }
    }

    public List<BoothArea> getBoothAreasByBoothId(Long boothId) {
        return boothAreaRepository.findAllByLinkedBoothId(boothId);
    }

    public Map<String, List<BoothAreaStatusData>> getBoothAreaProgress(EventLayout layout) {
        List<BoothArea> areas = boothAreaRepository.findAllByLinkedEventLayout(layout);
        return areas.stream().collect(
                Collectors.groupingBy(
                        BoothArea::getClassification,
                        Collectors.mapping(
                                e -> new BoothAreaStatusData(e.getId(), e.getStatus(), e.getNumber()),
                                Collectors.toList()
                        )
                ));
    }

    public void disconnectBooth(List<BoothArea> boothAreas){
        for(BoothArea boothArea : boothAreas){
            boothArea.updateBooth(BoothAreaStatus.EMPTY, null);
        }
    }

}
