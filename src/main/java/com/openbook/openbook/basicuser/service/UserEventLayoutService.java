package com.openbook.openbook.basicuser.service;

import static com.openbook.openbook.global.util.JsonService.convertJsonToList;

import com.google.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import com.openbook.openbook.basicuser.dto.EventLayoutCreateData;
import com.openbook.openbook.basicuser.dto.LayoutAreaCreateData;
import com.openbook.openbook.basicuser.dto.LayoutAreaStatusData;
import com.openbook.openbook.basicuser.dto.response.EventLayoutStatus;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.repository.EventLayoutRepository;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserEventLayoutService {

    private final EventLayoutRepository eventLayoutRepository;
    private final UserEventLayoutAreaService layoutAreaService;
    private final S3Service s3Service;

    @Transactional
    public EventLayout createEventLayout(EventLayoutCreateData layoutData) {
        String imagesJson = new Gson().toJson(getImageUrlList(layoutData.images()));
        EventLayout eventLayout = EventLayout.builder()
                .type(layoutData.type())
                .imageUrl(imagesJson)
                .build();
        for(LayoutAreaCreateData areaLine : layoutData.areas()) {
            layoutAreaService.createLayoutArea(eventLayout, areaLine);
        }
        eventLayoutRepository.save(eventLayout);
        return eventLayout;
    }

    public EventLayoutStatus getLayoutStatus(EventLayout eventLayout) {
        Map<String, List<LayoutAreaStatusData>> areas = layoutAreaService.getAreaStatus(eventLayout);
        return new EventLayoutStatus(convertJsonToList(eventLayout.getImageUrl()), eventLayout.getType(), areas);
    }

    private List<String> getImageUrlList(List<MultipartFile> layoutImages) {
        ArrayList<String> imageUrlList = new ArrayList<>(layoutImages.size());
        for (MultipartFile image : layoutImages) {
            imageUrlList.add(s3Service.uploadFileAndGetUrl(image));
        }
        return imageUrlList;
    }

}
