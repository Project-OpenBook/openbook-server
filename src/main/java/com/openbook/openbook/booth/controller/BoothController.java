package com.openbook.openbook.booth.controller;


import com.openbook.openbook.booth.controller.request.BoothRegistrationRequest;
import com.openbook.openbook.booth.controller.request.BoothStatusUpdateRequest;
import com.openbook.openbook.booth.controller.response.BoothBasicData;
import com.openbook.openbook.booth.controller.response.BoothDetail;
import com.openbook.openbook.booth.controller.response.BoothManageData;
import com.openbook.openbook.booth.service.BoothService;
import com.openbook.openbook.global.dto.PageResponse;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booths")
public class BoothController {

    private final BoothService boothService;

    @PostMapping
    public ResponseEntity<ResponseMessage> registration(Authentication authentication, @Valid BoothRegistrationRequest request){
        boothService.boothRegistration(Long.valueOf(authentication.getName()), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseMessage("신청 완료 되었습니다."));

    }

    @GetMapping
    public ResponseEntity<SliceResponse<BoothBasicData>> getBooths(@PageableDefault(size = 6) Pageable pageable){
        return ResponseEntity.ok(SliceResponse.of(boothService.getBoothBasicData(pageable)));
    }

    @GetMapping("/{boothId}")
    public ResponseEntity<BoothDetail> getBooth(@PathVariable Long boothId){
        return ResponseEntity.ok(boothService.getBoothDetail(boothId));
    }



    @GetMapping("/search")
    public ResponseEntity<SliceResponse<BoothBasicData>> searchBoothName(@RequestParam(value = "type") String searchType,
                                                                         @RequestParam(value = "query", defaultValue = "") String query,
                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "sort", defaultValue = "desc") String sort){
        Slice<BoothBasicData> result = boothService.searchBoothBy(searchType, query, page, sort);
        return ResponseEntity.ok(SliceResponse.of(result));
    }

    @GetMapping("/events/{eventId}/managed/booths")
    public ResponseEntity<PageResponse<BoothManageData>> getBoothManagePage(@RequestParam(defaultValue = "all") String status,
                                                                            @PathVariable Long eventId,
                                                                            @PageableDefault(size = 10) Pageable pageable,
                                                                            Authentication authentication){
        return ResponseEntity.ok(PageResponse.of(
                boothService.getBoothManageData(status, eventId, pageable, Long.valueOf(authentication.getName()))));
    }

    @PutMapping("/events/booths/{boothId}/status")
    public ResponseEntity<ResponseMessage> changeBoothStatus(@PathVariable Long boothId,
                                                             @RequestBody BoothStatusUpdateRequest request, Authentication authentication) {
        boothService.changeBoothStatus(boothId, request.boothStatus(), Long.valueOf(authentication.getName()));
        return ResponseEntity.ok(new ResponseMessage("부스 상태가 변경되었습니다."));
    }

    @GetMapping("manage/booths")
    public ResponseEntity<SliceResponse<BoothManageData>> getManagedBooth(Authentication authentication,
                                                                          @PageableDefault(size = 6)Pageable pageable,
                                                                          @RequestParam(defaultValue = "ALL") String status){
        return ResponseEntity.ok(SliceResponse.of(
                boothService.getManagedBoothList(Long.valueOf(authentication.getName()), pageable, status)));
    }

    @DeleteMapping("/booths/{boothId}")
    public ResponseEntity<ResponseMessage> deleteBooth(Authentication authentication, @PathVariable Long boothId){
        boothService.deleteBooth(Long.valueOf(authentication.getName()), boothId);
        return ResponseEntity.ok(new ResponseMessage("부스를 삭제했습니다."));
    }
}
