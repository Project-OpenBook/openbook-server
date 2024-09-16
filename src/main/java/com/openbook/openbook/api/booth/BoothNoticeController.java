package com.openbook.openbook.api.booth;


import com.openbook.openbook.api.booth.request.BoothNoticeRegisterRequest;
import com.openbook.openbook.api.booth.response.BoothNoticeResponse;
import com.openbook.openbook.service.booth.BoothNoticeService;
import com.openbook.openbook.api.ResponseMessage;
import com.openbook.openbook.api.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoothNoticeController {

    private final BoothNoticeService boothNoticeService;

    @PostMapping("/booths/{boothId}/notices")
    public ResponseEntity<ResponseMessage> postNotice(Authentication authentication,
                                                      @PathVariable Long boothId,
                                                      @Valid BoothNoticeRegisterRequest boothNoticeRegisterRequest){
        boothNoticeService.registerBoothNotice(Long.valueOf(authentication.getName()), boothId, boothNoticeRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("공지 등록에 성공했습니다."));
    }

    @GetMapping("/booths/{boothId}/notices")
    public SliceResponse<BoothNoticeResponse> getBoothNotices(@PathVariable Long boothId, @PageableDefault(size = 5) Pageable pageable){
        return SliceResponse.of(boothNoticeService.getBoothNotices(boothId, pageable).map(BoothNoticeResponse::of));
    }

    @GetMapping("/booths/notices/{noticeId}")
    public ResponseEntity<BoothNoticeResponse> getBoothNotice(@PathVariable Long noticeId){
        return ResponseEntity.ok(BoothNoticeResponse.of(boothNoticeService.getBoothNotice(noticeId)));
    }

}
