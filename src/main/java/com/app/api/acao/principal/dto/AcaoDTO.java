package com.app.api.acao.principal.dto;

import com.app.api.acao.principal.entity.Acao;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AcaoDTO {

    public AcaoDTO() {
    }

    private Long id;

    private String sigla;

    public AcaoDTO(Long id, String sigla) {
        this.id = id;
        this.sigla = sigla;
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
                .build();
    }

}
