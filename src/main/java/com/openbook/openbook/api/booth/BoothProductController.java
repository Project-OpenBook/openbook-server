package com.openbook.openbook.api.booth;

import com.openbook.openbook.api.booth.request.ProductModifyRequest;
import com.openbook.openbook.api.booth.request.ProductCategoryRegister;
import com.openbook.openbook.api.booth.request.ProductRegistrationRequest;
import com.openbook.openbook.api.booth.response.CategoryProductsResponse;
import com.openbook.openbook.api.booth.response.ProductCategoryResponse;
import com.openbook.openbook.service.booth.BoothProductService;
import com.openbook.openbook.api.ResponseMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoothProductController {

    private final BoothProductService boothProductService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/booths/{booth_id}/product-category")
    public ResponseMessage addProductCategory(Authentication authentication,
                                              @PathVariable Long booth_id,
                                              @Valid ProductCategoryRegister request) {
        boothProductService.addProductCategory(Long.valueOf(authentication.getName()), booth_id, request);
        return new ResponseMessage("상품 카테고리 생성에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/booths/{booth_id}/products")
    public ResponseMessage addProduct(Authentication authentication,
                                      @PathVariable Long booth_id,
                                      @Valid ProductRegistrationRequest request){
        boothProductService.addBoothProduct(Long.valueOf(authentication.getName()), booth_id, request);
        return new ResponseMessage("상품 추가에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/booths/{booth_id}/products")
    public List<CategoryProductsResponse> getAllBoothProducts(@PathVariable Long booth_id,
                                                              @PageableDefault(size = 5) Pageable pageable) {
        return boothProductService.findAllBoothProducts(booth_id, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/booths/products/category")
    public CategoryProductsResponse getProductsByCategory(@RequestParam Long category_id,
                                                          @PageableDefault(size = 5) Pageable pageable) {
        return boothProductService.findCategoryProducts(category_id, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/booths/{booth_id}/product-category")
    public List<ProductCategoryResponse> getProductCategory(@PathVariable Long booth_id) {
        return boothProductService.getProductCategoryResponseList(booth_id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/booths/products/{product_id}")
    public ResponseMessage modifyProduct(Authentication authentication,
                                         @PathVariable long product_id,
                                         @NotNull ProductModifyRequest request) {
        boothProductService.updateProduct(Long.parseLong(authentication.getName()), product_id, request);
        return new ResponseMessage("상품 수정에 성공했습니다.");
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/booths/products/{product_id}")
    public ResponseMessage deleteProduct(Authentication authentication,
                                         @PathVariable Long product_id) {
        boothProductService.deleteProduct(Long.parseLong(authentication.getName()), product_id);
        return new ResponseMessage("상품 삭제에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/booths/product-categories/{category_id}")
    public ResponseMessage deleteProductCategory(Authentication authentication,
                                                 @PathVariable Long category_id,
                                                 @RequestParam(defaultValue = "false") String deleteProducts) {
        boothProductService.deleteCategory(Long.parseLong(authentication.getName()), category_id, deleteProducts);
        return new ResponseMessage("상품 카테고리 삭제에 성공했습니다.");
    }

}
