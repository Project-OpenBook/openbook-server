package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.controller.request.ReservationRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothManageData;
import com.openbook.openbook.booth.service.ManagerBoothService;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ManagerBoothController {
    private final ManagerBoothService managerBoothService;

    @GetMapping("manage/booths")
    public ResponseEntity<SliceResponse<BoothManageData>> getManagedBooth(Authentication authentication,
                                                                          @PageableDefault(size = 6)Pageable pageable,
                                                                          @RequestParam(defaultValue = "ALL") String status){
        return ResponseEntity.ok(SliceResponse.of(
                managerBoothService.getManagedBoothList(Long.valueOf(authentication.getName()), pageable, status)));
    }

    @DeleteMapping("/booths/{boothId}")
    public ResponseEntity<ResponseMessage> deleteBooth(Authentication authentication, @PathVariable Long boothId){
        managerBoothService.deleteBooth(Long.valueOf(authentication.getName()), boothId);
        return ResponseEntity.ok(new ResponseMessage("부스를 삭제했습니다."));
    }

    @PostMapping("booths/{boothId}/reservation")
    public ResponseEntity<ResponseMessage> registerReservation(Authentication authentication,
                                                               @Valid ReservationRegistrationRequest request,
                                                               @PathVariable Long boothId){
        managerBoothService.registerReservation(Long.valueOf(authentication.getName()), request, boothId);
        return ResponseEntity.ok(new ResponseMessage("예약 서비스가 신청었습니다."));
    }
}
