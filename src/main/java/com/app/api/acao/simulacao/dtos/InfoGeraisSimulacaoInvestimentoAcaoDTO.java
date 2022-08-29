package com.app.api.acao.simulacao.dtos;

import com.app.api.acao.simulacao.entities.SimulaDetailInvestimentoAcao;
import com.app.api.acao.simulacao.entities.SimulaInvestimentoAcao;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class InfoGeraisSimulacaoInvestimentoAcaoDTO {

    private Long id;

    private Double valorInvestimento = 0d;

    private String valorInvestimentoFmt;

    private List<SimulacaoDetailInvestimentoAcaoDTO> list = new ArrayList<>();

    public InfoGeraisSimulacaoInvestimentoAcaoDTO() {
    }

    public InfoGeraisSimulacaoInvestimentoAcaoDTO(Long id, Double valorInvestimento, String valorInvestimentoFmt, List<SimulacaoDetailInvestimentoAcaoDTO> list) {
        this.id = id;
        this.valorInvestimento = valorInvestimento;
        this.valorInvestimentoFmt = valorInvestimentoFmt;
        this.list = list;
    }

    public static InfoGeraisSimulacaoInvestimentoAcaoDTO from(SimulaInvestimentoAcao simulaInvestimentoAcao, List<SimulaDetailInvestimentoAcao> list) {
        return InfoGeraisSimulacaoInvestimentoAcaoDTO.builder()
                                                     .id(simulaInvestimentoAcao.getId())
                                                     .valorInvestimento(simulaInvestimentoAcao.getValorInvestimento())
                                                     .valorInvestimentoFmt(Utils.converterDoubleDoisDecimaisString(simulaInvestimentoAcao.getValorInvestimento()))
                                                     .list(list != null && !list.isEmpty() ? list.stream().map(SimulacaoDetailInvestimentoAcaoDTO::from).collect(Collectors.toList()) : null)
                                                     .build();
    }
}
