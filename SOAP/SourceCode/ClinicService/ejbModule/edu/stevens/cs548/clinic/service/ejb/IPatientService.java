package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;

import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

public interface IPatientService {
	
	public class PatientServiceExn extends Exception{

		private static final long serialVersionUID = 1L;

		public PatientServiceExn(String m){
			super(m);
		}
	}
	public class PatientNotFoundExn extends PatientServiceExn{

		private static final long serialVersionUID = 1L;

		public PatientNotFoundExn(String m){
			super(m);
		}
	}
	
	public class TreatmentNotFoundExn extends PatientServiceExn{

		private static final long serialVersionUID = 1L;

		public TreatmentNotFoundExn(String m){
			super(m);
		}
	}
	
	public long createPatient(String name, Date dob, int age, long patientId) throws PatientServiceExn;
	
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn;

	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn;
	
	public PatientDto[] getPatientByNameDob(String name, Date dob);

	public void deletePatient(String name, long id) throws PatientServiceExn;
	
	public void addDrugTreatment(long id, Provider provider, String diagnosis, String drug, float dosage) throws PatientNotFoundExn;
	
	public TreatmentDto[] getTreatments(long id, long[] tids) throws PatientNotFoundExn, TreatmentNotFoundExn,PatientServiceExn;
	//id : patientDbId
	
	public TreatmentDto[] getTreatments(long id) throws PatientNotFoundExn, TreatmentNotFoundExn,PatientServiceExn;
	//id : patientDbId
	
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;
	//id : patientDbId
	public String siteInfo();
}
