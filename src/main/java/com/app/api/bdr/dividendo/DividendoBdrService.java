package com.app.api.bdr.dividendo;

import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.bdr.principal.BdrRepository;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.cotacao.repositories.CotacaoBdrDiarioRepository;
import com.app.api.bdr.dividendo.dto.BdrListDividendoDTO;
import com.app.api.bdr.dividendo.dto.DividendoBdrDTO;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.commons.basic.dividendo.BaseDividendoService;
import com.app.commons.dtos.*;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DividendoBdrService implements BaseDividendoService<DividendoBdr, DividendoBdrDTO, BdrListDividendoDTO, Bdr> {

    @Autowired
    DividendoBdrRepository repository;

    @Autowired
    BdrRepository bdrRepository;

    @Autowired
    CotacaoBdrDiarioRepository cotacaoBdrDiarioRepository;

    @Transactional
    @Override
    public void save(DividendoBdr dividendoBdr) {
        repository.save(dividendoBdr);
    }

    @Transactional
    @Override
    public void cleanAll() {
        repository.deleteAll();
    }

    @Override
    public List<DividendoBdrDTO> findDividendoByIdAtivo(Long id) {
        Optional<Bdr> bdrOpt = bdrRepository.findById(id);
        if ( bdrOpt.isPresent()){
            List<DividendoBdr> listDividendos = repository.findAllByBdr(bdrOpt.get(), Sort.by(Sort.Direction.DESC, "data") );
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoBdrDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<DividendoBdrDTO> findDividendoBySigla(String sigla) {
        Optional<Bdr> bdrOpt = bdrRepository.findBySigla(sigla);
        if ( bdrOpt.isPresent()){
            List<DividendoBdr> listDividendos = repository.findAllByBdr(bdrOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoBdrDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<BdrListDividendoDTO> findAtivoListDividendos() {
        List<Bdr> listBdr = bdrRepository.findAll();
        if (!listBdr.isEmpty()){
            List<BdrListDividendoDTO> listBdrDividendos = new ArrayList<BdrListDividendoDTO>();
            List<DividendoBdr> listDividendos = new ArrayList<>();
            listBdr.forEach(bdr-> {
                listBdrDividendos.add(BdrListDividendoDTO.fromEntity(bdr, repository.findAllByBdr(bdr, Sort.by(Sort.Direction.DESC, "data"))));
            });
           return listBdrDividendos;
        }
        return null;
    }

    @Override
    public List<BdrListDividendoDTO> filterDividendosByPeriod(FilterPeriodDTO dto) {

        List<BdrListDividendoDTO> listFinal = new ArrayList<>();

        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<DividendoBdr> listDividendos = repository.findByDataBetween(dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            HashMap<String, List<DividendoBdr>> map = new HashMap<>();
            listDividendos.forEach(dividendo ->{
                if ( map.containsKey(dividendo.getBdr().getSigla())){
                    List<DividendoBdr> list = map.get(dividendo.getBdr().getSigla());
                    list.add(dividendo);
                    map.put(dividendo.getBdr().getSigla(), list);
                }
                else {
                    List<DividendoBdr> list = new ArrayList<>();
                    list.add(dividendo);
                    map.put(dividendo.getBdr().getSigla(), list);
                }
            });

            if (! map.isEmpty() ){
                map.keySet().forEach(sigla -> {
                    List<DividendoBdr> list = map.get(sigla);
                    BdrListDividendoDTO bdrListDividendoDTO = BdrListDividendoDTO.from(sigla, list);
                    listFinal.add(bdrListDividendoDTO);
                });
            }

            return listFinal;
        }
        return listFinal;
    }

    @Override
    public List<SumAtivoDividendosDTO> sumDividendosByAtivo() {
        List<Bdr> listBDRs = bdrRepository.findAll();
        if (!listBDRs.isEmpty()){
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();
            listBDRs.forEach(bdr-> {
                List<DividendoBdr> listDividendos = repository.findAllByBdr(bdr);
                double sumDividendos = listDividendos.stream()
                                              .mapToDouble(dividendoBdr -> dividendoBdr.getDividend())
                                              .sum();

                SumAtivoDividendosDTO dto = SumAtivoDividendosDTO.from(bdr, sumDividendos);
                lisSumDividendos.add(dto);
            });
            return lisSumDividendos;
        }
        return null;
    }

    @Override
    public List<SumAtivoDividendosDTO> filterSumDividendosByAtivoByPeriod(FilterPeriodDTO dto) {

        List<Bdr> listBDRs = bdrRepository.findAll();
        if (!listBDRs.isEmpty()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();

            listBDRs.forEach(bdr -> {
                List<DividendoBdr> listDividendos = repository.findByBdrAndDataBetween(bdr, dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));

                double sumDividendos = listDividendos.stream()
                        .mapToDouble(dividendoBdr -> dividendoBdr.getDividend())
                        .sum();

                SumAtivoDividendosDTO sumAtivoDividendosDTO = SumAtivoDividendosDTO.from(bdr, sumDividendos);
                lisSumDividendos.add(sumAtivoDividendosDTO);
            });

            if ( !lisSumDividendos.isEmpty()){
                List<SumAtivoDividendosDTO> listFinal = lisSumDividendos.stream()
                                                                        .sorted(Comparator.comparingDouble(SumAtivoDividendosDTO::getSumDividendo).reversed())
                                                                        .collect(Collectors.toList());
                return listFinal;
            }

            return lisSumDividendos;
        }

        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotas(Long id, Long quantidadeCotas) {

        Optional<Bdr> bdrOpt = bdrRepository.findById(id);
        if (bdrOpt.isPresent()){
            List<DividendoBdr> listDividendos = repository.findAllByBdr(bdrOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoBdrDiario> listCotacaoDiario = cotacaoBdrDiarioRepository.findByBdr(bdrOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoBdr -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoBdr.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasBdr(dividendoBdr.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoBdr.getData(),
                                                                                                                 valorRendimentoDividendo,
                                                                                                                 valorCotacaoAcao,
                                                                                                                 dividendoBdr.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(bdrOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaAtivoByQuantCotas(String sigla, Long quantidadeCotas) {

        Optional<Bdr> bdrOpt = bdrRepository.findBySigla(sigla);
        if (bdrOpt.isPresent()){
            List<DividendoBdr> listDividendos = repository.findAllByBdr(bdrOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoBdrDiario> listCotacaoDiario = cotacaoBdrDiarioRepository.findByBdr(bdrOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoBdr -> {
                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoBdr.getData(),
                                                                                             quantidadeCotas * dividendoBdr.getDividend(),
                                                                                                                 this.getQuantidadeCotasBdr(dividendoBdr.getData(), listCotacaoDiario),
                                                                                                                 dividendoBdr.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(bdrOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    public List<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaAllAtivosByQuantCotas(Long quantidadeCotas) {
        List<Bdr> listBDRs = bdrRepository.findAll();
        if ( !listBDRs.isEmpty()){
            List<SumCalculateYieldDividendosAtivoDTO> listFinal = new ArrayList<>();
            listBDRs.forEach(bdr ->{
                List<DividendoBdr> listDividendos = repository.findAllByBdr(bdr, Sort.by(Sort.Direction.DESC, "data"));
                List<CotacaoBdrDiario> listCotacaoDiario = cotacaoBdrDiarioRepository.findByBdr(bdr);

                if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                    List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                    listDividendos.forEach(dividendoBdr -> {
                        SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoBdr.getData(),
                                quantidadeCotas * dividendoBdr.getDividend(),
                                this.getQuantidadeCotasBdr(dividendoBdr.getData(), listCotacaoDiario),
                                dividendoBdr.getDividend());
                        listCalcultaDetailYieldDividendos.add(dto);
                    });

                    listFinal.add(SumCalculateYieldDividendosAtivoDTO.from(bdr, listCalcultaDetailYieldDividendos));
                }
            });

            return listFinal;
        }

        return null;
    }

    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotasByPeriod(Long id, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<Bdr> bdrOpt = bdrRepository.findById(id);
        if (bdrOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoBdr> listDividendos = repository.findByBdrAndDataBetween(bdrOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoBdrDiario> listCotacaoDiario = cotacaoBdrDiarioRepository.findByBdr(bdrOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoBdr -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoBdr.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasBdr(dividendoBdr.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoBdr.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoAcao,
                            dividendoBdr.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(bdrOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaByQuantCotasByPeriod(String sigla, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<Bdr> bdrOpt = bdrRepository.findBySigla(sigla);
        if (bdrOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoBdr> listDividendos = repository.findByBdrAndDataBetween(bdrOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoBdrDiario> listCotacaoDiario = cotacaoBdrDiarioRepository.findByBdr(bdrOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoBdr -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoBdr.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasBdr(dividendoBdr.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoBdr.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoAcao,
                            dividendoBdr.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(bdrOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    private Double getQuantidadeCotasBdr(LocalDate data, List<CotacaoBdrDiario> listCotacaoDiario) {
        Optional<CotacaoBdrDiario> cotacaoBdrOpt = listCotacaoDiario.stream()
                .filter(cotacaoAcaoDiario -> cotacaoAcaoDiario.getData().equals(data) )
                .findFirst();

        if (cotacaoBdrOpt.isPresent()){
            return cotacaoBdrOpt.get().getClose();
        }
        return null;
    }


    @Transactional
    public boolean addDividendoFundoImobiliario(String line, Bdr bdr) {
        //0,HGLG11,2019-12-01, 0.78

        String[] arr = line.split(",");
        LocalDate dataDividendo = Utils.converteStringToLocalDateTime3(arr[2]);
        Double dividendo = Double.parseDouble(arr[3].replaceAll("G",""));

        DividendoBdr dividendoBdr = DividendoBdr.toEntity(bdr, dataDividendo, dividendo);
        repository.save(dividendoBdr);
        return true;
    }

    @Override
    public LastDividendoAtivoDTO getLastDividendo(Bdr bdr) {
        List<DividendoBdr> listDividendos = repository.findAllByBdr(bdr, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoBdr> optDividendoBdr = listDividendos.stream()
                    .findFirst();
            if ( optDividendoBdr.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoBdr.get());
            }
        }
        return null;
    }

    public List<DividendoBdr> findDividendoByBdr(Bdr bdr) {
        return repository.findAllByBdr(bdr, Sort.by(Sort.Direction.DESC, "data"));
    }

    public List<DividendoBdr> findDividendoBetweenDates(LocalDate dtInicio, LocalDate dtFim) {
        return repository.findByDataBetween(dtInicio, dtFim, Sort.by(Sort.Direction.DESC, "data"));
    }
}
