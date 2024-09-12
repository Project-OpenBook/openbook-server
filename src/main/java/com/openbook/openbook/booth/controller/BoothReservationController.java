package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.controller.request.ReserveRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothReserveDetailManageResponse;
import com.openbook.openbook.booth.controller.response.BoothReserveResponse;
import com.openbook.openbook.booth.controller.response.BoothReserveManageResponse;
import com.openbook.openbook.booth.service.BoothReservationService;
import com.openbook.openbook.global.dto.ResponseMessage;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoothReservationController {

    private final BoothReservationService reservationService;

    @PostMapping("booths/{boothId}/reservation")
    public ResponseEntity<ResponseMessage> addReservation(Authentication authentication,
                                                          @Valid ReserveRegistrationRequest request,
                                                          @PathVariable Long boothId){
        reservationService.addReservation(Long.valueOf(authentication.getName()), request, boothId);
        return ResponseEntity.ok(new ResponseMessage("예약 추가에 성공했습니다."));
    }

    @GetMapping("manage/booths/{boothId}/reservations")
    public List<BoothReserveManageResponse> getManagedReservation(Authentication authentication,
                                                                  @PathVariable Long boothId){
        return reservationService.getAllManageReservations(Long.valueOf(authentication.getName()), boothId)
                .stream().map(BoothReserveManageResponse::of).toList();
    }

    @PatchMapping("/reserve/{detail_id}")
    public ResponseEntity<ResponseMessage> reservation(Authentication authentication, @PathVariable Long detail_id){
        reservationService.reserveBooth(Long.valueOf(authentication.getName()), detail_id);
        return ResponseEntity.ok(new ResponseMessage("예약 신청이 되었습니다."));
    }

    @GetMapping("/{booth_id}/reservations")
    public List<BoothReserveResponse> getAllBoothReservations(@PathVariable Long booth_id){
        return reservationService.getReservationsByBooth(booth_id).stream().map(BoothReserveResponse::of).toList();
    }
}
