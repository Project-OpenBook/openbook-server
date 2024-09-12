package com.openbook.openbook.event.service;


import com.google.gson.Gson;
import com.openbook.openbook.booth.service.dto.BoothAreaCreateData;
import com.openbook.openbook.booth.service.BoothAreaService;
import com.openbook.openbook.event.dto.EventLayoutCreateData;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.repository.EventLayoutRepository;
import com.openbook.openbook.global.util.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventLayoutService {

    private final EventLayoutRepository eventLayoutRepository;

    private final BoothAreaService boothAreaService;
    private final S3Service s3Service;

    @Transactional
    public EventLayout createEventLayout(EventLayoutCreateData layoutData) {
        String imagesJson = new Gson().toJson(getImageUrlList(layoutData.images()));
        EventLayout eventLayout = eventLayoutRepository.save(EventLayout.builder()
                .type(layoutData.type())
                .imageUrl(imagesJson)
                .build()
        );
        for(BoothAreaCreateData areaData : layoutData.areas()) {
            boothAreaService.createBoothArea(eventLayout, areaData.classification(), areaData.maxNumber());
        }
        return eventLayout;
    }


    private List<String> getImageUrlList(List<MultipartFile> layoutImages) {
        return layoutImages.stream().map(s3Service::uploadFileAndGetUrl).toList();
    }


}
