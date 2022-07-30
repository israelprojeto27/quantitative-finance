package com.app.api.fundoimobiliario.dividendo.entity;

import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
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
@Table(name = "dividendo_fundo")
public class DividendoFundo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private Double dividend;

    @ManyToOne
    @JoinColumn(name = "fundo_id")
    private FundoImobiliario fundo;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public DividendoFundo() {
    }

    public DividendoFundo(Long id, LocalDate data, Double dividend, FundoImobiliario fundo, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.data = data;
        this.dividend = dividend;
        this.fundo = fundo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DividendoFundo toEntity(CotacaoFundoDiario entity) {
        return DividendoFundo.builder()
                .data(entity.getData())
                .dividend(entity.getDividend())
                .fundo(entity.getFundo())
                .build();
    }

    public static DividendoFundo toEntity(FundoImobiliario fundoImobiliario, LocalDate dataDividendo, Double dividendo) {
        return DividendoFundo.builder()
                .data(dataDividendo)
                .dividend(dividendo)
                .fundo(fundoImobiliario)
                .build();
    }
}
