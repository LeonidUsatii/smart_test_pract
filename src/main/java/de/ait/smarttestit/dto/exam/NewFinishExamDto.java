package de.ait.smarttestit.dto.exam;

import java.util.List;

public record NewFinishExamDto(

        List<Integer> answerIdsList,

        Long examId
) {

}
