package com.openbook.openbook.service.booth;

import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothTag;
import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.repository.booth.BoothTagRepository;
import com.openbook.openbook.util.TagUtil;
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
