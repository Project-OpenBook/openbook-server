package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.controller.request.ProductRegistrationRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/booths/{booth_id}/products")
    public ResponseEntity<ResponseMessage> addProduct(Authentication authentication,
                                                      @PathVariable Long booth_id,
                                                      @Valid ProductRegistrationRequest request){
        managerBoothService.addBoothProduct(Long.valueOf(authentication.getName()), booth_id, request);
        return ResponseEntity.ok(new ResponseMessage("상품 추가에 성공했습니다."));
    }


}
