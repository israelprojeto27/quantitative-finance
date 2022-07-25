package com.app.api.fundoimobiliario.dividendo.dto;

import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DividendoDTO {

    private String data;

    private Double dividend;

    public DividendoDTO() {
    }

    public DividendoDTO(String data, Double dividend) {
        this.data = data;
        this.dividend = dividend;
    }

    public static DividendoDTO from(DividendoFundo dividendoFundo) {
        return DividendoDTO.builder()
                .data(Utils.converteLocalDateToString(dividendoFundo.getData()))
                .dividend(dividendoFundo.getDividend())
                .build();
    }
}
