package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.BoothProductImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothProductImageRepository extends JpaRepository<BoothProductImage, Long> {

    List<BoothProductImage> findAllByLinkedProductId(Long linkedProductId);

    int countAllByLinkedProductId(Long linkedProductId);

}
