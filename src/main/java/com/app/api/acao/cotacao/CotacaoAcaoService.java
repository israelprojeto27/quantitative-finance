package com.app.api.acao.cotacao;

import com.app.api.acao.dividendo.dto.DividendoAcaoDTO;
import com.app.api.acao.increasepercent.IncreasePercentAcao;
import com.app.api.acao.increasepercent.IncreasePercentAcaoService;
import com.app.commons.dtos.*;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.acao.cotacao.dto.AcaoCotacaoDTO;
import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.principal.AcaoRepository;
import com.app.api.acao.cotacao.repositories.CotacaoAcaoDiarioRepository;
import com.app.api.acao.cotacao.repositories.CotacaoAcaoMensalRepository;
import com.app.api.acao.cotacao.repositories.CotacaoAcaoSemanalRepository;
import com.app.api.acao.dividendo.DividendoAcaoService;
import com.app.commons.basic.cotacao.BaseCotacaoService;
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
public class CotacaoAcaoService implements BaseCotacaoService<Acao, AcaoCotacaoDTO, CotacaoAcaoDiario, CotacaoAcaoSemanal, CotacaoAcaoMensal> {

    @Autowired
    CotacaoAcaoDiarioRepository cotacaoAcaoDiarioRepository;

    @Autowired
    CotacaoAcaoSemanalRepository cotacaoAcaoSemanalRepository;

    @Autowired
    CotacaoAcaoMensalRepository cotacaoAcaoMensalRepository;

    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    DividendoAcaoService dividendoAcaoService;


    @Autowired
    IncreasePercentAcaoService increasePercentAcaoService;


    @Transactional
    @Override
    public void addCotacaoAtivo(String line, Acao acao, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoAcaoDiario cotacaoAcaoDiario = CotacaoAcaoDiario.toEntity(array, acao);
            if ( cotacaoAcaoDiario != null){
                this.createCotacaoDiario(cotacaoAcaoDiario);

                if (cotacaoAcaoDiario.getDividend().doubleValue() > 0.0d){
                    DividendoAcao dividendoAcao = DividendoAcao.toEntity(cotacaoAcaoDiario);
                    dividendoAcaoService.save(dividendoAcao);
                }
            }
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoAcaoSemanal cotacaoAcaoSemanal = CotacaoAcaoSemanal.toEntity(array, acao);
            if ( cotacaoAcaoSemanal != null )
                this.createCotacaoSemanal(cotacaoAcaoSemanal);
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoAcaoMensal cotacaoAcaoMensal = CotacaoAcaoMensal.toEntity(array, acao);
            if ( cotacaoAcaoMensal != null)
                this.createCotacaoMensal(cotacaoAcaoMensal);
        }
    }

    @Override
    @Transactional
    public void addCotacaoAtivoPartial(String line, Acao acao, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoAcaoDiario cotacaoAcaoDiario = CotacaoAcaoDiario.toEntity(array, acao);
            List<CotacaoAcaoDiario> listCotacao = cotacaoAcaoDiarioRepository.findByAcaoAndData(acao, cotacaoAcaoDiario.getData());
            if ( listCotacao.isEmpty() && cotacaoAcaoDiario != null){
                this.createCotacaoDiario(cotacaoAcaoDiario);

                if (cotacaoAcaoDiario.getDividend().doubleValue() > 0.0d){
                    DividendoAcao dividendoAcao = DividendoAcao.toEntity(cotacaoAcaoDiario);
                    dividendoAcaoService.save(dividendoAcao);
                }
            }
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoAcaoSemanal cotacaoAcaoSemanal = CotacaoAcaoSemanal.toEntity(array, acao);
            List<CotacaoAcaoSemanal> listCotacao = cotacaoAcaoSemanalRepository.findByAcaoAndData(acao, cotacaoAcaoSemanal.getData());
            if ( listCotacao.isEmpty() && cotacaoAcaoSemanal != null){
                this.createCotacaoSemanal(cotacaoAcaoSemanal);
            }
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoAcaoMensal cotacaoAcaoMensal = CotacaoAcaoMensal.toEntity(array, acao);
            List<CotacaoAcaoMensal> listCotacao = cotacaoAcaoMensalRepository.findByAcaoAndData(acao, cotacaoAcaoMensal.getData());
            if ( listCotacao.isEmpty() && cotacaoAcaoMensal != null){
                this.createCotacaoMensal(cotacaoAcaoMensal);
            }
        }
    }



    @Transactional
    @Override
    public boolean createCotacaoDiario(CotacaoAcaoDiario cotacaoAcaoDiario) {
        cotacaoAcaoDiarioRepository.save(cotacaoAcaoDiario);
        return true;
    }

    @Transactional
    @Override
    public boolean createCotacaoSemanal(CotacaoAcaoSemanal cotacaoAcaoSemanal) {
        cotacaoAcaoSemanalRepository.save(cotacaoAcaoSemanal);
        return true;
    }

    @Transactional
    @Override
    public boolean createCotacaoMensal(CotacaoAcaoMensal cotacaoAcaoMensal) {
        cotacaoAcaoMensalRepository.save(cotacaoAcaoMensal);
        return true;
    }

    @Override
    public List<CotacaoAcaoDiario> findCotacaoDiarioByAtivo(Acao acao) {
        return cotacaoAcaoDiarioRepository.findByAcao(acao);
    }

    @Override
    public List<CotacaoAcaoDiario> findCotacaoDiarioByAtivo(Acao acao, Sort sort) {
        return cotacaoAcaoDiarioRepository.findByAcao(acao, sort);
    }

    @Override
    public List<CotacaoAcaoSemanal> findCotacaoSemanalByAtivo(Acao acao) {
        return cotacaoAcaoSemanalRepository.findByAcao(acao);
    }

    @Override
    public List<CotacaoAcaoSemanal> findCotacaoSemanalByAtivo(Acao acao, Sort sort) {
        return cotacaoAcaoSemanalRepository.findByAcao(acao, sort);
    }

    @Override
    public List<CotacaoAcaoMensal> findCotacaoMensalByAtivo(Acao acao) {
        return cotacaoAcaoMensalRepository.findByAcao(acao);
    }

    public CotacaoAcaoMensal findCotacaoMensalByAtivo(Acao acao, LocalDate periodo) { // Este campo periodo deve considerar apenas Ano e Mes

        List<CotacaoAcaoMensal> list = this.findCotacaoMensalByAtivo(acao);
        if ( !list.isEmpty()){
            Optional<CotacaoAcaoMensal> optCotacaoMensal = list.stream()
                                                               .filter(cotacao -> cotacao.getData().getYear() == periodo.getYear() && cotacao.getData().getMonthValue() == periodo.getMonthValue())
                                                               .findFirst();
            if ( optCotacaoMensal.isPresent()){
                return optCotacaoMensal.get();
            }
        }
        return null;
    }

    @Override
    public List<CotacaoAcaoMensal> findCotacaoMensalByAtivo(Acao acao, Sort sort) {
        return cotacaoAcaoMensalRepository.findByAcao(acao, sort);
    }

    @Transactional
    @Override
    public void cleanAll() {
        cotacaoAcaoDiarioRepository.deleteAll();
        cotacaoAcaoSemanalRepository.deleteAll();
        cotacaoAcaoMensalRepository.deleteAll();
    }

    @Transactional
    @Override
    public void cleanByPeriodo(String periodo) {
        if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoAcaoDiarioRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoAcaoSemanalRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoAcaoMensalRepository.deleteAll();
        }
    }

    @Override
    public AcaoCotacaoDTO findCotacaoByIdAtivo(Long idAcao) {
        Optional<Acao> acaoOpt = acaoRepository.findById(idAcao);
        if ( acaoOpt.isPresent()){
            List<CotacaoAcaoDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(acaoOpt.get());
            List<CotacaoAcaoSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(acaoOpt.get());
            List<CotacaoAcaoMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(acaoOpt.get());
            return AcaoCotacaoDTO.fromEntity(acaoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }
        return null;
    }

    @Override
    public AcaoCotacaoDTO findCotacaoByIdAtivoByPeriodo(Long idAcao, String periodo) {
        Optional<Acao> acaoOpt = acaoRepository.findById(idAcao);
        if ( acaoOpt.isPresent()){
            List<CotacaoAcaoDiario> listCotacaoDiario = null;
            List<CotacaoAcaoSemanal> listCotacaoSemanal = null;
            List<CotacaoAcaoMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(acaoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(acaoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(acaoOpt.get());
            }
            return AcaoCotacaoDTO.fromEntity(acaoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }


    @Override
    public AcaoCotacaoDTO findCotacaoBySiglaFull(String sigla) {
        Optional<Acao> acaoOpt = acaoRepository.findBySigla(sigla);
        if ( acaoOpt.isPresent()){
            List<CotacaoAcaoDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(acaoOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoAcaoSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(acaoOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoAcaoMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(acaoOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<IncreasePercentAcao> listIncreasePercentDiario = increasePercentAcaoService.findIncreasePercentByAcaoByPeriodo(acaoOpt.get(), PeriodoEnum.DIARIO);
            List<IncreasePercentAcao> listIncreasePercentSemanal = increasePercentAcaoService.findIncreasePercentByAcaoByPeriodo(acaoOpt.get(), PeriodoEnum.SEMANAL);
            List<IncreasePercentAcao> listIncreasePercentMensal = increasePercentAcaoService.findIncreasePercentByAcaoByPeriodo(acaoOpt.get(), PeriodoEnum.MENSAL);
            List<DividendoAcao> listDividendos = dividendoAcaoService.findDividendoByAcao(acaoOpt.get());
            List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao = this.getRoiDividendoCotacao(listDividendos, listCotacaoMensal);
            return AcaoCotacaoDTO.fromEntity(acaoOpt.get(),
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

    private List<RoiDividendoCotacaoDTO> getRoiDividendoCotacao(List<DividendoAcao> listDividendos, List<CotacaoAcaoMensal> listCotacaoMensal) {
        List<RoiDividendoCotacaoDTO> list = new ArrayList<>();

        if ( !listDividendos.isEmpty() && !listCotacaoMensal.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoAcaoMensal cotacaoAcaoMensal = this.getCotacaoMensalRoiDividendo(dividendo, listCotacaoMensal);
                if ( cotacaoAcaoMensal != null){
                    Double roiDividendoCotacao = dividendo.getDividend() / cotacaoAcaoMensal.getClose();
                    RoiDividendoCotacaoDTO dto = RoiDividendoCotacaoDTO.from(roiDividendoCotacao, dividendo, cotacaoAcaoMensal);
                    list.add(dto);
                }
            });
        }
        return list;
    }

    private CotacaoAcaoMensal getCotacaoMensalRoiDividendo(DividendoAcao dividendo, List<CotacaoAcaoMensal> listCotacaoMensal) {

        Optional<CotacaoAcaoMensal> optCotacaoAcaoMensal = listCotacaoMensal.stream()
                .filter(cotacaoAcaoMensal -> cotacaoAcaoMensal.getData().getYear() == dividendo.getData().getYear() && cotacaoAcaoMensal.getData().getMonthValue() == dividendo.getData().getMonthValue())
                .findFirst();

        if ( optCotacaoAcaoMensal.isPresent()){
            return optCotacaoAcaoMensal.get();
        }

        return null;
    }

    @Override
    public AcaoCotacaoDTO findCotacaoBySiglaByPeriodo(String sigla, String periodo) {
        Optional<Acao> acaoOpt = acaoRepository.findBySigla(sigla);

        if ( acaoOpt.isPresent()){
            List<CotacaoAcaoDiario> listCotacaoDiario = null;
            List<CotacaoAcaoSemanal> listCotacaoSemanal = null;
            List<CotacaoAcaoMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(acaoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(acaoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(acaoOpt.get());
            }
            return AcaoCotacaoDTO.fromEntity(acaoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowDiary(FilterAtivoCotacaoGrowDTO dto) {

        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoAcaoDiario> listCotacaoInicio = cotacaoAcaoDiarioRepository.findByData(dtStart);
        List<CotacaoAcaoDiario> listCotacaoFim = cotacaoAcaoDiarioRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoAcaoDiario> cotacaoAcaoDiarioFimOpt = this.getCotacaoDiarioFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoAcaoDiarioFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoAcaoDiarioFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                                                                                                                            cotacaoInicio.getAcao().getSigla(),
                                                                                                                            cotacaoInicio.getClose(),
                                                                                                                            cotacaoAcaoDiarioFimOpt.get().getClose(),
                                                                                                                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                                                                                                                            Utils.converteLocalDateToString(cotacaoAcaoDiarioFimOpt.get().getData()));
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

        List<CotacaoAcaoSemanal> listCotacaoInicio = cotacaoAcaoSemanalRepository.findByData(dtStart);
        List<CotacaoAcaoSemanal> listCotacaoFim = cotacaoAcaoSemanalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoAcaoSemanal> cotacaoAcaoSemanalFimOpt = this.getCotacaoSemanalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoAcaoSemanalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoAcaoSemanalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getAcao().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoAcaoSemanalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoAcaoSemanalFimOpt.get().getData()));
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

        List<CotacaoAcaoMensal> listCotacaoInicio = cotacaoAcaoMensalRepository.findByData(dtStart);
        List<CotacaoAcaoMensal> listCotacaoFim = cotacaoAcaoMensalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoAcaoMensal> cotacaoAcaoMensalFimOpt = this.getCotacaoMensalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoAcaoMensalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoAcaoMensalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getAcao().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoAcaoMensalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoAcaoMensalFimOpt.get().getData()));
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

    private Optional<CotacaoAcaoDiario> getCotacaoDiarioFim(CotacaoAcaoDiario cotacao, List<CotacaoAcaoDiario> listCotacaoFim) {
        return listCotacaoFim.stream()
                             .filter(cotacaoFim -> cotacaoFim.getAcao().getSigla().equals(cotacao.getAcao().getSigla()))
                              .findFirst();
    }

    private Optional<CotacaoAcaoSemanal> getCotacaoSemanalFim(CotacaoAcaoSemanal cotacao, List<CotacaoAcaoSemanal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getAcao().getSigla().equals(cotacao.getAcao().getSigla()))
                .findFirst();
    }

    private Optional<CotacaoAcaoMensal> getCotacaoMensalFim(CotacaoAcaoMensal cotacao, List<CotacaoAcaoMensal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getAcao().getSigla().equals(cotacao.getAcao().getSigla()))
                .findFirst();
    }

    @Override
    public LastCotacaoAtivoDiarioDTO getLastCotacaoDiario(Acao acao) {
        List<CotacaoAcaoDiario> listCotacaoAcaoDiario = cotacaoAcaoDiarioRepository.findByAcao(acao, Sort.by(Sort.Direction.DESC, "data"));
        if (! listCotacaoAcaoDiario.isEmpty()){
            Optional<CotacaoAcaoDiario> optCotacaoAcaoDiario = listCotacaoAcaoDiario.stream()
                                                                                    .findFirst();
            if ( optCotacaoAcaoDiario.isPresent()){
                return LastCotacaoAtivoDiarioDTO.from(optCotacaoAcaoDiario.get());
            }
        }
        return null;
    }

    @Override
    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {

        List<Acao> listAcao = acaoRepository.findAll();
        if ( !listAcao.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listAcao.forEach(acao ->{
                List<CotacaoAcaoDiario>  listCotacaoDiario  = cotacaoAcaoDiarioRepository.findByAcao(acao);
                List<CotacaoAcaoSemanal> listCotacaoSemanal = cotacaoAcaoSemanalRepository.findByAcao(acao);
                List<CotacaoAcaoMensal> listCotacaoMensal   = cotacaoAcaoMensalRepository.findByAcao(acao);

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, acao);
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, acao);
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, acao);

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
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoAcaoDiario> listCotacaoDiario, Acao acao) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoDiario.isEmpty()){
            List<CotacaoAcaoDiario>  list = listCotacaoDiario.stream()
                    .sorted(Comparator.comparingDouble(CotacaoAcaoDiario::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoAcaoDiario cotacaoAtual = list.get(0);
                    CotacaoAcaoDiario cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoAcaoDiario cotacaoAtual = list.get(i);
                            CotacaoAcaoDiario cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoAcaoSemanal> listCotacaoSemanal, Acao acao) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoSemanal.isEmpty()){
            List<CotacaoAcaoSemanal>  list = listCotacaoSemanal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoAcaoSemanal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoAcaoSemanal cotacaoAtual = list.get(0);
                    CotacaoAcaoSemanal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoAcaoSemanal cotacaoAtual = list.get(i);
                            CotacaoAcaoSemanal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoAcaoMensal> listCotacaoMensal, Acao acao) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoMensal.isEmpty()){
            List<CotacaoAcaoMensal>  list = listCotacaoMensal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoAcaoMensal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoAcaoMensal cotacaoAtual = list.get(0);
                    CotacaoAcaoMensal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoAcaoMensal cotacaoAtual = list.get(i);
                            CotacaoAcaoMensal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }
}
