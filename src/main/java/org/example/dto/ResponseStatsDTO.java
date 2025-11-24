package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseStatsDTO {

    @Schema(description = "Cantidad de mutantes encontrados", example = "2")
    private int cantMutantes;
    @Schema(description = "Cantidad de humanos encontrados", example = "2")
    private int cantHumanos;
    @Schema(description = "Ratio de mutantes sobre humanos", example = "3.2")
    private double ratio;


}
