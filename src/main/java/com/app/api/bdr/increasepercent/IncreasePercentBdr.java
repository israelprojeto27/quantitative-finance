package com.app.api.bdr.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.bdr.principal.entity.Bdr;
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
@Table(name = "increase_percent_bdr")
public class IncreasePercentBdr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_base")
    private LocalDate dataBase;

    @Column(name = "data_reference")
    private LocalDate dataReference;

    @Column(name = "periodo")
    private PeriodoEnum periodo;

    @Column(name = "intervalo_periodo")
    private Integer intervaloPeriodo; // Se periodo = 'diario' e intervalo_periodo = 5 entao significa que percentual calculado eh referente aos 5 ultimos dias

    private Double percentual;

    @Column(name = "valor_fechamento_atual")
    private Double valorFechamentoAtual;


    @Column(name = "valor_fechamento_anterior")
    private Double valorFechamentoAnterior;


    @ManyToOne
    @JoinColumn(name = "bdr_id")
    private Bdr bdr;


    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public IncreasePercentBdr() {
    }

    public IncreasePercentBdr(Long id, LocalDate dataBase, LocalDate dataReference, PeriodoEnum periodo, Integer intervaloPeriodo, Double percentual, Double valorFechamentoAtual, Double valorFechamentoAnterior, Bdr bdr, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.dataBase = dataBase;
        this.dataReference = dataReference;
        this.periodo = periodo;
        this.intervaloPeriodo = intervaloPeriodo;
        this.percentual = percentual;
        this.valorFechamentoAtual = valorFechamentoAtual;
        this.valorFechamentoAnterior = valorFechamentoAnterior;
        this.bdr = bdr;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
