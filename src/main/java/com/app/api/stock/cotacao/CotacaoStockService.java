package com.app.api.stock.cotacao;


import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.stock.cotacao.dto.StockCotacaoDTO;
import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.cotacao.entities.CotacaoStockMensal;
import com.app.api.stock.cotacao.entities.CotacaoStockSemanal;
import com.app.api.stock.cotacao.repositories.CotacaoStockDiarioRepository;
import com.app.api.stock.cotacao.repositories.CotacaoStockMensalRepository;
import com.app.api.stock.cotacao.repositories.CotacaoStockSemanalRepository;
import com.app.api.stock.dividendo.DividendoStockService;
import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.api.stock.increasepercent.IncreasePercentStock;
import com.app.api.stock.increasepercent.IncreasePercentStockService;
import com.app.api.stock.principal.StockRepository;
import com.app.api.stock.principal.entity.Stock;
import com.app.commons.basic.cotacao.BaseCotacaoService;
import com.app.commons.dtos.*;
import com.app.commons.dtos.dividendo.RoiDividendoCotacaoDTO;
import com.app.commons.enums.TipoOrdenacaoGrowEnum;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CotacaoStockService implements BaseCotacaoService<Stock, StockCotacaoDTO, CotacaoStockDiario, CotacaoStockSemanal, CotacaoStockMensal> {

    @Autowired
    CotacaoStockDiarioRepository cotacaoStockDiarioRepository;

    @Autowired
    CotacaoStockSemanalRepository cotacaoStockSemanalRepository;

    @Autowired
    CotacaoStockMensalRepository cotacaoStockMensalRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    DividendoStockService dividendoStockService;


    @Autowired
    IncreasePercentStockService increasePercentStockService;


    @Transactional
    @Override
    public void addCotacaoAtivo(String line, Stock stock, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoStockDiario cotacaoStockDiario = CotacaoStockDiario.toEntity(array, stock);
            if ( cotacaoStockDiario != null){
                this.createCotacaoDiario(cotacaoStockDiario);

                if (cotacaoStockDiario.getDividend().doubleValue() > 0.0d){
                    DividendoStock dividendoStock = DividendoStock.toEntity(cotacaoStockDiario);
                    dividendoStockService.save(dividendoStock);
                }
            }
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoStockSemanal cotacaoStockSemanal = CotacaoStockSemanal.toEntity(array, stock);
            if ( cotacaoStockSemanal != null )
                this.createCotacaoSemanal(cotacaoStockSemanal);
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoStockMensal cotacaoStockMensal = CotacaoStockMensal.toEntity(array, stock);
            if ( cotacaoStockMensal != null)
                this.createCotacaoMensal(cotacaoStockMensal);
        }
    }

    @Override
    @Transactional
    public void addCotacaoAtivoPartial(String line, Stock stock, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoStockDiario cotacaoStockDiario = CotacaoStockDiario.toEntity(array, stock);
            List<CotacaoStockDiario> listCotacao = cotacaoStockDiarioRepository.findByStockAndData(stock, cotacaoStockDiario.getData());
            if ( listCotacao.isEmpty() && cotacaoStockDiario != null){
                this.createCotacaoDiario(cotacaoStockDiario);

                if (cotacaoStockDiario.getDividend().doubleValue() > 0.0d){
                    DividendoStock  dividendoStock = DividendoStock.toEntity(cotacaoStockDiario);
                    dividendoStockService.save(dividendoStock);
                }
            }
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoStockSemanal cotacaoStockSemanal = CotacaoStockSemanal.toEntity(array, stock);
            if ( cotacaoStockSemanal != null){
                List<CotacaoStockSemanal> listCotacao = cotacaoStockSemanalRepository.findByStockAndData(stock, cotacaoStockSemanal.getData());
                if ( listCotacao != null && listCotacao.isEmpty() && cotacaoStockSemanal != null){
                    this.createCotacaoSemanal(cotacaoStockSemanal);
                }
            }
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoStockMensal cotacaoStockMensal = CotacaoStockMensal.toEntity(array, stock);
            if ( cotacaoStockMensal != null ){
                List<CotacaoStockMensal> listCotacao = cotacaoStockMensalRepository.findByStockAndData(stock, cotacaoStockMensal.getData());
                if ( listCotacao.isEmpty() && cotacaoStockMensal != null){
                    this.createCotacaoMensal(cotacaoStockMensal);
                }
            }
        }
    }



    @Transactional
    @Override
    public boolean createCotacaoDiario(CotacaoStockDiario cotacaoStockDiario) {
        cotacaoStockDiarioRepository.save(cotacaoStockDiario);
        return true;
    }

    @Transactional
    @Override
    public boolean createCotacaoSemanal(CotacaoStockSemanal cotacaoStockSemanal) {
        cotacaoStockSemanalRepository.save(cotacaoStockSemanal);
        return true;
    }

    @Transactional
    @Override
    public boolean createCotacaoMensal(CotacaoStockMensal cotacaoStockMensal) {
        cotacaoStockMensalRepository.save(cotacaoStockMensal);
        return true;
    }

    @Override
    public List<CotacaoStockDiario> findCotacaoDiarioByAtivo(Stock stock ) {
        return cotacaoStockDiarioRepository.findByStock(stock);
    }

    @Override
    public List<CotacaoStockDiario> findCotacaoDiarioByAtivo(Stock stock, Sort sort) {
        return cotacaoStockDiarioRepository.findByStock(stock, sort);
    }

    @Override
    public List<CotacaoStockSemanal> findCotacaoSemanalByAtivo(Stock stock) {
        return cotacaoStockSemanalRepository.findByStock(stock);
    }

    @Override
    public List<CotacaoStockSemanal> findCotacaoSemanalByAtivo(Stock stock, Sort sort) {
        return cotacaoStockSemanalRepository.findByStock(stock, sort);
    }

    @Override
    public List<CotacaoStockMensal> findCotacaoMensalByAtivo(Stock stock) {
        return cotacaoStockMensalRepository.findByStock(stock);
    }

    public CotacaoStockMensal findCotacaoMensalByAtivo(Stock stock, LocalDate periodo) { // Este campo periodo deve considerar apenas Ano e Mes

        List<CotacaoStockMensal> list = this.findCotacaoMensalByAtivo(stock);
        if ( !list.isEmpty()){
            Optional<CotacaoStockMensal> optCotacaoMensal = list.stream()
                                                               .filter(cotacao -> cotacao.getData().getYear() == periodo.getYear() && cotacao.getData().getMonthValue() == periodo.getMonthValue())
                                                               .findFirst();
            if ( optCotacaoMensal.isPresent()){
                return optCotacaoMensal.get();
            }
        }
        return null;
    }

    @Override
    public List<CotacaoStockMensal> findCotacaoMensalByAtivo(Stock stock, Sort sort) {
        return cotacaoStockMensalRepository.findByStock(stock, sort);
    }

    @Transactional
    @Override
    public void cleanAll() {
        cotacaoStockDiarioRepository.deleteAll();
        cotacaoStockSemanalRepository.deleteAll();
        cotacaoStockMensalRepository.deleteAll();
    }

    @Transactional
    @Override
    public void cleanByPeriodo(String periodo) {
        if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoStockDiarioRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoStockSemanalRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoStockMensalRepository.deleteAll();
        }
    }

    @Override
    public StockCotacaoDTO findCotacaoByIdAtivo(Long idStock) {
        Optional<Stock> stockOpt = stockRepository.findById(idStock);
        if ( stockOpt.isPresent()){
            List<CotacaoStockDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(stockOpt.get());
            List<CotacaoStockSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(stockOpt.get());
            List<CotacaoStockMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(stockOpt.get());
            return StockCotacaoDTO.fromEntity(stockOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }
        return null;
    }

    @Override
    public StockCotacaoDTO findCotacaoByIdAtivoByPeriodo(Long idStock, String periodo) {
        Optional<Stock> stockOpt = stockRepository.findById(idStock);
        if ( stockOpt.isPresent()){
            List<CotacaoStockDiario> listCotacaoDiario = null;
            List<CotacaoStockSemanal> listCotacaoSemanal = null;
            List<CotacaoStockMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(stockOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(stockOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(stockOpt.get());
            }
            return StockCotacaoDTO.fromEntity(stockOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }


    @Override
    public StockCotacaoDTO findCotacaoBySiglaFull(String sigla) {
        Optional<Stock> stockOpt = stockRepository.findBySigla(sigla);
        if ( stockOpt.isPresent()){
            List<CotacaoStockDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoStockSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoStockMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<IncreasePercentStock> listIncreasePercentDiario = increasePercentStockService.findIncreasePercentByStockByPeriodo(stockOpt.get(), PeriodoEnum.DIARIO);
            List<IncreasePercentStock> listIncreasePercentSemanal = increasePercentStockService.findIncreasePercentByStockByPeriodo(stockOpt.get(), PeriodoEnum.SEMANAL);
            List<IncreasePercentStock> listIncreasePercentMensal = increasePercentStockService.findIncreasePercentByStockByPeriodo(stockOpt.get(), PeriodoEnum.MENSAL);
            List<DividendoStock> listDividendos = dividendoStockService.findDividendoByStock(stockOpt.get());
            List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao = this.getRoiDividendoCotacao(listDividendos, listCotacaoMensal);
            return StockCotacaoDTO.fromEntity(stockOpt.get(),
                                             listCotacaoDiario,
                                             listCotacaoSemanal,
                                             listCotacaoMensal,
                                             listIncreasePercentDiario,
                                             listIncreasePercentSemanal,
                                             listIncreasePercentMensal,
                                             listDividendos,
                                             listRoiDividendoCotacao);
        }
        return null;
    }

    private List<RoiDividendoCotacaoDTO> getRoiDividendoCotacao(List<DividendoStock> listDividendos, List<CotacaoStockMensal> listCotacaoMensal) {
        List<RoiDividendoCotacaoDTO> list = new ArrayList<>();

        if ( !listDividendos.isEmpty() && !listCotacaoMensal.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoStockMensal cotacaoStockMensal = this.getCotacaoMensalRoiDividendo(dividendo, listCotacaoMensal);
                if ( cotacaoStockMensal != null){
                    Double roiDividendoCotacao = dividendo.getDividend() / cotacaoStockMensal.getClose();
                    RoiDividendoCotacaoDTO dto = RoiDividendoCotacaoDTO.from(roiDividendoCotacao, dividendo, cotacaoStockMensal);
                    list.add(dto);
                }
            });
        }
        return list;
    }

    private CotacaoStockMensal getCotacaoMensalRoiDividendo(DividendoStock dividendo, List<CotacaoStockMensal> listCotacaoMensal) {

        Optional<CotacaoStockMensal> optCotacaoStockMensal = listCotacaoMensal.stream()
                .filter(CotacaoStockMensal -> CotacaoStockMensal.getData().getYear() == dividendo.getData().getYear() && CotacaoStockMensal.getData().getMonthValue() == dividendo.getData().getMonthValue())
                .findFirst();

        if ( optCotacaoStockMensal.isPresent()){
            return optCotacaoStockMensal.get();
        }

        return null;
    }

    @Override
    public StockCotacaoDTO findCotacaoBySiglaByPeriodo(String sigla, String periodo) {
        Optional<Stock> stockOpt = stockRepository.findBySigla(sigla);

        if ( stockOpt.isPresent()){
            List<CotacaoStockDiario> listCotacaoDiario = null;
            List<CotacaoStockSemanal> listCotacaoSemanal = null;
            List<CotacaoStockMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(stockOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(stockOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(stockOpt.get());
            }
            return StockCotacaoDTO.fromEntity(stockOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowDiary(FilterAtivoCotacaoGrowDTO dto) {

        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoStockDiario> listCotacaoInicio = cotacaoStockDiarioRepository.findByData(dtStart);
        List<CotacaoStockDiario> listCotacaoFim = cotacaoStockDiarioRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoStockDiario> cotacaoStockDiarioFimOpt = this.getCotacaoDiarioFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoStockDiarioFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoStockDiarioFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                                                                                                                            cotacaoInicio.getStock().getSigla(),
                                                                                                                            cotacaoInicio.getClose(),
                            cotacaoStockDiarioFimOpt.get().getClose(),
                                                                                                                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                                                                                                                            Utils.converteLocalDateToString(cotacaoStockDiarioFimOpt.get().getData()));
                    listResultFilterAtivoCotacaoGrow.add(resultFilterAtivoCotacaoGrowDTO);
                }
            });
        }

        List<ResultFilterAtivoCotacaoGrowDTO> listFinal = new ArrayList<>();
        if ( !listResultFilterAtivoCotacaoGrow.isEmpty()){
            if (dto.getTipoOrdenacaoGrow().equals(TipoOrdenacaoGrowEnum.MAIS)){
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                                                             .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow).reversed())
                                                             .collect(Collectors.toList());
            }
            else {
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                                                             .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow))
                                                             .collect(Collectors.toList());
            }
        }
        return listFinal;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowWeek(FilterAtivoCotacaoGrowDTO dto) {

        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoStockSemanal> listCotacaoInicio = cotacaoStockSemanalRepository.findByData(dtStart);
        List<CotacaoStockSemanal> listCotacaoFim = cotacaoStockSemanalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoStockSemanal> cotacaoStockSemanalFimOpt = this.getCotacaoSemanalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoStockSemanalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoStockSemanalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getStock().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoStockSemanalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoStockSemanalFimOpt.get().getData()));
                    listResultFilterAtivoCotacaoGrow.add(resultFilterAtivoCotacaoGrowDTO);
                }
            });
        }

        List<ResultFilterAtivoCotacaoGrowDTO> listFinal = new ArrayList<>();
        if ( !listResultFilterAtivoCotacaoGrow.isEmpty()){
            if (dto.getTipoOrdenacaoGrow().equals(TipoOrdenacaoGrowEnum.MAIS)){
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow).reversed())
                        .collect(Collectors.toList());
            }
            else {
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow))
                        .collect(Collectors.toList());
            }
        }
        return listFinal;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowMonth(FilterAtivoCotacaoGrowDTO dto) {

        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoStockMensal> listCotacaoInicio = cotacaoStockMensalRepository.findByData(dtStart);
        List<CotacaoStockMensal> listCotacaoFim = cotacaoStockMensalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoStockMensal> cotacaoStockMensalFimOpt = this.getCotacaoMensalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoStockMensalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoStockMensalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getStock().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoStockMensalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoStockMensalFimOpt.get().getData()));
                    listResultFilterAtivoCotacaoGrow.add(resultFilterAtivoCotacaoGrowDTO);
                }
            });
        }

        List<ResultFilterAtivoCotacaoGrowDTO> listFinal = new ArrayList<>();
        if ( !listResultFilterAtivoCotacaoGrow.isEmpty()){
            if (dto.getTipoOrdenacaoGrow().equals(TipoOrdenacaoGrowEnum.MAIS)){
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow).reversed())
                        .collect(Collectors.toList());
            }
            else {
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow))
                        .collect(Collectors.toList());
            }
        }
        return listFinal;
    }

    private Optional<CotacaoStockDiario> getCotacaoDiarioFim(CotacaoStockDiario cotacao, List<CotacaoStockDiario> listCotacaoFim) {
        return listCotacaoFim.stream()
                             .filter(cotacaoFim -> cotacaoFim.getStock().getSigla().equals(cotacao.getStock().getSigla()))
                              .findFirst();
    }

    private Optional<CotacaoStockSemanal> getCotacaoSemanalFim(CotacaoStockSemanal cotacao, List<CotacaoStockSemanal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getStock().getSigla().equals(cotacao.getStock().getSigla()))
                .findFirst();
    }

    private Optional<CotacaoStockMensal> getCotacaoMensalFim(CotacaoStockMensal cotacao, List<CotacaoStockMensal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getStock().getSigla().equals(cotacao.getStock().getSigla()))
                .findFirst();
    }

    @Override
    public LastCotacaoAtivoDiarioDTO getLastCotacaoDiario(Stock stock) {
        List<CotacaoStockDiario> listCotacaoStockDiario = cotacaoStockDiarioRepository.findByStock(stock, Sort.by(Sort.Direction.DESC, "data"));
        if (! listCotacaoStockDiario.isEmpty()){
            Optional<CotacaoStockDiario> optCotacaoStockDiario = listCotacaoStockDiario.stream()
                                                                                    .findFirst();
            if ( optCotacaoStockDiario.isPresent()){
                return LastCotacaoAtivoDiarioDTO.from(optCotacaoStockDiario.get());
            }
        }
        return null;
    }

    @Override
    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {

        List<Stock> listStock = stockRepository.findAll();
        if ( !listStock.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listStock.forEach(stock ->{
                List<CotacaoStockDiario>  listCotacaoDiario  = cotacaoStockDiarioRepository.findByStock(stock);
                List<CotacaoStockSemanal> listCotacaoSemanal = cotacaoStockSemanalRepository.findByStock(stock);
                List<CotacaoStockMensal> listCotacaoMensal   = cotacaoStockMensalRepository.findByStock(stock);

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, stock);
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, stock);
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, stock);

                dto.getListDiario().addAll(listDiario);
                dto.getListSemanal().addAll(listSemanal);
                dto.getListMensal().addAll(listMensal);
            });

            List<SumIncreasePercentCotacaoDTO> listDiarioFinal  = dto.getListDiario().stream().sorted(Comparator.comparingDouble(SumIncreasePercentCotacaoDTO::getSumIncreasePercent).reversed()).collect(Collectors.toList());
            List<SumIncreasePercentCotacaoDTO> listSemanalFinal = dto.getListSemanal().stream().sorted(Comparator.comparingDouble(SumIncreasePercentCotacaoDTO::getSumIncreasePercent).reversed()).collect(Collectors.toList());
            List<SumIncreasePercentCotacaoDTO> listMensalFinal  = dto.getListMensal().stream().sorted(Comparator.comparingDouble(SumIncreasePercentCotacaoDTO::getSumIncreasePercent).reversed()).collect(Collectors.toList());

            dto.setListDiario(listDiarioFinal);
            dto.setListSemanal(listSemanalFinal);
            dto.setListMensal(listMensalFinal);
            return dto;
        }
        return null;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoStockDiario> listCotacaoDiario, Stock stock) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoDiario.isEmpty()){
            List<CotacaoStockDiario>  list = listCotacaoDiario.stream()
                    .sorted(Comparator.comparingDouble(CotacaoStockDiario::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoStockDiario cotacaoAtual = list.get(0);
                    CotacaoStockDiario cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoStockDiario cotacaoAtual = list.get(i);
                            CotacaoStockDiario cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoStockSemanal> listCotacaoSemanal, Stock stock) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoSemanal.isEmpty()){
            List<CotacaoStockSemanal>  list = listCotacaoSemanal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoStockSemanal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoStockSemanal cotacaoAtual = list.get(0);
                    CotacaoStockSemanal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoStockSemanal cotacaoAtual = list.get(i);
                            CotacaoStockSemanal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoStockMensal> listCotacaoMensal, Stock stock) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoMensal.isEmpty()){
            List<CotacaoStockMensal>  list = listCotacaoMensal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoStockMensal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoStockMensal cotacaoAtual = list.get(0);
                    CotacaoStockMensal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoStockMensal cotacaoAtual = list.get(i);
                            CotacaoStockMensal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }
}
