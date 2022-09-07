create table ativo_analise(
    id bigint auto_increment not null,
    created_at timestamp,
    updated_at timestamp,
    fundo_id bigint null,
    acao_id bigint null,
    bdr_id bigint null,
	constraint pk_ativo_analise primary key(id)
)

