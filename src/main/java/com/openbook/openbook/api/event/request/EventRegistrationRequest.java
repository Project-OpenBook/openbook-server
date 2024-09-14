package com.openbook.openbook.api.event.request;

import com.openbook.openbook.domain.event.dto.EventLayoutType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record EventRegistrationRequest(
        @NotBlank String name,
        @NotBlank String location,
        @NotBlank String description,
        @NotNull MultipartFile mainImage,
        @NotNull LocalDate openDate,
        @NotNull LocalDate closeDate,
        @NotNull LocalDate boothRecruitmentStartDate,
        @NotNull LocalDate boothRecruitmentEndDate,
        @Enumerated(EnumType.STRING)
        @NotNull EventLayoutType layoutType,
        @NotNull @Size(max = 3) List<MultipartFile> layoutImages,
        @NotNull List<String> areaClassifications,
        @NotNull List<Integer> areaMaxNumbers,
        @Size(max = 5) List<String> tags
) {
}
