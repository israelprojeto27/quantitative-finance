package com.app.commons.dtos.mapadividendo;

import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapaDividendoSumDTO {

    private String sigla;

    private Double sumDividendos;

    private String sumDividendosFmt;

    public static MapaDividendoSumDTO from(String sigla, Double sumDividendo) {
        return MapaDividendoSumDTO.builder()
                .sigla(sigla)
                .sumDividendos(sumDividendo)
                .sumDividendosFmt(Utils.converterDoubleDoisDecimaisString(sumDividendo))
                .build();
    }
}
