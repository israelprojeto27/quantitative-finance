package com.app.commons.dtos;

import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LastDividendoAtivoDTO {

    private Double valorUltimoDividendo;

    private String dataUltimoDividendo;

    private LocalDate dataUltimoDividendoFmt;

    public LastDividendoAtivoDTO() {
    }

    public LastDividendoAtivoDTO(Double valorUltimoDividendo, String dataUltimoDividendo, LocalDate dataUltimoDividendoFmt) {
        this.valorUltimoDividendo = valorUltimoDividendo;
        this.dataUltimoDividendo = dataUltimoDividendo;
        this.dataUltimoDividendoFmt = dataUltimoDividendoFmt;
    }

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
}
