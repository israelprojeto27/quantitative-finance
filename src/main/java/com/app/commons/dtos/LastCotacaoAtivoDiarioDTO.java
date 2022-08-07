package com.app.commons.dtos;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LastCotacaoAtivoDiarioDTO {

    private Double valorUltimaCotacao;

    private String dataUltimaCotacao;

    public LastCotacaoAtivoDiarioDTO() {
    }

    public LastCotacaoAtivoDiarioDTO(Double valorUltimaCotacao, String dataUltimaCotacao) {
        this.valorUltimaCotacao = valorUltimaCotacao;
        this.dataUltimaCotacao = dataUltimaCotacao;
    }

    public static LastCotacaoAtivoDiarioDTO from(CotacaoAcaoDiario cotacaoDiario) {
        return LastCotacaoAtivoDiarioDTO.builder()
                                   .valorUltimaCotacao(cotacaoDiario.getClose())
                                   .dataUltimaCotacao(Utils.converteLocalDateToString(cotacaoDiario.getData()))
                                   .build();
    }

    public static LastCotacaoAtivoDiarioDTO from(CotacaoBdrDiario cotacaoDiario) {
        return LastCotacaoAtivoDiarioDTO.builder()
                .valorUltimaCotacao(cotacaoDiario.getClose())
                .dataUltimaCotacao(Utils.converteLocalDateToString(cotacaoDiario.getData()))
                .build();
    }

    public static LastCotacaoAtivoDiarioDTO from(CotacaoFundoDiario cotacaoDiario) {
        return LastCotacaoAtivoDiarioDTO.builder()
                .valorUltimaCotacao(cotacaoDiario.getClose())
                .dataUltimaCotacao(Utils.converteLocalDateToString(cotacaoDiario.getData()))
                .build();
    }
}
