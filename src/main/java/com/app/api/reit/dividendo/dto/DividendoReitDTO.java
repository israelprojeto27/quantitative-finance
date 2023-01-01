package com.app.api.reit.dividendo.dto;

import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DividendoReitDTO {

    private String sigla;

    private String data;

    private Double dividend;

    public DividendoReitDTO() {
    }

    public DividendoReitDTO(String sigla, String data, Double dividend) {
        this.sigla = sigla;
        this.data = data;
        this.dividend = dividend;
    }

    public static DividendoReitDTO fromEntity(DividendoReit entity) {
        return DividendoReitDTO.builder()
                .sigla(entity.getReit().getSigla())
                .data(Utils.converteLocalDateToString(entity.getData()))
                .dividend((entity.getDividend()))
                .build();
    }
}
