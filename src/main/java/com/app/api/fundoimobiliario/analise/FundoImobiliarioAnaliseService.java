package com.app.api.fundoimobiliario.analise;

import com.app.api.acao.analise.entities.AcaoAnalise;
import com.app.api.acao.cotacao.CotacaoAcaoService;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.dividendo.DividendoAcaoRepository;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.principal.AcaoRepository;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.fundoimobiliario.analise.entities.FundoImobiliarioAnalise;
import com.app.api.fundoimobiliario.cotacao.CotacaoFundoService;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.dividendo.DividendoFundoRepository;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
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
public class FundoImobiliarioAnaliseService implements BaseAtivoAnaliseService {

    @Autowired
    FundoImobiliarioAnaliseRepository repository;

    @Autowired
    FundoImobiliarioRepository fundoImobiliarioRepository;

    @Autowired
    CotacaoFundoService cotacaoFundoService;

    @Autowired
    DividendoFundoRepository dividendoFundoRepository;


    @Override
    @Transactional
    public boolean addAtivoAnalise(String sigla) {
        Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(sigla);
        if ( optFundo.isPresent()){
            Optional<FundoImobiliarioAnalise> optFundoAnalise = repository.findByFundo(optFundo.get());
            if (!optFundoAnalise.isPresent()){
                FundoImobiliarioAnalise fundoImobiliarioAnalise = FundoImobiliarioAnalise.toEntity(optFundo.get());
                repository.save(fundoImobiliarioAnalise);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AtivoAnaliseDTO> findAll() {
        List<FundoImobiliarioAnalise> listFundoAnalise = repository.findAll();
        if (! listFundoAnalise.isEmpty()){
            List<AtivoAnaliseDTO> list = new ArrayList<>();
            listFundoAnalise.forEach(fundoAnalise -> {
                FundoImobiliario fundoImobiliario = fundoAnalise.getFundo();
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoImobiliario);
                List<CotacaoFundoMensal> listCotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(fundoImobiliario);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(fundoImobiliario);
                List<DividendoFundo> listDividendos = dividendoFundoRepository.findAllByFundo(fundoImobiliario);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                AtivoAnaliseDTO dto = AtivoAnaliseDTO.from(fundoAnalise, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo);
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

    @Override
    @Transactional
    public boolean deleteAtivoAnalise(String sigla) {
        Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(sigla);
        if ( optFundo.isPresent()){
            Optional<FundoImobiliarioAnalise> optFundoAnalise = repository.findByFundo(optFundo.get());
            if (optFundoAnalise.isPresent()){
                repository.delete(optFundoAnalise.get());
                return true;
            }
        }
        return false;
    }
}
