package com.openbook.openbook.api.booth;

import com.openbook.openbook.api.booth.request.ProductCategoryRegister;
import com.openbook.openbook.api.booth.request.ProductRegistrationRequest;
import com.openbook.openbook.api.booth.response.CategoryProductsResponse;
import com.openbook.openbook.api.booth.response.ProductCategoryResponse;
import com.openbook.openbook.service.booth.BoothProductService;
import com.openbook.openbook.api.ResponseMessage;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoothProductController {

    private final BoothProductService boothProductService;

    @PostMapping("/booths/{booth_id}/product-category")
    public ResponseEntity<ResponseMessage> addProductCategory(Authentication authentication,
                                                              @PathVariable Long booth_id,
                                                              @Valid ProductCategoryRegister request) {
        boothProductService.addProductCategory(Long.valueOf(authentication.getName()), booth_id, request);
        return ResponseEntity.ok(new ResponseMessage("상품 카테고리 생성에 성공했습니다."));
    }

    @PostMapping("/booths/{booth_id}/products")
    public ResponseEntity<ResponseMessage> addProduct(Authentication authentication,
                                                      @PathVariable Long booth_id,
                                                      @Valid ProductRegistrationRequest request){
        boothProductService.addBoothProduct(Long.valueOf(authentication.getName()), booth_id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("상품 추가에 성공했습니다."));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/booths/products/{product_id}")
    public ResponseMessage deleteProduct(Authentication authentication, @PathVariable Long product_id) {
        boothProductService.deleteProduct(Long.parseLong(authentication.getName()), product_id);
        return new ResponseMessage("상품 삭제에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/booths/product-categories/{category_id}")
    public ResponseMessage deleteProductCategory(Authentication authentication,
                                                 @PathVariable Long category_id,
                                                 @RequestParam(defaultValue = "false") String deleteProducts) {
        return new ResponseMessage("상품 카테고리 삭제에 성공했습니다.");
    }

    @GetMapping("/booths/{booth_id}/product-category")
    public ResponseEntity<List<ProductCategoryResponse>> getProductCategory(@PathVariable Long booth_id) {
        return ResponseEntity.ok(boothProductService.getProductCategoryResponseList(booth_id));
    }

    @GetMapping("/booths/{booth_id}/products")
    public ResponseEntity<List<CategoryProductsResponse>> getAllBoothProducts(@PathVariable Long booth_id,
                                                                              @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(boothProductService.findAllBoothProducts(booth_id, pageable));
    }

    @GetMapping("/booths/products/category")
    public ResponseEntity<CategoryProductsResponse> getProductsByCategory(@RequestParam Long category_id,
                                                                          @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(boothProductService.findCategoryProducts(category_id, pageable));
    }

}
