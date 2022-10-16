package com.app.api.fundoimobiliario.principal.dto;

import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class FundoImobiliarioDTO {

    public FundoImobiliarioDTO() {
    }

    private Long id;

    private String sigla;

    private Double dividendYield;

    private String dividendYieldFmt;

    public FundoImobiliarioDTO(Long id, String sigla, Double dividendYield, String dividendYieldFmt) {
        this.id = id;
        this.sigla = sigla;
        this.dividendYield = dividendYield;
        this.dividendYieldFmt = dividendYieldFmt;
    }

    public static FundoImobiliario toEntity(FundoImobiliarioDTO dto){
        return FundoImobiliario.builder()
                .id(dto.getId())
                .sigla(dto.getSigla())
                .build();
    }

    public static FundoImobiliarioDTO fromEntity(FundoImobiliario entity){
        return FundoImobiliarioDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield())
                .dividendYieldFmt(Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()))
                .build();
    }

}