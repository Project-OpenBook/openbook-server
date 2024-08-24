package com.openbook.openbook.booth.repository;


import com.openbook.openbook.booth.entity.BoothProductCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothProductCategoryRepository extends JpaRepository<BoothProductCategory, Long> {

    int countByLinkedBoothId(Long linkedBoothId);

    boolean existsByLinkedBoothIdAndName(Long linkedBoothId, String name);

    List<BoothProductCategory> findAllByLinkedBoothId(Long linkedBoothId);

}
