package com.app.api.bdr.analise.entities;

import com.app.api.acao.analise.entities.AcaoAnalise;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.principal.entity.Bdr;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@Table(name = "bdr_analise")
public class BdrAnalise {

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
    @JoinColumn(name = "bdr_id")
    private Bdr bdr;

    public BdrAnalise() {
    }

    public BdrAnalise(Long id, Timestamp createdAt, Timestamp updatedAt, Bdr bdr) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.bdr = bdr;
    }

    public static BdrAnalise toEntity(Bdr bdr) {
        return BdrAnalise.builder()
                .bdr(bdr)
                .build();
    }
}
