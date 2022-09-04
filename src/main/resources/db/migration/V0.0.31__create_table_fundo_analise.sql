create table fundo_analise(
    id bigint auto_increment not null,
    created_at timestamp,
    updated_at timestamp,
    fundo_id bigint,
    constraint fk_fundo_analise FOREIGN KEY (fundo_id) REFERENCES fundo_imobiliario(id),
	constraint pk_fundo_analise primary key(id)
)

