package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Long> {
}
