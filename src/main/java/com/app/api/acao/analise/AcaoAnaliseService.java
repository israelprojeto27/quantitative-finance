package com.app.api.acao.analise;

import com.app.commons.basic.analise.BaseAtivoAnaliseService;
import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
import com.app.api.acao.analise.entities.AcaoAnalise;
import com.app.api.acao.cotacao.CotacaoAcaoService;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.dividendo.DividendoAcaoRepository;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.principal.AcaoRepository;
import com.app.api.acao.principal.entity.Acao;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AcaoAnaliseService implements BaseAtivoAnaliseService {

    @Autowired
    AcaoAnaliseRepository repository;

    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    CotacaoAcaoService cotacaoAcaoService;

    @Autowired
    DividendoAcaoRepository dividendoAcaoRepository;


    @Override
    @Transactional
    public boolean addAtivoAnalise(String sigla) {
        Optional<Acao> optAcao = acaoRepository.findBySigla(sigla);
        if ( optAcao.isPresent()){
            Optional<AcaoAnalise> optAcaoAnalise = repository.findByAcao(optAcao.get());
            if (!optAcaoAnalise.isPresent()){
                AcaoAnalise acaoAnalise = AcaoAnalise.toEntity(optAcao.get());
                repository.save(acaoAnalise);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AtivoAnaliseDTO> findAll() {

        List<AcaoAnalise> listAcaoAnalise = repository.findAll();
        if (! listAcaoAnalise.isEmpty()){
            List<AtivoAnaliseDTO> list = new ArrayList<>();
            listAcaoAnalise.forEach(acaoAnalise -> {
                Acao acao = acaoAnalise.getAcao();
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                List<CotacaoAcaoMensal> listCotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(acao);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(acao);
                List<DividendoAcao> listDividendos = dividendoAcaoRepository.findAllByAcao(acao);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                AtivoAnaliseDTO dto = AtivoAnaliseDTO.from(acaoAnalise, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo);
                list.add(dto);
            });
            return list;
        }

        return null;
    }

    private Double calculateCoeficienteRoiDividendo(List<DividendoAcao> listDividendos) {

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

    @Override
    @Transactional
    public boolean deleteAtivoAnalise(String sigla) {
        Optional<Acao> optAcao = acaoRepository.findBySigla(sigla);
        if ( optAcao.isPresent()) {
            Optional<AcaoAnalise> optAcaoAnalise = repository.findByAcao(optAcao.get());
            if (optAcaoAnalise.isPresent()) {
                repository.delete(optAcaoAnalise.get());
                return true;
            }
        }
        return false;
    }
}
