package com.openbook.openbook.booth.repository;


import com.openbook.openbook.booth.entity.BoothProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothProductCategoryRepository extends JpaRepository<BoothProductCategory, Long> {
}
