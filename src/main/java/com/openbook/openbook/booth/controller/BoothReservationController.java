package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.controller.request.ReserveRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothReservationsResponse;
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
    public ResponseEntity<List<BoothReserveManageResponse>> getManagedReservation(Authentication authentication,
                                                                                  @PathVariable Long boothId){
        return ResponseEntity.ok(reservationService.getAllManageReservations(Long.valueOf(authentication.getName()), boothId));
    }

    @PatchMapping("/reserve/{detail_id}")
    public ResponseEntity<ResponseMessage> reservation(Authentication authentication, @PathVariable Long detail_id){
        reservationService.reserveBooth(Long.valueOf(authentication.getName()), detail_id);
        return ResponseEntity.ok(new ResponseMessage("예약 신청이 되었습니다."));
    }

    @GetMapping("/{booth_id}/reservations")
    public ResponseEntity<List<BoothReservationsResponse>> getAllBoothReservations(@PathVariable Long booth_id){
        return ResponseEntity.ok(reservationService.getAllBoothReservations(booth_id));
    }
}
