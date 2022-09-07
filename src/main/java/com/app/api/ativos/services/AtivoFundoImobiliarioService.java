package com.app.api.ativos.services;

import com.app.api.ativos.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.enums.TipoAtivoEnum;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.analise.entities.FundoImobiliarioAnalise;
import com.app.api.fundoimobiliario.cotacao.CotacaoFundoService;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.dividendo.DividendoFundoRepository;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AtivoFundoImobiliarioService {

    @Autowired
    FundoImobiliarioRepository fundoImobiliarioRepository;

    @Autowired
    CotacaoFundoService cotacaoFundoService;

    @Autowired
    DividendoFundoRepository dividendoFundoRepository;


    public List<InfoGeraisAtivosDTO> getInfoGerais() {
        List<FundoImobiliario> listFundo  = fundoImobiliarioRepository.findAll();
        return getInfoGeraisFundos(listFundo);
    }

    public InfoGeraisAtivosDTO getInfoGeraisByFundo(FundoImobiliario fundoImobiliario) {
        LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoImobiliario);
        List<CotacaoFundoMensal> listCotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(fundoImobiliario);
        LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(fundoImobiliario);
        List<DividendoFundo> listDividendos = dividendoFundoRepository.findAllByFundo(fundoImobiliario);
        Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
        return InfoGeraisAtivosDTO.from(fundoImobiliario, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.FUNDO_IMOBILIARIO.getLabel());
    }

    private List<InfoGeraisAtivosDTO> getInfoGeraisFundos(List<FundoImobiliario> listFundo) {

        if ( !listFundo.isEmpty()){
            List<InfoGeraisAtivosDTO> list = new ArrayList<>();
            listFundo.forEach(fundoImobiliario -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoImobiliario);
                List<CotacaoFundoMensal> listCotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(fundoImobiliario);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(fundoImobiliario);
                List<DividendoFundo> listDividendos = dividendoFundoRepository.findAllByFundo(fundoImobiliario);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                InfoGeraisAtivosDTO dto = InfoGeraisAtivosDTO.from(fundoImobiliario, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.FUNDO_IMOBILIARIO.getLabel());
                list.add(dto);
            });
            return list;
        }

        return null;
    }

    private Double calculateCoeficienteRoiDividendo(List<DividendoFundo> listDividendos) {

        List<Double> listCoeficiente = new ArrayList<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoFundoMensal cotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(dividendo.getFundo(), dividendo.getData());
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

    public LastDividendoAtivoDTO getLastDividendo(FundoImobiliario fundoImobiliario) {
        List<DividendoFundo> listDividendos = dividendoFundoRepository.findAllByFundo(fundoImobiliario, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoFundo> optDividendoFundo = listDividendos.stream()
                    .findFirst();
            if ( optDividendoFundo.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoFundo.get());
            }
        }
        return null;
    }
}
