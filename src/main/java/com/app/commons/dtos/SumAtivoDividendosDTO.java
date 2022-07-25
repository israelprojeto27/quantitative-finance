package com.app.commons.dtos;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SumAtivoDividendosDTO {

    private String sigla;

    private Double sumDividendo;

    public SumAtivoDividendosDTO() {
    }

    public SumAtivoDividendosDTO(String sigla, Double sumDividendo) {
        this.sigla = sigla;
        this.sumDividendo = sumDividendo;
    }

    public static SumAtivoDividendosDTO from(Acao acao, double sumDividendos) {
        return SumAtivoDividendosDTO.builder()
                .sigla(acao.getSigla())
                .sumDividendo(Utils.converterDoubleSeisDecimais(sumDividendos))
                .build();
    }

    public static SumAtivoDividendosDTO from(FundoImobiliario fundoImobiliario, double sumDividendos) {
        return SumAtivoDividendosDTO.builder()
                .sigla(fundoImobiliario.getSigla())
                .sumDividendo(Utils.converterDoubleSeisDecimais(sumDividendos))
                .build();
    }
}
