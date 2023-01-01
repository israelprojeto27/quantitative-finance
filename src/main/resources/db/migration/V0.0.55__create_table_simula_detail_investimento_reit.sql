create table simula_detail_investimento_reit(
    id bigint auto_increment not null,
    sigla varchar(20) not null,
    valor_investido decimal(24,12),
    porcentagem_valor_investido decimal(24,12),
    ultima_cotacao_acao decimal(24,12),
    data_ultima_cotacao_reit datetime not null,
    quantidade_cotas_reit decimal(24,12)  not null,
    created_at timestamp,
    updated_at timestamp,
	constraint pk_simula_detail_investimento_reit primary key(id)
)