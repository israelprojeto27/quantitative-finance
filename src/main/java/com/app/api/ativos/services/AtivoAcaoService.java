package com.app.api.ativos.services;

import com.app.api.acao.cotacao.CotacaoAcaoService;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.dividendo.DividendoAcaoRepository;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.principal.AcaoRepository;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.ativos.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.enums.TipoAtivoEnum;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AtivoAcaoService {

    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    DividendoAcaoRepository dividendoAcaoRepository;

    @Autowired
    CotacaoAcaoService cotacaoAcaoService;


    public List<InfoGeraisAtivosDTO> getInfoGerais() {
        List<Acao> listAcao = acaoRepository.findAll();
        return getInfoGeraisAcoes(listAcao);
    }

    public InfoGeraisAtivosDTO getInfoGeraisByAcao(Acao acao){
        List<DividendoAcao> listDividendos = dividendoAcaoRepository.findAllByAcao(acao);

        LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
        List<CotacaoAcaoMensal> listCotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(acao);
        LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(acao);
        Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoAcao(listDividendos);

        return InfoGeraisAtivosDTO.from(acao, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.ACAO.getLabel());
    }

    private List<InfoGeraisAtivosDTO> getInfoGeraisAcoes(List<Acao> listAcao){
        if ( !listAcao.isEmpty()){
            List<InfoGeraisAtivosDTO> list = new ArrayList<>();
            listAcao.forEach(acao -> {
                List<DividendoAcao> listDividendos = dividendoAcaoRepository.findAllByAcao(acao);

                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                List<CotacaoAcaoMensal> listCotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(acao);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(acao);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoAcao(listDividendos);

                InfoGeraisAtivosDTO dto = InfoGeraisAtivosDTO.from(acao, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.ACAO.getLabel());
                list.add(dto);

            });

            return list;
        }
        return null;
    }

    private Double calculateCoeficienteRoiDividendoAcao(List<DividendoAcao> listDividendos) {

        List<Double> listCoeficiente = new ArrayList<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoAcaoMensal cotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(dividendo.getAcao(), dividendo.getData());
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

    public LastDividendoAtivoDTO getLastDividendo(Acao acao) {
        List<DividendoAcao> listDividendos = dividendoAcaoRepository.findAllByAcao(acao, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoAcao> optDividendoAcao = listDividendos.stream()
                    .findFirst();
            if ( optDividendoAcao.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoAcao.get());
            }
        }
        return null;
    }
}
