package com.app.api.acao.dividendo.dto;

import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.principal.entity.Acao;
import com.app.commons.dtos.DividendoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class AcaoListDividendoDTO {

    private String sigla;

    private List<DividendoDTO> listDividendos;

    public AcaoListDividendoDTO() {
    }

    public AcaoListDividendoDTO(String sigla, List<DividendoDTO> listDividendos) {
        this.sigla = sigla;
        this.listDividendos = listDividendos;
    }

    public static AcaoListDividendoDTO fromEntity(Acao acao, List<DividendoAcao> listDividendos){
        return AcaoListDividendoDTO.builder()
                .sigla(acao.getSigla())
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }

    public static AcaoListDividendoDTO from(String sigla, List<DividendoAcao> listDividendos) {
        return AcaoListDividendoDTO.builder()
                .sigla(sigla)
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }
}
