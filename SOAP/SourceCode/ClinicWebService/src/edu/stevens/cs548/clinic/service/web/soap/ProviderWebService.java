package edu.stevens.cs548.clinic.service.web.soap;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceRemote;
@WebService(
		endpointInterface="edu.stevens.cs548.clinic.service.web.soap.IProviderWebService",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap/provider", //nameSpace for the generated xml
		serviceName="ProviderWebService",
		portName="ProviderWebPort")
public class ProviderWebService implements IProviderWebService {

	@EJB(beanName="ProviderServiceBean")
	IProviderServiceRemote service;
	
	@Override
	public void createProvider(long npi,String name,  String specialization)
			throws ProviderServiceExn {
		service.createProvider(npi,name,  specialization);
	}

	@Override
	public ProviderDto[] getProvidersByName (String name) throws ProviderServiceExn{
		return service.getProvidersByName(name);
	}

	@Override
	public ProviderDto getProviderByNPI(long npi) throws ProviderServiceExn {
		return service.getProviderByNPI(npi);
	}

	@Override
	public void addTreatment(long patientId, long providerNPI,
			TreatmentDto treatmentDto) throws ProviderServiceExn {
		service.addTreatment(patientId, providerNPI, treatmentDto);
	}
	

	@Override
	public void deleteTreatment(long id, long tid) throws ProviderServiceExn,
			ProviderNotFoundExn, TreatmentNotFoundExn {
		service.deleteTreatment(id, tid);
	}

	@Override
	public TreatmentDto[] getPatientTreatments(long id, long npi)
			throws ProviderServiceExn, ProviderNotFoundExn,
			TreatmentNotFoundExn {
		return service.getPatientTreatments(id,npi);
	}

	@Override
	public TreatmentDto[] getTreatments(long npi) throws ProviderServiceExn,
			ProviderNotFoundExn, TreatmentNotFoundExn {
		return service.getTreatments(npi);
	}
	@Override
	public String siteInfo() {
		return service.siteInfo();
	}

}
