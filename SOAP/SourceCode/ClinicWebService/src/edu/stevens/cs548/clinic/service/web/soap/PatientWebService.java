package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceRemote;

@WebService(
		endpointInterface="edu.stevens.cs548.clinic.service.web.soap.IPatientWebService",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap/patient", //nameSpace for the generated xml
		serviceName="PatientWebService",
		portName="PatientWebPort")
public class PatientWebService implements IPatientWebService{
	//this beanName will lead Container to inject a PatientService Instance (Session Bean)
	//At run time, the Web service implementation obtains a reference to 
	//an EJB for the service facede using dependency injection (using the @EJB annotation)
	@EJB(beanName = "PatientServiceBean")
	IPatientServiceRemote service;  //a referrence to the service facede
	
	@Override
	@WebMethod(operationName = "create")
	public long createPatient(String name, Date dob, int age, long patientId)
			throws PatientServiceExn {
		return service.createPatient(name, dob, age, patientId);
	}

	@Override
	@WebMethod
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn {
		return service.getPatientByDbId(id);
	}

	@Override
	@WebMethod
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		return service.getPatientByPatId(pid);
	}

	@Override
	@WebMethod
	public PatientDto[] getPatientByNameDob(String name, Date dob) {
		return service.getPatientByNameDob(name, dob);
	}

	@Override
	@WebMethod
	public void deletePatient(String name, long id) throws PatientServiceExn {
		service.deletePatient(name, id);
	}

	@Override
	@WebMethod
	public void addDrugTreatment(long id, Provider provider, String diagnosis,
			String drug, float dosage) throws PatientNotFoundExn {
		service.addDrugTreatment(id, provider, diagnosis, drug, dosage);
	}

	@Override
	@WebMethod
	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		return service.getTreatments(id, tids);
	}

	@Override
	@WebMethod
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn,
			TreatmentNotFoundExn, PatientServiceExn {
		service.deleteTreatment(id, tid);
	}

	@Override
	@WebMethod
	public String siteInfo() {
		return service.siteInfo();
	}
}
