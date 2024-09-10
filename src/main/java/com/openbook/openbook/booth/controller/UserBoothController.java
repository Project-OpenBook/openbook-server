package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.controller.request.BoothRegistrationRequest;
import com.openbook.openbook.booth.controller.request.BoothReviewRegisterRequest;
import com.openbook.openbook.booth.controller.response.*;
import com.openbook.openbook.booth.service.BoothCommonService;
import com.openbook.openbook.booth.service.common.CommonBoothReviewService;
import com.openbook.openbook.booth.service.common.CommonProductService;
import com.openbook.openbook.booth.service.common.CommonReservationService;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booths")
public class UserBoothController {

    private final BoothCommonService boothCommonService;
    private final CommonProductService commonProductService;
    private final CommonReservationService commonReservationService;
    private final CommonBoothReviewService commonBoothReviewService;

    @PostMapping
    public ResponseEntity <ResponseMessage>  registration(Authentication authentication, @Valid BoothRegistrationRequest request){
        boothCommonService.boothRegistration(Long.valueOf(authentication.getName()), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseMessage("신청 완료 되었습니다."));

    }

    @GetMapping
    public ResponseEntity<SliceResponse<BoothBasicData>> getBooths(@PageableDefault(size = 6)Pageable pageable){
        return ResponseEntity.ok(SliceResponse.of(boothCommonService.getBoothBasicData(pageable)));
    }

    @GetMapping("/{boothId}")
    public ResponseEntity<BoothDetail> getBooth(@PathVariable Long boothId){
        return ResponseEntity.ok(boothCommonService.getBoothDetail(boothId));
    }

    @GetMapping("/{boothId}/notices")
    public ResponseEntity<SliceResponse<BoothNoticeResponse>> getBoothNotices(@PathVariable Long boothId, @PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(SliceResponse.of(boothCommonService.getBoothNotices(boothId, pageable)));
    }

    @GetMapping("/notices/{noticeId}")
    public ResponseEntity<BoothNoticeResponse> getBoothNotice(@PathVariable Long noticeId){
        return ResponseEntity.ok(boothCommonService.getBoothNotice(noticeId));
    }

    @GetMapping("/search")
    public ResponseEntity<SliceResponse<BoothBasicData>> searchBoothName(@RequestParam(value = "type") String searchType,
                                                                         @RequestParam(value = "query", defaultValue = "") String query,
                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "sort", defaultValue = "desc") String sort){
        Slice<BoothBasicData> result = boothCommonService.searchBoothBy(searchType, query, page, sort);
        return ResponseEntity.ok(SliceResponse.of(result));
    }

    @GetMapping("/{booth_id}/product-category")
    public ResponseEntity<List<ProductCategoryResponse>> getProductCategory(@PathVariable Long booth_id) {
        return ResponseEntity.ok(boothCommonService.getProductCategoryResponseList(booth_id));
    }

    @GetMapping("/{booth_id}/products")
    public ResponseEntity<List<CategoryProductsResponse>> getAllBoothProducts(@PathVariable Long booth_id,
                                                                              @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(commonProductService.findAllBoothProducts(booth_id, pageable));
    }

    @GetMapping("/products/category")
    public ResponseEntity<CategoryProductsResponse> getProductsByCategory(@RequestParam Long category_id,
                                                                          @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(commonProductService.findCategoryProducts(category_id, pageable));
    }

    @GetMapping("/{booth_id}/reservations")
    public ResponseEntity<List<BoothReservationsResponse>> getAllBoothReservations(@PathVariable Long booth_id){
        return ResponseEntity.ok(commonReservationService.getAllBoothReservations(booth_id));
    }

    @PostMapping("/review")
    public ResponseEntity<ResponseMessage> postReview(Authentication authentication,
                                                      @Valid BoothReviewRegisterRequest request){
        commonBoothReviewService.registerBoothReview(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("부스 리뷰 작성에 성공했습니다."));
    }
}
