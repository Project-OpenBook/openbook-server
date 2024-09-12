package com.openbook.openbook.service.event.dto;

import com.openbook.openbook.service.booth.dto.BoothAreaCreateData;
import com.openbook.openbook.domain.event.dto.EventLayoutType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record EventLayoutCreateData(
        @Enumerated(EnumType.STRING)
        @NotBlank EventLayoutType type,
        @NotNull @Size(max = 3) List<MultipartFile> images,
        @NotNull List<BoothAreaCreateData> areas
) {
}
