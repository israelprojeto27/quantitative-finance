package com.app.api.bdr.dividendo.dto;

import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DividendoBdrDTO {

    private String sigla;

    private String data;

    private Double dividend;

    public DividendoBdrDTO() {
    }

    public DividendoBdrDTO(String sigla, String data, Double dividend) {
        this.sigla = sigla;
        this.data = data;
        this.dividend = dividend;
    }

    public static DividendoBdrDTO fromEntity(DividendoBdr entity) {
        return DividendoBdrDTO.builder()
                .sigla(entity.getBdr().getSigla())
                .data(Utils.converteLocalDateToString(entity.getData()))
                .dividend((entity.getDividend()))
                .build();
    }
}
