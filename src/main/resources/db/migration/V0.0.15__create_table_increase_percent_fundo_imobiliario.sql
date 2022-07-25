create table increase_percent_fundo_imobiliario(
    id bigint auto_increment not null,
    data_base datetime not null,
    data_reference datetime not null,
    percentual decimal(24,12),
	valor_fechamento_atual decimal(24,12),
	valor_fechamento_anterior decimal(24,12),
	periodo varchar(20) not null,
	intervalo_periodo int not null,
	fundo_id bigint,
    created_at timestamp,
    updated_at timestamp,
    constraint fk_increase_percent_fundo_imobiliario FOREIGN KEY (fundo_id) REFERENCES fundo_imobiliario(id),
	constraint pk_increase_percent_fundo_imobiliario primary key(id)
)

