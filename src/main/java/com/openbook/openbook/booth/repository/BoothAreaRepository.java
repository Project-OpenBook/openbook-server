package com.openbook.openbook.booth.repository;

import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.booth.entity.BoothArea;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothAreaRepository extends JpaRepository<BoothArea, Long> {
    @Query("SELECT a FROM BoothArea a WHERE a.linkedEventLayout=:linkedLayout")
    List<BoothArea> findAllByLinkedEventLayout(EventLayout linkedLayout);

    @Query("SELECT a FROM BoothArea a WHERE a.linkedBooth.id=:boothId")
    List<BoothArea> findAllByLinkedBoothId(Long boothId);
}