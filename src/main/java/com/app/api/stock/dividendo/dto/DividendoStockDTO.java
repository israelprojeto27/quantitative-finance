package com.app.api.stock.dividendo.dto;

import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DividendoStockDTO {

    private String sigla;

    private String data;

    private Double dividend;

    public DividendoStockDTO() {
    }

    public DividendoStockDTO(String sigla, String data, Double dividend) {
        this.sigla = sigla;
        this.data = data;
        this.dividend = dividend;
    }

    public static DividendoStockDTO fromEntity(DividendoStock entity) {
        return DividendoStockDTO.builder()
                .sigla(entity.getStock().getSigla())
                .data(Utils.converteLocalDateToString(entity.getData()))
                .dividend((entity.getDividend()))
                .build();
    }
}
