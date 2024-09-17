package com.openbook.openbook.api.booth;

import com.openbook.openbook.api.booth.request.ReserveRegistrationRequest;
import com.openbook.openbook.api.booth.request.ReserveStatusUpdateRequest;
import com.openbook.openbook.api.booth.response.BoothReserveResponse;
import com.openbook.openbook.api.booth.response.BoothReserveManageResponse;
import com.openbook.openbook.service.booth.BoothReservationService;
import com.openbook.openbook.api.ResponseMessage;
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
import org.springframework.web.bind.annotation.RequestBody;

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

    @PatchMapping("/manage/booths/reserve/{detail_id}")
    public ResponseEntity<ResponseMessage> changeReserveStatus(Authentication authentication,
                                                               @RequestBody ReserveStatusUpdateRequest request,
                                                               @PathVariable Long detail_id){
        reservationService.changeReserveStatus(detail_id, request, Long.valueOf(authentication.getName()));
        return ResponseEntity.ok(new ResponseMessage("예약 상태가 변경되었습니다."));
    }

    @PatchMapping("/booths/reserve/{detail_id}")
    public ResponseEntity<ResponseMessage> reservation(Authentication authentication, @PathVariable Long detail_id){
        reservationService.reserveBooth(Long.valueOf(authentication.getName()), detail_id);
        return ResponseEntity.ok(new ResponseMessage("예약 신청이 되었습니다."));
    }

    @GetMapping("/booths/{booth_id}/reservations")
    public List<BoothReserveResponse> getAllBoothReservations(@PathVariable Long booth_id){
        return reservationService.getReservationsByBooth(booth_id).stream().map(BoothReserveResponse::of).toList();
    }
}
