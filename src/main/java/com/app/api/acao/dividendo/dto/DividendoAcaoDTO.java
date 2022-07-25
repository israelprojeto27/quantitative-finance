package com.app.api.acao.dividendo.dto;

import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DividendoAcaoDTO {

    private String sigla;

    private String data;

    private Double dividend;

    public DividendoAcaoDTO() {
    }

    public DividendoAcaoDTO(String sigla, String data, Double dividend) {
        this.sigla = sigla;
        this.data = data;
        this.dividend = dividend;
    }

    public static DividendoAcaoDTO fromEntity(DividendoAcao entity) {
        return DividendoAcaoDTO.builder()
                .sigla(entity.getAcao().getSigla())
                .data(Utils.converteLocalDateToString(entity.getData()))
                .dividend((entity.getDividend()))
                .build();
    }
}
