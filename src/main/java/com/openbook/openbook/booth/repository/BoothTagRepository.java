package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothTagRepository extends JpaRepository<BoothTag, Long> {

    @Query("SELECT bt.booth FROM BoothTag bt WHERE bt.name LIKE :name")
    Slice<Booth> findBoothByName(Pageable pageable, @Param(value = "name") String name);
}
