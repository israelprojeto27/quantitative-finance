package com.app.commons.basic.simulacao.dto;

import com.app.api.acao.simulacao.entities.SimulaDetailInvestimentoAcao;
import com.app.api.bdr.simulacao.entities.SimulaDetailInvestimentoBdr;
import com.app.api.fundoimobiliario.simulacao.entities.SimulaDetailInvestimentoFundoImobiliario;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SimulacaoDetailInvestimentoAtivoDTO {

    private String sigla;

    private Double valorInvestido;

    private String valorInvestidoFmt;

    private Double porcentagemValorInvestido;

    private String porcentagemValorInvestidoFmt;

    private Double ultimaCotacao;

    private String ultimaCotacaoFmt;

    private LocalDate dataUltimaCotacao;

    private String dataUltimaCotacaoFmt;

    private Double quantidadeCotas;

    private String quantidadeCotasFmt;

    public SimulacaoDetailInvestimentoAtivoDTO() {
    }

    public SimulacaoDetailInvestimentoAtivoDTO(String sigla, Double valorInvestido, String valorInvestidoFmt, Double porcentagemValorInvestido, String porcentagemValorInvestidoFmt, Double ultimaCotacao, String ultimaCotacaoFmt, LocalDate dataUltimaCotacao, String dataUltimaCotacaoFmt, Double quantidadeCotas, String quantidadeCotasFmt) {
        this.sigla = sigla;
        this.valorInvestido = valorInvestido;
        this.valorInvestidoFmt = valorInvestidoFmt;
        this.porcentagemValorInvestido = porcentagemValorInvestido;
        this.porcentagemValorInvestidoFmt = porcentagemValorInvestidoFmt;
        this.ultimaCotacao = ultimaCotacao;
        this.ultimaCotacaoFmt = ultimaCotacaoFmt;
        this.dataUltimaCotacao = dataUltimaCotacao;
        this.dataUltimaCotacaoFmt = dataUltimaCotacaoFmt;
        this.quantidadeCotas = quantidadeCotas;
        this.quantidadeCotasFmt = quantidadeCotasFmt;
    }

    public static SimulacaoDetailInvestimentoAtivoDTO from(SimulaDetailInvestimentoAcao entity) {
        return SimulacaoDetailInvestimentoAtivoDTO.builder()
                                                    .sigla(entity.getSigla())
                                                    .valorInvestido(entity.getValorInvestido())
                                                    .valorInvestidoFmt(Utils.converterDoubleDoisDecimaisString(entity.getValorInvestido()))
                                                    .porcentagemValorInvestido(entity.getPorcentagemValorInvestido())
                                                    .porcentagemValorInvestidoFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getPorcentagemValorInvestido()))
                                                    .ultimaCotacao(entity.getUltimaCotacaoAcao())
                                                    .ultimaCotacaoFmt(Utils.converterDoubleDoisDecimaisString(entity.getUltimaCotacaoAcao()))
                                                    .quantidadeCotas(entity.getQuantidadeCotasAcao())
                                                    .quantidadeCotasFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getQuantidadeCotasAcao()))
                                                    .dataUltimaCotacao(entity.getDataUltimaCotacaoAcao())
                                                    .dataUltimaCotacaoFmt(Utils.converteLocalDateToString(entity.getDataUltimaCotacaoAcao()))
                                                 .build();
    }

    public static SimulacaoDetailInvestimentoAtivoDTO from(SimulaDetailInvestimentoBdr entity) {
        return SimulacaoDetailInvestimentoAtivoDTO.builder()
                .sigla(entity.getSigla())
                .valorInvestido(entity.getValorInvestido())
                .valorInvestidoFmt(Utils.converterDoubleDoisDecimaisString(entity.getValorInvestido()))
                .porcentagemValorInvestido(entity.getPorcentagemValorInvestido())
                .porcentagemValorInvestidoFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getPorcentagemValorInvestido()))
                .ultimaCotacao(entity.getUltimaCotacaoBdr())
                .ultimaCotacaoFmt(Utils.converterDoubleDoisDecimaisString(entity.getUltimaCotacaoBdr()))
                .quantidadeCotas(entity.getQuantidadeCotasBdr())
                .quantidadeCotasFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getQuantidadeCotasBdr()))
                .dataUltimaCotacao(entity.getDataUltimaCotacaoBdr())
                .dataUltimaCotacaoFmt(Utils.converteLocalDateToString(entity.getDataUltimaCotacaoBdr()))
                .build();
    }

    public static SimulacaoDetailInvestimentoAtivoDTO from(SimulaDetailInvestimentoFundoImobiliario entity) {
        return SimulacaoDetailInvestimentoAtivoDTO.builder()
                .sigla(entity.getSigla())
                .valorInvestido(entity.getValorInvestido())
                .valorInvestidoFmt(Utils.converterDoubleDoisDecimaisString(entity.getValorInvestido()))
                .porcentagemValorInvestido(entity.getPorcentagemValorInvestido())
                .porcentagemValorInvestidoFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getPorcentagemValorInvestido()))
                .ultimaCotacao(entity.getUltimaCotacaoFundo())
                .ultimaCotacaoFmt(Utils.converterDoubleDoisDecimaisString(entity.getUltimaCotacaoFundo()))
                .quantidadeCotas(entity.getQuantidadeCotasFundo())
                .quantidadeCotasFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getQuantidadeCotasFundo()))
                .dataUltimaCotacao(entity.getDataUltimaCotacaoFundo())
                .dataUltimaCotacaoFmt(Utils.converteLocalDateToString(entity.getDataUltimaCotacaoFundo()))
                .build();
    }
}
