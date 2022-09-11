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
import com.app.api.fundoimobiliario.increasepercent.IncreasePercentFundoImobiliario;
import com.app.api.fundoimobiliario.increasepercent.IncreasePercentFundoService;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
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

    @Autowired
    IncreasePercentFundoService increasePercentFundoService;


    @Transactional
    @Override
    public void addCotacaoAtivo(String line, FundoImobiliario fundo, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoFundoDiario cotacaoFundoDiario = CotacaoFundoDiario.toEntity(array, fundo);
            if ( cotacaoFundoDiario != null)
                this.createCotacaoDiario(cotacaoFundoDiario);
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoFundoSemanal cotacaoFundoSemanal = CotacaoFundoSemanal.toEntity(array, fundo);
            if ( cotacaoFundoSemanal != null)
                this.createCotacaoSemanal(cotacaoFundoSemanal);
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoFundoMensal cotacaoFundoMensal = CotacaoFundoMensal.toEntity(array, fundo);
            if ( cotacaoFundoMensal != null )
                this.createCotacaoMensal(cotacaoFundoMensal);
        }
    }


    @Transactional
    @Override
    public void addCotacaoAtivoPartial(String line, FundoImobiliario fundo, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoFundoDiario cotacaoFundoDiario = CotacaoFundoDiario.toEntity(array, fundo);
            List<CotacaoFundoDiario> listCotacao = cotacaoFundoDiarioRepository.findByFundoAndData(fundo, cotacaoFundoDiario.getData());
            if ( listCotacao.isEmpty() && cotacaoFundoDiario != null){
                this.createCotacaoDiario(cotacaoFundoDiario);
            }
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoFundoSemanal cotacaoFundoSemanal = CotacaoFundoSemanal.toEntity(array, fundo);
            List<CotacaoFundoSemanal> listCotacao = cotacaoFundoSemanalRepository.findByFundoAndData(fundo, cotacaoFundoSemanal.getData());
            if (listCotacao.isEmpty() && cotacaoFundoSemanal != null){
                this.createCotacaoSemanal(cotacaoFundoSemanal);
            }
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoFundoMensal cotacaoFundoMensal = CotacaoFundoMensal.toEntity(array, fundo);
            List<CotacaoFundoMensal> listCotacao = cotacaoFundoMensalRepository.findByFundoAndData(fundo, cotacaoFundoMensal.getData());
            if (listCotacao.isEmpty() && cotacaoFundoMensal != null ){
                this.createCotacaoMensal(cotacaoFundoMensal);
            }
        }
    }

    @Override
    @Transactional
    public boolean createCotacaoDiario(CotacaoFundoDiario cotacaoFundoDiario) {
        cotacaoFundoDiarioRepository.save(cotacaoFundoDiario);
        return true;
    }


    @Override
    @Transactional
    public boolean createCotacaoSemanal(CotacaoFundoSemanal cotacaoFundoSemanal) {
        cotacaoFundoSemanalRepository.save(cotacaoFundoSemanal);
        return true;
    }

    @Override
    @Transactional
    public boolean createCotacaoMensal(CotacaoFundoMensal cotacaoFundoMensal) {
        cotacaoFundoMensalRepository.save(cotacaoFundoMensal);
        return true;
    }

    @Override
    public List<CotacaoFundoDiario> findCotacaoDiarioByAtivo(FundoImobiliario fundoImobiliario) {
        return cotacaoFundoDiarioRepository.findByFundo(fundoImobiliario);
    }

    @Override
    public List<CotacaoFundoDiario> findCotacaoDiarioByAtivo(FundoImobiliario fundoImobiliario, Sort sort) {
        return cotacaoFundoDiarioRepository.findByFundo(fundoImobiliario, sort);
    }

    @Override
    public List<CotacaoFundoSemanal> findCotacaoSemanalByAtivo(FundoImobiliario fundoImobiliario) {
        return cotacaoFundoSemanalRepository.findByFundo(fundoImobiliario);
    }

    @Override
    public List<CotacaoFundoSemanal> findCotacaoSemanalByAtivo(FundoImobiliario fundoImobiliario, Sort sort) {
        return cotacaoFundoSemanalRepository.findByFundo(fundoImobiliario, sort);
    }

    @Override
    public List<CotacaoFundoMensal> findCotacaoMensalByAtivo(FundoImobiliario fundoImobiliario) {
        return cotacaoFundoMensalRepository.findByFundo(fundoImobiliario);
    }

    @Override
    public List<CotacaoFundoMensal> findCotacaoMensalByAtivo(FundoImobiliario fundoImobiliario, Sort sort) {
        return cotacaoFundoMensalRepository.findByFundo(fundoImobiliario, sort);
    }

    public CotacaoFundoMensal findCotacaoMensalByAtivo(FundoImobiliario fundoImobiliario, LocalDate periodo) { // Este campo periodo deve considerar apenas Ano e Mes

        List<CotacaoFundoMensal> list = this.findCotacaoMensalByAtivo(fundoImobiliario);
        if ( !list.isEmpty()){
            Optional<CotacaoFundoMensal> optCotacaoMensal = list.stream()
                    .filter(cotacao -> cotacao.getData().getYear() == periodo.getYear() && cotacao.getData().getMonthValue() == periodo.getMonthValue())
                    .findFirst();
            if ( optCotacaoMensal.isPresent()){
                return optCotacaoMensal.get();
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void cleanAll() {
        cotacaoFundoDiarioRepository.deleteAll();
        cotacaoFundoSemanalRepository.deleteAll();
        cotacaoFundoMensalRepository.deleteAll();
    }

    @Override
    @Transactional
    public void cleanByPeriodo(String periodo) {
        if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoFundoDiarioRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoFundoSemanalRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoFundoMensalRepository.deleteAll();
        }
    }

    @Override
    public FundoCotacaoDTO findCotacaoByIdAtivo(Long id) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findById(id);
        if ( fundoOpt.isPresent()){
            List<CotacaoFundoDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(fundoOpt.get());
            List<CotacaoFundoSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(fundoOpt.get());
            List<CotacaoFundoMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(fundoOpt.get());
            return FundoCotacaoDTO.fromEntity(fundoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }
        return null;
    }

    @Override
    public FundoCotacaoDTO findCotacaoByIdAtivoByPeriodo(Long id, String periodo) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findById(id);
        if ( fundoOpt.isPresent()){
            List<CotacaoFundoDiario> listCotacaoDiario = null;
            List<CotacaoFundoSemanal> listCotacaoSemanal = null;
            List<CotacaoFundoMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(fundoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(fundoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(fundoOpt.get());
            }
            return FundoCotacaoDTO.fromEntity(fundoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public FundoCotacaoDTO findCotacaoBySiglaFull(String sigla) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findBySigla(sigla);
        if ( fundoOpt.isPresent()){
            List<CotacaoFundoDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(fundoOpt.get());
            List<CotacaoFundoSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(fundoOpt.get());
            List<CotacaoFundoMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(fundoOpt.get());

            List<IncreasePercentFundoImobiliario> listIncreasePercentDiario = increasePercentFundoService.findIncreasePercentByFundoByPeriodo(fundoOpt.get(), PeriodoEnum.DIARIO);
            List<IncreasePercentFundoImobiliario> listIncreasePercentSemanal = increasePercentFundoService.findIncreasePercentByFundoByPeriodo(fundoOpt.get(), PeriodoEnum.SEMANAL);
            List<IncreasePercentFundoImobiliario> listIncreasePercentMensal = increasePercentFundoService.findIncreasePercentByFundoByPeriodo(fundoOpt.get(), PeriodoEnum.MENSAL);
            List<DividendoFundo> listDividendos = dividendoFundoService.findDividendoByFundo(fundoOpt.get());
            List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao = this.getRoiDividendoCotacao(listDividendos, listCotacaoMensal);
            return FundoCotacaoDTO.fromEntity(fundoOpt.get(),
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

    private List<RoiDividendoCotacaoDTO> getRoiDividendoCotacao(List<DividendoFundo> listDividendos, List<CotacaoFundoMensal> listCotacaoMensal) {
        List<RoiDividendoCotacaoDTO> list = new ArrayList<>();

        if ( !listDividendos.isEmpty() && !listCotacaoMensal.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoFundoMensal cotacaoBdrMensal = this.getCotacaoMensalRoiDividendo(dividendo, listCotacaoMensal);
                if ( cotacaoBdrMensal != null){
                    Double roiDividendoCotacao = dividendo.getDividend() / cotacaoBdrMensal.getClose();
                    RoiDividendoCotacaoDTO dto = RoiDividendoCotacaoDTO.from(roiDividendoCotacao, dividendo, cotacaoBdrMensal);
                    list.add(dto);
                }
            });
        }
        return list;
    }

    private CotacaoFundoMensal getCotacaoMensalRoiDividendo(DividendoFundo dividendo, List<CotacaoFundoMensal> listCotacaoMensal) {

        Optional<CotacaoFundoMensal> optCotacaoFundoMensal = listCotacaoMensal.stream()
                .filter(cotacaoFundoMensal -> cotacaoFundoMensal.getData().getYear() == dividendo.getData().getYear() && cotacaoFundoMensal.getData().getMonthValue() == dividendo.getData().getMonthValue())
                .findFirst();

        if ( optCotacaoFundoMensal.isPresent()){
            return optCotacaoFundoMensal.get();
        }

        return null;
    }

    @Override
    public FundoCotacaoDTO findCotacaoBySiglaByPeriodo(String sigla, String periodo) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findBySigla(sigla);

        if ( fundoOpt.isPresent()){
            List<CotacaoFundoDiario> listCotacaoDiario = null;
            List<CotacaoFundoSemanal> listCotacaoSemanal = null;
            List<CotacaoFundoMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(fundoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(fundoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(fundoOpt.get());
            }
            return FundoCotacaoDTO.fromEntity(fundoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowDiary(FilterAtivoCotacaoGrowDTO dto) {
        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoFundoDiario> listCotacaoInicio = cotacaoFundoDiarioRepository.findByData(dtStart);
        List<CotacaoFundoDiario> listCotacaoFim = cotacaoFundoDiarioRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoFundoDiario> cotacaoFundoDiarioFimOpt = this.getCotacaoDiarioFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoFundoDiarioFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoFundoDiarioFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getFundo().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoFundoDiarioFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoFundoDiarioFimOpt.get().getData()));
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

        List<CotacaoFundoSemanal> listCotacaoInicio = cotacaoFundoSemanalRepository.findByData(dtStart);
        List<CotacaoFundoSemanal> listCotacaoFim = cotacaoFundoSemanalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoFundoSemanal> cotacaoFundoSemanalFimOpt = this.getCotacaoSemanalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoFundoSemanalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoFundoSemanalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getFundo().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoFundoSemanalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoFundoSemanalFimOpt.get().getData()));
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

        List<CotacaoFundoMensal> listCotacaoInicio = cotacaoFundoMensalRepository.findByData(dtStart);
        List<CotacaoFundoMensal> listCotacaoFim = cotacaoFundoMensalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoFundoMensal> cotacaoFundoMensalFimOpt = this.getCotacaoMensalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoFundoMensalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoFundoMensalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getFundo().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoFundoMensalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoFundoMensalFimOpt.get().getData()));
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
    public LastCotacaoAtivoDiarioDTO getLastCotacaoDiario(FundoImobiliario fundoImobiliario) {
        List<CotacaoFundoDiario> listCotacaoFundoDiario = cotacaoFundoDiarioRepository.findByFundo(fundoImobiliario, Sort.by(Sort.Direction.DESC, "data"));
        if (! listCotacaoFundoDiario.isEmpty()){
            Optional<CotacaoFundoDiario> optCotacaoFundoDiario = listCotacaoFundoDiario.stream()
                    .findFirst();
            if ( optCotacaoFundoDiario.isPresent()){
                return LastCotacaoAtivoDiarioDTO.from(optCotacaoFundoDiario.get());
            }
        }
        return null;
    }

    @Override
    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {
        List<FundoImobiliario> listFundo = fundoRepository.findAll();
        if ( !listFundo.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listFundo.forEach(fundo ->{
                List<CotacaoFundoDiario>  listCotacaoDiario  = cotacaoFundoDiarioRepository.findByFundo(fundo);
                List<CotacaoFundoSemanal> listCotacaoSemanal = cotacaoFundoSemanalRepository.findByFundo(fundo);
                List<CotacaoFundoMensal> listCotacaoMensal   = cotacaoFundoMensalRepository.findByFundo(fundo);

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, fundo);
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, fundo);
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, fundo);

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
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoFundoDiario> listCotacaoDiario, FundoImobiliario fundoImobiliario) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoDiario.isEmpty()){
            List<CotacaoFundoDiario>  list = listCotacaoDiario.stream()
                    .sorted(Comparator.comparingDouble(CotacaoFundoDiario::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoFundoDiario cotacaoAtual = list.get(0);
                    CotacaoFundoDiario cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoFundoDiario cotacaoAtual = list.get(i);
                            CotacaoFundoDiario cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoFundoSemanal> listCotacaoSemanal, FundoImobiliario fundoImobiliario) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoSemanal.isEmpty()){
            List<CotacaoFundoSemanal>  list = listCotacaoSemanal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoFundoSemanal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoFundoSemanal cotacaoAtual = list.get(0);
                    CotacaoFundoSemanal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoFundoSemanal cotacaoAtual = list.get(i);
                            CotacaoFundoSemanal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoFundoMensal> listCotacaoMensal, FundoImobiliario fundoImobiliario) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoMensal.isEmpty()){
            List<CotacaoFundoMensal>  list = listCotacaoMensal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoFundoMensal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoFundoMensal cotacaoAtual = list.get(0);
                    CotacaoFundoMensal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoFundoMensal cotacaoAtual = list.get(i);
                            CotacaoFundoMensal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    private Optional<CotacaoFundoDiario> getCotacaoDiarioFim(CotacaoFundoDiario cotacao, List<CotacaoFundoDiario> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getFundo().getSigla().equals(cotacao.getFundo().getSigla()))
                .findFirst();
    }


    private Optional<CotacaoFundoSemanal> getCotacaoSemanalFim(CotacaoFundoSemanal cotacao, List<CotacaoFundoSemanal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getFundo().getSigla().equals(cotacao.getFundo().getSigla()))
                .findFirst();
    }

    private Optional<CotacaoFundoMensal> getCotacaoMensalFim(CotacaoFundoMensal cotacao, List<CotacaoFundoMensal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getFundo().getSigla().equals(cotacao.getFundo().getSigla()))
                .findFirst();
    }
}
