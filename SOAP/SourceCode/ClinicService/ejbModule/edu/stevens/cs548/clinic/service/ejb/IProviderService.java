package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;


public interface IProviderService {

	public class ProviderServiceExn extends Exception {
		private static final long serialVersionUID = 1L;

		public ProviderServiceExn (String m) {
			super(m);
		}
	}

	public class ProviderNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;
		public ProviderNotFoundExn (String m) {
			super(m);
		}
	}
	public class TreatmentNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;
		public TreatmentNotFoundExn (String m) {
			super(m);
		}
	}
	//1. adding a provider to a clinic
	public void createProvider(long npi,String name,  String specialization) throws ProviderServiceExn;
	
	//2. Obtaining a list of provider DTOs
	public ProviderDto[] getProvidersByName (String name) throws ProviderServiceExn;

	//3. Obtaining a single provider DTO, given a national provider identifier (NPI).
	public ProviderDto getProviderByNPI (long id) throws ProviderServiceExn;
	//4. Adding a treatment for a patient. 
	public void addTreatment(long patientId, long providerNPI, TreatmentDto treatmentDto) throws ProviderServiceExn;


	//	public void deleteProvider (String name, long id) throws ProviderServiceExn;

	//5. Delete a treatment for a patient. 
	public void deleteTreatment(long id, long tid) throws ProviderServiceExn,ProviderNotFoundExn, TreatmentNotFoundExn;

	//6. Obtaining a list of treatment DTOs, for all treatments for a particular patient(id) that are supervised by this provider(npi).
	public TreatmentDto[] getPatientTreatments(long id, long npi) throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;
	
	//7. Obtaining a list of treatment DTOs for the treatments supervised by this provider(npi).
	public TreatmentDto[] getTreatments(long npi) throws ProviderServiceExn,ProviderNotFoundExn, TreatmentNotFoundExn;

	public String siteInfo(); 

}
