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

-- Agendas (Horários disponíveis)
INSERT INTO agenda (id, medico_id, data_hora, status) VALUES (1, 3, '2025-09-10T09:00:00', 'AGENDADO');
INSERT INTO agenda (id, medico_id, data_hora, status) VALUES (2, 3, '2025-09-10T10:00:00', 'DISPONIVEL');
INSERT INTO agenda (id, medico_id, data_hora, status) VALUES (3, 4, '2025-09-11T14:00:00', 'AGENDADO');
INSERT INTO agenda (id, medico_id, data_hora, status) VALUES (4, 4, '2025-09-11T15:00:00', 'DISPONIVEL');

-- Consultas (agora vinculadas à agenda)
INSERT INTO consulta (id, valor, observacao, paciente_id, medico_id, agenda_id) VALUES (1, 200.0, 'Primeira consulta', 1, 3, 1);
INSERT INTO consulta (id, valor, observacao, paciente_id, medico_id, agenda_id) VALUES (2, 250.0, 'Retorno', 2, 4, 3);


-- Reinicia a contagem dos IDs para evitar conflitos
ALTER TABLE pessoa ALTER COLUMN id RESTART WITH 5;
ALTER TABLE agenda ALTER COLUMN id RESTART WITH 5;
ALTER TABLE consulta ALTER COLUMN id RESTART WITH 3;