package com.app.api.fundoimobiliario.dividendo.dto;

import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DividendoFundoDTO {

    private String sigla;

    private String data;

    private Double dividend;

    public DividendoFundoDTO() {
    }

    public DividendoFundoDTO(String sigla, String data, Double dividend) {
        this.sigla = sigla;
        this.data = data;
        this.dividend = dividend;
    }

    public static DividendoFundoDTO fromEntity(DividendoFundo entity) {
        return DividendoFundoDTO.builder()
                .sigla(entity.getFundo().getSigla())
                .data(Utils.converteLocalDateToString(entity.getData()))
                .dividend((entity.getDividend()))
                .build();
    }
}
