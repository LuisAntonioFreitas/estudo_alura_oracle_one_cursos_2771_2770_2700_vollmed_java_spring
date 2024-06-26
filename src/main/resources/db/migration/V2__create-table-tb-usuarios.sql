create table tb_usuarios(

    id bigint not null unique auto_increment,

    nome varchar(255) not null unique,
    login varchar(100) not null unique,
    email varchar(255) not null unique,
    senha varchar(255) not null,

    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,

    primary key(id),

    key `idx_login` (`login`),
    key `idx_email` (`email`)
);