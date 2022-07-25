package com.app.api.acao.cotacao.dto;

import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CotacaoAcaoSemanalDTO {

    public CotacaoAcaoSemanalDTO() {
    }

    private Long id;

    private LocalDate data;

    private Double high;

    private Double low;

    private Double open;

    private Double close;

    private Double adjclose;

    private Long volume;

    public CotacaoAcaoSemanalDTO(Long id, LocalDate data, Double high, Double low, Double open, Double close, Double adjclose, Long volume) {
        this.id = id;
        this.data = data;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.adjclose = adjclose;
        this.volume = volume;
    }

    public static CotacaoAcaoSemanalDTO  fromEntity(CotacaoAcaoSemanal entity) {
        return CotacaoAcaoSemanalDTO.builder()
                .id(entity.getId())
                .data(entity.getData())
                .high(entity.getHigh())
                .low(entity.getLow())
                .open(entity.getOpen())
                .close(entity.getClose())
                .adjclose(entity.getAdjclose())
                .volume(entity.getVolume())
                .build();
    }
}
