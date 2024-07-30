package com.openbook.openbook.booth.service.core;

import com.openbook.openbook.booth.dto.BoothTagDTO;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothTag;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.repository.BoothTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothTagService {
    private final BoothTagRepository boothTagRepository;

    public BoothTag createBoothTag(BoothTagDTO boothTag){
        return boothTagRepository.save(
                BoothTag.builder()
                        .name(boothTag.content())
                        .booth(boothTag.booth())
                        .build()
            );
    }

    public List<BoothTag> getBoothTag(Long id){
        return boothTagRepository.findAllByLinkedBoothId(id);
    }

    public Slice<Booth> getBoothByTag(Pageable pageable, String boothTag, BoothStatus status){
        return boothTagRepository.findBoothByName(pageable, boothTag, status);
    }
}
