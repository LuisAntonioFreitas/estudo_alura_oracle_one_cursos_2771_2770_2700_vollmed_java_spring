create table tb_pacientes(

    id bigint not null unique auto_increment,

    nome varchar(255) not null unique,
    email varchar(255) not null unique,
    telefone varchar(50) not null,
    cpf varchar(20) not null unique,

    logradouro varchar(100) not null,
    numero varchar(20),
    complemento varchar(100),
    bairro varchar(100) not null,
    cidade varchar(100) not null,
    uf char(2) not null,
    cep varchar(10) not null,

    ativo tinyint not null default 1,

    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,

    primary key(id),

    key `idx_cpf` (`cpf`)
);