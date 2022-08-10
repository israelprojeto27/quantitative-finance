package com.app.commons.dtos;

import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DividendoDTO {

    private String data;

    private String dividend;

    public DividendoDTO() {
    }

    public DividendoDTO(String data, String dividend) {
        this.data = data;
        this.dividend = dividend;
    }

    public static DividendoDTO from(DividendoAcao dividendoAcao) {
        return DividendoDTO.builder()
                .data(Utils.converteLocalDateToString(dividendoAcao.getData()))
                .dividend(Utils.converterDoubleDoisDecimaisString(dividendoAcao.getDividend()))
                .build();
    }

    public static DividendoDTO from(DividendoBdr dividendoBdr) {
        return DividendoDTO.builder()
                .data(Utils.converteLocalDateToString(dividendoBdr.getData()))
                .dividend(Utils.converterDoubleDoisDecimaisString(dividendoBdr.getDividend()))
                .build();
    }

    public static DividendoDTO from(DividendoFundo dividendo) {
        return DividendoDTO.builder()
                .data(Utils.converteLocalDateToString(dividendo.getData()))
                .dividend(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .build();
    }

}
