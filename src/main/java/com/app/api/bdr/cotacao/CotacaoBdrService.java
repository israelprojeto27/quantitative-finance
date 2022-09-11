package com.app.api.bdr.cotacao;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.bdr.cotacao.dto.BdrCotacaoDTO;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.cotacao.entities.CotacaoBdrSemanal;
import com.app.api.bdr.cotacao.repositories.CotacaoBdrDiarioRepository;
import com.app.api.bdr.cotacao.repositories.CotacaoBdrMensalRepository;
import com.app.api.bdr.cotacao.repositories.CotacaoBdrSemanalRepository;
import com.app.api.bdr.dividendo.DividendoBdrService;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.increasepercent.IncreasePercentBdr;
import com.app.api.bdr.increasepercent.IncreasePercentBdrService;
import com.app.api.bdr.principal.BdrRepository;
import com.app.api.bdr.principal.entity.Bdr;
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
public class CotacaoBdrService implements BaseCotacaoService<Bdr, BdrCotacaoDTO, CotacaoBdrDiario, CotacaoBdrSemanal, CotacaoBdrMensal> {


    @Autowired
    CotacaoBdrDiarioRepository cotacaoBdrDiarioRepository;

    @Autowired
    CotacaoBdrSemanalRepository cotacaoBdrSemanalRepository;

    @Autowired
    CotacaoBdrMensalRepository cotacaoBdrMensalRepository;

    @Autowired
    BdrRepository bdrRepository;

    @Autowired
    DividendoBdrService dividendoBdrService;

    @Autowired
    IncreasePercentBdrService increasePercentBdrService;


    @Transactional
    @Override
    public void addCotacaoAtivo(String line, Bdr bdr, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoBdrDiario cotacaoBdrDiario = CotacaoBdrDiario.toEntity(array, bdr);
            if ( cotacaoBdrDiario != null)
                this.createCotacaoDiario(cotacaoBdrDiario);

            if (cotacaoBdrDiario.getDividend().doubleValue() > 0.0d){
                DividendoBdr dividendoBdr = DividendoBdr.toEntity(cotacaoBdrDiario);
                dividendoBdrService.save(dividendoBdr);
            }

        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoBdrSemanal cotacaoBdrSemanal = CotacaoBdrSemanal.toEntity(array, bdr);
            if ( cotacaoBdrSemanal != null)
                this.createCotacaoSemanal(cotacaoBdrSemanal);
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoBdrMensal cotacaoBdrMensal = CotacaoBdrMensal.toEntity(array, bdr);
            if ( cotacaoBdrMensal != null)
                this.createCotacaoMensal(cotacaoBdrMensal);
        }
    }

    @Transactional
    @Override
    public void addCotacaoAtivoPartial(String line, Bdr bdr, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoBdrDiario cotacaoBdrDiario = CotacaoBdrDiario.toEntity(array, bdr);
            List<CotacaoBdrDiario> listCotacao = cotacaoBdrDiarioRepository.findByBdrAndData(bdr, cotacaoBdrDiario.getData());
            if ( listCotacao.isEmpty() && cotacaoBdrDiario != null){
                this.createCotacaoDiario(cotacaoBdrDiario);

                if (cotacaoBdrDiario.getDividend().doubleValue() > 0.0d){
                    DividendoBdr dividendoBdr = DividendoBdr.toEntity(cotacaoBdrDiario);
                    dividendoBdrService.save(dividendoBdr);
                }
            }
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoBdrSemanal cotacaoBdrSemanal = CotacaoBdrSemanal.toEntity(array, bdr);
            List<CotacaoBdrSemanal> listCotacao = cotacaoBdrSemanalRepository.findByBdrAndData(bdr, cotacaoBdrSemanal.getData());
            if (listCotacao.isEmpty() && cotacaoBdrSemanal != null){
                this.createCotacaoSemanal(cotacaoBdrSemanal);
            }
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoBdrMensal cotacaoBdrMensal = CotacaoBdrMensal.toEntity(array, bdr);
            List<CotacaoBdrMensal> listCotacao = cotacaoBdrMensalRepository.findByBdrAndData(bdr, cotacaoBdrMensal.getData());
            if ( listCotacao.isEmpty() && cotacaoBdrMensal != null){
                this.createCotacaoMensal(cotacaoBdrMensal);
            }
        }
    }


    @Override
    @Transactional
    public boolean createCotacaoDiario(CotacaoBdrDiario cotacaoBdrDiario) {
        cotacaoBdrDiarioRepository.save(cotacaoBdrDiario);
        return true;
    }

    @Override
    @Transactional
    public boolean createCotacaoSemanal(CotacaoBdrSemanal cotacaoBdrSemanal) {
        cotacaoBdrSemanalRepository.save(cotacaoBdrSemanal);
        return true;
    }

    @Override
    @Transactional
    public boolean createCotacaoMensal(CotacaoBdrMensal cotacaoBdrMensal) {
        cotacaoBdrMensalRepository.save(cotacaoBdrMensal);
        return true;
    }

    @Override
    public List<CotacaoBdrDiario> findCotacaoDiarioByAtivo(Bdr bdr) {
        return cotacaoBdrDiarioRepository.findByBdr(bdr);
    }

    @Override
    public List<CotacaoBdrDiario> findCotacaoDiarioByAtivo(Bdr bdr, Sort sort) {
        return cotacaoBdrDiarioRepository.findByBdr(bdr, sort);
    }

    @Override
    public List<CotacaoBdrSemanal> findCotacaoSemanalByAtivo(Bdr bdr) {
        return cotacaoBdrSemanalRepository.findByBdr(bdr);
    }

    @Override
    public List<CotacaoBdrSemanal> findCotacaoSemanalByAtivo(Bdr bdr, Sort sort) {
        return cotacaoBdrSemanalRepository.findByBdr(bdr, sort);
    }

    @Override
    public List<CotacaoBdrMensal> findCotacaoMensalByAtivo(Bdr bdr) {
        return cotacaoBdrMensalRepository.findByBdr(bdr);
    }

    @Override
    public List<CotacaoBdrMensal> findCotacaoMensalByAtivo(Bdr bdr, Sort sort) {
        return cotacaoBdrMensalRepository.findByBdr(bdr, sort);
    }

    public CotacaoBdrMensal findCotacaoMensalByAtivo(Bdr bdr, LocalDate periodo) { // Este campo periodo deve considerar apenas Ano e Mes

        List<CotacaoBdrMensal> list = this.findCotacaoMensalByAtivo(bdr);
        if ( !list.isEmpty()){
            Optional<CotacaoBdrMensal> optCotacaoMensal = list.stream()
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
        cotacaoBdrDiarioRepository.deleteAll();
        cotacaoBdrSemanalRepository.deleteAll();
        cotacaoBdrMensalRepository.deleteAll();
    }

    @Override
    @Transactional
    public void cleanByPeriodo(String periodo) {
        if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoBdrDiarioRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoBdrSemanalRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoBdrMensalRepository.deleteAll();
        }
    }

    @Override
    public BdrCotacaoDTO findCotacaoByIdAtivo(Long id) {
        Optional<Bdr> bdrOpt = bdrRepository.findById(id);
        if ( bdrOpt.isPresent()){
            List<CotacaoBdrDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(bdrOpt.get());
            List<CotacaoBdrSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(bdrOpt.get());
            List<CotacaoBdrMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(bdrOpt.get());
            return BdrCotacaoDTO.fromEntity(bdrOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }
        return null;
    }

    @Override
    public BdrCotacaoDTO findCotacaoByIdAtivoByPeriodo(Long id, String periodo) {
        Optional<Bdr> bdrOpt = bdrRepository.findById(id);
        if ( bdrOpt.isPresent()){
            List<CotacaoBdrDiario> listCotacaoDiario = null;
            List<CotacaoBdrSemanal> listCotacaoSemanal = null;
            List<CotacaoBdrMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(bdrOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(bdrOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(bdrOpt.get());
            }
            return BdrCotacaoDTO.fromEntity(bdrOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public BdrCotacaoDTO findCotacaoBySiglaFull(String sigla) {
        Optional<Bdr> bdrOpt = bdrRepository.findBySigla(sigla);
        if ( bdrOpt.isPresent()){
            List<CotacaoBdrDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(bdrOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoBdrSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(bdrOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoBdrMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(bdrOpt.get(), Sort.by(Sort.Direction.DESC, "data"));

            List<IncreasePercentBdr> listIncreasePercentDiario = increasePercentBdrService.findIncreasePercentByBdrByPeriodo(bdrOpt.get(), PeriodoEnum.DIARIO);
            List<IncreasePercentBdr> listIncreasePercentSemanal = increasePercentBdrService.findIncreasePercentByBdrByPeriodo(bdrOpt.get(), PeriodoEnum.SEMANAL);
            List<IncreasePercentBdr> listIncreasePercentMensal = increasePercentBdrService.findIncreasePercentByBdrByPeriodo(bdrOpt.get(), PeriodoEnum.MENSAL);

            List<DividendoBdr> listDividendos = dividendoBdrService.findDividendoByBdr(bdrOpt.get());
            List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao = this.getRoiDividendoCotacao(listDividendos, listCotacaoMensal);
            return BdrCotacaoDTO.fromEntity(bdrOpt.get(),
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

    private List<RoiDividendoCotacaoDTO> getRoiDividendoCotacao(List<DividendoBdr> listDividendos, List<CotacaoBdrMensal> listCotacaoMensal) {
        List<RoiDividendoCotacaoDTO> list = new ArrayList<>();

        if ( !listDividendos.isEmpty() && !listCotacaoMensal.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoBdrMensal cotacaoBdrMensal = this.getCotacaoMensalRoiDividendo(dividendo, listCotacaoMensal);
                if ( cotacaoBdrMensal != null){
                    Double roiDividendoCotacao = dividendo.getDividend() / cotacaoBdrMensal.getClose();
                    RoiDividendoCotacaoDTO dto = RoiDividendoCotacaoDTO.from(roiDividendoCotacao, dividendo, cotacaoBdrMensal);
                    list.add(dto);
                }
            });
        }
        return list;
    }

    private CotacaoBdrMensal getCotacaoMensalRoiDividendo(DividendoBdr dividendo, List<CotacaoBdrMensal> listCotacaoMensal) {

        Optional<CotacaoBdrMensal> optCotacaoBdrMensal = listCotacaoMensal.stream()
                .filter(cotacaoBdrMensal -> cotacaoBdrMensal.getData().getYear() == dividendo.getData().getYear() && cotacaoBdrMensal.getData().getMonthValue() == dividendo.getData().getMonthValue())
                .findFirst();

        if ( optCotacaoBdrMensal.isPresent()){
            return optCotacaoBdrMensal.get();
        }

        return null;
    }

    @Override
    public BdrCotacaoDTO findCotacaoBySiglaByPeriodo(String sigla, String periodo) {
        Optional<Bdr> bdrOpt = bdrRepository.findBySigla(sigla);

        if ( bdrOpt.isPresent()){
            List<CotacaoBdrDiario> listCotacaoDiario = null;
            List<CotacaoBdrSemanal> listCotacaoSemanal = null;
            List<CotacaoBdrMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(bdrOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(bdrOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(bdrOpt.get());
            }
            return BdrCotacaoDTO.fromEntity(bdrOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowDiary(FilterAtivoCotacaoGrowDTO dto) {
        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoBdrDiario> listCotacaoInicio = cotacaoBdrDiarioRepository.findByData(dtStart);
        List<CotacaoBdrDiario> listCotacaoFim = cotacaoBdrDiarioRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoBdrDiario> cotacaoBdrDiarioFimOpt = this.getCotacaoDiarioFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoBdrDiarioFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoBdrDiarioFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getBdr().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoBdrDiarioFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoBdrDiarioFimOpt.get().getData()));
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

        List<CotacaoBdrSemanal> listCotacaoInicio = cotacaoBdrSemanalRepository.findByData(dtStart);
        List<CotacaoBdrSemanal> listCotacaoFim = cotacaoBdrSemanalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoBdrSemanal> cotacaoBdrSemanalFimOpt = this.getCotacaoSemanalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoBdrSemanalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoBdrSemanalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getBdr().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoBdrSemanalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoBdrSemanalFimOpt.get().getData()));
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

        List<CotacaoBdrMensal> listCotacaoInicio = cotacaoBdrMensalRepository.findByData(dtStart);
        List<CotacaoBdrMensal> listCotacaoFim = cotacaoBdrMensalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoBdrMensal> cotacaoBdrMensalFimOpt = this.getCotacaoMensalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoBdrMensalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoBdrMensalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getBdr().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoBdrMensalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoBdrMensalFimOpt.get().getData()));
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
    public LastCotacaoAtivoDiarioDTO getLastCotacaoDiario(Bdr bdr) {
        List<CotacaoBdrDiario> listCotacaoBdrDiario = cotacaoBdrDiarioRepository.findByBdr(bdr, Sort.by(Sort.Direction.DESC, "data"));
        if (! listCotacaoBdrDiario.isEmpty()){
            Optional<CotacaoBdrDiario> optCotacaoBdrDiario = listCotacaoBdrDiario.stream()
                    .findFirst();
            if ( optCotacaoBdrDiario.isPresent()){
                return LastCotacaoAtivoDiarioDTO.from(optCotacaoBdrDiario.get());
            }
        }
        return null;
    }

    @Override
    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {
        List<Bdr> listBdr = bdrRepository.findAll();
        if ( !listBdr.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listBdr.forEach(bdr ->{
                List<CotacaoBdrDiario>  listCotacaoDiario  = cotacaoBdrDiarioRepository.findByBdr(bdr);
                List<CotacaoBdrSemanal> listCotacaoSemanal = cotacaoBdrSemanalRepository.findByBdr(bdr);
                List<CotacaoBdrMensal> listCotacaoMensal   = cotacaoBdrMensalRepository.findByBdr(bdr);

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, bdr);
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, bdr);
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, bdr);

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
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoBdrDiario> listCotacaoDiario, Bdr bdr) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoDiario.isEmpty()){
            List<CotacaoBdrDiario>  list = listCotacaoDiario.stream()
                    .sorted(Comparator.comparingDouble(CotacaoBdrDiario::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoBdrDiario cotacaoAtual = list.get(0);
                    CotacaoBdrDiario cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoBdrDiario cotacaoAtual = list.get(i);
                            CotacaoBdrDiario cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoBdrSemanal> listCotacaoSemanal, Bdr bdr) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoSemanal.isEmpty()){
            List<CotacaoBdrSemanal>  list = listCotacaoSemanal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoBdrSemanal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoBdrSemanal cotacaoAtual = list.get(0);
                    CotacaoBdrSemanal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoBdrSemanal cotacaoAtual = list.get(i);
                            CotacaoBdrSemanal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoBdrMensal> listCotacaoMensal, Bdr bdr) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoMensal.isEmpty()){
            List<CotacaoBdrMensal>  list = listCotacaoMensal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoBdrMensal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoBdrMensal cotacaoAtual = list.get(0);
                    CotacaoBdrMensal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoBdrMensal cotacaoAtual = list.get(i);
                            CotacaoBdrMensal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    private Optional<CotacaoBdrDiario> getCotacaoDiarioFim(CotacaoBdrDiario cotacao, List<CotacaoBdrDiario> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getBdr().getSigla().equals(cotacao.getBdr().getSigla()))
                .findFirst();
    }

    private Optional<CotacaoBdrSemanal> getCotacaoSemanalFim(CotacaoBdrSemanal cotacao, List<CotacaoBdrSemanal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getBdr().getSigla().equals(cotacao.getBdr().getSigla()))
                .findFirst();
    }

    private Optional<CotacaoBdrMensal> getCotacaoMensalFim(CotacaoBdrMensal cotacao, List<CotacaoBdrMensal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getBdr().getSigla().equals(cotacao.getBdr().getSigla()))
                .findFirst();
    }
}
