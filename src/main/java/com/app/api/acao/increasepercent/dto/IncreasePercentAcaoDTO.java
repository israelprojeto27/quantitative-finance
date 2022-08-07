package com.app.api.acao.increasepercent.dto;

import com.app.api.acao.increasepercent.IncreasePercentAcao;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncreasePercentAcaoDTO {

    private String dataBase;

    private String dataReference;

    private Double percentual;

    private Double valorFechamentoAtual;

    private Double valorFechamentoAnterior;

    public IncreasePercentAcaoDTO() {
    }

    public IncreasePercentAcaoDTO(String dataBase, String dataReference, Double percentual, Double valorFechamentoAtual, Double valorFechamentoAnterior) {
        this.dataBase = dataBase;
        this.dataReference = dataReference;
        this.percentual = percentual;
        this.valorFechamentoAtual = valorFechamentoAtual;
        this.valorFechamentoAnterior = valorFechamentoAnterior;
    }

    public static IncreasePercentAcaoDTO fromEntity(IncreasePercentAcao entity){
        return IncreasePercentAcaoDTO.builder()
                .dataBase(Utils.converteLocalDateToString(entity.getDataBase()))
                .dataReference(Utils.converteLocalDateToString(entity.getDataReference()))
                .percentual(Utils.converterDoubleDoisDecimais(entity.getPercentual()))
                .valorFechamentoAtual(Utils.converterDoubleDoisDecimais(entity.getValorFechamentoAtual()))
                .valorFechamentoAnterior(Utils.converterDoubleDoisDecimais(entity.getValorFechamentoAnterior()))
                .build();
    }
}
