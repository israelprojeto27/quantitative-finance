package com.app.api.acao.cotacao;

import com.app.api.acao.dividendo.dto.DividendoAcaoDTO;
import com.app.api.acao.increasepercent.IncreasePercentAcao;
import com.app.api.acao.increasepercent.IncreasePercentAcaoService;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
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
import com.app.commons.dtos.FilterAtivoCotacaoGrowDTO;
import com.app.commons.dtos.ResultFilterAtivoCotacaoGrowDTO;
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
            return AcaoCotacaoDTO.fromEntity(acaoOpt.get(),
                                             listCotacaoDiario,
                                             listCotacaoSemanal,
                                             listCotacaoMensal,
                                             listIncreasePercentDiario,
                                             listIncreasePercentSemanal,
                                             listIncreasePercentMensal,
                                             listDividendos );
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
}
