package com.app.commons.dtos;

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

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LastDividendoAtivoDTO {

    private Double valorUltimoDividendo;

    private String dataUltimoDividendo;

    private LocalDate dataUltimoDividendoFmt;


    public static LastDividendoAtivoDTO from(DividendoAcao dividendo) {
        return LastDividendoAtivoDTO.builder()
                                   .valorUltimoDividendo(dividendo.getDividend())
                                   .dataUltimoDividendo(Utils.converteLocalDateToString(dividendo.getData()))
                                   .dataUltimoDividendoFmt(dividendo.getData())
                                   .build();
    }

    public static LastDividendoAtivoDTO from(DividendoBdr dividendo) {
        return LastDividendoAtivoDTO.builder()
                .valorUltimoDividendo(dividendo.getDividend())
                .dataUltimoDividendo(Utils.converteLocalDateToString(dividendo.getData()))
                .dataUltimoDividendoFmt(dividendo.getData())
                .build();
    }

    public static LastDividendoAtivoDTO from(DividendoFundo dividendo) {
        return LastDividendoAtivoDTO.builder()
                .valorUltimoDividendo(dividendo.getDividend())
                .dataUltimoDividendo(Utils.converteLocalDateToString(dividendo.getData()))
                .dataUltimoDividendoFmt(dividendo.getData())
                .build();
    }

    public static LastDividendoAtivoDTO from(DividendoStock dividendo) {
        return LastDividendoAtivoDTO.builder()
                .valorUltimoDividendo(dividendo.getDividend())
                .dataUltimoDividendo(Utils.converteLocalDateToString(dividendo.getData()))
                .dataUltimoDividendoFmt(dividendo.getData())
                .build();
    }

    public static LastDividendoAtivoDTO from(DividendoReit dividendo) {
        return LastDividendoAtivoDTO.builder()
                .valorUltimoDividendo(dividendo.getDividend())
                .dataUltimoDividendo(Utils.converteLocalDateToString(dividendo.getData()))
                .dataUltimoDividendoFmt(dividendo.getData())
                .build();
    }
}
