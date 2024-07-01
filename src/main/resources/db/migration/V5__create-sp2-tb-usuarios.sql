-- MySQL
CREATE PROCEDURE `tb_usuarios_param_listar`(
	IN _nome            mediumtext,
	IN _email           mediumtext
)
BEGIN
    DECLARE _sp_nome    mediumtext;
    DECLARE _sp_email   mediumtext;

    SET _sp_nome        = IFNULL(_nome, NULL);
    SET _sp_email       = IFNULL(_email, NULL);

    START TRANSACTION;

	SELECT 	id, nome, login, email,
			ativo, created_at, updated_at
    FROM    tb_usuarios
    WHERE   ( IFNULL(nome,'') LIKE CONCAT('%', _sp_nome, '%') OR _sp_nome IS NULL )
    AND     ( IFNULL(email, '') LIKE CONCAT('%', _sp_email, '%') OR _sp_email IS NULL )
    ORDER BY nome ASC;

    COMMIT;
END;