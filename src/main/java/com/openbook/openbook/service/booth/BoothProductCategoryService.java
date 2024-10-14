package com.openbook.openbook.service.booth;


import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothProduct;
import com.openbook.openbook.domain.booth.BoothProductCategory;
import com.openbook.openbook.repository.booth.BoothProductCategoryRepository;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothProductCategoryService {

    private final BoothProductCategoryRepository categoryRepository;
    private final String DEFAULT_NAME = "기본";


    public BoothProductCategory getProductCategoryOrException(final long id) {
        return categoryRepository.findById(id).orElseThrow(()->
                new OpenBookException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND)
        );
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

    public void updateProductCategory(Long id, final BoothProduct product) {
        if(id==null) return;
        BoothProductCategory category = getProductCategoryOrException(id);
        if(category.getLinkedBooth()!=product.getLinkedCategory().getLinkedBooth()) {
            throw new OpenBookException(ErrorCode.NOT_SELECTABLE_CATEGORY);
        }
        product.updateCategory(category);
    }

    public void deleteProductCategory(final BoothProductCategory category) {
        if(category.getName().equals(DEFAULT_NAME)) {
            throw new OpenBookException(ErrorCode.DEFAULT_CATEGORY_CANNOT_DELETED);
        }
        categoryRepository.deleteById(category.getId());
    }

    public BoothProductCategory getDefaultCategory(Booth linkedBooth) {
        return categoryRepository.findByLinkedBoothIdAndName(linkedBooth.getId(), DEFAULT_NAME);
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


}
