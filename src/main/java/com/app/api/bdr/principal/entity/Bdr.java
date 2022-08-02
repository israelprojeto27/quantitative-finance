package com.app.api.bdr.principal.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "bdr")
public class Bdr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sigla;

    public Bdr() {
    }

    public Bdr(Long id, String sigla) {
        this.id = id;
        this.sigla = sigla;
    }

    public Bdr(String sigla) {
        this.sigla = sigla;
    }
}
