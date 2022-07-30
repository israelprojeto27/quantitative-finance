package com.app.api.fundoimobiliario.cotacao.dto;


import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoSemanal;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
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

    public FundoCotacaoDTO() {
    }

    public FundoCotacaoDTO(Long id, String sigla, Integer quantCotacoesDiarias, Integer quantCotacoesSemanais, Integer quantCotacoesMensais, List<CotacaoFundoDiarioDTO> listCotacaoDiario, List<CotacaoFundoSemanalDTO> listCotacaoSemanal, List<CotacaoFundoMensalDTO> listCotacaoMensal) {
        this.id = id;
        this.sigla = sigla;
        this.quantCotacoesDiarias = quantCotacoesDiarias;
        this.quantCotacoesSemanais = quantCotacoesSemanais;
        this.quantCotacoesMensais = quantCotacoesMensais;
        this.listCotacaoDiario = listCotacaoDiario;
        this.listCotacaoSemanal = listCotacaoSemanal;
        this.listCotacaoMensal = listCotacaoMensal;
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
                .build();
    }
}
