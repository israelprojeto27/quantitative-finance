package com.app.api.ativos.principal.entities;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.principal.entity.Bdr;
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
@Table(name = "ativo_analise")
public class AtivoAnalise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "acao_id")
    private Acao acao;

    @ManyToOne
    @JoinColumn(name = "bdr_id")
    private Bdr bdr;

    @ManyToOne
    @JoinColumn(name = "fundo_id")
    private FundoImobiliario fundo;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public AtivoAnalise() {
    }

    public AtivoAnalise(Long id, Acao acao, Bdr bdr, FundoImobiliario fundo, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.acao = acao;
        this.bdr = bdr;
        this.fundo = fundo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AtivoAnalise toEntity(Acao acao) {
        return AtivoAnalise.builder()
                .acao(acao)
                .build();
    }

    public static AtivoAnalise toEntity(Bdr bdr) {
        return AtivoAnalise.builder()
                .bdr(bdr)
                .build();
    }

    public static AtivoAnalise toEntity(FundoImobiliario fundoImobiliario) {
        return AtivoAnalise.builder()
                .fundo(fundoImobiliario)
                .build();
    }
}
