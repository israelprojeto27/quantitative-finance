create table increase_percent(
    id bigint auto_increment not null,
    data_base datetime not null,
    data_reference datetime not null,
    percentual decimal(24,12),
	valor_fechamento_atual decimal(24,12),
	valor_fechamento_anterior decimal(24,12),
	periodo varchar(20) not null,
	intervalo_periodo int not null,
	acao_id bigint,
    created_at timestamp,
    updated_at timestamp,
    constraint fk_increase_percent FOREIGN KEY (acao_id) REFERENCES acao(id),
	constraint pk_increase_percent primary key(id)
)

