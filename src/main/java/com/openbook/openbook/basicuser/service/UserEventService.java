package com.openbook.openbook.basicuser.service;


import com.openbook.openbook.basicuser.dto.request.EventRegistrationRequest;
import com.openbook.openbook.basicuser.dto.EventLayoutData;
import com.openbook.openbook.basicuser.dto.LayoutAreaData;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.repository.EventRepository;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.repository.UserRepository;
import com.openbook.openbook.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserEventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventLayoutService layoutService;
    private final S3Service s3Service;

    @Transactional
    public void eventRegistration(final Long userId, final EventRegistrationRequest request) {
        User user = getUserOrException(userId);

        dateValidityCheck(request.openDate(), request.closeDate());
        dateValidityCheck(request.boothRecruitmentStartDate(), request.boothRecruitmentEndDate());
        dateValidityCheck(request.boothRecruitmentEndDate(),request.openDate());

        List<LayoutAreaData> areaData = getLayoutAreaList(request.areaClassifications(), request.areaMaxNumbers());
        EventLayoutData layoutData = new EventLayoutData(request.layoutType(),request.layoutImages(), areaData);
        EventLayout layout = layoutService.createEventLayout(layoutData);

        Event event = Event.builder()
                .manager(user)
                .name(request.name())
                .location(request.location())
                .description(request.description())
                .mainImageUrl(uploadAndGetS3ImageUrl(request.mainImage()))
                .layout(layout)
                .openDate(request.openDate())
                .closeDate(request.closeDate())
                .boothRecruitmentStartDate(request.boothRecruitmentStartDate())
                .boothRecruitmentEndDate(request.boothRecruitmentEndDate())
                .build();
        eventRepository.save(event);
    }

    private void dateValidityCheck(LocalDate start, LocalDate end) {
        if(start.isAfter(end)) {
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "날짜 입력 오류");
        }
    }

    private List<LayoutAreaData> getLayoutAreaList(List<String> classifications, List<Integer> maxNumbers) {
        if(classifications.size()!=maxNumbers.size()) {
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "배치도 구역 입력 오류");
        }
        return IntStream.range(0, classifications.size())
                .mapToObj( i -> new LayoutAreaData(classifications.get(i), maxNumbers.get(i)))
                .toList();
    }

    private String uploadAndGetS3ImageUrl(MultipartFile image) {
        String imageName = getRandomFileName(image);
        s3Service.uploadFileToS3(image, imageName);
        return s3Service.getFileUrlFromS3(imageName);
    }

    private User getUserOrException(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));
    }

    private String getRandomFileName(MultipartFile file) {
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID + file.getOriginalFilename();
    }

}
