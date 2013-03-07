--<ScriptOptions statementTerminator=";"/>

CREATE TABLE treatment_drugtreatment (
		id INT8 NOT NULL,
		dosage FLOAT8,
		drug VARCHAR(255)
	);

CREATE UNIQUE INDEX treatment_drugtreatment_pkey ON treatment_drugtreatment (id ASC);

ALTER TABLE treatment_drugtreatment ADD CONSTRAINT treatment_drugtreatment_pkey PRIMARY KEY (id);

ALTER TABLE treatment_drugtreatment ADD CONSTRAINT fk_treatment_drugtreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

