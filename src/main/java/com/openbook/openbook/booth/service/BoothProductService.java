package com.openbook.openbook.booth.service;


import com.openbook.openbook.booth.controller.request.ProductCategoryRegister;
import com.openbook.openbook.booth.controller.request.ProductRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothProductResponse;
import com.openbook.openbook.booth.controller.response.CategoryProductsResponse;
import com.openbook.openbook.booth.controller.response.ProductCategoryResponse;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothProduct;
import com.openbook.openbook.booth.entity.BoothProductCategory;
import com.openbook.openbook.booth.entity.BoothProductImage;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.repository.BoothProductCategoryRepository;
import com.openbook.openbook.booth.repository.BoothProductImageRepository;
import com.openbook.openbook.booth.repository.BoothProductRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoothProductService {

    private final BoothProductCategoryRepository categoryRepository;
    private final BoothProductRepository boothProductRepository;
    private final BoothProductImageRepository boothProductImageRepository;
    private final S3Service s3Service;

    private final UserService userService;
    private final BoothService boothService;


    public BoothProductCategory getProductCategoryOrException(final long id) {
        return categoryRepository.findById(id).orElseThrow(()->
                new OpenBookException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public List<CategoryProductsResponse> findAllBoothProducts(final long boothId, final Pageable pageable) {
        Booth booth = boothService.getBoothOrException(boothId);
        List<BoothProductCategory> categories = getProductCategories(booth);
        List<CategoryProductsResponse> productsList = new ArrayList<>();
        for(BoothProductCategory category : categories) {
            Slice<BoothProductResponse> products = getProductsByCategory(category, pageable).map(
                    boothProduct -> BoothProductResponse.of(
                            boothProduct,
                            getProductImages(boothProduct)
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
        return getProductCategories(booth).stream().map(ProductCategoryResponse::of).toList();
    }

    @Transactional(readOnly = true)
    public CategoryProductsResponse findCategoryProducts(final long categoryId, final Pageable pageable) {
        BoothProductCategory category = getProductCategoryOrException(categoryId);
        Slice<BoothProductResponse> products = getProductsByCategory(category, pageable).map(
                boothProduct -> BoothProductResponse.of(
                        boothProduct,
                        getProductImages(boothProduct)
                )
        );
        return CategoryProductsResponse.of(category, products);
    }


    @Transactional
    public void addProductCategory(Long userId, Long boothId, ProductCategoryRegister request) {
        Booth booth = getValidBoothOrException(userId, boothId);
        if(getProductCategoryCountBy(booth) > 5) {
            throw new OpenBookException(ErrorCode.EXCEED_MAXIMUM_CATEGORY);
        }
        if(isExistsCategoryIn(booth, request.name())) {
            throw new OpenBookException(ErrorCode.ALREADY_EXIST_CATEGORY);
        }
        createProductCategory(request.name(), request.description(), booth);
    }

    @Transactional
    public void addBoothProduct(Long userId, Long boothId, ProductRegistrationRequest request) {
        getValidBoothOrException(userId, boothId);
        BoothProduct product = boothProductRepository.save(BoothProduct.builder()
                .name(request.name())
                .description(request.description())
                .stock(request.stock())
                .price(request.price())
                .linkedCategory(getProductCategoryOrException(request.categoryId()))
                .build()
        );
        if(request.images()!=null && !request.images().isEmpty()) {
            request.images().forEach(imageUrl -> {
                createBoothProductImage(imageUrl, product);
            });
        }
    }

    public List<BoothProductCategory> getProductCategories(final Booth linkedBooth) {
        return categoryRepository.findAllByLinkedBoothId(linkedBooth.getId());
    }

    public Slice<BoothProduct> getProductsByCategory(final BoothProductCategory category, final Pageable pageable) {
        return boothProductRepository.findAllByLinkedCategoryId(category.getId(), pageable);
    }

    public List<BoothProductImage> getProductImages(final BoothProduct product) {
        return boothProductImageRepository.findAllByLinkedProductId(product.getId());
    }

    public boolean isExistsCategoryIn(Booth linkedBooth, String name) {
        return categoryRepository.existsByLinkedBoothIdAndName(linkedBooth.getId(), name);
    }

    public int getProductCategoryCountBy(Booth linkedBooth) {
        return categoryRepository.countByLinkedBoothId(linkedBooth.getId());
    }

    public void createProductCategory(String categoryName, String description, Booth linkedBooth) {
        categoryRepository.save(
                BoothProductCategory.builder()
                        .name(categoryName)
                        .description(description)
                        .linkedBooth(linkedBooth)
                        .build()
        );
    }

    public void createBoothProductImage(final MultipartFile imageUrl, final BoothProduct boothProduct) {
        boothProductImageRepository.save(
                BoothProductImage.builder()
                        .imageUrl(s3Service.uploadFileAndGetUrl(imageUrl))
                        .linkedProduct(boothProduct)
                        .build()
        );
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

}
