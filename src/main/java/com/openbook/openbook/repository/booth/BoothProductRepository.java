package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.BoothProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothProductRepository extends JpaRepository<BoothProduct, Long> {

    Slice<BoothProduct> findAllByLinkedCategoryId(Long linkedCategoryId, Pageable pageable);

}
