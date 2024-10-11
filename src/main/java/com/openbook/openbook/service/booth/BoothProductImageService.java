package com.openbook.openbook.service.booth;

import com.openbook.openbook.domain.booth.BoothProduct;
import com.openbook.openbook.domain.booth.BoothProductImage;
import com.openbook.openbook.repository.booth.BoothProductImageRepository;
import com.openbook.openbook.util.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoothProductImageService {

    private final BoothProductImageRepository boothProductImageRepository;
    private final S3Service s3Service;

    public void createBoothProductImage(final List<MultipartFile> images, final BoothProduct product) {
        if(images==null || images.isEmpty()) return;
        images.forEach(image -> boothProductImageRepository.save(
                BoothProductImage.builder()
                        .imageUrl(s3Service.uploadFileAndGetUrl(image))
                        .linkedProduct(product)
                        .build()
        ));
    }

    public List<BoothProductImage> getProductImages(final BoothProduct product) {
        return boothProductImageRepository.findAllByLinkedProductId(product.getId());
    }

}
