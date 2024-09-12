package com.openbook.openbook.booth.service;


import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothProductCategory;
import com.openbook.openbook.booth.repository.BoothProductCategoryRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothProductCategoryService {

    private final BoothProductCategoryRepository categoryRepository;


    public BoothProductCategory getProductCategoryOrException(final long id) {
        return categoryRepository.findById(id).orElseThrow(()->
                new OpenBookException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND)
        );
    }

    public boolean isExistsCategoryIn(Booth linkedBooth, String name) {
        return categoryRepository.existsByLinkedBoothIdAndName(linkedBooth.getId(), name);
    }

    public int getProductCategoryCountBy(Booth linkedBooth) {
        return categoryRepository.countByLinkedBoothId(linkedBooth.getId());
    }

    public List<BoothProductCategory> getProductCategories(final Booth linkedBooth) {
        return categoryRepository.findAllByLinkedBoothId(linkedBooth.getId());
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
}
