package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.treatment.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

@Stateless(name="ProviderServiceBean")
public class ProviderService implements IProviderServiceLocal,
		IProviderServiceRemote {
	

	private ProviderFactory providerFactory;

	private IProviderDAO providerDAO;
	
	private IPatientDAO patientDAO;//some API require to access to patient object: addTreatment(...)

	/**
	 * Default constructor.
	 */
	public ProviderService() {
		providerFactory = new ProviderFactory();
	}

	/*@PersistenceContext
	 * Expresses a dependency on a container-managed EntityManager and
	 *  its associated persistence context.
	 *  --- Dependency Injection: let the container inject an EntityManager
	 */
	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;

	/*The PostConstruct annotation is used on a method that needs to 
	be executed after dependency injection is done to perform any initialization. */
	@PostConstruct
	private void initialize() {
		providerDAO = new ProviderDAO(em);
		patientDAO = new PatientDAO(em);
	}

	@Override
	public void createProvider(long npi, String name,  String specialization)
			throws ProviderServiceExn {
		Provider provider = this.providerFactory.createProvider(npi, name, specialization);
		try {
			providerDAO.addProvider(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public ProviderDto getProviderByNPI (long id) throws ProviderServiceExn{
		try {
			Provider provider = providerDAO.getProviderByNpi(id);
			return new ProviderDto(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}


	@Override
	public ProviderDto[] getProvidersByName(String name) throws ProviderServiceExn{
		try {
			List<Provider> providers = providerDAO.getProviderByName(name);
			ProviderDto[] providerDtos = new ProviderDto[providers.size()];
			for(int i=0;i<providers.size();i++){
				providerDtos[i] = new ProviderDto(providers.get(i));
			}
			return providerDtos;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}
	}
	
	//Deploy RuntimeException 

	@Override
	public void addTreatment(long patientId, long providerNPI,
			TreatmentDto treatmentDto) throws ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNpi(providerNPI);
			DrugTreatmentType dt;
			RadiologyType ra;
			SurgeryType sur;
			Patient patient = patientDAO.getPatientByDbId(patientId);
			if((dt = treatmentDto.getDrugTreatment())!=null){
				provider.addDrugTrearment(patient,
						treatmentDto.getDiagnosis(),
						dt.getName(),
						dt.getDosage());
			}
			else if((ra = treatmentDto.getRadiology())!=null){
				provider.addRadiologyTreatment(patient, 
						treatmentDto.getDiagnosis(), 
						ra.getDate());
			}
			else//treatmentDto.getSurgery()!=null
			{
				sur = treatmentDto.getSurgery();
				provider.addSurgeryTreatment(patient,
						treatmentDto.getDiagnosis(), 
						sur.getDate());
			}
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString()); 
		}catch(PatientExn e){
			throw new ProviderServiceExn(e.toString()); 
		}
	}

	@Override
	public TreatmentDto[] getPatientTreatments(long id, long npi) 
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn{
		try {
			Provider provider = providerDAO.getProviderByNpi(npi);
			Patient patient = patientDAO.getPatientByDbId(id);
			List<Long> treatmentIds = provider.getTreatmentIds(patient);
			TreatmentDto[] treatmentDtos = new TreatmentDto[treatmentIds.size()];
			TreatmentPDOtoDTO ptd = new TreatmentPDOtoDTO();
			for(int i=0;i<treatmentIds.size();i++){
				provider.visitTreatment(treatmentIds.get(i), ptd);//ptd is the visitor
				treatmentDtos[i] = ptd.getDTO();
			}
			return treatmentDtos;
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e){
			throw new ProviderServiceExn(e.toString());
		} catch(TreatmentExn e){
			throw new TreatmentNotFoundExn(e.toString());
		}
	}
	
	//Deploy RuntimeException 
	
	@Override
	public TreatmentDto[] getTreatments(long npi) throws ProviderServiceExn,
			ProviderNotFoundExn, TreatmentNotFoundExn {
		try {
			Provider provider = providerDAO.getProviderByNpi(npi);
			List<Long> treatments = provider.getTreatmentIds();
			TreatmentDto[] treatmentDtos = new TreatmentDto[treatments.size()];
			TreatmentPDOtoDTO ptd = new TreatmentPDOtoDTO();
			for(int i=0;i<treatments.size();i++){
				provider.visitTreatment(treatments.get(i), ptd);
				treatmentDtos[i] = ptd.getDTO();
			}
			return treatmentDtos;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e){
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public void deleteTreatment(long id, long tid) throws ProviderServiceExn,
			ProviderNotFoundExn, TreatmentNotFoundExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			patient.deleteTreatment(tid);
		} catch (PatientExn e) {
			throw new ProviderServiceExn(e.toString());
		}catch(TreatmentExn e){
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Resource(name="SiteInfo")
	private String siteInformation;

	@Override
	public String siteInfo() {
		return siteInformation;
	}


	//visitor
	static class TreatmentPDOtoDTO implements ITreatmentVisitor {

		private TreatmentDto dto;
		public TreatmentDto getDTO(){
			return dto;
		}

		@Override
		public void visitDrugTreatment(long tid, String diagnosis, String drug,
				float dosage) {
			dto = new TreatmentDto();
			dto.setDiagnosis(diagnosis);
			DrugTreatmentType drugInfo = new DrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			dto.setDrugTreatment(drugInfo);
		}

		@Override
		public void visitRadiology(long tid, String diagnosis, List<Date> dates) {
			dto = new TreatmentDto();
			dto.setDiagnosis(diagnosis);
			RadiologyType radiologyInfo = new RadiologyType();
			radiologyInfo.setDate(dates);
			dto.setRadiology(radiologyInfo);
		}

		@Override
		public void visitSurgery(long tid, String diagnosis, Date date) {
			dto = new TreatmentDto();
			dto.setDiagnosis(diagnosis);
			SurgeryType surgeryInfo = new SurgeryType();
			surgeryInfo.setDate(date);
			dto.setSurgery(surgeryInfo);
		}
	}
}
