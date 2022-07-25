package com.app.commons.dtos;

import com.app.api.acao.principal.entity.Acao;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SumCalculateYieldDividendosAtivoDTO {

    private String sigla;

    private Double totalDividendos;

    private List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos;

    public SumCalculateYieldDividendosAtivoDTO() {
    }

    public SumCalculateYieldDividendosAtivoDTO(String sigla, Double totalDividendos, List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos) {
        this.sigla = sigla;
        this.totalDividendos = totalDividendos;
        this.listCalcultaDetailYieldDividendos = listCalcultaDetailYieldDividendos;
    }

    public static SumCalculateYieldDividendosAtivoDTO from(Acao acao, List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos) {
        return SumCalculateYieldDividendosAtivoDTO.builder()
                                                  .sigla(acao.getSigla())
                                                  .totalDividendos(Utils.converterDoubleDoisDecimais(listCalcultaDetailYieldDividendos.stream().mapToDouble(dividendo -> dividendo.getRendimentoDividendo()).sum()))
                                                  .listCalcultaDetailYieldDividendos(listCalcultaDetailYieldDividendos)
                                                  .build();
    }
}
