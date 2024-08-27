package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.controller.request.BoothRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothBasicData;
import com.openbook.openbook.booth.controller.response.BoothDetail;
import com.openbook.openbook.booth.controller.response.CategoryProducts;
import com.openbook.openbook.booth.controller.response.ProductCategoryResponse;
import com.openbook.openbook.booth.controller.response.BoothNoticeResponse;
import com.openbook.openbook.booth.service.BoothCommonService;
import com.openbook.openbook.booth.service.common.CommonProductService;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<BoothDetail> getBooth(Authentication authentication, @PathVariable Long boothId){
        return ResponseEntity.ok(boothCommonService.getBoothDetail(Long.valueOf(authentication.getName()), boothId));
    }

    @GetMapping("/{boothId}/notices")
    public ResponseEntity<SliceResponse<BoothNoticeResponse>> getBoothNotice(@PathVariable Long boothId, @PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(SliceResponse.of(boothCommonService.getBoothNotices(boothId, pageable)));
    }

    @GetMapping("/search")
    public ResponseEntity<SliceResponse<BoothBasicData>> searchBoothName(@RequestParam(value = "type") String searchType,
                                                                         @RequestParam(value = "query") String query,
                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "sort", defaultValue = "desc") String sort){
        return ResponseEntity.ok(SliceResponse.of(boothCommonService.searchBoothBy(searchType, query, page, sort)));
    }

    @GetMapping("/{booth_id}/product-category")
    public ResponseEntity<List<ProductCategoryResponse>> getProductCategory(@PathVariable Long booth_id) {
        return ResponseEntity.ok(boothCommonService.getProductCategoryResponseList(booth_id));
    }

    @GetMapping("/{booth_id}/products")
    public ResponseEntity<List<CategoryProducts>> getAllProductsBy(@PathVariable Long booth_id,
                                                                   @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(commonProductService.findAllBoothProducts(booth_id, pageable));
    }

}
