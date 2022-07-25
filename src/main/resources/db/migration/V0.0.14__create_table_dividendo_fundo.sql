create table dividendo_fundo(
    id bigint auto_increment not null,
    data datetime not null,
	dividend decimal(24,12),
    created_at timestamp,
    updated_at timestamp,
    fundo_id bigint,
    constraint fk_dividendo_fundo FOREIGN KEY (fundo_id) REFERENCES fundo_imobiliario(id),
	constraint pk_dividendo_fundo primary key(id)
)

