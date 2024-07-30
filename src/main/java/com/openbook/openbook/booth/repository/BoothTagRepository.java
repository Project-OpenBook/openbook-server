package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothTag;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.event.entity.EventTag;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothTagRepository extends JpaRepository<BoothTag, Long> {

    @Query("SELECT t FROM BoothTag t WHERE t.linkedBooth.id=:boothId")
    List<BoothTag> findAllByLinkedBoothId(Long boothId);

    @Query("SELECT bt.linkedBooth FROM BoothTag bt WHERE bt.name LIKE :name AND bt.linkedBooth.status=:boothStatus")
    Slice<Booth> findBoothByName(Pageable pageable, String name, BoothStatus boothStatus);

}
