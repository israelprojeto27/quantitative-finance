package com.app.api.stock.cotacao.dto;


import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.cotacao.entities.CotacaoStockMensal;
import com.app.api.stock.cotacao.entities.CotacaoStockSemanal;
import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.api.stock.increasepercent.IncreasePercentStock;
import com.app.api.stock.principal.entity.Stock;
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
public class StockCotacaoDTO {

    private Long id;

    private String sigla;

    private String dividendYield;

    private Integer quantCotacoesDiarias = 0;
    private Integer quantCotacoesSemanais = 0;
    private Integer quantCotacoesMensais = 0;

    private List<CotacaoStockDiarioDTO> listCotacaoDiario ;
    private List<CotacaoStockSemanalDTO> listCotacaoSemanal;
    private List<CotacaoStockMensalDTO> listCotacaoMensal;

    private List<IncreasePercentAtivoDTO> listIncreasePercentDiario;
    private List<IncreasePercentAtivoDTO> listIncreasePercentSemanal;
    private List<IncreasePercentAtivoDTO> listIncreasePercentMensal;

    private List<DividendoDTO> listDividendos;

    private List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao;


    public static StockCotacaoDTO fromEntity(Stock entity, List<CotacaoStockDiario> listCotacaoDiario, List<CotacaoStockSemanal> listCotacaoSemanal, List<CotacaoStockMensal> listCotacaoMensal) {
        return StockCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoStockDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoStockSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoStockMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .build();
    }

    public static StockCotacaoDTO fromEntity(Stock entity, List<CotacaoStockDiario> listCotacaoDiario, List<CotacaoStockSemanal> listCotacaoSemanal, List<CotacaoStockMensal> listCotacaoMensal, List<IncreasePercentStock> listIncreasePercentDiario, List<IncreasePercentStock> listIncreasePercentSemanal, List<IncreasePercentStock> listIncreasePercentMensal, List<DividendoStock> listaDividendos) {

        return StockCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoStockDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoStockSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoStockMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentDiario(listIncreasePercentDiario != null && !listIncreasePercentDiario.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentSemanal(listIncreasePercentSemanal != null && !listIncreasePercentSemanal.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentMensal(listIncreasePercentMensal != null && !listIncreasePercentMensal.isEmpty() ? listIncreasePercentMensal.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listDividendos(listaDividendos != null && !listaDividendos.isEmpty() ? listaDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .build();

    }

    public static StockCotacaoDTO fromEntity(Stock entity, List<CotacaoStockDiario> listCotacaoDiario, List<CotacaoStockSemanal> listCotacaoSemanal, List<CotacaoStockMensal> listCotacaoMensal, List<IncreasePercentStock> listIncreasePercentDiario, List<IncreasePercentStock> listIncreasePercentSemanal, List<IncreasePercentStock> listIncreasePercentMensal, List<DividendoStock> listaDividendos, List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao) {

        return StockCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()): "")
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoStockDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoStockSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoStockMensalDTO::fromEntity).collect(Collectors.toList()) : null)
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
