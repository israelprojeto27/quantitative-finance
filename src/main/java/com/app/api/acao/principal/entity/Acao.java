package com.app.api.acao.principal.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "acao")
public class Acao  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sigla;


    public Acao(String sigla) {
        this.sigla = sigla;

    }

    public Acao(Long id, String sigla) {
        this.id = id;
        this.sigla = sigla;
    }

    public Acao() {
    }


}
