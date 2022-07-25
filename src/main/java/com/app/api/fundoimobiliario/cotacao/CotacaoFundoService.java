package com.app.api.fundoimobiliario.cotacao;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.fundoimobiliario.cotacao.dto.FundoCotacaoDTO;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoSemanal;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoDiarioRepository;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoMensalRepository;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoSemanalRepository;
import com.app.api.fundoimobiliario.dividendo.DividendoFundoService;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.basic.cotacao.BaseCotacaoService;
import com.app.commons.dtos.FilterAtivoCotacaoGrowDTO;
import com.app.commons.dtos.ResultFilterAtivoCotacaoGrowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CotacaoFundoService implements BaseCotacaoService<FundoImobiliario, FundoCotacaoDTO, CotacaoFundoDiario, CotacaoFundoSemanal, CotacaoFundoMensal> {

    @Autowired
    CotacaoFundoDiarioRepository cotacaoFundoDiarioRepository;

    @Autowired
    CotacaoFundoSemanalRepository cotacaoFundoSemanalRepository;

    @Autowired
    CotacaoFundoMensalRepository cotacaoFundoMensalRepository;

    @Autowired
    FundoImobiliarioRepository fundoRepository;

    @Autowired
    DividendoFundoService dividendoFundoService;


    @Transactional
    @Override
    public void addCotacaoAtivo(String line, FundoImobiliario fundo, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoFundoDiario cotacaoFundoDiario = CotacaoFundoDiario.toEntity(array, fundo);
            this.createCotacaoDiario(cotacaoFundoDiario);

            if (cotacaoFundoDiario.getDividend().doubleValue() > 0.0d){
                DividendoFundo dividendoFundo = DividendoFundo.toEntity(cotacaoFundoDiario);
                dividendoFundoService.save(dividendoFundo);
            }
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoFundoSemanal cotacaoFundoSemanal = CotacaoFundoSemanal.toEntity(array, fundo);
            this.createCotacaoSemanal(cotacaoFundoSemanal);
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoFundoMensal cotacaoFundoMensal = CotacaoFundoMensal.toEntity(array, fundo);
            this.createCotacaoMensal(cotacaoFundoMensal);
        }
    }

    @Override
    public boolean createCotacaoDiario(CotacaoFundoDiario cotacaoFundoDiario) {
        return false;
    }

    @Override
    public boolean createCotacaoSemanal(CotacaoFundoSemanal cotacaoFundoSemanal) {
        return false;
    }

    @Override
    public boolean createCotacaoMensal(CotacaoFundoMensal cotacaoFundoMensal) {
        return false;
    }

    @Override
    public List<CotacaoFundoDiario> findCotacaoDiarioByAtivo(FundoImobiliario fundoImobiliario) {
        return null;
    }

    @Override
    public List<CotacaoFundoDiario> findCotacaoDiarioByAtivo(FundoImobiliario fundoImobiliario, Sort sort) {
        return null;
    }

    @Override
    public List<CotacaoFundoSemanal> findCotacaoSemanalByAtivo(FundoImobiliario fundoImobiliario) {
        return null;
    }

    @Override
    public List<CotacaoFundoSemanal> findCotacaoSemanalByAtivo(FundoImobiliario fundoImobiliario, Sort sort) {
        return null;
    }

    @Override
    public List<CotacaoFundoMensal> findCotacaoMensalByAtivo(FundoImobiliario fundoImobiliario) {
        return null;
    }

    @Override
    public List<CotacaoFundoMensal> findCotacaoMensalByAtivo(FundoImobiliario fundoImobiliario, Sort sort) {
        return null;
    }

    @Override
    public void cleanAll() {

    }

    @Override
    public void cleanByPeriodo(String periodo) {

    }

    @Override
    public FundoCotacaoDTO findCotacaoByIdAtivo(Long id) {
        return null;
    }

    @Override
    public FundoCotacaoDTO findCotacaoByIdAtivoByPeriodo(Long id, String periodo) {
        return null;
    }

    @Override
    public FundoCotacaoDTO findCotacaoBySiglaFull(String sigla) {
        return null;
    }

    @Override
    public FundoCotacaoDTO findCotacaoBySiglaByPeriodo(String sigla, String periodo) {
        return null;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowDiary(FilterAtivoCotacaoGrowDTO dto) {
        return null;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowWeek(FilterAtivoCotacaoGrowDTO dto) {
        return null;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowMonth(FilterAtivoCotacaoGrowDTO dto) {
        return null;
    }
}
