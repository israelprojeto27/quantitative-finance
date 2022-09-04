package com.app.api.acao.analise.entities;

import com.app.api.acao.principal.entity.Acao;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@Table(name = "acao_analise")
public class AcaoAnalise {

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
    @JoinColumn(name = "acao_id")
    private Acao acao;

    public AcaoAnalise() {
    }

    public AcaoAnalise(Long id, Timestamp createdAt, Timestamp updatedAt, Acao acao) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.acao = acao;
    }

    public static AcaoAnalise toEntity(Acao acao) {
        return AcaoAnalise.builder()
                          .acao(acao)
                          .build();
    }
}
