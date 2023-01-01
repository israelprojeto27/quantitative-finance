package com.app.api.reit.cotacao.dto;

import com.app.api.reit.cotacao.entities.CotacaoReitMensal;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CotacaoReitMensalDTO {


    private Long id;

    private String data;

    private String high;

    private String low;

    private String open;

    private String close;

    private String adjclose;

    private Long volume;


    public static CotacaoReitMensalDTO fromEntity(CotacaoReitMensal entity) {
        return CotacaoReitMensalDTO.builder()
                .id(entity.getId())
                .data(Utils.converteLocalDateToString(entity.getData()))
                .high(Utils.converterDoubleDoisDecimaisString(entity.getHigh()))
                .low(Utils.converterDoubleDoisDecimaisString(entity.getLow()))
                .open(Utils.converterDoubleDoisDecimaisString(entity.getOpen()))
                .close(Utils.converterDoubleDoisDecimaisString(entity.getClose()))
                .adjclose(Utils.converterDoubleDoisDecimaisString(entity.getAdjclose()))
                .volume(entity.getVolume())
                .build();
    }
}
