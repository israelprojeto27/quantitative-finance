package com.app.commons.dtos;

import com.app.api.acao.increasepercent.IncreasePercentAcao;
import com.app.api.bdr.increasepercent.IncreasePercentBdr;
import com.app.api.fundoimobiliario.increasepercent.IncreasePercentFundoImobiliario;
import com.app.api.stock.increasepercent.IncreasePercentStock;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class IncreasePercentAtivoDTO {

    private String dataBase;

    private String dataReference;

    private Double percentual;

    private String valorFechamentoAtual;

    private String valorFechamentoAnterior;


    public static IncreasePercentAtivoDTO fromEntity(IncreasePercentAcao entity){
        return IncreasePercentAtivoDTO.builder()
                .dataBase(Utils.converteLocalDateToString(entity.getDataBase()))
                .dataReference(Utils.converteLocalDateToString(entity.getDataReference()))
                .percentual(Utils.converterDoubleDoisDecimais(entity.getPercentual()))
                .valorFechamentoAtual(Utils.converterDoubleDoisDecimaisString(entity.getValorFechamentoAtual()))
                .valorFechamentoAnterior(Utils.converterDoubleDoisDecimaisString(entity.getValorFechamentoAnterior()))
                .build();
    }

    public static IncreasePercentAtivoDTO fromEntity(IncreasePercentBdr entity){
        return IncreasePercentAtivoDTO.builder()
                .dataBase(Utils.converteLocalDateToString(entity.getDataBase()))
                .dataReference(Utils.converteLocalDateToString(entity.getDataReference()))
                .percentual(Utils.converterDoubleDoisDecimais(entity.getPercentual()))
                .valorFechamentoAtual(Utils.converterDoubleDoisDecimaisString(entity.getValorFechamentoAtual()))
                .valorFechamentoAnterior(Utils.converterDoubleDoisDecimaisString(entity.getValorFechamentoAnterior()))
                .build();
    }

    public static IncreasePercentAtivoDTO fromEntity(IncreasePercentFundoImobiliario entity){
        return IncreasePercentAtivoDTO.builder()
                .dataBase(Utils.converteLocalDateToString(entity.getDataBase()))
                .dataReference(Utils.converteLocalDateToString(entity.getDataReference()))
                .percentual(Utils.converterDoubleDoisDecimais(entity.getPercentual()))
                .valorFechamentoAtual(Utils.converterDoubleDoisDecimaisString(entity.getValorFechamentoAtual()))
                .valorFechamentoAnterior(Utils.converterDoubleDoisDecimaisString(entity.getValorFechamentoAnterior()))
                .build();
    }

    public static IncreasePercentAtivoDTO fromEntity(IncreasePercentStock entity){
        return IncreasePercentAtivoDTO.builder()
                .dataBase(Utils.converteLocalDateToString(entity.getDataBase()))
                .dataReference(Utils.converteLocalDateToString(entity.getDataReference()))
                .percentual(Utils.converterDoubleDoisDecimais(entity.getPercentual()))
                .valorFechamentoAtual(Utils.converterDoubleDoisDecimaisString(entity.getValorFechamentoAtual()))
                .valorFechamentoAnterior(Utils.converterDoubleDoisDecimaisString(entity.getValorFechamentoAnterior()))
                .build();
    }
}
