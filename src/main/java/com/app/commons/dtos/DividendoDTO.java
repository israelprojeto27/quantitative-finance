package com.app.commons.dtos;

import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
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

    public static DividendoDTO from(DividendoAcao dividendoAcao) {
        return DividendoDTO.builder()
                .data(Utils.converteLocalDateToString(dividendoAcao.getData()))
                .dividend(dividendoAcao.getDividend())
                .build();
    }

    public static DividendoDTO from(DividendoBdr dividendoBdr) {
        return DividendoDTO.builder()
                .data(Utils.converteLocalDateToString(dividendoBdr.getData()))
                .dividend(dividendoBdr.getDividend())
                .build();
    }


}
