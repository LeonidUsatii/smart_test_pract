package de.ait.smarttestit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(name = "Message", description = "Any message from the server")
public record StandardResponseDto (
        @Schema(description = "Maybe : error message , status change message etc",
                example = "Error or any other message from the server")
        String message){
}
