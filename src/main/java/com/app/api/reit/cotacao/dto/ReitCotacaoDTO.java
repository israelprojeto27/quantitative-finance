package com.app.api.reit.cotacao.dto;


import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.reit.cotacao.entities.CotacaoReitMensal;
import com.app.api.reit.cotacao.entities.CotacaoReitSemanal;
import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.reit.increasepercent.IncreasePercentReit;
import com.app.api.reit.principal.entity.Reit;
import com.app.api.stock.dividendo.entity.DividendoStock;
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
public class ReitCotacaoDTO {

    private Long id;

    private String sigla;

    private String dividendYield;

    private Integer quantCotacoesDiarias = 0;
    private Integer quantCotacoesSemanais = 0;
    private Integer quantCotacoesMensais = 0;

    private List<CotacaoReitDiarioDTO> listCotacaoDiario ;
    private List<CotacaoReitSemanalDTO> listCotacaoSemanal;
    private List<CotacaoReitMensalDTO> listCotacaoMensal;

    private List<IncreasePercentAtivoDTO> listIncreasePercentDiario;
    private List<IncreasePercentAtivoDTO> listIncreasePercentSemanal;
    private List<IncreasePercentAtivoDTO> listIncreasePercentMensal;

    private List<DividendoDTO> listDividendos;

    private List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao;


    public static ReitCotacaoDTO fromEntity(Reit entity, List<CotacaoReitDiario> listCotacaoDiario, List<CotacaoReitSemanal> listCotacaoSemanal, List<CotacaoReitMensal> listCotacaoMensal) {
        return ReitCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoReitDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoReitSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoReitMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .build();
    }

    public static ReitCotacaoDTO fromEntity(Reit entity, List<CotacaoReitDiario> listCotacaoDiario, List<CotacaoReitSemanal> listCotacaoSemanal, List<CotacaoReitMensal> listCotacaoMensal, List<IncreasePercentReit> listIncreasePercentDiario, List<IncreasePercentReit> listIncreasePercentSemanal, List<IncreasePercentReit> listIncreasePercentMensal, List<DividendoStock> listaDividendos) {

        return ReitCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoReitDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoReitSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoReitMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentDiario(listIncreasePercentDiario != null && !listIncreasePercentDiario.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentSemanal(listIncreasePercentSemanal != null && !listIncreasePercentSemanal.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentMensal(listIncreasePercentMensal != null && !listIncreasePercentMensal.isEmpty() ? listIncreasePercentMensal.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listDividendos(listaDividendos != null && !listaDividendos.isEmpty() ? listaDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .build();

    }

    public static ReitCotacaoDTO fromEntity(Reit entity, List<CotacaoReitDiario> listCotacaoDiario, List<CotacaoReitSemanal> listCotacaoSemanal, List<CotacaoReitMensal> listCotacaoMensal, List<IncreasePercentReit> listIncreasePercentDiario, List<IncreasePercentReit> listIncreasePercentSemanal, List<IncreasePercentReit> listIncreasePercentMensal, List<DividendoReit> listaDividendos, List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao) {

        return ReitCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoReitDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoReitSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoReitMensalDTO::fromEntity).collect(Collectors.toList()) : null)
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
