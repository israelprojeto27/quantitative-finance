package com.app.commons.dtos.mapadividendo;

import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MapaDividendoDetailDTO {

    private String sigla;

    private Double dividendo;

    private String dividendoFmt;


    public static MapaDividendoDetailDTO from(DividendoAcao dividendo) {
        return MapaDividendoDetailDTO.builder()
                .sigla(dividendo.getAcao().getSigla())
                .dividendo(dividendo.getDividend())
                .dividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .build();
    }

    public static MapaDividendoDetailDTO from(DividendoFundo dividendo) {
        return MapaDividendoDetailDTO.builder()
                .sigla(dividendo.getFundo().getSigla())
                .dividendo(dividendo.getDividend())
                .dividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .build();
    }

    public static MapaDividendoDetailDTO from(DividendoBdr dividendo) {
        return MapaDividendoDetailDTO.builder()
                .sigla(dividendo.getBdr().getSigla())
                .dividendo(dividendo.getDividend())
                .dividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .build();
    }

    public static MapaDividendoDetailDTO from(DividendoStock dividendo) {
        return MapaDividendoDetailDTO.builder()
                .sigla(dividendo.getStock().getSigla())
                .dividendo(dividendo.getDividend())
                .dividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .build();
    }

    public static MapaDividendoDetailDTO from(DividendoReit dividendo) {
        return MapaDividendoDetailDTO.builder()
                .sigla(dividendo.getReit().getSigla())
                .dividendo(dividendo.getDividend())
                .dividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .build();
    }
}
