package com.openbook.openbook.booth.service.common;

import com.openbook.openbook.booth.controller.response.BoothProductResponse;
import com.openbook.openbook.booth.controller.response.CategoryProductsResponse;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothProductCategory;
import com.openbook.openbook.booth.service.core.BoothProductService;
import com.openbook.openbook.booth.service.core.BoothService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonProductService {

    private final BoothService boothService;
    private final BoothProductService boothProductService;

    @Transactional(readOnly = true)
    public List<CategoryProductsResponse> findAllBoothProducts(final long boothId, final Pageable pageable) {
        Booth booth = boothService.getBoothOrException(boothId);
        List<BoothProductCategory> categories = boothProductService.getProductCategories(booth);
        List<CategoryProductsResponse> productsList = new ArrayList<>();
        for(BoothProductCategory category : categories) {
            Slice<BoothProductResponse> products = boothProductService.getProductsByCategory(category, pageable).map(
                    boothProduct -> BoothProductResponse.of(
                            boothProduct,
                            boothProductService.getProductImages(boothProduct)
                    )
            );
            if(products.getNumberOfElements()!=0) {
                productsList.add(CategoryProductsResponse.of(category, products));
            }
        }
        return productsList;
    }

    @Transactional(readOnly = true)
    public CategoryProductsResponse findCategoryProducts(final long categoryId, final Pageable pageable) {
        BoothProductCategory category = boothProductService.getProductCategoryOrException(categoryId);
        Slice<BoothProductResponse> products = boothProductService.getProductsByCategory(category, pageable).map(
                boothProduct -> BoothProductResponse.of(
                        boothProduct,
                        boothProductService.getProductImages(boothProduct)
                )
        );
        return CategoryProductsResponse.of(category, products);
    }


}
