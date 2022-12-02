package com.app.commons.dtos.dividendo;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.api.stock.principal.entity.Stock;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SumAtivoDividendosDTO {

    private String sigla;

    private Double sumDividendo;


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

    public static SumAtivoDividendosDTO from(Bdr bdr, double sumDividendos) {
        return SumAtivoDividendosDTO.builder()
                .sigla(bdr.getSigla())
                .sumDividendo(Utils.converterDoubleSeisDecimais(sumDividendos))
                .build();
    }

    public static SumAtivoDividendosDTO from(Stock stock, double sumDividendos) {
        return SumAtivoDividendosDTO.builder()
                .sigla(stock.getSigla())
                .sumDividendo(Utils.converterDoubleSeisDecimais(sumDividendos))
                .build();
    }
}
