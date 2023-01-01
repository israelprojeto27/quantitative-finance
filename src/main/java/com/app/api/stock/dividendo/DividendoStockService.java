package com.app.api.stock.dividendo;


import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.cotacao.repositories.CotacaoStockDiarioRepository;
import com.app.api.stock.dividendo.dto.DividendoStockDTO;
import com.app.api.stock.dividendo.dto.StockListDividendoDTO;
import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.api.stock.principal.StockRepository;
import com.app.api.stock.principal.entity.Stock;
import com.app.commons.basic.dividendo.BaseDividendoService;
import com.app.commons.dtos.FilterPeriodDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.dtos.dividendo.*;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DividendoStockService implements BaseDividendoService<DividendoStock, DividendoStockDTO, StockListDividendoDTO, Stock> {

    @Autowired
    DividendoStockRepository repository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    CotacaoStockDiarioRepository cotacaoStockDiarioRepository;

    @Transactional
    @Override
    public void save(DividendoStock dividendoStock) {
        repository.save(dividendoStock);
    }

    @Transactional
    @Override
    public void cleanAll() {
        repository.deleteAll();
    }

    @Override
    public List<DividendoStockDTO> findDividendoByIdAtivo(Long idAStock) {
        Optional<Stock> stockOpt = stockRepository.findById(idAStock);
        if ( stockOpt.isPresent()){
            List<DividendoStock> listDividendos = repository.findAllByStock(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data") );
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoStockDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<DividendoStockDTO> findDividendoBySigla(String sigla) {
        Optional<Stock> stockOpt = stockRepository.findBySigla(sigla);
        if ( stockOpt.isPresent()){
            List<DividendoStock> listDividendos = repository.findAllByStock(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoStockDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<StockListDividendoDTO> findAtivoListDividendos() {
        List<Stock> listStocks = stockRepository.findAll();
        if (!listStocks.isEmpty()){
            List<StockListDividendoDTO> listStockDividendos = new ArrayList<StockListDividendoDTO>();
            List<DividendoStock> listDividendos = new ArrayList<>();
            listStocks.forEach(stock-> {
                listStockDividendos.add(StockListDividendoDTO.fromEntity(stock, repository.findAllByStock(stock, Sort.by(Sort.Direction.DESC, "data"))));
            });
           return listStockDividendos;
        }
        return null;
    }

    @Override
    public List<StockListDividendoDTO> filterDividendosByPeriod(FilterPeriodDTO dto) {

        List<StockListDividendoDTO> listFinal = new ArrayList<>();

        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<DividendoStock> listDividendos = repository.findByDataBetween(dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            HashMap<String, List<DividendoStock>> map = new HashMap<>();
            listDividendos.forEach(dividendo ->{
                if ( map.containsKey(dividendo.getStock().getSigla())){
                    List<DividendoStock> list = map.get(dividendo.getStock().getSigla());
                    list.add(dividendo);
                    map.put(dividendo.getStock().getSigla(), list);
                }
                else {
                    List<DividendoStock> list = new ArrayList<>();
                    list.add(dividendo);
                    map.put(dividendo.getStock().getSigla(), list);
                }
            });

            if (! map.isEmpty() ){
                map.keySet().forEach(sigla -> {
                    List<DividendoStock> list = map.get(sigla);
                    StockListDividendoDTO stockListDividendoDTO = StockListDividendoDTO.from(sigla, list);
                    listFinal.add(stockListDividendoDTO);
                });
            }

            return listFinal;
        }
        return listFinal;
    }

    @Override
    public List<SumAtivoDividendosDTO> sumDividendosByAtivo() {
        List<Stock> listStocks = stockRepository.findAll();
        if (!listStocks.isEmpty()){
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();
            listStocks.forEach(stock-> {
                List<DividendoStock> listDividendos = repository.findAllByStock(stock);
                double sumDividendos = listDividendos.stream()
                                              .mapToDouble(dividendoStock -> dividendoStock.getDividend())
                                              .sum();

                SumAtivoDividendosDTO dto = SumAtivoDividendosDTO.from(stock, sumDividendos);
                lisSumDividendos.add(dto);
            });
            return lisSumDividendos;
        }
        return null;
    }

    @Override
    public List<SumAtivoDividendosDTO> filterSumDividendosByAtivoByPeriod(FilterPeriodDTO dto) {

        List<Stock> listStocks = stockRepository.findAll();
        if (!listStocks.isEmpty()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();

            listStocks.forEach(stock -> {
                List<DividendoStock> listDividendos = repository.findByStockAndDataBetween(stock, dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));

                double sumDividendos = listDividendos.stream()
                        .mapToDouble(dividendoStock -> dividendoStock.getDividend())
                        .sum();

                SumAtivoDividendosDTO sumAtivoDividendosDTO = SumAtivoDividendosDTO.from(stock, sumDividendos);
                lisSumDividendos.add(sumAtivoDividendosDTO);
            });
            return lisSumDividendos;
        }

        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotas(Long idStock, Long quantidadeCotas) {

        Optional<Stock> stockOpt = stockRepository.findById(idStock);
        if (stockOpt.isPresent()){
            List<DividendoStock> listDividendos = repository.findAllByStock(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoStockDiario> listCotacaoDiario = cotacaoStockDiarioRepository.findByStock(stockOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoStock -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoStock.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasStock(dividendoStock.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoStock.getData(),
                                                                                                                 valorRendimentoDividendo,
                                                                                                                 valorCotacaoAcao,
                                                                                                                 dividendoStock.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(stockOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaAtivoByQuantCotas(String sigla, Long quantidadeCotas) {

        Optional<Stock> stockOpt = stockRepository.findBySigla(sigla);
        if (stockOpt.isPresent()){
            List<DividendoStock> listDividendos = repository.findAllByStock(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoStockDiario> listCotacaoDiario = cotacaoStockDiarioRepository.findByStock(stockOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoStock -> {
                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoStock.getData(),
                                                                                             quantidadeCotas * dividendoStock.getDividend(),
                                                                                                                 this.getQuantidadeCotasStock(dividendoStock.getData(), listCotacaoDiario),
                                                                                                                 dividendoStock.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(stockOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    public List<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaAllAtivosByQuantCotas(Long quantidadeCotas) {
        List<Stock> listStocks = stockRepository.findAll();
        if ( !listStocks.isEmpty()){
            List<SumCalculateYieldDividendosAtivoDTO> listFinal = new ArrayList<>();
            listStocks.forEach(stock ->{
                List<DividendoStock> listDividendos = repository.findAllByStock(stock, Sort.by(Sort.Direction.DESC, "data"));
                List<CotacaoStockDiario> listCotacaoDiario = cotacaoStockDiarioRepository.findByStock(stock);

                if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                    List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                    listDividendos.forEach(dividendoStock -> {
                        SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoStock.getData(),
                                quantidadeCotas * dividendoStock.getDividend(),
                                this.getQuantidadeCotasStock(dividendoStock.getData(), listCotacaoDiario),
                                dividendoStock.getDividend());
                        listCalcultaDetailYieldDividendos.add(dto);
                    });

                    listFinal.add(SumCalculateYieldDividendosAtivoDTO.from(stock, listCalcultaDetailYieldDividendos));
                }
            });

            return listFinal;
        }

        return null;
    }

    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotasByPeriod(Long idStock, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<Stock> stockOpt = stockRepository.findById(idStock);
        if (stockOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoStock> listDividendos = repository.findByStockAndDataBetween(stockOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoStockDiario> listCotacaoDiario = cotacaoStockDiarioRepository.findByStock(stockOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoAcao.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasStock(dividendoAcao.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoAcao,
                            dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(stockOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaByQuantCotasByPeriod(String sigla, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<Stock> stockOpt = stockRepository.findBySigla(sigla);
        if (stockOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoStock> listDividendos = repository.findByStockAndDataBetween(stockOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoStockDiario> listCotacaoDiario = cotacaoStockDiarioRepository.findByStock(stockOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoStock -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoStock.getDividend();
                    Double valorCotacaoStock = this.getQuantidadeCotasStock(dividendoStock.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoStock.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoStock,
                            dividendoStock.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(stockOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    private Double getQuantidadeCotasStock(LocalDate data, List<CotacaoStockDiario> listCotacaoDiario) {
        Optional<CotacaoStockDiario> cotacaoStockOpt = listCotacaoDiario.stream()
                .filter(cotacaoAcaoDiario -> cotacaoAcaoDiario.getData().equals(data) )
                .findFirst();
        if (cotacaoStockOpt.isPresent()){
            return cotacaoStockOpt.get().getClose();
        }
        return null;
    }


    public LastDividendoAtivoDTO getLastDividendo(Stock stock) {
        List<DividendoStock> listDividendos = repository.findAllByStock(stock, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoStock> optDividendoStock = listDividendos.stream()
                                                        .findFirst();
            if ( optDividendoStock.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoStock.get());
            }
        }
        return null;
    }

    public List<DividendoStock> findDividendoByStock(Stock stock) {
        return repository.findAllByStock(stock, Sort.by(Sort.Direction.DESC, "data"));
    }

    //
    public List<DividendoStock> findDividendoBetweenDates(LocalDate dtInicio, LocalDate dtFim) {
       return repository.findByDataBetween(dtInicio, dtFim, Sort.by(Sort.Direction.DESC, "data"));
    }

    public ResultSimulaDividendoSiglaDTO simulaRendimentoDividendoBySigla(String sigla, String valorInvestimento) {

        Optional<Stock> stockOpt = stockRepository.findBySigla(sigla);
        if (stockOpt.isPresent()){
            List<CotacaoStockDiario> listCotacaoStockDiario = cotacaoStockDiarioRepository.findByStock(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            Optional<CotacaoStockDiario> optCotacaoStockDiario = listCotacaoStockDiario.stream().findFirst();
            List<DividendoStock> listDividendos = repository.findAllByStock(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            if (! listDividendos.isEmpty() && optCotacaoStockDiario.isPresent()){
                Double valorInvest = Double.parseDouble(valorInvestimento);
                ResultSimulaDividendoSiglaDTO dto = new ResultSimulaDividendoSiglaDTO();
                List<ResultSimulaDividendoSiglaDetailDTO> list = new ArrayList<>();
                List<ResultSimulaDividendoSiglaDetailDTO> listFinal = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    if (dividendoAcao != null && dividendoAcao.getDividend() != null ){
                        dto.setTotalGanhoDividendos(dto.getTotalGanhoDividendos() + dividendoAcao.getDividend());
                        list.add(ResultSimulaDividendoSiglaDetailDTO.from(valorInvest, dividendoAcao, optCotacaoStockDiario.get()));
                    }
                });

                if ( !list.isEmpty()){
                    dto.setGanhoMedioDividendos(dto.getTotalGanhoDividendos() / list.size());
                }

                dto.setTotalGanhoDividendosFmt(Utils.converterDoubleDoisDecimaisString(dto.getTotalGanhoDividendos()));
                dto.setGanhoMedioDividendosFmt(Utils.converterDoubleDoisDecimaisString(dto.getGanhoMedioDividendos()));
                dto.setQuantidadeCotas(listDividendos.size());
                dto.setList(list);
                return dto;
            }
        }
        return null;
    }


    public ResultSimulaDividendoSiglaDTO simulaRendimentoDividendoBySiglaByQuantCotas(String sigla, String quantCotas) {

        Optional<Stock> stockOpt = stockRepository.findBySigla(sigla);
        if (stockOpt.isPresent()){
            List<CotacaoStockDiario> listCotacaoStockDiario = cotacaoStockDiarioRepository.findByStock(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            Optional<CotacaoStockDiario> optCotacaoStockDiario = listCotacaoStockDiario.stream().findFirst();
            List<DividendoStock> listDividendos = repository.findAllByStock(stockOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            if (! listDividendos.isEmpty() && optCotacaoStockDiario.isPresent()){
                Integer quantidadeCotas = Integer.parseInt(quantCotas);
                ResultSimulaDividendoSiglaDTO dto = new ResultSimulaDividendoSiglaDTO();
                List<ResultSimulaDividendoSiglaDetailDTO> list = new ArrayList<>();
                List<ResultSimulaDividendoSiglaDetailDTO> listFinal = new ArrayList<>();
                listDividendos.forEach(dividendoStock -> {
                    if (dividendoStock != null && dividendoStock.getDividend() != null ){
                        dto.setTotalGanhoDividendos(dto.getTotalGanhoDividendos() + dividendoStock.getDividend());
                        list.add(ResultSimulaDividendoSiglaDetailDTO.from(quantidadeCotas, dividendoStock, optCotacaoStockDiario.get()));
                    }
                });

                if ( !list.isEmpty()){
                    dto.setGanhoMedioDividendos(dto.getTotalGanhoDividendos() / list.size());
                }

                dto.setTotalGanhoDividendosFmt(Utils.converterDoubleDoisDecimaisString(dto.getTotalGanhoDividendos()));
                dto.setGanhoMedioDividendosFmt(Utils.converterDoubleDoisDecimaisString(dto.getGanhoMedioDividendos()));
                dto.setQuantidadeCotas(listDividendos.size());
                dto.setList(list);
                return dto;
            }
        }
        return null;
    }
}
