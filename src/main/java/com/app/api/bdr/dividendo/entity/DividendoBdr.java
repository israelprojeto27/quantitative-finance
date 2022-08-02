package com.app.api.bdr.dividendo.entity;

import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.principal.entity.Bdr;
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
@Table(name = "dividendo_Bdr")
public class DividendoBdr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private Double dividend;

    @ManyToOne
    @JoinColumn(name = "bdr_id")
    private Bdr bdr;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public DividendoBdr() {
    }

    public DividendoBdr(Long id, LocalDate data, Double dividend, Bdr bdr, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.data = data;
        this.dividend = dividend;
        this.bdr = bdr;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DividendoBdr toEntity(CotacaoBdrDiario entity) {
        return DividendoBdr.builder()
                .data(entity.getData())
                .dividend(entity.getDividend())
                .bdr(entity.getBdr())
                .build();
    }

    public static DividendoBdr toEntity(Bdr bdr, LocalDate dataDividendo, Double dividendo) {
        return DividendoBdr.builder()
                .data(dataDividendo)
                .dividend(dividendo)
                .bdr(bdr)
                .build();
    }
}
