package com.app.api.acao.dividendo.entity;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.principal.entity.Acao;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Builder
@Data
@Entity
@Table(name = "dividendo_acao")
public class DividendoAcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private Double dividend;

    @ManyToOne
    @JoinColumn(name = "acao_id")
    private Acao acao;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public DividendoAcao() {
    }

    public DividendoAcao(Long id, LocalDate data, Double dividend, Acao acao, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.data = data;
        this.dividend = dividend;
        this.acao = acao;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DividendoAcao toEntity(CotacaoAcaoDiario entity) {
        return DividendoAcao.builder()
                .data(entity.getData())
                .dividend(entity.getDividend())
                .acao(entity.getAcao())
                .build();
    }
}
