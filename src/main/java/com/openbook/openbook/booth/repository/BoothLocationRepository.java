package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.BoothLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothLocationRepository extends JpaRepository<BoothLocation, Long> {
}
