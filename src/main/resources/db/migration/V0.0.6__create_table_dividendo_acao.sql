create table dividendo_acao(
    id bigint auto_increment not null,
    data datetime not null,
	dividend decimal(24,12),
    created_at timestamp,
    updated_at timestamp,
    acao_id bigint,
    constraint fk_dividendo_acao FOREIGN KEY (acao_id) REFERENCES acao(id),
	constraint pk_dividendo_acao primary key(id)
)

