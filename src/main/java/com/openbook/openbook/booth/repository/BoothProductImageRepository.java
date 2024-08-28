package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.BoothProductImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothProductImageRepository extends JpaRepository<BoothProductImage, Long> {

    List<BoothProductImage> findAllByLinkedProductId(Long linkedProductId);

}
