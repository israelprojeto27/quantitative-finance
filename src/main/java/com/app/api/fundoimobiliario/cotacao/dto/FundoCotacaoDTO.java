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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
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

    private String pvp;
    private String dividendoCota;
    private String ffoYield;
    private String ffoCota;
    private String vpCota;
    private String valorMercado;
    private String nroCota;
    private String qtdImoveis;
    private String capRate;
    private String qtdUnid;
    private String aluguelM2;
    private String vacanciaMedia;
    private String imoveisPl;
    private String precoM2;



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

                .pvp(entity.getPvp() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getPvp()): "")
                .dividendoCota(entity.getDividendoCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendoCota()): "")
                .ffoYield(entity.getFfoYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getFfoYield()): "")
                .ffoCota(entity.getFfoCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getFfoCota()): "")
                .vpCota(entity.getVpCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getVpCota()): "")
                .valorMercado(entity.getValorMercado() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getValorMercado()): "")
                .nroCota(entity.getNroCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getNroCota()): "")
                .qtdImoveis(entity.getQtdImoveis() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getQtdImoveis()): "")
                .capRate(entity.getCapRate() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getCapRate()): "")
                .qtdUnid(entity.getQtdUnid() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getQtdUnid()): "")
                .aluguelM2(entity.getAluguelM2() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getAluguelM2()): "")
                .vacanciaMedia(entity.getVacanciaMedia() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getVacanciaMedia()): "")
                .imoveisPl(entity.getImoveisPl() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getImoveisPl()): "")
                .precoM2(entity.getPrecoM2() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getPrecoM2()): "")
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

                .pvp(entity.getPvp() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getPvp()): "")
                .dividendoCota(entity.getDividendoCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendoCota()): "")
                .ffoYield(entity.getFfoYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getFfoYield()): "")
                .ffoCota(entity.getFfoCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getFfoCota()): "")
                .vpCota(entity.getVpCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getVpCota()): "")
                .valorMercado(entity.getValorMercado() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getValorMercado()): "")
                .nroCota(entity.getNroCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getNroCota()): "")
                .qtdImoveis(entity.getQtdImoveis() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getQtdImoveis()): "")
                .capRate(entity.getCapRate() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getCapRate()): "")
                .qtdUnid(entity.getQtdUnid() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getQtdUnid()): "")
                .aluguelM2(entity.getAluguelM2() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getAluguelM2()): "")
                .vacanciaMedia(entity.getVacanciaMedia() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getVacanciaMedia()): "")
                .imoveisPl(entity.getImoveisPl() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getImoveisPl()): "")
                .precoM2(entity.getPrecoM2() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getPrecoM2()): "")
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

                .pvp(entity.getPvp() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getPvp()): "")
                .dividendoCota(entity.getDividendoCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendoCota()): "")
                .ffoYield(entity.getFfoYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getFfoYield()): "")
                .ffoCota(entity.getFfoCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getFfoCota()): "")
                .vpCota(entity.getVpCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getVpCota()): "")
                .valorMercado(entity.getValorMercado() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getValorMercado()): "")
                .nroCota(entity.getNroCota() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getNroCota()): "")
                .qtdImoveis(entity.getQtdImoveis() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getQtdImoveis()): "")
                .capRate(entity.getCapRate() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getCapRate()): "")
                .qtdUnid(entity.getQtdUnid() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getQtdUnid()): "")
                .aluguelM2(entity.getAluguelM2() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getAluguelM2()): "")
                .vacanciaMedia(entity.getVacanciaMedia() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getVacanciaMedia()): "")
                .imoveisPl(entity.getImoveisPl() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getImoveisPl()): "")
                .precoM2(entity.getPrecoM2() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getPrecoM2()): "")
                .build();
    }
}
