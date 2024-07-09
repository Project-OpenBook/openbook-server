package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.BoothTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothTagRepository extends JpaRepository<BoothTag, Long> {
}
