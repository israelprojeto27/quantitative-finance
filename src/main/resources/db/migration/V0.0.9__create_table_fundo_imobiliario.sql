create table fundo_imobiliario(
    id bigint auto_increment not null,
	sigla varchar(100) not null,
    created_at timestamp,
	constraint pk_fundo_imobiliario primary key(id)
)

