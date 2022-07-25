package com.app.api.parametro;

import com.app.api.parametro.dto.CreateParametroDTO;
import com.app.api.parametro.dto.ParametroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parametro")
public class ParametroController {

    @Autowired
    ParametroService service;


    @GetMapping
    public ResponseEntity<List<ParametroDTO>> getListAll() {
        return new ResponseEntity<List<ParametroDTO>>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParametroDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<ParametroDTO>(service.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParametroDTO> create(@RequestBody CreateParametroDTO dto) {
        return new ResponseEntity<ParametroDTO>(service.create(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        ParametroDTO dto = service.findById(id);
        if ( dto != null ) {
            if (service.deleteById(id))
                return new ResponseEntity<>(true, HttpStatus.OK);
            else
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PatchMapping
    public ResponseEntity<ParametroDTO> update(@RequestBody ParametroDTO dto) {
        return new ResponseEntity<ParametroDTO>(service.update(dto), HttpStatus.OK);
    }
}
