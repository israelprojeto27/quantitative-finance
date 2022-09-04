create table bdr_analise(
    id bigint auto_increment not null,
    created_at timestamp,
    updated_at timestamp,
    bdr_id bigint,
    constraint fk_bdr_analise FOREIGN KEY (bdr_id) REFERENCES bdr(id),
	constraint pk_bdr_analise primary key(id)
)

