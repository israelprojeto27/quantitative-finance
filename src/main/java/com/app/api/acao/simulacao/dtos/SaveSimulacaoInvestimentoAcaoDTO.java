package com.app.api.acao.simulacao.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveSimulacaoInvestimentoAcaoDTO {


    private Double valorInvestimento;

    public SaveSimulacaoInvestimentoAcaoDTO() {
    }

    public SaveSimulacaoInvestimentoAcaoDTO(Double valorInvestimento) {
        this.valorInvestimento = valorInvestimento;
    }
}
