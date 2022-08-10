package com.app.api.bdr.cotacao.dto;

import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CotacaoBdrMensalDTO {
    public CotacaoBdrMensalDTO() {
    }

    private Long id;

    private String data;

    private String high;

    private String low;

    private String open;

    private String close;

    private String adjclose;

    private Long volume;

    public CotacaoBdrMensalDTO(Long id, String data, String high, String low, String open, String close, String adjclose, Long volume) {
        this.id = id;
        this.data = data;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.adjclose = adjclose;
        this.volume = volume;
    }

    public static CotacaoBdrMensalDTO fromEntity(CotacaoBdrMensal entity) {
        return CotacaoBdrMensalDTO.builder()
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
