package com.openbook.openbook.event.repository;


import com.openbook.openbook.event.entity.EventLayoutArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLayoutAreaRepository extends JpaRepository<EventLayoutArea, Long> {
}
