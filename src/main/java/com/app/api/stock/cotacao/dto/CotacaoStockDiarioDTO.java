package com.app.api.stock.cotacao.dto;

import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CotacaoStockDiarioDTO {


    private Long id;

    private String data;

    private String high;

    private String low;

    private String open;

    private String close;

    private String adjclose;

    private Long volume;


    public static CotacaoStockDiarioDTO fromEntity(CotacaoStockDiario entity) {
        return CotacaoStockDiarioDTO.builder()
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
