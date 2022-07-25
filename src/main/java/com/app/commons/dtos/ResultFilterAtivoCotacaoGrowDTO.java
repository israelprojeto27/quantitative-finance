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

    public ResultFilterAtivoCotacaoGrowDTO() {
    }

    public ResultFilterAtivoCotacaoGrowDTO(String sigla, Double valorPercentGrow, Double valorCotacaoInicio, Double valorCotacaoFim) {
        this.sigla = sigla;
        this.valorPercentGrow = valorPercentGrow;
        this.valorCotacaoInicio = valorCotacaoInicio;
        this.valorCotacaoFim = valorCotacaoFim;
    }

    public static ResultFilterAtivoCotacaoGrowDTO from(Double valorPercentGrow, String sigla, Double valorCotacaoInicio, Double valorCotacaoFim) {
        return ResultFilterAtivoCotacaoGrowDTO.builder()
                .sigla(sigla)
                .valorCotacaoFim(valorPercentGrow)
                .valorCotacaoInicio(valorCotacaoInicio)
                .valorCotacaoFim(valorCotacaoFim)
                .build();
    }
}
