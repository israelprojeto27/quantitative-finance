package com.app.commons.dtos;

import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SumCalculateDetailYieldDividendosAcaoDTO {

    private String data;

    private Double rendimentoDividendo;

    private Double dividendo;

    private Double valorCotacao;

    public SumCalculateDetailYieldDividendosAcaoDTO() {
    }

    public SumCalculateDetailYieldDividendosAcaoDTO(String data, Double rendimentoDividendo, Double dividendo, Double valorCotacao) {
        this.data = data;
        this.rendimentoDividendo = rendimentoDividendo;
        this.dividendo = dividendo;
        this.valorCotacao = valorCotacao;
    }

    public static SumCalculateDetailYieldDividendosAcaoDTO from(LocalDate data, Double valorRendimentoDividendo, Double valorCotacaoAcao, Double dividend) {
        return SumCalculateDetailYieldDividendosAcaoDTO.builder()
                                                        .data(Utils.converteLocalDateToString(data))
                                                        .rendimentoDividendo(valorRendimentoDividendo)
                                                        .valorCotacao(valorCotacaoAcao)
                                                        .dividendo(dividend)
                                                        .build();
    }
}
