-- src/main/resources/data.sql

-- Roles
INSERT INTO role (id, nome) VALUES (1, 'ROLE_ADMIN');
INSERT INTO role (id, nome) VALUES (2, 'ROLE_USER');

-- Usuarios
-- Senha para 'admin' é 'admin'
INSERT INTO usuario (id, usuario, senha) VALUES (1, 'admin', '$2a$10$kaSwNhaEX4SJU6x3pBAX4.keKmtjLbpoW1j5NOrmIaOLJLBXScctW');
-- Senha para 'user' é '123'
INSERT INTO usuario (id, usuario, senha) VALUES (2, 'user', '$2a$10$QCiMFmydE3GsNgJeSHIY6./IUssQwNdVuJJ2rMVHqO/De0G6boBdq');

-- Ligação Usuario <-> Role
INSERT INTO usuario_role (usuario_id, role_id) VALUES (1, 1); -- admin -> ROLE_ADMIN
INSERT INTO usuario_role (usuario_id, role_id) VALUES (1, 2); -- admin -> ROLE_USER
INSERT INTO usuario_role (usuario_id, role_id) VALUES (2, 2); -- user -> ROLE_USER

-- Pessoas (associando a usuários existentes)
-- Medico admin
INSERT INTO pessoa (id, nome, usuario_id) VALUES (3, 'Dra. Ana', 1);
INSERT INTO medico (id, crm) VALUES (3, '12345');

-- Paciente user
INSERT INTO pessoa (id, nome, usuario_id) VALUES (1, 'João Silva', 2);
INSERT INTO paciente (id, telefone) VALUES (1, '99999-1111');

-- Dados existentes sem associação direta de usuário (ou associar se necessário)
INSERT INTO pessoa (id, nome) VALUES (2, 'Maria Souza');
INSERT INTO paciente (id, telefone) VALUES (2, '88888-2222');
INSERT INTO pessoa (id, nome) VALUES (4, 'Dr. Carlos');
INSERT INTO medico (id, crm) VALUES (4, '67890');

-- Endereços (agora com cidade e estado como texto)
INSERT INTO endereco (id, rua, numero, bairro, cep, cidade, estado) VALUES (1, 'Av. JK', '100', 'Centro', '77000-000', 'Palmas', 'TO');
INSERT INTO endereco (id, rua, numero, bairro, cep, cidade, estado) VALUES (2, 'Rua das Flores', '25', 'Jardim Aureny IV', '77000-001', 'Palmas', 'TO');

-- Associe os endereços aos pacientes existentes
UPDATE paciente SET endereco_id = 1 WHERE id = 1;
UPDATE paciente SET endereco_id = 2 WHERE id = 2;


-- Agendas
INSERT INTO agenda (id, medico_id, data_hora, status) VALUES (1, 3, '2025-09-10T09:00:00', 'AGENDADO');
INSERT INTO agenda (id, medico_id, data_hora, status) VALUES (2, 3, '2025-09-10T10:00:00', 'DISPONIVEL');
INSERT INTO agenda (id, medico_id, data_hora, status) VALUES (3, 4, '2025-09-11T14:00:00', 'AGENDADO');
INSERT INTO agenda (id, medico_id, data_hora, status) VALUES (4, 4, '2025-09-11T15:00:00', 'DISPONIVEL');

-- Consultas
INSERT INTO consulta (id, valor, observacao, paciente_id, medico_id, agenda_id) VALUES (1, 200.0, 'Primeira consulta', 1, 3, 1);
INSERT INTO consulta (id, valor, observacao, paciente_id, medico_id, agenda_id) VALUES (2, 250.0, 'Retorno', 2, 4, 3);

-- Reinicia a contagem dos IDs
ALTER TABLE pessoa ALTER COLUMN id RESTART WITH 5;
ALTER TABLE agenda ALTER COLUMN id RESTART WITH 5;
ALTER TABLE consulta ALTER COLUMN id RESTART WITH 3;
ALTER TABLE usuario ALTER COLUMN id RESTART WITH 3;
ALTER TABLE role ALTER COLUMN id RESTART WITH 3;
ALTER TABLE endereco ALTER COLUMN id RESTART WITH 3;