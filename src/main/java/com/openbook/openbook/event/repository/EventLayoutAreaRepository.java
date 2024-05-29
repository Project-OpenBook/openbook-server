package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.entity.EventLayoutArea;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLayoutAreaRepository extends JpaRepository<EventLayoutArea, Long> {
    @Query("SELECT a FROM EventLayoutArea a WHERE a.linkedEventLayout=:linkedLayout")
    List<EventLayoutArea> findAllByLinkedEventLayout(EventLayout linkedLayout);

    @Query("SELECT a FROM EventLayoutArea a WHERE a.linkedBooth.id=:boothId")
    List<EventLayoutArea> findAllByLinkedBoothId(Long boothId);
}