package de.ait.smarttestit.dto.exam_task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewTestsParamDto {

    @Schema(description = "Test type id", example = "1")
    @Positive
    private Long testTypeId;

    @Positive
    @Schema(description = "Level of question", example = "1")
    private int questionsLevel;

    @Positive
    @Schema(description = "Number of questions", example = "3")
    private int questionsCount;
}