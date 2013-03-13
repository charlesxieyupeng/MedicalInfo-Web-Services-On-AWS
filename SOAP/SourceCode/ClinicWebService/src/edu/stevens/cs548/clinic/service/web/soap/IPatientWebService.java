package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.TreatmentNotFoundExn;


@WebService(
		name="IPatientWebPort",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap/patient")
/*
 * Endpoint interface for the patient Web service
 */
public interface IPatientWebService {
	
	@WebMethod(operationName = "create")
	public long createPatient(String name, Date dob, int age, long patientId)throws PatientServiceExn;
	
	@WebMethod
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn;
	
	@WebMethod
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn;
	@WebMethod
	public PatientDto[] getPatientByNameDob(String name, Date dob);
	@WebMethod
	public void deletePatient(String name, long id) throws PatientServiceExn;
	@WebMethod
	public void addDrugTreatment(long id, Provider provider, String diagnosis, String drug, float dosage) throws PatientNotFoundExn;
	@WebMethod
	public TreatmentDto[] getTreatments(long id, long[] tids) throws PatientNotFoundExn, TreatmentNotFoundExn,PatientServiceExn;
	//id : patientDbId
	@WebMethod
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;
	//id : patientDbId
	@WebMethod
	public String siteInfo();
}
