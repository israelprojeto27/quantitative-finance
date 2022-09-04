package com.app.api.fundoimobiliario.analise.entities;

import com.app.api.acao.analise.entities.AcaoAnalise;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@Table(name = "fundo_analise")
public class FundoImobiliarioAnalise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "fundo_id")
    private FundoImobiliario fundo;

    public FundoImobiliarioAnalise() {
    }

    public FundoImobiliarioAnalise(Long id, Timestamp createdAt, Timestamp updatedAt, FundoImobiliario fundo) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fundo = fundo;
    }


    public static FundoImobiliarioAnalise toEntity(FundoImobiliario fundo) {
        return FundoImobiliarioAnalise.builder()
                .fundo(fundo)
                .build();
    }
}
