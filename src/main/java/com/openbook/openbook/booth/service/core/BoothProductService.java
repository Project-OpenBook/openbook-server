package com.openbook.openbook.booth.service.core;


import com.openbook.openbook.booth.dto.BoothProductDto;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothProduct;
import com.openbook.openbook.booth.entity.BoothProductCategory;
import com.openbook.openbook.booth.entity.BoothProductImage;
import com.openbook.openbook.booth.repository.BoothProductCategoryRepository;
import com.openbook.openbook.booth.repository.BoothProductImageRepository;
import com.openbook.openbook.booth.repository.BoothProductRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoothProductService {

    private final BoothProductCategoryRepository categoryRepository;
    private final BoothProductRepository boothProductRepository;
    private final BoothProductImageRepository boothProductImageRepository;
    private final S3Service s3Service;

    public BoothProductCategory getProductCategoryOrException(final long id) {
        return categoryRepository.findById(id).orElseThrow(()->
                new OpenBookException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND)
        );
    }

    public void createProductCategory(String categoryName, Booth linkedBooth) {
        categoryRepository.save(
                BoothProductCategory.builder()
                        .name(categoryName)
                        .linkedBooth(linkedBooth)
                        .build()
        );
    }

    public void createBoothProduct(final BoothProductDto boothProduct) {
        BoothProduct product = boothProductRepository.save(
                BoothProduct.builder()
                        .name(boothProduct.name())
                        .description(boothProduct.description())
                        .stock(boothProduct.stock())
                        .price(boothProduct.price())
                        .linkedBooth(boothProduct.linkedBooth())
                        .build()
        );
        if(boothProduct.images()!=null && !boothProduct.images().isEmpty()) {
            boothProduct.images().forEach(imageUrl -> {
                createBoothProductImage(imageUrl, product);
            });
        }
    }

    public void createBoothProductImage(final MultipartFile imageUrl, final BoothProduct boothProduct) {
        boothProductImageRepository.save(
                BoothProductImage.builder()
                        .imageUrl(s3Service.uploadFileAndGetUrl(imageUrl))
                        .linkedProduct(boothProduct)
                        .build()
        );
    }

}
