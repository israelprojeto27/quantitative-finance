package com.app.commons.dtos.mapadividendo;

import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MapaRoiInvestimentoDividendoDTO {

    private String sigla;

    private Double coeficienteRoi;

    private String coeficienteRoiFmt;

    public MapaRoiInvestimentoDividendoDTO() {
    }

    public MapaRoiInvestimentoDividendoDTO(String sigla, Double coeficienteRoi, String coeficienteRoiFmt) {
        this.sigla = sigla;
        this.coeficienteRoi = coeficienteRoi;
        this.coeficienteRoiFmt = coeficienteRoiFmt;
    }

    public static MapaRoiInvestimentoDividendoDTO from(String sigla, Double coeficienteTotal) {
        return MapaRoiInvestimentoDividendoDTO.builder()
                                              .sigla(sigla)
                                              .coeficienteRoi(coeficienteTotal)
                                              .coeficienteRoiFmt(Utils.converterDoubleQuatroDecimaisString(coeficienteTotal))
                                              .build();
    }
}
