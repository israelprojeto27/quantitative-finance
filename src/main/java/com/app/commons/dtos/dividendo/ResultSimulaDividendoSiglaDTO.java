package com.app.commons.dtos.dividendo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResultSimulaDividendoSiglaDTO {

    private Integer quantidadeCotas = 0;

    private Double totalGanhoDividendos = 0d;

    private String totalGanhoDividendosFmt = "";

    private Double ganhoMedioDividendos = 0d;

    private String ganhoMedioDividendosFmt = "";

    private List<ResultSimulaDividendoSiglaDetailDTO> list;

    public ResultSimulaDividendoSiglaDTO() {
    }

    public ResultSimulaDividendoSiglaDTO(Integer quantidadeCotas, Double totalGanhoDividendos, String totalGanhoDividendosFmt, Double ganhoMedioDividendos, String ganhoMedioDividendosFmt, List<ResultSimulaDividendoSiglaDetailDTO> list) {
        this.quantidadeCotas = quantidadeCotas;
        this.totalGanhoDividendos = totalGanhoDividendos;
        this.totalGanhoDividendosFmt = totalGanhoDividendosFmt;
        this.ganhoMedioDividendos = ganhoMedioDividendos;
        this.ganhoMedioDividendosFmt = ganhoMedioDividendosFmt;
        this.list = list;
    }
}
