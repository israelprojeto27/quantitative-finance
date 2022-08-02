package com.app.api.bdr.dividendo.dto;


import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.entity.Bdr;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class BdrListDividendoDTO {

    private String sigla;

    private List<DividendoDTO> listDividendos;

    public BdrListDividendoDTO() {
    }

    public BdrListDividendoDTO(String sigla, List<DividendoDTO> listDividendos) {
        this.sigla = sigla;
        this.listDividendos = listDividendos;
    }

    public static BdrListDividendoDTO fromEntity(Bdr bdr, List<DividendoBdr> listDividendos){
        return BdrListDividendoDTO.builder()
                .sigla(bdr.getSigla())
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }

    public static BdrListDividendoDTO from(String sigla, List<DividendoBdr> listDividendos) {
        return BdrListDividendoDTO.builder()
                .sigla(sigla)
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }
}
