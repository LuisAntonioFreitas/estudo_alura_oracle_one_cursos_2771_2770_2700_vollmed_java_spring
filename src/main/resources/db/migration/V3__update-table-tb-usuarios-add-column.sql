alter table tb_usuarios add column ativo tinyint NOT NULL DEFAULT 1;
update tb_usuarios set ativo = 1;