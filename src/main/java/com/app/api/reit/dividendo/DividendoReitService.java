package com.app.api.reit.dividendo;

import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.reit.cotacao.repositories.CotacaoReitDiarioRepository;
import com.app.api.reit.dividendo.dto.DividendoReitDTO;
import com.app.api.reit.dividendo.dto.ReitListDividendoDTO;
import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.reit.principal.ReitRepository;
import com.app.api.reit.principal.entity.Reit;
import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.dividendo.entity.DividendoStock;
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
public class DividendoReitService implements BaseDividendoService<DividendoReit, DividendoReitDTO, ReitListDividendoDTO, Reit> {

    @Autowired
    DividendoReitRepository repository;

    @Autowired
    ReitRepository reitRepository;

    @Autowired
    CotacaoReitDiarioRepository cotacaoReitDiarioRepository;


    @Transactional
    @Override
    public void save(DividendoReit dividendoReit) {
        repository.save(dividendoReit);
    }

    @Transactional
    @Override
    public void cleanAll() {
        repository.deleteAll();
    }

    @Override
    public List<DividendoReitDTO> findDividendoByIdAtivo(Long idReit) {
        Optional<Reit> reitOpt = reitRepository.findById(idReit);
        if ( reitOpt.isPresent()){
            List<DividendoReit> listDividendos = repository.findAllByReit(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data") );
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoReitDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<DividendoReitDTO> findDividendoBySigla(String sigla) {
        Optional<Reit> reitOpt = reitRepository.findBySigla(sigla);
        if ( reitOpt.isPresent()){
            List<DividendoReit> listDividendos = repository.findAllByReit(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data") );
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoReitDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<ReitListDividendoDTO> findAtivoListDividendos() {
        List<Reit> listReits = reitRepository.findAll();
        if (!listReits.isEmpty()){
            List<ReitListDividendoDTO> listReitDividendos = new ArrayList<ReitListDividendoDTO>();
            listReits.forEach(reit-> {
                listReitDividendos.add(ReitListDividendoDTO.fromEntity(reit, repository.findAllByReit(reit, Sort.by(Sort.Direction.DESC, "data"))));
            });
            return listReitDividendos;
        }
        return null;
    }

    @Override
    public List<ReitListDividendoDTO> filterDividendosByPeriod(FilterPeriodDTO dto) {
        List<ReitListDividendoDTO> listFinal = new ArrayList<>();

        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<DividendoReit> listDividendos = repository.findByDataBetween(dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            HashMap<String, List<DividendoReit>> map = new HashMap<>();
            listDividendos.forEach(dividendo ->{
                if ( map.containsKey(dividendo.getReit().getSigla())){
                    List<DividendoReit> list = map.get(dividendo.getReit().getSigla());
                    list.add(dividendo);
                    map.put(dividendo.getReit().getSigla(), list);
                }
                else {
                    List<DividendoReit> list = new ArrayList<>();
                    list.add(dividendo);
                    map.put(dividendo.getReit().getSigla(), list);
                }
            });

            if (! map.isEmpty() ){
                map.keySet().forEach(sigla -> {
                    List<DividendoReit> list = map.get(sigla);
                    ReitListDividendoDTO reitListDividendoDTO = ReitListDividendoDTO.from(sigla, list);
                    listFinal.add(reitListDividendoDTO);
                });
            }

            return listFinal;
        }
        return listFinal;
    }

    @Override
    public List<SumAtivoDividendosDTO> sumDividendosByAtivo() {
        List<Reit> listReits = reitRepository.findAll();
        if (!listReits.isEmpty()){
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();
            listReits.forEach(reit -> {
                List<DividendoReit> listDividendos = repository.findAllByReit(reit);
                double sumDividendos = listDividendos.stream()
                        .mapToDouble(dividendoReit -> dividendoReit.getDividend())
                        .sum();

                SumAtivoDividendosDTO dto = SumAtivoDividendosDTO.from(reit, sumDividendos);
                lisSumDividendos.add(dto);
            });
            return lisSumDividendos;
        }
        return null;
    }

    @Override
    public List<SumAtivoDividendosDTO> filterSumDividendosByAtivoByPeriod(FilterPeriodDTO dto) {
        List<Reit> listReits = reitRepository.findAll();
        if (!listReits.isEmpty()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();

            listReits.forEach(reit -> {
                List<DividendoReit> listDividendos = repository.findByReitAndDataBetween(reit, dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));

                double sumDividendos = listDividendos.stream()
                        .mapToDouble(dividendoReit -> dividendoReit.getDividend())
                        .sum();

                SumAtivoDividendosDTO sumAtivoDividendosDTO = SumAtivoDividendosDTO.from(reit, sumDividendos);
                lisSumDividendos.add(sumAtivoDividendosDTO);
            });
            return lisSumDividendos;
        }

        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotas(Long idReit, Long quantidadeCotas) {
        Optional<Reit> reitOpt = reitRepository.findById(idReit);
        if (reitOpt.isPresent()){
            List<DividendoReit> listDividendos = repository.findAllByReit(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoReitDiario> listCotacaoDiario = cotacaoReitDiarioRepository.findByReit(reitOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoReit -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoReit.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasReit(dividendoReit.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoReit.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoAcao,
                            dividendoReit.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(reitOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaAtivoByQuantCotas(String sigla, Long quantidadeCotas) {
        Optional<Reit> reitOpt = reitRepository.findBySigla(sigla);
        if (reitOpt.isPresent()){
            List<DividendoReit> listDividendos = repository.findAllByReit(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoReitDiario> listCotacaoDiario = cotacaoReitDiarioRepository.findByReit(reitOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoReit -> {
                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoReit.getData(),
                            quantidadeCotas * dividendoReit.getDividend(),
                            this.getQuantidadeCotasReit(dividendoReit.getData(), listCotacaoDiario),
                            dividendoReit.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(reitOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotasByPeriod(Long idReit, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<Reit> reitOpt = reitRepository.findById(idReit);
        if (reitOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoReit> listDividendos = repository.findByReitAndDataBetween(reitOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoReitDiario> listCotacaoDiario = cotacaoReitDiarioRepository.findByReit(reitOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoAcao.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasReit(dividendoAcao.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoAcao,
                            dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(reitOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }

    @Override
    public LastDividendoAtivoDTO getLastDividendo(Reit reit) {
        List<DividendoReit> listDividendos = repository.findAllByReit(reit, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoReit> optDividendoReit = listDividendos.stream()
                    .findFirst();
            if ( optDividendoReit.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoReit.get());
            }
        }
        return null;
    }

    public List<DividendoReit> findDividendoByReit(Reit reit) {
        return repository.findAllByReit(reit, Sort.by(Sort.Direction.DESC, "data"));
    }

    @Override
    public List<DividendoReit> findDividendoBetweenDates(LocalDate dtInicio, LocalDate dtFim) {
        return repository.findByDataBetween(dtInicio, dtFim, Sort.by(Sort.Direction.DESC, "data"));
    }

    @Override
    public ResultSimulaDividendoSiglaDTO simulaRendimentoDividendoBySigla(String sigla, String valorInvestimento) {
        Optional<Reit> reitOpt = reitRepository.findBySigla(sigla);
        if (reitOpt.isPresent()){
            List<CotacaoReitDiario> listCotacaoReitDiario = cotacaoReitDiarioRepository.findByReit(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            Optional<CotacaoReitDiario> optCotacaoReitDiario = listCotacaoReitDiario.stream().findFirst();
            List<DividendoReit> listDividendos = repository.findAllByReit(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            if (! listDividendos.isEmpty() && optCotacaoReitDiario.isPresent()){
                Double valorInvest = Double.parseDouble(valorInvestimento);
                ResultSimulaDividendoSiglaDTO dto = new ResultSimulaDividendoSiglaDTO();
                List<ResultSimulaDividendoSiglaDetailDTO> list = new ArrayList<>();
                List<ResultSimulaDividendoSiglaDetailDTO> listFinal = new ArrayList<>();
                listDividendos.forEach(dividendoReit -> {
                    if (dividendoReit != null && dividendoReit.getDividend() != null ){
                        dto.setTotalGanhoDividendos(dto.getTotalGanhoDividendos() + dividendoReit.getDividend());
                        list.add(ResultSimulaDividendoSiglaDetailDTO.from(valorInvest, dividendoReit, optCotacaoReitDiario.get()));
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

    @Override
    public ResultSimulaDividendoSiglaDTO simulaRendimentoDividendoBySiglaByQuantCotas(String sigla, String quantCotas) {
        Optional<Reit> reitOpt = reitRepository.findBySigla(sigla);
        if (reitOpt.isPresent()){
            List<CotacaoReitDiario> listCotacaoReitDiario = cotacaoReitDiarioRepository.findByReit(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            Optional<CotacaoReitDiario> optCotacaoReitDiario = listCotacaoReitDiario.stream().findFirst();
            List<DividendoReit> listDividendos = repository.findByReit(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            if (! listDividendos.isEmpty() && optCotacaoReitDiario.isPresent()){
                Integer quantidadeCotas = Integer.parseInt(quantCotas);
                ResultSimulaDividendoSiglaDTO dto = new ResultSimulaDividendoSiglaDTO();
                List<ResultSimulaDividendoSiglaDetailDTO> list = new ArrayList<>();
                List<ResultSimulaDividendoSiglaDetailDTO> listFinal = new ArrayList<>();
                listDividendos.forEach(dividendoReit -> {
                    if (dividendoReit != null && dividendoReit.getDividend() != null ){
                        dto.setTotalGanhoDividendos(dto.getTotalGanhoDividendos() + dividendoReit.getDividend());
                        list.add(ResultSimulaDividendoSiglaDetailDTO.from(quantidadeCotas, dividendoReit, optCotacaoReitDiario.get()));
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


    public List<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaAllAtivosByQuantCotas(Long quantidadeCotas) {
        List<Reit> listReits = reitRepository.findAll();
        if ( !listReits.isEmpty()){
            List<SumCalculateYieldDividendosAtivoDTO> listFinal = new ArrayList<>();
            listReits.forEach(reit ->{
                List<DividendoReit> listDividendos = repository.findAllByReit(reit, Sort.by(Sort.Direction.DESC, "data"));
                List<CotacaoReitDiario> listCotacaoDiario = cotacaoReitDiarioRepository.findByReit(reit);

                if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                    List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                    listDividendos.forEach(dividendoReit -> {
                        SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoReit.getData(),
                                quantidadeCotas * dividendoReit.getDividend(),
                                this.getQuantidadeCotasReit(dividendoReit.getData(), listCotacaoDiario),
                                dividendoReit.getDividend());
                        listCalcultaDetailYieldDividendos.add(dto);
                    });

                    listFinal.add(SumCalculateYieldDividendosAtivoDTO.from(reit, listCalcultaDetailYieldDividendos));
                }
            });

            return listFinal;
        }

        return null;
    }

    private Double getQuantidadeCotasReit(LocalDate data, List<CotacaoReitDiario> listCotacaoDiario) {
        Optional<CotacaoReitDiario> cotacaoReitOpt = listCotacaoDiario.stream()
                .filter(cotacaoAcaoDiario -> cotacaoAcaoDiario.getData().equals(data) )
                .findFirst();
        if (cotacaoReitOpt.isPresent()){
            return cotacaoReitOpt.get().getClose();
        }
        return null;
    }


    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaByQuantCotasByPeriod(String sigla, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<Reit> reitOpt = reitRepository.findBySigla(sigla);
        if (reitOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoReit> listDividendos = repository.findByReitAndDataBetween(reitOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoReitDiario> listCotacaoDiario = cotacaoReitDiarioRepository.findByReit(reitOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoReit -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoReit.getDividend();
                    Double valorCotacaoReit = this.getQuantidadeCotasReit(dividendoReit.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoReit.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoReit,
                            dividendoReit.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(reitOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }
}
