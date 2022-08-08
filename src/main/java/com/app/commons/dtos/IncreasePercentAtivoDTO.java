package com.app.commons.dtos;

import com.app.api.acao.increasepercent.IncreasePercentAcao;
import com.app.api.bdr.increasepercent.IncreasePercentBdr;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncreasePercentAtivoDTO {

    private String dataBase;

    private String dataReference;

    private Double percentual;

    private Double valorFechamentoAtual;

    private Double valorFechamentoAnterior;

    public IncreasePercentAtivoDTO() {
    }

    public IncreasePercentAtivoDTO(String dataBase, String dataReference, Double percentual, Double valorFechamentoAtual, Double valorFechamentoAnterior) {
        this.dataBase = dataBase;
        this.dataReference = dataReference;
        this.percentual = percentual;
        this.valorFechamentoAtual = valorFechamentoAtual;
        this.valorFechamentoAnterior = valorFechamentoAnterior;
    }

    public static IncreasePercentAtivoDTO fromEntity(IncreasePercentAcao entity){
        return IncreasePercentAtivoDTO.builder()
                .dataBase(Utils.converteLocalDateToString(entity.getDataBase()))
                .dataReference(Utils.converteLocalDateToString(entity.getDataReference()))
                .percentual(Utils.converterDoubleDoisDecimais(entity.getPercentual()))
                .valorFechamentoAtual(Utils.converterDoubleDoisDecimais(entity.getValorFechamentoAtual()))
                .valorFechamentoAnterior(Utils.converterDoubleDoisDecimais(entity.getValorFechamentoAnterior()))
                .build();
    }

    public static IncreasePercentAtivoDTO fromEntity(IncreasePercentBdr entity){
        return IncreasePercentAtivoDTO.builder()
                .dataBase(Utils.converteLocalDateToString(entity.getDataBase()))
                .dataReference(Utils.converteLocalDateToString(entity.getDataReference()))
                .percentual(Utils.converterDoubleDoisDecimais(entity.getPercentual()))
                .valorFechamentoAtual(Utils.converterDoubleDoisDecimais(entity.getValorFechamentoAtual()))
                .valorFechamentoAnterior(Utils.converterDoubleDoisDecimais(entity.getValorFechamentoAnterior()))
                .build();
    }
}
