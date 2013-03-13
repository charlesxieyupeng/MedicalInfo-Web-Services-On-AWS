package edu.stevens.cs548.clinic.domain;

import java.util.Date;

public interface IPatientFactory {
	
	Patient createPatient(long pid, String name, Date dob);

}
