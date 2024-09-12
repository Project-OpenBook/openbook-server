package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.BoothArea;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothAreaRepository extends JpaRepository<BoothArea, Long> {

    @Query("SELECT a FROM BoothArea a WHERE a.linkedEventLayout.id=:linkedLayoutId")
    List<BoothArea> findAllByLinkedEventLayoutId(Long linkedLayoutId);

    @Query("SELECT a FROM BoothArea a WHERE a.linkedBooth.id=:boothId")
    List<BoothArea> findAllByLinkedBoothId(Long boothId);
}