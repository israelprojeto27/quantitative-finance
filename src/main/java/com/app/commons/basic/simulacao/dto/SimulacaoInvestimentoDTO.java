package com.app.commons.basic.simulacao.dto;

import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SimulacaoInvestimentoDTO {

    private LocalDate periodo;

    private String periodoFmt;

    private Double valorTotalDividendo;

    private String valorTotalDividendoFmt;

    private String siglas;

    public SimulacaoInvestimentoDTO() {
    }

    public SimulacaoInvestimentoDTO(LocalDate periodo, String periodoFmt, Double valorTotalDividendo, String valorTotalDividendoFmt, String siglas) {
        this.periodo = periodo;
        this.periodoFmt = periodoFmt;
        this.valorTotalDividendo = valorTotalDividendo;
        this.valorTotalDividendoFmt = valorTotalDividendoFmt;
        this.siglas = siglas;
    }

    public static SimulacaoInvestimentoDTO from(String sigla, Double quantidadeCotas, Double valorDividendo, LocalDate dataDividendo) {
        return SimulacaoInvestimentoDTO.builder()
                                                .siglas(sigla)
                                                .periodo(dataDividendo)
                                                .periodoFmt(Utils.converteLocalDateToString2(dataDividendo))
                                                .valorTotalDividendo(valorDividendo *  quantidadeCotas)
                                                .valorTotalDividendoFmt(Utils.converterDoubleDoisDecimaisString(valorDividendo *  quantidadeCotas))
                                              .build();
    }
}
