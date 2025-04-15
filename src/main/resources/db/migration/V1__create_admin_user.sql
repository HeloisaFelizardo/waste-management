-- Verifica se o admin já existe
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM tb_users WHERE email = 'admin@reciclamais.com') THEN
        INSERT INTO tb_users (name, email, password, type_user)
        VALUES (
            'Administrador',
            'admin@reciclamais.com',
            -- Senha: Admin@123 (você deve alterar esta senha após o primeiro acesso)
            '$2a$10$rDmFN6ZJvwFqMz1qkqkqkqkqkqkqkqkqkqkqkqkqkqkqkqkqkqkq',
            'ADMIN'
        );
    END IF;
END $$; 