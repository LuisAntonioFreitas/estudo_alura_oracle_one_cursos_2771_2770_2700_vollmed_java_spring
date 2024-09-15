create table mv_consultas(

    -- primary key
    id bigint not null unique auto_increment,

    medico_id bigint not null,
    paciente_id bigint not null,

    data datetime not null,

    motivo_cancelamento varchar(100),

    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,

    -- index | primary key
    primary key(id),

    -- index
    key idx_medico_id (medico_id),
    key idx_paciente_id (paciente_id),

    -- foreign key
    constraint fk_mv_consultas_medico_id foreign key (medico_id) references tb_medicos (id),
    constraint fk_mv_consultas_paciente_id foreign key (paciente_id) references tb_pacientes (id)
);
