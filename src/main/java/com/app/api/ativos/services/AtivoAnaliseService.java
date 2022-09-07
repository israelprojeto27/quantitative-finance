package com.app.api.ativos.services;

import com.app.api.acao.principal.AcaoRepository;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.ativos.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.entities.AtivoAnalise;
import com.app.api.ativos.enums.TipoAtivoEnum;
import com.app.api.ativos.repositories.AtivoAnaliseRepository;
import com.app.api.bdr.principal.BdrRepository;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.enums.OrderFilterEnum;
import com.app.commons.enums.TypeOrderFilterEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtivoAnaliseService {

    @Autowired
    AtivoAnaliseRepository ativoAnaliseRepository;

    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    BdrRepository bdrRepository;

    @Autowired
    FundoImobiliarioRepository fundoImobiliarioRepository;

    @Autowired
    AtivoAcaoService ativoAcaoService;

    @Autowired
    AtivoBdrService ativoBdrService;

    @Autowired
    AtivoFundoImobiliarioService ativoFundoImobiliarioService;


    @Transactional
    public boolean addAnaliseAtivo(String tipoAtivo, String sigla) {
        if (tipoAtivo.equals(TipoAtivoEnum.ACAO.getLabel())){
            Optional<Acao> optAcao = acaoRepository.findBySigla(sigla);
            if (optAcao.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseAcao = ativoAnaliseRepository.findByAcao(optAcao.get());
                if (!optAtivoAnaliseAcao.isPresent()){
                    AtivoAnalise ativoAnalise = AtivoAnalise.toEntity(optAcao.get());
                    ativoAnaliseRepository.save(ativoAnalise);
                }
                return true;
            }
        }
        else if (tipoAtivo.equals(TipoAtivoEnum.BDR.getLabel())){
            Optional<Bdr> optBdr = bdrRepository.findBySigla(sigla);
            if (optBdr.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseBdr = ativoAnaliseRepository.findByBdr(optBdr.get());
                if (!optAtivoAnaliseBdr.isPresent()){
                    AtivoAnalise ativoAnalise = AtivoAnalise.toEntity(optBdr.get());
                    ativoAnaliseRepository.save(ativoAnalise);
                }
                return true;
            }
        }
        else if (tipoAtivo.equals(TipoAtivoEnum.FUNDO_IMOBILIARIO.getLabel())){
            Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(sigla);
            if (optFundo.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseFundo = ativoAnaliseRepository.findByFundo(optFundo.get());
                if (!optAtivoAnaliseFundo.isPresent()){
                    AtivoAnalise ativoAnalise = AtivoAnalise.toEntity(optFundo.get());
                    ativoAnaliseRepository.save(ativoAnalise);
                }
                return true;
            }
        }
        return false;
    }

    public List<InfoGeraisAtivosDTO> findAllAtivosAnalise() {
        List<InfoGeraisAtivosDTO> list = new ArrayList<>();
        List<AtivoAnalise> listAtivosAnalise = ativoAnaliseRepository.findAll();
        if (! listAtivosAnalise.isEmpty()){
            listAtivosAnalise.forEach(ativoAnalise -> {
                if ( ativoAnalise.getAcao() != null ){
                    InfoGeraisAtivosDTO dtoAcao = ativoAcaoService.getInfoGeraisByAcao(ativoAnalise.getAcao());
                    list.add(dtoAcao);
                }
                else if ( ativoAnalise.getBdr() != null ){
                    InfoGeraisAtivosDTO dtoBdr = ativoBdrService.getInfoGeraisByBdr(ativoAnalise.getBdr());
                    list.add(dtoBdr);
                }
                else if ( ativoAnalise.getFundo() != null ){
                    InfoGeraisAtivosDTO dtoFundo = ativoFundoImobiliarioService.getInfoGeraisByFundo(ativoAnalise.getFundo());
                    list.add(dtoFundo);
                }
            });
        }

        return list;
    }

    public List<InfoGeraisAtivosDTO> filterAllAtivosAnalise(String orderFilter, String typeOrderFilter) {

        List<InfoGeraisAtivosDTO> listFinal = new ArrayList<>();
        List<InfoGeraisAtivosDTO> list = this.findAllAtivosAnalise();

        if ( !list.isEmpty()){
            if ( orderFilter.equals(OrderFilterEnum.COEFICIENTE_ROI_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getCoeficienteRoiDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getCoeficienteRoiDividendo).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.QUANTIDADE_OCORRENCIA_DIVIDENDOS.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getQuantidadeOcorrenciasDividendos)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getQuantidadeOcorrenciasDividendos).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_COTACAO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimaCotacao)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimaCotacao).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_COTACAO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimaCotacao)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimaCotacao).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimoDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimoDividendo).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimoDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimoDividendo).reversed()).collect(Collectors.toList());
                }
            }
        }

        return listFinal;
    }

    @Transactional
    public boolean  deleteAnaliseAtivo(String tipoAtivo, String sigla) {
        if (tipoAtivo.equals(TipoAtivoEnum.ACAO.getLabel())){
            Optional<Acao> optAcao = acaoRepository.findBySigla(sigla);
            if (optAcao.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseAcao = ativoAnaliseRepository.findByAcao(optAcao.get());
                if (optAtivoAnaliseAcao.isPresent()){
                    ativoAnaliseRepository.delete(optAtivoAnaliseAcao.get());
                    return true;
                }
            }
        }
        else if (tipoAtivo.equals(TipoAtivoEnum.BDR.getLabel())){
            Optional<Bdr> optBdr = bdrRepository.findBySigla(sigla);
            if (optBdr.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseBdr = ativoAnaliseRepository.findByBdr(optBdr.get());
                if (optAtivoAnaliseBdr.isPresent()){
                    ativoAnaliseRepository.delete(optAtivoAnaliseBdr.get());
                    return true;
                }
            }
        }
        else if (tipoAtivo.equals(TipoAtivoEnum.FUNDO_IMOBILIARIO.getLabel())){
            Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(sigla);
            if (optFundo.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseFundo = ativoAnaliseRepository.findByFundo(optFundo.get());
                if (optAtivoAnaliseFundo.isPresent()){
                    ativoAnaliseRepository.delete(optAtivoAnaliseFundo.get());
                    return true;
                }
            }
        }
        return false;
    }
}
