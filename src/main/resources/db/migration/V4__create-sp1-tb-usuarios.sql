-- MySQL
CREATE PROCEDURE `tb_usuarios_listar`()
BEGIN
    START TRANSACTION;

	SELECT 	id, nome, login, email,
			ativo, created_at, updated_at
	FROM    tb_usuarios
	ORDER BY nome ASC;

    COMMIT;
END;