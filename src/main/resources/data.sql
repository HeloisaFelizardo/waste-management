-- Inserir um usuário administrador (senha: admin123)
INSERT INTO tb_users (name, email, password, type_user)
VALUES ('Administrador', 'admin@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'ADMIN');