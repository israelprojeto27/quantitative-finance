package com.app.api.fundoimobiliario.cotacao.dto;


import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoSemanal;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.increasepercent.IncreasePercentFundoImobiliario;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.dtos.DividendoDTO;
import com.app.commons.dtos.IncreasePercentAtivoDTO;
import com.app.commons.dtos.dividendo.RoiDividendoCotacaoDTO;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class FundoCotacaoDTO {

    private Long id;

    private String sigla;

    private Integer quantCotacoesDiarias = 0;
    private Integer quantCotacoesSemanais = 0;
    private Integer quantCotacoesMensais = 0;

    private List<CotacaoFundoDiarioDTO> listCotacaoDiario ;
    private List<CotacaoFundoSemanalDTO> listCotacaoSemanal;
    private List<CotacaoFundoMensalDTO> listCotacaoMensal;

    private List<IncreasePercentAtivoDTO> listIncreasePercentDiario;
    private List<IncreasePercentAtivoDTO> listIncreasePercentSemanal;
    private List<IncreasePercentAtivoDTO> listIncreasePercentMensal;

    private List<DividendoDTO> listDividendos;

    private List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao;

    private String dividendYield;

    public FundoCotacaoDTO() {
    }

    public FundoCotacaoDTO(Long id, String sigla, Integer quantCotacoesDiarias, Integer quantCotacoesSemanais, Integer quantCotacoesMensais, List<CotacaoFundoDiarioDTO> listCotacaoDiario, List<CotacaoFundoSemanalDTO> listCotacaoSemanal, List<CotacaoFundoMensalDTO> listCotacaoMensal, List<IncreasePercentAtivoDTO> listIncreasePercentDiario, List<IncreasePercentAtivoDTO> listIncreasePercentSemanal, List<IncreasePercentAtivoDTO> listIncreasePercentMensal, List<DividendoDTO> listDividendos, List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao, String dividendYield) {
        this.id = id;
        this.sigla = sigla;
        this.quantCotacoesDiarias = quantCotacoesDiarias;
        this.quantCotacoesSemanais = quantCotacoesSemanais;
        this.quantCotacoesMensais = quantCotacoesMensais;
        this.listCotacaoDiario = listCotacaoDiario;
        this.listCotacaoSemanal = listCotacaoSemanal;
        this.listCotacaoMensal = listCotacaoMensal;
        this.listIncreasePercentDiario = listIncreasePercentDiario;
        this.listIncreasePercentSemanal = listIncreasePercentSemanal;
        this.listIncreasePercentMensal = listIncreasePercentMensal;
        this.listDividendos = listDividendos;
        this.listRoiDividendoCotacao = listRoiDividendoCotacao;
        this.dividendYield = dividendYield;
    }

    public static FundoCotacaoDTO fromEntity(FundoImobiliario entity, List<CotacaoFundoDiario> listCotacaoDiario, List<CotacaoFundoSemanal> listCotacaoSemanal, List<CotacaoFundoMensal> listCotacaoMensal) {
        return FundoCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoFundoDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoFundoSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoFundoMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .build();
    }

    public static FundoCotacaoDTO fromEntity(FundoImobiliario entity, List<CotacaoFundoDiario> listCotacaoDiario, List<CotacaoFundoSemanal> listCotacaoSemanal, List<CotacaoFundoMensal> listCotacaoMensal, List<IncreasePercentFundoImobiliario> listIncreasePercentDiario, List<IncreasePercentFundoImobiliario> listIncreasePercentSemanal, List<IncreasePercentFundoImobiliario> listIncreasePercentMensal, List<DividendoFundo> listaDividendos) {
        return FundoCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoFundoDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoFundoSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoFundoMensalDTO::fromEntity).collect(Collectors.toList()) : null)

                .listIncreasePercentDiario(listIncreasePercentDiario != null && !listIncreasePercentDiario.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentSemanal(listIncreasePercentSemanal != null && !listIncreasePercentSemanal.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentMensal(listIncreasePercentMensal != null && !listIncreasePercentMensal.isEmpty() ? listIncreasePercentMensal.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listDividendos(listaDividendos != null && !listaDividendos.isEmpty() ? listaDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()) : null)

                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .build();
    }

    public static FundoCotacaoDTO fromEntity(FundoImobiliario entity, List<CotacaoFundoDiario> listCotacaoDiario, List<CotacaoFundoSemanal> listCotacaoSemanal, List<CotacaoFundoMensal> listCotacaoMensal, List<IncreasePercentFundoImobiliario> listIncreasePercentDiario, List<IncreasePercentFundoImobiliario> listIncreasePercentSemanal, List<IncreasePercentFundoImobiliario> listIncreasePercentMensal, List<DividendoFundo> listaDividendos, List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao) {
        return FundoCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoFundoDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoFundoSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoFundoMensalDTO::fromEntity).collect(Collectors.toList()) : null)

                .listIncreasePercentDiario(listIncreasePercentDiario != null && !listIncreasePercentDiario.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentSemanal(listIncreasePercentSemanal != null && !listIncreasePercentSemanal.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentMensal(listIncreasePercentMensal != null && !listIncreasePercentMensal.isEmpty() ? listIncreasePercentMensal.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listDividendos(listaDividendos != null && !listaDividendos.isEmpty() ? listaDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()) : null)

                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .listRoiDividendoCotacao(listRoiDividendoCotacao)
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .build();
    }
}
