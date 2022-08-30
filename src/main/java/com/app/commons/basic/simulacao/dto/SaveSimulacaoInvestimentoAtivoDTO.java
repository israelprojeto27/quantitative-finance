package com.app.commons.basic.simulacao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveSimulacaoInvestimentoAtivoDTO {

    private Double valorInvestimento;

    public SaveSimulacaoInvestimentoAtivoDTO() {
    }

    public SaveSimulacaoInvestimentoAtivoDTO(Double valorInvestimento) {
        this.valorInvestimento = valorInvestimento;
    }
}
