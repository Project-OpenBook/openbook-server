package com.openbook.openbook.service.booth;


import com.openbook.openbook.api.booth.request.ProductModifyRequest;
import com.openbook.openbook.api.booth.request.ProductCategoryRegister;
import com.openbook.openbook.api.booth.request.ProductRegistrationRequest;
import com.openbook.openbook.api.booth.response.BoothProductResponse;
import com.openbook.openbook.api.booth.response.CategoryProductsResponse;
import com.openbook.openbook.api.booth.response.ProductCategoryResponse;
import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothProduct;
import com.openbook.openbook.domain.booth.BoothProductCategory;
import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.repository.booth.BoothProductRepository;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.service.booth.dto.BoothProductUpdateData;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.service.user.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoothProductService {

    private final BoothProductRepository boothProductRepository;

    private final UserService userService;
    private final BoothService boothService;
    private final BoothProductCategoryService categoryService;
    private final BoothProductImageService imageService;


    @Transactional(readOnly = true)
    public List<CategoryProductsResponse> findAllBoothProducts(final long boothId, final Pageable pageable) {
        Booth booth = boothService.getBoothOrException(boothId);
        List<BoothProductCategory> categories = categoryService.getProductCategories(booth);
        List<CategoryProductsResponse> productsList = new ArrayList<>();
        for(BoothProductCategory category : categories) {
            Slice<BoothProductResponse> products = getProductsByCategory(category, pageable).map(
                    boothProduct -> BoothProductResponse.of(
                            boothProduct,
                            imageService.getProductImages(boothProduct)
                    )
            );
            if(products.getNumberOfElements()!=0) {
                productsList.add(CategoryProductsResponse.of(category, products));
            }
        }
        return productsList;
    }


    @Transactional(readOnly = true)
    public List<ProductCategoryResponse> getProductCategoryResponseList(long boothId) {
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        return categoryService.getProductCategories(booth).stream().map(ProductCategoryResponse::of).toList();
    }

    @Transactional(readOnly = true)
    public CategoryProductsResponse findCategoryProducts(final long categoryId, final Pageable pageable) {
        BoothProductCategory category = categoryService.getProductCategoryOrException(categoryId);
        Slice<BoothProductResponse> products = getProductsByCategory(category, pageable).map(
                boothProduct -> BoothProductResponse.of(
                        boothProduct,
                        imageService.getProductImages(boothProduct)
                )
        );
        return CategoryProductsResponse.of(category, products);
    }


    @Transactional
    public void addProductCategory(Long userId, Long boothId, ProductCategoryRegister request) {
        Booth booth = getValidBoothOrException(userId, boothId);
        if(categoryService.getProductCategoryCountBy(booth) > 5) {
            throw new OpenBookException(ErrorCode.EXCEED_MAXIMUM_CATEGORY);
        }
        if(categoryService.isExistsCategoryIn(booth, request.name())) {
            throw new OpenBookException(ErrorCode.ALREADY_EXIST_CATEGORY);
        }
        categoryService.createProductCategory(request.name(), request.description(), booth);
    }

    @Transactional
    public void addBoothProduct(Long userId, Long boothId, ProductRegistrationRequest request) {
        getValidBoothOrException(userId, boothId);
        BoothProduct product = boothProductRepository.save(BoothProduct.builder()
                .name(request.name())
                .description(request.description())
                .stock(request.stock())
                .price(request.price())
                .linkedCategory(categoryService.getProductCategoryOrException(request.categoryId()))
                .build()
        );
        imageService.createBoothProductImage(request.images(), product);
    }

    @Transactional
    public void updateProduct(final long userId, final long productId, final ProductModifyRequest request) {
        BoothProduct product = getBoothProductOrException(productId);
        if(product.getLinkedCategory().getLinkedBooth().getManager().getId()!=userId) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        product.updateProduct(BoothProductUpdateData.builder()
                .name(request.name())
                .description(request.description())
                .stock(request.stock())
                .price(request.price())
                .build()
        );
        categoryService.updateProductCategory(request.categoryId(), product);
        imageService.modifyReviewImage(request.imageToAdd(), request.imageToDelete(), product);
    }

    @Transactional
    public void deleteProduct(final long userId, final long productId) {
        BoothProduct product = getBoothProductOrException(productId);
        if(product.getLinkedCategory().getLinkedBooth().getManager().getId()!=userId) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        boothProductRepository.delete(product);
    }

    @Transactional
    public void deleteCategory(final long userId, final long categoryId, final String deleteProduct) {
        BoothProductCategory category = categoryService.getProductCategoryOrException(categoryId);
        if(category.getLinkedBooth().getManager().getId()!=userId) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        BoothProductCategory defaultCategory = categoryService.getDefaultCategory(category.getLinkedBooth());
        boolean delete = deleteProduct.equals("true");
        boothProductRepository.findAllByLinkedCategoryId(categoryId).forEach(product -> {
            if(delete) {
                boothProductRepository.delete(product);
            } else {
                product.updateCategory(defaultCategory);
            }
        });
        categoryService.deleteProductCategory(category);
    }

    public Slice<BoothProduct> getProductsByCategory(final BoothProductCategory category, final Pageable pageable) {
        return boothProductRepository.findAllByLinkedCategoryId(category.getId(), pageable);
    }

    private Booth getValidBoothOrException(Long userId, Long boothId) {
        User user = userService.getUserOrException(userId);
        Booth booth = boothService.getBoothOrException(boothId);
        if (user != booth.getManager()) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if (!booth.getStatus().equals(BoothStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }
        return booth;
    }

    public BoothProduct getBoothProductOrException(final long productId) {
        return boothProductRepository.findById(productId).orElseThrow(() ->
                new OpenBookException(ErrorCode.PRODUCT_NOT_FOUND)
        );
    }

}
