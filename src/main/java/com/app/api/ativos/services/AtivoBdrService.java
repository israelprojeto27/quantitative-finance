package com.app.api.ativos.services;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.ativos.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.enums.TipoAtivoEnum;
import com.app.api.bdr.cotacao.CotacaoBdrService;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.dividendo.DividendoBdrRepository;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.BdrRepository;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AtivoBdrService {

    @Autowired
    BdrRepository bdrRepository;

    @Autowired
    CotacaoBdrService cotacaoBdrService;

    @Autowired
    DividendoBdrRepository dividendoBdrRepository;


    public List<InfoGeraisAtivosDTO> getInfoGerais() {
        List<Bdr> listBdr = bdrRepository.findAll();
        return getInfoGeraisBdrs(listBdr);
    }

    public InfoGeraisAtivosDTO getInfoGeraisByBdr(Bdr bdr) {
        LastCotacaoAtivoDiarioDTO lastCotBdrAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
        List<CotacaoBdrMensal> listCotacaoBdrMensal = cotacaoBdrService.findCotacaoMensalByAtivo(bdr);
        LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(bdr);
        List<DividendoBdr> listDividendos = dividendoBdrRepository.findAllByBdr(bdr);
        Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoBdr(listDividendos);

        return InfoGeraisAtivosDTO.from(bdr, lastCotBdrAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.BDR.getLabel());
    }

    private List<InfoGeraisAtivosDTO> getInfoGeraisBdrs(List<Bdr> listBdr) {
        if ( !listBdr.isEmpty()){
            List<InfoGeraisAtivosDTO> list = new ArrayList<>();
            listBdr.forEach(bdr ->{

                LastCotacaoAtivoDiarioDTO lastCotBdrAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                List<CotacaoBdrMensal> listCotacaoBdrMensal = cotacaoBdrService.findCotacaoMensalByAtivo(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(bdr);
                List<DividendoBdr> listDividendos = dividendoBdrRepository.findAllByBdr(bdr);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoBdr(listDividendos);

                InfoGeraisAtivosDTO dto = InfoGeraisAtivosDTO.from(bdr, lastCotBdrAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.BDR.getLabel());
                list.add(dto);

            });
            return list;
        }
        return null;
    }

    private Double calculateCoeficienteRoiDividendoBdr(List<DividendoBdr> listDividendos) {

        List<Double> listCoeficiente = new ArrayList<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoBdrMensal cotacaoMensal = cotacaoBdrService.findCotacaoMensalByAtivo(dividendo.getBdr(), dividendo.getData());
                if (cotacaoMensal != null && cotacaoMensal.getClose() != null ) {
                    listCoeficiente.add(dividendo.getDividend() / cotacaoMensal.getClose());
                }
            });
        }

        if (! listCoeficiente.isEmpty()){
            return listCoeficiente.stream().mapToDouble(valor -> valor).sum();
        }

        return 0d;

    }

    public LastDividendoAtivoDTO getLastDividendo(Bdr bdr) {
        List<DividendoBdr> listDividendos = dividendoBdrRepository.findAllByBdr(bdr, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoBdr> optDividendoBdr = listDividendos.stream()
                    .findFirst();
            if ( optDividendoBdr.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoBdr.get());
            }
        }
        return null;
    }
}
