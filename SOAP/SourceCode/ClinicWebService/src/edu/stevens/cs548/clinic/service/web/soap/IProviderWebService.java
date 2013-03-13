package edu.stevens.cs548.clinic.service.web.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.TreatmentNotFoundExn;

@WebService(
		name="IProviderWebPort",
		targetNamespace="http://www.example.org/clinic/wsdl/provider")
public interface IProviderWebService {
	//1. adding a provider to a clinic
	@WebMethod
	public void createProvider(long npi,String name,  String specialization) throws ProviderServiceExn;
	
	//2. Obtaining a list of provider DTOs
	@WebMethod
	public ProviderDto[] getProvidersByName (String name) throws ProviderServiceExn;

	//3. Obtaining a single provider DTO, given a national provider identifier (NPI).
	@WebMethod
	public ProviderDto getProviderByNPI (long id) throws ProviderServiceExn;
	//4. Adding a treatment for a patient. 
	@WebMethod
	public void addTreatment(long patientId, long providerNPI, TreatmentDto treatmentDto) throws ProviderServiceExn;

	//5. Delete a treatment for a patient. 
	@WebMethod
	public void deleteTreatment(long id, long tid) throws ProviderServiceExn,ProviderNotFoundExn, TreatmentNotFoundExn;

	//6. Obtaining a list of treatment DTOs, for all treatments for a particular patient(id) that are supervised by this provider(npi).
	@WebMethod
	public TreatmentDto[] getPatientTreatments(long id, long npi) 
	throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;
	
	//7. Obtaining a list of treatment DTOs for the treatments supervised by this provider(npi).
	@WebMethod
	public TreatmentDto[] getTreatments(long npi) throws ProviderServiceExn,ProviderNotFoundExn, TreatmentNotFoundExn;

	@WebMethod
	public String siteInfo(); 
}
