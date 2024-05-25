package com.openbook.openbook.basicuser.service;

import com.google.gson.Gson;
import com.openbook.openbook.basicuser.dto.EventLayoutData;
import com.openbook.openbook.basicuser.dto.LayoutAreaData;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.repository.EventLayoutRepository;
import com.openbook.openbook.global.util.S3Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventLayoutService {

    private final EventLayoutRepository layoutRepository;
    private final EventLayoutAreaService areaService;
    private final S3Service s3Service;

    @Transactional
    public EventLayout createEventLayout(EventLayoutData layoutData) {
        String imagesJson = new Gson().toJson(getImageUrlList(layoutData.images()));
        EventLayout eventLayout = EventLayout.builder()
                .type(layoutData.type())
                .imageUrl(imagesJson)
                .build();
        for(LayoutAreaData areaLine : layoutData.areas()) {
            areaService.createLayoutArea(eventLayout, areaLine);
        }
        layoutRepository.save(eventLayout);
        return eventLayout;
    }

    private List<String> getImageUrlList(List<MultipartFile> layoutImages) {
        ArrayList<String> imageUrlList = new ArrayList<>(layoutImages.size());
        for (MultipartFile image : layoutImages) {
            String randomImageName = getRandomFileName(image);
            s3Service.uploadFileToS3(image, randomImageName);
            imageUrlList.add(s3Service.getFileUrlFromS3(randomImageName));
        }
        return imageUrlList;
    }

    private String getRandomFileName(MultipartFile file) {
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID + file.getOriginalFilename();
    }
}
