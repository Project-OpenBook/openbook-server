package com.openbook.openbook.booth.service.core;


import com.openbook.openbook.booth.dto.BoothProductDto;
import com.openbook.openbook.booth.entity.BoothProduct;
import com.openbook.openbook.booth.entity.BoothProductImage;
import com.openbook.openbook.booth.repository.BoothProductImageRepository;
import com.openbook.openbook.booth.repository.BoothProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothProductService {

    private final BoothProductRepository boothProductRepository;
    private final BoothProductImageRepository boothProductImageRepository;

    public BoothProduct createBoothProduct(final BoothProductDto boothProduct) {
        return boothProductRepository.save(
                BoothProduct.builder()
                        .name(boothProduct.name())
                        .description(boothProduct.description())
                        .stock(boothProduct.stock())
                        .price(boothProduct.price())
                        .linkedBooth(boothProduct.linkedBooth())
                        .build()
        );
    }

    public void createBoothProductImage(final String imageUrl, final BoothProduct boothProduct) {
        boothProductImageRepository.save(
                BoothProductImage.builder()
                        .imageUrl(imageUrl)
                        .linkedProduct(boothProduct)
                        .build()
        );
    }

}
