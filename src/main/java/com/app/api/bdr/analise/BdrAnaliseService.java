package com.app.api.bdr.analise;

import com.app.api.acao.analise.entities.AcaoAnalise;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.analise.entities.BdrAnalise;
import com.app.api.bdr.cotacao.CotacaoBdrService;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.dividendo.DividendoBdrRepository;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.BdrRepository;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.commons.basic.analise.BaseAtivoAnaliseService;
import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
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
public class BdrAnaliseService implements BaseAtivoAnaliseService {

    @Autowired
    BdrAnaliseRepository repository;

    @Autowired
    BdrRepository bdrRepository;

    @Autowired
    CotacaoBdrService cotacaoBdrService;

    @Autowired
    DividendoBdrRepository dividendoBdrRepository;


    @Override
    @Transactional
    public boolean addAtivoAnalise(String sigla) {
        Optional<Bdr> optBdr = bdrRepository.findBySigla(sigla);
        if ( optBdr.isPresent()){
            Optional<BdrAnalise> optBdrAnalise = repository.findByBdr(optBdr.get());
            if (!optBdrAnalise.isPresent()){
                BdrAnalise bdrAnalise = BdrAnalise.toEntity(optBdr.get());
                repository.save(bdrAnalise);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AtivoAnaliseDTO> findAll() {
        List<BdrAnalise> listBdrAnalise = repository.findAll();
        if (! listBdrAnalise.isEmpty()){
            List<AtivoAnaliseDTO> list = new ArrayList<>();
            listBdrAnalise.forEach(bdrAnalise -> {
                Bdr bdr = bdrAnalise.getBdr();
                LastCotacaoAtivoDiarioDTO lastCotBdrAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                List<CotacaoBdrMensal> listCotacaoBdrMensal = cotacaoBdrService.findCotacaoMensalByAtivo(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(bdr);
                List<DividendoBdr> listDividendos = dividendoBdrRepository.findAllByBdr(bdr);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                AtivoAnaliseDTO dto = AtivoAnaliseDTO.from(bdrAnalise, lastCotBdrAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo);
                list.add(dto);
            });
            return list;
        }

        return null;
    }

    private Double calculateCoeficienteRoiDividendo(List<DividendoBdr> listDividendos) {

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

    @Override
    @Transactional
    public boolean deleteAtivoAnalise(String sigla) {
        Optional<Bdr> optBdr = bdrRepository.findBySigla(sigla);
        if ( optBdr.isPresent()){
            Optional<BdrAnalise> optBdrAnalise = repository.findByBdr(optBdr.get());
            if (optBdrAnalise.isPresent()){
                repository.delete(optBdrAnalise.get());
                return true;
            }
        }
        return false;
    }
}
