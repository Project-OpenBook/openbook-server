package com.openbook.openbook.service.booth;


import com.openbook.openbook.service.booth.dto.BoothAreaDto;
import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.dto.BoothAreaStatus;
import com.openbook.openbook.domain.event.EventLayout;
import com.openbook.openbook.domain.booth.BoothArea;
import com.openbook.openbook.repository.booth.BoothAreaRepository;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
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

    public boolean hasLinkedBooth(List<Long> targetAreas){
        for(Long id : targetAreas){
            if(!getBoothAreaOrException(id).getStatus().equals(BoothAreaStatus.EMPTY)){
                return true;
            }
        }
        return false;
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

    public void setBoothToArea(List<Long> targetAreas, Booth booth){
        for(Long areaId : targetAreas){
            BoothArea boothArea = getBoothAreaOrException(areaId);
            boothArea.updateStatus(booth, BoothAreaStatus.WAITING);
        }
    }

    public void changeAreaStatusBy(Booth booth, BoothAreaStatus boothAreaStatus){
        for(BoothArea boothArea : boothAreaRepository.findAllByLinkedBoothId(booth.getId())){
            boothArea.updateStatus(booth, boothAreaStatus);
        }
    }

    public void disconnectBooth(Booth booth){
        for(BoothArea boothArea : boothAreaRepository.findAllByLinkedBoothId(booth.getId())){
            boothArea.updateStatus(null, BoothAreaStatus.EMPTY);
        }
    }

    public List<BoothAreaDto> getBoothAreasByBoothId(Long boothId) {
        return boothAreaRepository.findAllByLinkedBoothId(boothId)
                .stream()
                .map(BoothAreaDto::of)
                .toList();
    }

    public Map<String, List<BoothAreaDto>> getBoothAreasOf(EventLayout layout) {
        return boothAreaRepository.findAllByLinkedEventLayoutId(layout.getId())
                .stream()
                .collect(Collectors.groupingBy(
                        BoothArea::getClassification,
                        Collectors.mapping(
                                BoothAreaDto::of,
                                Collectors.toList()
                        )
                ));
    }

}
