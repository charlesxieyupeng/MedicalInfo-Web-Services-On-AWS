package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface IPatientDAO {
	
	public static class PatientExn extends Exception{
		private static final long serialVersionUID = 1L;
		public PatientExn(String msg){
			super(msg);
		}
	}
	
	//retrieve patient info
	public Patient getPatientByPatientId(long pid) throws PatientExn;
	
	public Patient getPatientByDbId(long id) throws PatientExn;
	
	public List<Patient> getPatientByNameDob(String name, Date dob);
	 
	//add patient info
	public void addPatient(Patient pat) throws PatientExn;
	
	public Long addPatient(String name, Date dob, int age) throws PatientExn;

	//1. add a new patient to the clinic
	public Long addPatient(long pid, String name, Date dob, int age) throws PatientExn;
	
	public void deletePatient(Patient pat) throws PatientExn;
	//3.	Delete a patient aggregate from the system, given the primary key for the patient.
	public void deletePatientByPrimaryKey(long id) throws PatientExn;
	
}
