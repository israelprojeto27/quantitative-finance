package com.app.commons.dtos.dividendo;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.api.reit.principal.entity.Reit;
import com.app.api.stock.principal.entity.Stock;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SumCalculateYieldDividendosAtivoDTO {

    private String sigla;

    private Double totalDividendos;

    private List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos;



    public static SumCalculateYieldDividendosAtivoDTO from(Acao acao, List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos) {
        return SumCalculateYieldDividendosAtivoDTO.builder()
                                                  .sigla(acao.getSigla())
                                                  .totalDividendos(Utils.converterDoubleDoisDecimais(listCalcultaDetailYieldDividendos.stream().mapToDouble(dividendo -> dividendo.getRendimentoDividendo()).sum()))
                                                  .listCalcultaDetailYieldDividendos(listCalcultaDetailYieldDividendos)
                                                  .build();
    }

    public static SumCalculateYieldDividendosAtivoDTO from(FundoImobiliario fundoImobiliario, List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos) {
        return SumCalculateYieldDividendosAtivoDTO.builder()
                .sigla(fundoImobiliario.getSigla())
                .totalDividendos(Utils.converterDoubleDoisDecimais(listCalcultaDetailYieldDividendos.stream().mapToDouble(dividendo -> dividendo.getRendimentoDividendo()).sum()))
                .listCalcultaDetailYieldDividendos(listCalcultaDetailYieldDividendos)
                .build();
    }

    public static SumCalculateYieldDividendosAtivoDTO from(Bdr bdr, List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos) {
        return SumCalculateYieldDividendosAtivoDTO.builder()
                .sigla(bdr.getSigla())
                .totalDividendos(Utils.converterDoubleDoisDecimais(listCalcultaDetailYieldDividendos.stream().mapToDouble(dividendo -> dividendo.getRendimentoDividendo()).sum()))
                .listCalcultaDetailYieldDividendos(listCalcultaDetailYieldDividendos)
                .build();
    }

    public static SumCalculateYieldDividendosAtivoDTO from(Stock stock, List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos) {
        return SumCalculateYieldDividendosAtivoDTO.builder()
                .sigla(stock.getSigla())
                .totalDividendos(Utils.converterDoubleDoisDecimais(listCalcultaDetailYieldDividendos.stream().mapToDouble(dividendo -> dividendo.getRendimentoDividendo()).sum()))
                .listCalcultaDetailYieldDividendos(listCalcultaDetailYieldDividendos)
                .build();
    }


    public static SumCalculateYieldDividendosAtivoDTO from(Reit reit, List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos) {
        return SumCalculateYieldDividendosAtivoDTO.builder()
                .sigla(reit.getSigla())
                .totalDividendos(Utils.converterDoubleDoisDecimais(listCalcultaDetailYieldDividendos.stream().mapToDouble(dividendo -> dividendo.getRendimentoDividendo()).sum()))
                .listCalcultaDetailYieldDividendos(listCalcultaDetailYieldDividendos)
                .build();
    }
}
