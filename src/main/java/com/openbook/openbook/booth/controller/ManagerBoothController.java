package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.controller.request.BoothNoticeRegisterRequest;
import com.openbook.openbook.booth.controller.request.ProductCategoryRegister;
import com.openbook.openbook.booth.controller.request.ReserveRegistrationRequest;
import com.openbook.openbook.booth.controller.request.ProductRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothManageData;
import com.openbook.openbook.booth.controller.response.BoothReserveManageResponse;
import com.openbook.openbook.booth.service.ManagerBoothService;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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

    @PostMapping("/booths/{booth_id}/product-category")
    public ResponseEntity<ResponseMessage> addProductCategory(Authentication authentication,
                                                              @PathVariable Long booth_id,
                                                              @Valid ProductCategoryRegister request) {
        managerBoothService.addProductCategory(Long.valueOf(authentication.getName()), booth_id, request);
        return ResponseEntity.ok(new ResponseMessage("상품 카테고리 생성에 성공했습니다."));
    }

    @PostMapping("/booths/{booth_id}/products")
    public ResponseEntity<ResponseMessage> addProduct(Authentication authentication,
                                                      @PathVariable Long booth_id,
                                                      @Valid ProductRegistrationRequest request){
        managerBoothService.addBoothProduct(Long.valueOf(authentication.getName()), booth_id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("상품 추가에 성공했습니다."));
    }

    @PostMapping("booths/{boothId}/reservation")
    public ResponseEntity<ResponseMessage> addReservation(Authentication authentication,
                                                               @Valid ReserveRegistrationRequest request,
                                                               @PathVariable Long boothId){
        managerBoothService.addReservation(Long.valueOf(authentication.getName()), request, boothId);
        return ResponseEntity.ok(new ResponseMessage("예약 추가에 성공했습니다."));
    }

    @GetMapping("manage/booths/{boothId}/reservations")
    public ResponseEntity<List<BoothReserveManageResponse>> getManagedReservation(Authentication authentication,
                                                                                  @PathVariable Long boothId){
        return ResponseEntity.ok(managerBoothService.getAllManageReservations(Long.valueOf(authentication.getName()), boothId));
    }

    @PostMapping("/booths/{boothId}/notices")
    public ResponseEntity<ResponseMessage> postNotice(Authentication authentication,
                                                      @PathVariable Long boothId,
                                                      @Valid BoothNoticeRegisterRequest boothNoticeRegisterRequest){
        managerBoothService.registerBoothNotice(Long.valueOf(authentication.getName()), boothId, boothNoticeRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("공지 등록에 성공했습니다."));
    }
}
