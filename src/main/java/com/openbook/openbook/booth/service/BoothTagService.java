package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.dto.BoothTagDTO;
import com.openbook.openbook.booth.entity.BoothTag;
import com.openbook.openbook.booth.repository.BoothTagRepository;
import lombok.RequiredArgsConstructor;
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
}
