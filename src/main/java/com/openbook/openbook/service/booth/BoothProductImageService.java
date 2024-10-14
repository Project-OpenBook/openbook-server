package com.openbook.openbook.service.booth;

import com.openbook.openbook.domain.booth.BoothProduct;
import com.openbook.openbook.domain.booth.BoothProductImage;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
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

    public void modifyReviewImage(List<MultipartFile> add, List<Long> delete, BoothProduct product) {
        int addSize = (add!=null)?add.size():0, deleteSize = (delete!=null)?delete.size():0;
        if(getProductImageCount(product) - deleteSize + addSize > 5) {
            throw new OpenBookException(ErrorCode.EXCEED_MAXIMUM_IMAGE);
        }
        createBoothProductImage(add, product);
        for(int i = 0; i < deleteSize; i++) {
            BoothProductImage image = getProductImageOrException(delete.get(i));
            boothProductImageRepository.delete(image);
        }
    }

    private BoothProductImage getProductImageOrException(long id) {
        return boothProductImageRepository.findById(id).orElseThrow(() ->
                new OpenBookException(ErrorCode.IMAGE_NOT_FOUND)
        );
    }

    private int getProductImageCount(final BoothProduct product) {
        return boothProductImageRepository.countAllByLinkedProductId(product.getId());
    }

}
