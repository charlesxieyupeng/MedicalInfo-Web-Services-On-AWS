--<ScriptOptions statementTerminator=";"/>

CREATE TABLE treatment_surgerytreatment (
		id INT8 NOT NULL,
		surgerydate DATE
	);

CREATE UNIQUE INDEX treatment_surgerytreatment_pkey ON treatment_surgerytreatment (id ASC);

ALTER TABLE treatment_surgerytreatment ADD CONSTRAINT treatment_surgerytreatment_pkey PRIMARY KEY (id);

ALTER TABLE treatment_surgerytreatment ADD CONSTRAINT fk_treatment_surgerytreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

