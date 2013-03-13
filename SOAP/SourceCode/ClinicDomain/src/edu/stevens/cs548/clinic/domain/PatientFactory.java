package edu.stevens.cs548.clinic.domain;

import java.util.Date;

public class PatientFactory implements IPatientFactory {

	@Override
	public Patient createPatient(long pid, String name, Date dob) {
		Patient patient = new Patient();
		patient.setPatientId(pid);
		patient.setName(name);
		patient.setBirthDate(dob);
		return patient;
	}
}
