package com.app.api.bdr.cotacao.dto;


import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.cotacao.entities.CotacaoBdrSemanal;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.cotacao.dto.CotacaoFundoDiarioDTO;
import com.app.api.fundoimobiliario.cotacao.dto.CotacaoFundoMensalDTO;
import com.app.api.fundoimobiliario.cotacao.dto.CotacaoFundoSemanalDTO;
import com.app.api.fundoimobiliario.cotacao.dto.FundoCotacaoDTO;
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
public class BdrCotacaoDTO {

    private Long id;

    private String sigla;

    private Integer quantCotacoesDiarias = 0;
    private Integer quantCotacoesSemanais = 0;
    private Integer quantCotacoesMensais = 0;

    private List<CotacaoBdrDiarioDTO> listCotacaoDiario ;
    private List<CotacaoBdrSemanalDTO> listCotacaoSemanal;
    private List<CotacaoBdrMensalDTO> listCotacaoMensal;

    public BdrCotacaoDTO() {
    }

    public BdrCotacaoDTO(Long id, String sigla, Integer quantCotacoesDiarias, Integer quantCotacoesSemanais, Integer quantCotacoesMensais, List<CotacaoBdrDiarioDTO> listCotacaoDiario, List<CotacaoBdrSemanalDTO> listCotacaoSemanal, List<CotacaoBdrMensalDTO> listCotacaoMensal) {
        this.id = id;
        this.sigla = sigla;
        this.quantCotacoesDiarias = quantCotacoesDiarias;
        this.quantCotacoesSemanais = quantCotacoesSemanais;
        this.quantCotacoesMensais = quantCotacoesMensais;
        this.listCotacaoDiario = listCotacaoDiario;
        this.listCotacaoSemanal = listCotacaoSemanal;
        this.listCotacaoMensal = listCotacaoMensal;
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
}
