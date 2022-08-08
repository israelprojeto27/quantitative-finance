package com.app.api.bdr.cotacao.dto;


import com.app.commons.dtos.DividendoDTO;
import com.app.commons.dtos.IncreasePercentAtivoDTO;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.cotacao.entities.CotacaoBdrSemanal;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.increasepercent.IncreasePercentBdr;
import com.app.api.bdr.principal.entity.Bdr;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class BdrCotacaoDTO {

    private Long id;

    private String sigla;

    private Integer quantCotacoesDiarias = 0;
    private Integer quantCotacoesSemanais = 0;
    private Integer quantCotacoesMensais = 0;

    private List<CotacaoBdrDiarioDTO> listCotacaoDiario ;
    private List<CotacaoBdrSemanalDTO> listCotacaoSemanal;
    private List<CotacaoBdrMensalDTO> listCotacaoMensal;

    private List<IncreasePercentAtivoDTO> listIncreasePercentDiario;
    private List<IncreasePercentAtivoDTO> listIncreasePercentSemanal;
    private List<IncreasePercentAtivoDTO> listIncreasePercentMensal;

    private List<DividendoDTO> listDividendos;

    public BdrCotacaoDTO() {
    }

    public BdrCotacaoDTO(Long id, String sigla, Integer quantCotacoesDiarias, Integer quantCotacoesSemanais, Integer quantCotacoesMensais, List<CotacaoBdrDiarioDTO> listCotacaoDiario, List<CotacaoBdrSemanalDTO> listCotacaoSemanal, List<CotacaoBdrMensalDTO> listCotacaoMensal, List<IncreasePercentAtivoDTO> listIncreasePercentDiario, List<IncreasePercentAtivoDTO> listIncreasePercentSemanal, List<IncreasePercentAtivoDTO> listIncreasePercentMensal, List<DividendoDTO> listDividendos) {
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
    }

    public static BdrCotacaoDTO fromEntity(Bdr entity, List<CotacaoBdrDiario> listCotacaoDiario, List<CotacaoBdrSemanal> listCotacaoSemanal, List<CotacaoBdrMensal> listCotacaoMensal) {
        return BdrCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoBdrDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoBdrSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoBdrMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .build();
    }

    public static BdrCotacaoDTO fromEntity(Bdr entity, List<CotacaoBdrDiario> listCotacaoDiario, List<CotacaoBdrSemanal> listCotacaoSemanal, List<CotacaoBdrMensal> listCotacaoMensal, List<IncreasePercentBdr> listIncreasePercentDiario, List<IncreasePercentBdr> listIncreasePercentSemanal, List<IncreasePercentBdr> listIncreasePercentMensal, List<DividendoBdr> listaDividendos) {
        return BdrCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .listCotacaoDiario(listCotacaoDiario != null && !listCotacaoDiario.isEmpty() ? listCotacaoDiario.stream().map(CotacaoBdrDiarioDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoSemanal(listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty() ? listCotacaoSemanal.stream().map(CotacaoBdrSemanalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listCotacaoMensal(listCotacaoMensal != null && !listCotacaoMensal.isEmpty() ? listCotacaoMensal.stream().map(CotacaoBdrMensalDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentDiario(listIncreasePercentDiario != null && !listIncreasePercentDiario.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentSemanal(listIncreasePercentSemanal != null && !listIncreasePercentSemanal.isEmpty() ? listIncreasePercentDiario.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listIncreasePercentMensal(listIncreasePercentMensal != null && !listIncreasePercentMensal.isEmpty() ? listIncreasePercentMensal.stream().map(IncreasePercentAtivoDTO::fromEntity).collect(Collectors.toList()) : null)
                .listDividendos(listaDividendos != null && !listaDividendos.isEmpty() ? listaDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()) : null)
                .quantCotacoesDiarias(listCotacaoDiario!= null ? listCotacaoDiario.size() : 0)
                .quantCotacoesSemanais(listCotacaoSemanal!= null ? listCotacaoSemanal.size() : 0)
                .quantCotacoesMensais(listCotacaoMensal != null ? listCotacaoMensal.size() : 0)
                .build();
    }
}
