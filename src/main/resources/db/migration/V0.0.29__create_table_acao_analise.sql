create table acao_analise(
    id bigint auto_increment not null,
    created_at timestamp,
    updated_at timestamp,
    acao_id bigint,
    constraint fk_acao_analise FOREIGN KEY (acao_id) REFERENCES acao(id),
	constraint pk_acao_analise primary key(id)
)

