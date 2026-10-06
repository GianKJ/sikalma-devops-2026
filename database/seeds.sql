-- =========================================================
-- SIKALMA - Datos Semilla de Prueba (seeds.sql)
-- =========================================================

INSERT INTO especialidades (nombre, descripcion, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Psicología Clínica', 'Evaluación, diagnóstico y tratamiento de trastornos psicológicos y emocionales.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Psicología Infantil y Adolescentes', 'Intervención en problemas de conducta, desarrollo socioemocional y crianza.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Terapia de Pareja y Familia', 'Acompañamiento en resolución de conflictos afectivos y vínculos familiares.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Psicología Educativa', 'Orientación vocacional y abordaje de dificultades del aprendizaje.', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO servicios (nombre, descripcion, modalidad, duracion_minutos, publico_objetivo, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Consulta Psicológica Individual', 'Sesión de evaluación y abordaje terapéutico integral para adultos y jóvenes.', 'AMBAS', 50, 'Jóvenes y Adultos', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Terapia de Pareja', 'Sesión especializada para reconstrucción de vínculos y acuerdos afectivos.', 'PRESENCIAL', 60, 'Parejas', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Evaluación Psicométrica Integral', 'Batería de test psicológicos con emisión de informe formal.', 'PRESENCIAL', 90, 'Niños, Jóvenes y Adultos', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO psicologos (tipo_documento, numero_documento, nombres, apellidos, colegiatura, telefono, correo, descripcion_profesional, modalidad, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('DNI', '45892147', 'Mariana', 'Vásquez Benavides', 'C.Ps.P. 28415', '962841520', 'mvasquez@sikalma.pe', 'Psicóloga Clínica con más de 8 años de experiencia en psicoterapia cognitivo conductual.', 'AMBAS', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DNI', '70214896', 'Carlos', 'Rojas Morales', 'C.Ps.P. 31920', '987452103', 'crojas@sikalma.pe', 'Especialista en terapia sistémica familiar y manejo de crisis emocionales.', 'PRESENCIAL', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (numero_documento) DO NOTHING;

INSERT INTO pacientes (tipo_documento, numero_documento, nombres, apellidos, fecha_nacimiento, telefono, correo, direccion, contacto_emergencia, apoderado, observaciones_administrativas, consentimiento_registrado, preferencias_privacidad, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('DNI', '74125896', 'Lucía Andrea', 'Mendoza Carrillo', '1998-05-14', '951478230', 'lucia.mendoza@gmail.com', 'Jr. Dos de Mayo 450, Huánuco', 'Rosa Carrillo - 951478231', NULL, 'Primera consulta por derivación médica.', TRUE, 'Prefiere contacto por WhatsApp', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (numero_documento) DO NOTHING;
