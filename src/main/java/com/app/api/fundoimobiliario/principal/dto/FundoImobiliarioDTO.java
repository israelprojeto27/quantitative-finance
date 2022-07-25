package com.app.api.fundoimobiliario.principal.dto;

import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class FundoImobiliarioDTO {

    public FundoImobiliarioDTO() {
    }

    private Long id;

    private String sigla;

    public FundoImobiliarioDTO(Long id, String sigla) {
        this.id = id;
        this.sigla = sigla;
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
                .build();
    }

}