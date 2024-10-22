package com.openbook.openbook.api.booth;

import com.openbook.openbook.api.booth.request.ReserveModifyRequest;
import com.openbook.openbook.api.booth.request.ReserveRegistrationRequest;
import com.openbook.openbook.api.booth.request.ReserveStatusUpdateRequest;
import com.openbook.openbook.api.booth.response.BoothReserveResponse;
import com.openbook.openbook.api.booth.response.BoothReserveManageResponse;
import com.openbook.openbook.service.booth.BoothReservationService;
import com.openbook.openbook.api.ResponseMessage;
import jakarta.validation.Valid;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class BoothReservationController {

    private final BoothReservationService reservationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/booths/{boothId}/reservation")
    public ResponseMessage addReservation(Authentication authentication,
                                          @PathVariable Long boothId,
                                          @Valid ReserveRegistrationRequest request){
        reservationService.addReservation(Long.valueOf(authentication.getName()), request, boothId);
        return new ResponseMessage("예약 추가에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/booths/{booth_id}/reservations")
    public List<BoothReserveResponse> getAllBoothReservations(@PathVariable Long booth_id){
        return reservationService.getReservationsByBooth(booth_id).stream()
                .map(BoothReserveResponse::of)
                .toList();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manage/booths/{boothId}/reservations")
    public List<BoothReserveManageResponse> getManagedReservations(Authentication authentication,
                                                                   @PathVariable Long boothId){
        return reservationService.getAllManageReservations(Long.valueOf(authentication.getName()), boothId)
                .stream()
                .map(BoothReserveManageResponse::of)
                .toList();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/booths/reserve/{detail_id}")
    public ResponseMessage reservation(Authentication authentication,
                                       @PathVariable Long detail_id){
        reservationService.reserveBooth(Long.valueOf(authentication.getName()), detail_id);
        return new ResponseMessage("예약 신청이 되었습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/manage/booths/reserve/{detail_id}")
    public ResponseMessage changeReserveStatus(Authentication authentication,
                                               @PathVariable Long detail_id,
                                               @RequestBody ReserveStatusUpdateRequest request ){
        reservationService.changeReserveStatus(detail_id, request, Long.valueOf(authentication.getName()));
        return new ResponseMessage("예약 상태가 변경되었습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/booth/reserve/{reserve_id}")
    public ResponseMessage modifyReservation(Authentication authentication,
                                             @PathVariable Long reserve_id,
                                             @NotNull ReserveModifyRequest request){
        reservationService.modifyReservation(Long.valueOf(authentication.getName()), reserve_id, request);
        return new ResponseMessage("예약이 변경되었습니다.");
    }

}
