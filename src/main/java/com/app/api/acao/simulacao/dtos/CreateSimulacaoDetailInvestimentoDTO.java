package com.app.api.acao.simulacao.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSimulacaoDetailInvestimentoDTO {

    private String sigla;

    private Double porcentagemValorInvestido;

    public CreateSimulacaoDetailInvestimentoDTO() {
    }

    public CreateSimulacaoDetailInvestimentoDTO(String sigla, Double porcentagemValorInvestido) {
        this.sigla = sigla;
        this.porcentagemValorInvestido = porcentagemValorInvestido;
    }
}
