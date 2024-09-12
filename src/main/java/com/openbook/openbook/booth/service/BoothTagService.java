package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothTag;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.repository.BoothTagRepository;
import com.openbook.openbook.global.util.TagUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothTagService {
    private final BoothTagRepository boothTagRepository;

    public void createBoothTags(List<String> names, Booth booth) {
        TagUtil.getValidTagsOrException(names).forEach(
                name ->  boothTagRepository.save(BoothTag.builder()
                        .name(name)
                        .booth(booth)
                        .build()
                )
        );
    }

    public List<BoothTag> getBoothTag(Long id){
        return boothTagRepository.findAllByLinkedBoothId(id);
    }

    public Slice<Booth> getBoothByTag(Pageable pageable, String boothTag, BoothStatus status){
        return boothTagRepository.findAllBoothByName(pageable, boothTag, status);
    }
}
