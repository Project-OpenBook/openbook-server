package com.openbook.openbook.event.service;


import static com.openbook.openbook.global.util.JsonService.convertJsonToList;

import com.google.gson.Gson;
import com.openbook.openbook.booth.service.dto.BoothAreaCreateData;
import com.openbook.openbook.booth.service.dto.BoothAreaDto;
import com.openbook.openbook.booth.service.BoothAreaService;
import com.openbook.openbook.event.controller.response.EventLayoutStatus;
import com.openbook.openbook.event.dto.EventLayoutCreateData;
import com.openbook.openbook.event.dto.EventLayoutDTO;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.repository.EventLayoutRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventLayoutService {

    private final EventLayoutRepository eventLayoutRepository;

    private final EventService eventService;
    private final BoothAreaService boothAreaService;
    private final S3Service s3Service;

    @Transactional
    public EventLayout createEventLayout(EventLayoutCreateData layoutData) {
        String imagesJson = new Gson().toJson(getImageUrlList(layoutData.images()));
        EventLayoutDTO eventLayoutDTO = EventLayoutDTO.builder()
                .type(layoutData.type())
                .imageUrl(imagesJson)
                .build();
        EventLayout eventLayout = createEventLayout(eventLayoutDTO);
        for(BoothAreaCreateData areaData : layoutData.areas()) {
            boothAreaService.createBoothArea(eventLayout, areaData.classification(), areaData.maxNumber());
        }
        return eventLayout;
    }

    @Transactional(readOnly = true)
    public EventLayoutStatus getEventLayoutStatus(Long eventId) {

        Event event = eventService.getEventOrException(eventId);
        if(isNotRecruitmentPeriod(event.getBoothRecruitmentStartDate(), event.getBoothRecruitmentEndDate())) {
            throw new OpenBookException(ErrorCode.INACCESSIBLE_PERIOD);
        }
        EventLayout layout = event.getLayout();
        Map<String, List<BoothAreaDto>> areas = boothAreaService.getBoothAreasOf(layout);
        return new EventLayoutStatus(convertJsonToList(layout.getImageUrl()), layout.getType(), areas);
    }

    private List<String> getImageUrlList(List<MultipartFile> layoutImages) {
        return layoutImages.stream().map(s3Service::uploadFileAndGetUrl).toList();
    }

    private boolean isNotRecruitmentPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();
        return now.isBefore(startDate) || now.isAfter(endDate);
    }

    public EventLayout createEventLayout(final EventLayoutDTO eventLayout) {
        return eventLayoutRepository.save(
                EventLayout.builder()
                        .type(eventLayout.type())
                        .imageUrl(eventLayout.imageUrl())
                        .build()
        );
    }

}
