package com.openbook.openbook.api;

import java.util.List;
import org.springframework.data.domain.Slice;

public record SliceResponse<T>(
        boolean hasNext,
        int sliceNumber,
        int numberOfElements,
        List<T> content
) {
    public static <T> SliceResponse<T> of(Slice<T> slice){
        return new SliceResponse<>(
                slice.hasNext(),
                slice.getNumber(),
                slice.getNumberOfElements(),
                slice.getContent()
        );
    }
}
