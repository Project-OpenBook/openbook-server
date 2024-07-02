package com.openbook.openbook.basicuser.service;

import static com.openbook.openbook.global.util.JsonService.convertJsonToList;

import com.google.gson.Gson;
import com.openbook.openbook.basicuser.dto.EventLayoutCreateData;
import com.openbook.openbook.basicuser.dto.LayoutAreaCreateData;
import com.openbook.openbook.basicuser.dto.LayoutAreaStatusData;
import com.openbook.openbook.basicuser.dto.response.EventLayoutStatus;
import com.openbook.openbook.event.dto.EventLayoutDTO;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.service.EventLayoutService;
import com.openbook.openbook.event.service.LayoutAreaService;
import com.openbook.openbook.global.util.S3Service;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserEventLayoutService {

    private final EventLayoutService eventLayoutService;
    private final LayoutAreaService layoutAreaService;
    private final S3Service s3Service;

    @Transactional
    public EventLayout createEventLayout(EventLayoutCreateData layoutData) {
        String imagesJson = new Gson().toJson(getImageUrlList(layoutData.images()));
        EventLayoutDTO eventLayoutDTO = EventLayoutDTO.builder()
                .type(layoutData.type())
                .imageUrl(imagesJson)
                .build();
        EventLayout eventLayout = eventLayoutService.createEventLayout(eventLayoutDTO);
        for(LayoutAreaCreateData areaData : layoutData.areas()) {
            layoutAreaService.createLayoutArea(eventLayout, areaData.classification(), areaData.maxNumber());
        }
        return eventLayout;
    }


    public EventLayoutStatus getLayoutStatus(EventLayout eventLayout) {
        Map<String, List<LayoutAreaStatusData>> areas = layoutAreaService.getLayoutAreaProgress(eventLayout);
        return new EventLayoutStatus(convertJsonToList(eventLayout.getImageUrl()), eventLayout.getType(), areas);
    }

    private List<String> getImageUrlList(List<MultipartFile> layoutImages) {
        return layoutImages.stream().map(s3Service::uploadFileAndGetUrl).toList();
    }

}
