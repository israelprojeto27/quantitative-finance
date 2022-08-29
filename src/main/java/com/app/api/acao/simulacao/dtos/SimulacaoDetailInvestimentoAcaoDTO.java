package com.app.api.acao.simulacao.dtos;

import com.app.api.acao.simulacao.entities.SimulaDetailInvestimentoAcao;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SimulacaoDetailInvestimentoAcaoDTO {

    private String sigla;

    private Double valorInvestido;

    private String valorInvestidoFmt;

    private Double porcentagemValorInvestido;

    private String porcentagemValorInvestidoFmt;

    private Double ultimaCotacaoAcao;

    private String ultimaCotacaoAcaoFmt;

    private LocalDate dataUltimaCotacao;

    private String dataUltimaCotacaoFmt;

    private Double quantidadeCotasAcao;

    private String quantidadeCotasAcaoFmt;

    public SimulacaoDetailInvestimentoAcaoDTO() {
    }

    public SimulacaoDetailInvestimentoAcaoDTO(String sigla, Double valorInvestido, String valorInvestidoFmt, Double porcentagemValorInvestido, String porcentagemValorInvestidoFmt, Double ultimaCotacaoAcao, String ultimaCotacaoAcaoFmt, LocalDate dataUltimaCotacao, String dataUltimaCotacaoFmt, Double quantidadeCotasAcao, String quantidadeCotasAcaoFmt) {
        this.sigla = sigla;
        this.valorInvestido = valorInvestido;
        this.valorInvestidoFmt = valorInvestidoFmt;
        this.porcentagemValorInvestido = porcentagemValorInvestido;
        this.porcentagemValorInvestidoFmt = porcentagemValorInvestidoFmt;
        this.ultimaCotacaoAcao = ultimaCotacaoAcao;
        this.ultimaCotacaoAcaoFmt = ultimaCotacaoAcaoFmt;
        this.dataUltimaCotacao = dataUltimaCotacao;
        this.dataUltimaCotacaoFmt = dataUltimaCotacaoFmt;
        this.quantidadeCotasAcao = quantidadeCotasAcao;
        this.quantidadeCotasAcaoFmt = quantidadeCotasAcaoFmt;
    }

    public static SimulacaoDetailInvestimentoAcaoDTO from(SimulaDetailInvestimentoAcao entity) {
        return SimulacaoDetailInvestimentoAcaoDTO.builder()
                                                    .sigla(entity.getSigla())
                                                    .valorInvestido(entity.getValorInvestido())
                                                    .valorInvestidoFmt(Utils.converterDoubleDoisDecimaisString(entity.getValorInvestido()))
                                                    .porcentagemValorInvestido(entity.getPorcentagemValorInvestido())
                                                    .porcentagemValorInvestidoFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getPorcentagemValorInvestido()))
                                                    .ultimaCotacaoAcao(entity.getUltimaCotacaoAcao())
                                                    .ultimaCotacaoAcaoFmt(Utils.converterDoubleDoisDecimaisString(entity.getUltimaCotacaoAcao()))
                                                    .quantidadeCotasAcao(entity.getQuantidadeCotasAcao())
                                                    .quantidadeCotasAcaoFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getQuantidadeCotasAcao()))
                                                    .dataUltimaCotacao(entity.getDataUltimaCotacaoAcao())
                                                    .dataUltimaCotacaoFmt(Utils.converteLocalDateToString(entity.getDataUltimaCotacaoAcao()))
                                                 .build();
    }
}
