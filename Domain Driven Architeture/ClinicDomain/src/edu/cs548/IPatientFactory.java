package edu.cs548;

import java.util.Date;

public interface IPatientFactory {
	
	Patient createPatient(long pid, String name, Date dob);

}
