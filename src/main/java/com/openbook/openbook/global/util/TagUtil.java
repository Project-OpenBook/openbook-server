package com.openbook.openbook.global.util;

import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class TagUtil {

    public Set<String> getValidTagsOrException(List<String> tags) {
        Set<String> validTags = new HashSet<>();
        tags.stream()
                .map(String::trim)
                .forEach(tag -> {
                    if (tag.isEmpty()) {
                        throw new OpenBookException(ErrorCode.EMPTY_TAG_DATA);
                    }
                    if (!validTags.add(tag)) {
                        throw new OpenBookException(ErrorCode.ALREADY_TAG_DATA);
                    }
                });
        return validTags;
    }

}
