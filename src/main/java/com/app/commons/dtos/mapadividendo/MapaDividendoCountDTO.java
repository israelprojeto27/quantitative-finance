package com.app.commons.dtos.mapadividendo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapaDividendoCountDTO {

    private String sigla;

    private Integer countDividendos; // este campo sinaliza quantas vezes aquela sigla apareceu no periodo selecionado


    public MapaDividendoCountDTO() {
    }

    public MapaDividendoCountDTO(String sigla, Integer countDividendos) {
        this.sigla = sigla;
        this.countDividendos = countDividendos;
    }

    public static MapaDividendoCountDTO from(String sigla, Integer count) {
        return MapaDividendoCountDTO.builder()
                .sigla(sigla)
                .countDividendos(count)
                .build();
    }
}
