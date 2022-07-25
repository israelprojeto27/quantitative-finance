package com.app.commons.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultFilterAtivoCotacaoGrowDTO {

    private String sigla;

    private Double valorPercentGrow;

    private Double valorCotacaoInicio;

    private Double valorCotacaoFim;

    private String dataInicio;

    private String dataFim;

    public ResultFilterAtivoCotacaoGrowDTO() {
    }

    public ResultFilterAtivoCotacaoGrowDTO(String sigla, Double valorPercentGrow, Double valorCotacaoInicio, Double valorCotacaoFim, String dataInicio, String dataFim) {
        this.sigla = sigla;
        this.valorPercentGrow = valorPercentGrow;
        this.valorCotacaoInicio = valorCotacaoInicio;
        this.valorCotacaoFim = valorCotacaoFim;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public static ResultFilterAtivoCotacaoGrowDTO from(Double valorPercentGrow, String sigla, Double valorCotacaoInicio, Double valorCotacaoFim, String dataInicio, String dataFim) {
        return ResultFilterAtivoCotacaoGrowDTO.builder()
                .sigla(sigla)
                .valorPercentGrow(valorPercentGrow)
                .valorCotacaoInicio(valorCotacaoInicio)
                .valorCotacaoFim(valorCotacaoFim)
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .build();
    }
}

