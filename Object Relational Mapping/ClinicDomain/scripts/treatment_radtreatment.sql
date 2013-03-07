--<ScriptOptions statementTerminator=";"/>

CREATE TABLE treatment_radtreatment (
		id INT8 NOT NULL,
		radiologydates BYTEA(2147483647)
	);

CREATE UNIQUE INDEX treatment_radtreatment_pkey ON treatment_radtreatment (id ASC);

ALTER TABLE treatment_radtreatment ADD CONSTRAINT treatment_radtreatment_pkey PRIMARY KEY (id);

ALTER TABLE treatment_radtreatment ADD CONSTRAINT fk_treatment_radtreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

