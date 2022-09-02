package com.app.api.acao.cotacao.dto;


import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.increasepercent.IncreasePercentAcao;
import com.app.commons.dtos.IncreasePercentAtivoDTO;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.commons.dtos.dividendo.RoiDividendoCotacaoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import com.app.commons.dtos.DividendoDTO;

@Builder
@Data
public class AcaoCotacaoDTO {

    private Long id;

    private String sigla;

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

    public AcaoCotacaoDTO() {
    }

    public AcaoCotacaoDTO(Long id, String sigla, Integer quantCotacoesDiarias, Integer quantCotacoesSemanais, Integer quantCotacoesMensais, List<CotacaoAcaoDiarioDTO> listCotacaoDiario, List<CotacaoAcaoSemanalDTO> listCotacaoSemanal, List<CotacaoAcaoMensalDTO> listCotacaoMensal, List<IncreasePercentAtivoDTO> listIncreasePercentDiario, List<IncreasePercentAtivoDTO> listIncreasePercentSemanal, List<IncreasePercentAtivoDTO> listIncreasePercentMensal, List<DividendoDTO> listDividendos, List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao) {
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
    }

    public static AcaoCotacaoDTO fromEntity(Acao entity, List<CotacaoAcaoDiario> listCotacaoDiario, List<CotacaoAcaoSemanal> listCotacaoSemanal, List<CotacaoAcaoMensal> listCotacaoMensal) {
        return AcaoCotacaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
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
