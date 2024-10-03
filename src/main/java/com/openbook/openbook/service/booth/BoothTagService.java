package com.openbook.openbook.service.booth;

import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothTag;
import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.repository.booth.BoothTagRepository;
import com.openbook.openbook.util.TagUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoothTagService {
    private final BoothTagRepository boothTagRepository;

    public BoothTag getBoothTagOrException(long boothTagId){
        return boothTagRepository.findById(boothTagId).orElseThrow(
                () -> new OpenBookException(ErrorCode.TAG_NOT_FOUND)
        );
    }

    public void createBoothTags(List<String> names, Booth booth) {
        TagUtil.getValidTagsOrException(names).forEach(
                name ->  boothTagRepository.save(BoothTag.builder()
                        .name(name)
                        .booth(booth)
                        .build()
                )
        );
    }

    public Slice<Booth> getBoothByTag(Pageable pageable, String boothTag, BoothStatus status){
        return boothTagRepository.findAllBoothByName(pageable, boothTag, status);
    }

    @Transactional
    public void modifyBoothTag(List<String> tagsToAdd, List<Long> tagsToDelete, Booth booth){
        int add = (tagsToAdd != null) ? tagsToAdd.size() : 0;
        int delete = (tagsToDelete != null) ? tagsToDelete.size() : 0;
        if(booth.getBoothTags().size() - delete + add > 5){
            throw new OpenBookException(ErrorCode.EXCEED_MAXIMUM_TAG);
        }
        createBoothTags(tagsToAdd, booth);

        for(int i = 0; i < delete; i++){
            BoothTag tag = getBoothTagOrException(tagsToDelete.get(i));
            if(tag.getLinkedBooth() != booth){
                throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
            }
            boothTagRepository.deleteById(tag.getId());
        }
    }
}
