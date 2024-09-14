package com.openbook.openbook.service.event;


import com.google.gson.Gson;
import com.openbook.openbook.service.booth.dto.BoothAreaCreateData;
import com.openbook.openbook.service.booth.BoothAreaService;
import com.openbook.openbook.service.event.dto.EventLayoutCreateData;
import com.openbook.openbook.domain.event.EventLayout;
import com.openbook.openbook.repository.event.EventLayoutRepository;
import com.openbook.openbook.util.S3Service;
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
