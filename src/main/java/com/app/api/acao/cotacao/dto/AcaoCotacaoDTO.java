package com.app.api.acao.cotacao.dto;


import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.increasepercent.IncreasePercentAcao;
import com.app.commons.dtos.IncreasePercentAtivoDTO;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.commons.dtos.dividendo.RoiDividendoCotacaoDTO;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import com.app.commons.dtos.DividendoDTO;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AcaoCotacaoDTO {

    private Long id;

    private String sigla;

    private String dividendYield;

    private Integer quantCotacoesDiarias = 0;
    private Integer quantCotacoesSemanais = 0;
    private Integer quantCotacoesMensais = 0;

    private List<CotacaoAcaoDiarioDTO> listCotacaoDiario ;
    private List<CotacaoAcaoSemanalDTO> listCotacaoSemanal;
    private List<CotacaoAcaoMensalDTO> listCotacaoMensal;

    private List<IncreasePercentAtivoDTO> listIncreasePercentDiario;
    private List<IncreasePercentAtivoDTO> listIncreasePercentSemanal;
    private List<IncreasePercentAtivoDTO> listIncreasePercentMensal;

    private List<DividendoDTO> listDividendos;

    private List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao;


    public static AcaoCotacaoDTO fromEntity(Acao entity, List<CotacaoAcaoDiario> listCotacaoDiario, List<CotacaoAcaoSemanal> listCotacaoSemanal, List<CotacaoAcaoMensal> listCotacaoMensal) {
        return AcaoCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoAcaoDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoAcaoSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoAcaoMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .build();
    }

    public static AcaoCotacaoDTO fromEntity(Acao entity, List<CotacaoAcaoDiario> listCotacaoDiario, List<CotacaoAcaoSemanal> listCotacaoSemanal, List<CotacaoAcaoMensal> listCotacaoMensal, List<IncreasePercentAcao> listIncreasePercentDiario, List<IncreasePercentAcao> listIncreasePercentSemanal, List<IncreasePercentAcao> listIncreasePercentMensal, List<DividendoAcao> listaDividendos) {

        return AcaoCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoAcaoDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoAcaoSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoAcaoMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentDiario(listIncreasePercentDiario != null && !listIncreasePercentDiario.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentSemanal(listIncreasePercentSemanal != null && !listIncreasePercentSemanal.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentMensal(listIncreasePercentMensal != null && !listIncreasePercentMensal.isEmpty() ? listIncreasePercentMensal.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listDividendos(listaDividendos != null && !listaDividendos.isEmpty() ? listaDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .build();

    }

    public static AcaoCotacaoDTO fromEntity(Acao entity, List<CotacaoAcaoDiario> listCotacaoDiario, List<CotacaoAcaoSemanal> listCotacaoSemanal, List<CotacaoAcaoMensal> listCotacaoMensal, List<IncreasePercentAcao> listIncreasePercentDiario, List<IncreasePercentAcao> listIncreasePercentSemanal, List<IncreasePercentAcao> listIncreasePercentMensal, List<DividendoAcao> listaDividendos, List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao) {

        return AcaoCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoAcaoDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoAcaoSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoAcaoMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentDiario(listIncreasePercentDiario != null && !listIncreasePercentDiario.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentSemanal(listIncreasePercentSemanal != null && !listIncreasePercentSemanal.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentMensal(listIncreasePercentMensal != null && !listIncreasePercentMensal.isEmpty() ? listIncreasePercentMensal.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listDividendos(listaDividendos != null && !listaDividendos.isEmpty() ? listaDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .listRoiDividendoCotacao(listRoiDividendoCotacao)
                .build();

    }
}
