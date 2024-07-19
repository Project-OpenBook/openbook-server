package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.dto.BoothTagDTO;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothTag;
import com.openbook.openbook.booth.repository.BoothTagRepository;
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
                        .content(boothTag.content())
                        .booth(boothTag.booth())
                        .build()
            );
    }

    public Slice<Booth> getBoothByTag(Pageable pageable, String boothTag){
        return boothTagRepository.findBoothIdByContent(pageable, boothTag);
    }
}
