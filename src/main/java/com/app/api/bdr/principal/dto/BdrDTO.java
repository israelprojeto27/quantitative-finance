package com.app.api.bdr.principal.dto;

import com.app.api.bdr.principal.entity.Bdr;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BdrDTO {

    private Long id;

    private String sigla;

    public BdrDTO() {
    }

    public BdrDTO(Long id, String sigla) {
        this.id = id;
        this.sigla = sigla;
    }

    public static Bdr toEntity(BdrDTO dto){
        return Bdr.builder()
                .id(dto.getId())
                .sigla(dto.getSigla())
                .build();
    }

    public static BdrDTO fromEntity(Bdr entity){
        return BdrDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .build();
    }
}
