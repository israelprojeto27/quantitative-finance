package com.app.commons.basic.simulacao.dto;

import com.app.api.acao.simulacao.entities.SimulaDetailInvestimentoAcao;
import com.app.api.bdr.simulacao.entities.SimulaDetailInvestimentoBdr;
import com.app.api.fundoimobiliario.simulacao.entities.SimulaDetailInvestimentoFundoImobiliario;
import com.app.api.stock.simulacao.entities.SimulaDetailInvestimentoStock;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
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

    public static SimulacaoDetailInvestimentoAtivoDTO from(SimulaDetailInvestimentoStock entity) {
        return SimulacaoDetailInvestimentoAtivoDTO.builder()
                .sigla(entity.getSigla())
                .valorInvestido(entity.getValorInvestido())
                .valorInvestidoFmt(Utils.converterDoubleDoisDecimaisString(entity.getValorInvestido()))
                .porcentagemValorInvestido(entity.getPorcentagemValorInvestido())
                .porcentagemValorInvestidoFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getPorcentagemValorInvestido()))
                .ultimaCotacao(entity.getUltimaCotacaoStock())
                .ultimaCotacaoFmt(Utils.converterDoubleDoisDecimaisString(entity.getUltimaCotacaoStock()))
                .quantidadeCotas(entity.getQuantidadeCotasStock())
                .quantidadeCotasFmt(Utils.converteDoubleToStringValorAbsoluto(entity.getQuantidadeCotasStock()))
                .dataUltimaCotacao(entity.getDataUltimaCotacaoStock())
                .dataUltimaCotacaoFmt(Utils.converteLocalDateToString(entity.getDataUltimaCotacaoStock()))
                .build();
    }
}
