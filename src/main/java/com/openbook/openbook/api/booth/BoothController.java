package com.openbook.openbook.api.booth;


import com.openbook.openbook.api.booth.request.BoothModifyRequest;
import com.openbook.openbook.api.booth.request.BoothRegistrationRequest;
import com.openbook.openbook.api.booth.request.BoothStatusUpdateRequest;
import com.openbook.openbook.api.booth.response.BoothBasicData;
import com.openbook.openbook.api.booth.response.BoothDetail;
import com.openbook.openbook.api.booth.response.BoothManageData;
import com.openbook.openbook.service.booth.BoothService;
import com.openbook.openbook.api.PageResponse;
import com.openbook.openbook.api.ResponseMessage;
import com.openbook.openbook.api.SliceResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/booths")
    public ResponseMessage registration(Authentication authentication,
                                        @Valid BoothRegistrationRequest request){
        boothService.boothRegistration(Long.valueOf(authentication.getName()), request);
        return new ResponseMessage("부스 신청이 완료 되었습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/booths")
    public SliceResponse<BoothBasicData> getBooths(@PageableDefault(size = 6) Pageable pageable,
                                                   @RequestParam(defaultValue = "ALL") String event){
        return SliceResponse.of(boothService.getBooths(pageable, event).map(BoothBasicData::of));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/booths/{boothId}")
    public BoothDetail getBoothDetail(@PathVariable Long boothId){
        return BoothDetail.of(boothService.getBoothById(boothId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manage/booths")
    public SliceResponse<BoothManageData> getManagedBooth(Authentication authentication,
                                                          @PageableDefault(size = 6)Pageable pageable,
                                                          @RequestParam(defaultValue = "ALL") String status){
        return SliceResponse.of(
                boothService.getBoothsByManager(Long.valueOf(authentication.getName()), pageable, status)
                .map(BoothManageData::of)
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/{eventId}/managed/booths")
    public PageResponse<BoothManageData> getBoothManagePage(Authentication authentication,
                                                            @PathVariable Long eventId,
                                                            @RequestParam(defaultValue = "all") String status,
                                                            @PageableDefault(size = 10) Pageable pageable){
        return PageResponse.of(
                boothService.getBoothsOfEvent(status, eventId, pageable, Long.valueOf(authentication.getName()))
                        .map(BoothManageData::of)
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/booths/search")
    public SliceResponse<BoothBasicData> searchBooth(@RequestParam(value = "type") String searchType,
                                                     @RequestParam(value = "query", defaultValue = "") String query,
                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "sort", defaultValue = "desc") String sort){
        return SliceResponse.of(
                boothService.searchBoothBy(searchType, query, page, sort)
                .map(BoothBasicData::of)
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/events/booths/{boothId}/status")
    public ResponseMessage changeBoothStatus(Authentication authentication,
                                             @PathVariable Long boothId,
                                             @RequestBody BoothStatusUpdateRequest request) {
        boothService.changeBoothStatus(boothId, request.boothStatus(), Long.valueOf(authentication.getName()));
        return new ResponseMessage("부스 상태가 변경되었습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/booths/{booth_id}")
    public ResponseMessage modifyBooth(Authentication authentication,
                                       @PathVariable Long booth_id,
                                       @NotNull BoothModifyRequest request){
        boothService.modifyBooth(Long.parseLong(authentication.getName()), booth_id, request);
        return new ResponseMessage("부스 수정에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/booths/{boothId}")
    public ResponseMessage deleteBooth(Authentication authentication,
                                       @PathVariable Long boothId){
        boothService.deleteBooth(Long.valueOf(authentication.getName()), boothId);
        return new ResponseMessage("부스 삭제에 성공했습니다.");
    }

}
