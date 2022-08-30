package com.app.commons.basic.simulacao.dto;

import com.app.commons.basic.simulacao.dto.SimulacaoInvestimentoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ResultSimulacaoInvestimentoDTO {

    private Double totalDividendos = 0d;

    private String totalDividendosFmt;

    private Double totalGanhosDividendos = 0d;

    private String totalGanhosDividendosFmt;

    private Double ganhoMedioMensalDividendos = 0d;

    private String ganhoMedioMensalDividendosFmt;

    private List<SimulacaoInvestimentoDTO> list = new ArrayList<>();

    public ResultSimulacaoInvestimentoDTO() {
    }

    public ResultSimulacaoInvestimentoDTO(Double totalDividendos, String totalDividendosFmt, Double totalGanhosDividendos, String totalGanhosDividendosFmt, Double ganhoMedioMensalDividendos, String ganhoMedioMensalDividendosFmt, List<SimulacaoInvestimentoDTO> list) {
        this.totalDividendos = totalDividendos;
        this.totalDividendosFmt = totalDividendosFmt;
        this.totalGanhosDividendos = totalGanhosDividendos;
        this.totalGanhosDividendosFmt = totalGanhosDividendosFmt;
        this.ganhoMedioMensalDividendos = ganhoMedioMensalDividendos;
        this.ganhoMedioMensalDividendosFmt = ganhoMedioMensalDividendosFmt;
        this.list = list;
    }
}
