--<ScriptOptions statementTerminator=";"/>

CREATE TABLE provider (
		npi INT8 NOT NULL,
		specialization VARCHAR(255),
		name VARCHAR(255)
	);

CREATE UNIQUE INDEX provider_pkey ON provider (npi ASC);

ALTER TABLE provider ADD CONSTRAINT provider_pkey PRIMARY KEY (npi);

