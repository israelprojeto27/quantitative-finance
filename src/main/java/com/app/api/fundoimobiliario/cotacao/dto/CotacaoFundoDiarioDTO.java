package com.app.api.fundoimobiliario.cotacao.dto;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CotacaoFundoDiarioDTO {

    public CotacaoFundoDiarioDTO() {
    }

    private Long id;

    private LocalDate data;

    private Double high;

    private Double low;

    private Double open;

    private Double close;

    private Double adjclose;

    private Long volume;

    public CotacaoFundoDiarioDTO(Long id, LocalDate data, Double high, Double low, Double open, Double close, Double adjclose, Long volume) {
        this.id = id;
        this.data = data;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.adjclose = adjclose;
        this.volume = volume;
    }


    public static CotacaoFundoDiarioDTO fromEntity(CotacaoFundoDiario entity) {
        return CotacaoFundoDiarioDTO.builder()
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
