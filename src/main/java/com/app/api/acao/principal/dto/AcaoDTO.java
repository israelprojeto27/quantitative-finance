package com.app.api.acao.principal.dto;

import com.app.api.acao.principal.entity.Acao;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AcaoDTO {

    public AcaoDTO() {
    }

    private Long id;

    private String sigla;

    private Double dividendYield;

    private String dividendYieldFmt;

    public AcaoDTO(Long id, String sigla, Double dividendYield, String dividendYieldFmt) {
        this.id = id;
        this.sigla = sigla;
        this.dividendYield = dividendYield;
        this.dividendYieldFmt = dividendYieldFmt;
    }

    public static Acao toEntity(AcaoDTO dto){
        return Acao.builder()
                .id(dto.getId())
                .sigla(dto.getSigla())
                .build();
    }

    public static AcaoDTO fromEntity(Acao entity){
        return AcaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield())
                .dividendYieldFmt(Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()))
                .build();
    }

}
