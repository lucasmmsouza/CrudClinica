-- Pacientes
INSERT INTO pessoa (id, nome) VALUES (1, 'João Silva');
INSERT INTO paciente (id, telefone) VALUES (1, '99999-1111');
INSERT INTO pessoa (id, nome) VALUES (2, 'Maria Souza');
INSERT INTO paciente (id, telefone) VALUES (2, '88888-2222');

-- Medicos
INSERT INTO pessoa (id, nome) VALUES (3, 'Dra. Ana');
INSERT INTO medico (id, crm) VALUES (3, '12345');
INSERT INTO pessoa (id, nome) VALUES (4, 'Dr. Carlos');
INSERT INTO medico (id, crm) VALUES (4, '67890');

-- Consultas
INSERT INTO consulta (id, data, valor, observacao, paciente_id, medico_id) VALUES (1, '2025-06-01T10:00:00', 200.0, 'Primeira consulta', 1, 3);
INSERT INTO consulta (id, data, valor, observacao, paciente_id, medico_id) VALUES (2, '2025-06-02T14:00:00', 250.0, 'Retorno', 2, 4);

ALTER TABLE pessoa ALTER COLUMN id RESTART WITH 5;
ALTER TABLE consulta ALTER COLUMN id RESTART WITH 5;