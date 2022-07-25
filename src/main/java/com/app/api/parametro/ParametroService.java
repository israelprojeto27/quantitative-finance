package com.app.api.parametro;

import com.app.api.parametro.dto.CreateParametroDTO;
import com.app.api.parametro.dto.ParametroDTO;
import com.app.api.parametro.entity.Parametro;
import com.app.api.parametro.enums.TipoParametroEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParametroService {

    @Autowired
    ParametroRepository repository;


    public List<ParametroDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(Parametro::fromEntity)
                .collect(Collectors.toList());
    }

    public ParametroDTO findById(Long id) {
        Optional<Parametro> parametroOpt = repository.findById(id);
        return parametroOpt.isPresent() ? Parametro.fromEntity(parametroOpt.get()) : null ;
    }


    @Transactional
    public ParametroDTO create(CreateParametroDTO dto) {
        Parametro parametro = repository.save(CreateParametroDTO.toEntity(dto)) ;
        return Parametro.fromEntity(parametro);
    }

    @Transactional
    public boolean deleteById(Long id) {
        try{
            repository.deleteById(id);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Transactional
    public ParametroDTO update(ParametroDTO dto) {
        Optional<Parametro> parametroOpt = repository.findById(dto.getId());
        if ( parametroOpt.isPresent()){
            Parametro  parametro = ParametroDTO.toEntity(dto);
            repository.save(parametro);
            return dto;
        }
        return null;
    }

    public List<ParametroDTO> findByTipoParametro(TipoParametroEnum tipoParametro) {
        return repository.findByTipoParametro(tipoParametro, Sort.by(Sort.Direction.ASC, "valor"))
                .stream()
                .map(Parametro::fromEntity)
                .collect(Collectors.toList());
    }
}
