package com.app.commons.basic.simulacao.dto;


import com.app.api.acao.simulacao.entities.SimulaDetailInvestimentoAcao;
import com.app.api.acao.simulacao.entities.SimulaInvestimentoAcao;
import com.app.api.bdr.simulacao.entities.SimulaDetailInvestimentoBdr;
import com.app.api.bdr.simulacao.entities.SimulaInvestimentoBdr;
import com.app.api.fundoimobiliario.simulacao.entities.SimulaDetailInvestimentoFundoImobiliario;
import com.app.api.fundoimobiliario.simulacao.entities.SimulaInvestimentoFundoImobiliario;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class InfoGeraisSimulacaoInvestimentoAtivoDTO {

    private Long id;

    private Double valorInvestimento = 0d;

    private String valorInvestimentoFmt;

    private List<SimulacaoDetailInvestimentoAtivoDTO> list = new ArrayList<>();

    public InfoGeraisSimulacaoInvestimentoAtivoDTO() {
    }

    public InfoGeraisSimulacaoInvestimentoAtivoDTO(Long id, Double valorInvestimento, String valorInvestimentoFmt, List<SimulacaoDetailInvestimentoAtivoDTO> list) {
        this.id = id;
        this.valorInvestimento = valorInvestimento;
        this.valorInvestimentoFmt = valorInvestimentoFmt;
        this.list = list;
    }

    public static InfoGeraisSimulacaoInvestimentoAtivoDTO from(SimulaInvestimentoAcao simulaInvestimento , List<SimulaDetailInvestimentoAcao> list) {
        return InfoGeraisSimulacaoInvestimentoAtivoDTO.builder()
                                                     .id(simulaInvestimento.getId())
                                                     .valorInvestimento(simulaInvestimento.getValorInvestimento())
                                                     .valorInvestimentoFmt(Utils.converterDoubleDoisDecimaisString(simulaInvestimento.getValorInvestimento()))
                                                     .list(list != null && !list.isEmpty() ? list.stream().map(SimulacaoDetailInvestimentoAtivoDTO::from).collect(Collectors.toList()) : null)
                                                     .build();
    }

    public static InfoGeraisSimulacaoInvestimentoAtivoDTO from(SimulaInvestimentoBdr simulaInvestimento, List<SimulaDetailInvestimentoBdr> list) {
        return InfoGeraisSimulacaoInvestimentoAtivoDTO.builder()
                .id(simulaInvestimento.getId())
                .valorInvestimento(simulaInvestimento.getValorInvestimento())
                .valorInvestimentoFmt(Utils.converterDoubleDoisDecimaisString(simulaInvestimento.getValorInvestimento()))
                .list(list != null && !list.isEmpty() ? list.stream().map(SimulacaoDetailInvestimentoAtivoDTO::from).collect(Collectors.toList()) : null)
                .build();
    }


    public static InfoGeraisSimulacaoInvestimentoAtivoDTO from(SimulaInvestimentoFundoImobiliario simulaInvestimento, List<SimulaDetailInvestimentoFundoImobiliario> list) {
        return InfoGeraisSimulacaoInvestimentoAtivoDTO.builder()
                .id(simulaInvestimento.getId())
                .valorInvestimento(simulaInvestimento.getValorInvestimento())
                .valorInvestimentoFmt(Utils.converterDoubleDoisDecimaisString(simulaInvestimento.getValorInvestimento()))
                .list(list != null && !list.isEmpty() ? list.stream().map(SimulacaoDetailInvestimentoAtivoDTO::from).collect(Collectors.toList()) : null)
                .build();
    }


}
