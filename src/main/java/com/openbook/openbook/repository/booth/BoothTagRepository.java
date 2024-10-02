package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothTag;
import com.openbook.openbook.domain.booth.dto.BoothStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothTagRepository extends JpaRepository<BoothTag, Long> {

    @Query("SELECT t FROM BoothTag t WHERE t.linkedBooth.id=:boothId")
    List<BoothTag> findAllByLinkedBoothId(Long boothId);

    @Query("SELECT distinct bt.linkedBooth FROM BoothTag bt WHERE bt.name LIKE %:name% AND bt.linkedBooth.status=:boothStatus")
    Slice<Booth> findAllBoothByName(Pageable pageable, String name, BoothStatus boothStatus);

    @Modifying
    @Query("DELETE FROM BoothTag bt WHERE bt.id = :id")
    void deleteById(Long id);
}
