create table reit_analise(
    id bigint auto_increment not null,
    created_at timestamp,
    updated_at timestamp,
    reit_id bigint,
    constraint fk_reit_analise FOREIGN KEY (reit_id) REFERENCES reit(id),
	constraint pk_reit_analise primary key(id)
)

