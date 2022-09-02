package com.app.commons.dtos;

import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SumIncreasePercentCotacaoDTO {

    private String sigla;

    private Double sumIncreasePercent;

    private String sumIncreasePercentFmt;

    public SumIncreasePercentCotacaoDTO() {
    }

    public SumIncreasePercentCotacaoDTO(String sigla, Double sumIncreasePercent, String sumIncreasePercentFmt) {
        this.sigla = sigla;
        this.sumIncreasePercent = sumIncreasePercent;
        this.sumIncreasePercentFmt = sumIncreasePercentFmt;
    }

    public static SumIncreasePercentCotacaoDTO from(String sigla, Double valorPercentGrow) {
        return SumIncreasePercentCotacaoDTO.builder()
                                            .sigla(sigla)
                                            .sumIncreasePercent(Utils.converterDoubleQuatroDecimais(valorPercentGrow))
                                            .sumIncreasePercentFmt(Utils.converterDoubleQuatroDecimaisString(valorPercentGrow))
                                            .build();
    }
}
