package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.treatment.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.domain.PatientDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class PatientService
 * 
 * @Stateless
 * Component-defining annotation for a stateless session bean.
 */
@Stateless(name="PatientServiceBean")
public class PatientService implements IPatientServiceLocal, IPatientServiceRemote {

	private PatientFactory patientFactory;

	private IPatientDAO patientDAO;

	/**
	 * Default constructor. 
	 */
	public PatientService() {
		patientFactory = new PatientFactory();
		//    	patientDAO = new PatientDAO(em);
	}

	/*@PersistenceContext
	 * Expresses a dependency on a container-managed EntityManager and
	 *  its associated persistence context.
	 *  --- Dependency Injection: let the container inject an EntityManager
	 */
	@PersistenceContext(unitName = "ClinicDomain")//Expresses a dependency on a container-managed EntityManager and its associated persistence context.
	private EntityManager em;


	/*The PostConstruct annotation is used on a method that needs to 
	be executed after dependency injection is done to perform any initialization. */
	@PostConstruct
	private void initialize(){
		patientDAO = new PatientDAO(em);
	}

	/**
	 * @see IPatientService#createPatient(String, Date, long)
	 */
	public long createPatient(String name, Date dob, int age, long patientId) throws PatientServiceExn{
		//check the age and dob
		DateFormat dateformat = new SimpleDateFormat("YYYY");
		int currentYear = Integer.parseInt(dateformat.format(new Date()));
		int bornYear = Integer.parseInt(dateformat.format(dob));
		if((currentYear-bornYear)!=age) throw new PatientServiceExn("Age does not mat date of birth.");
		Patient patient = this.patientFactory.createPatient(patientId, name, dob);
		try {
			patientDAO.addPatient(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
		return patient.getId();
	}

	/**
	 * @see IPatientService#getPatient(long)
	 */
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn{
		try{
			Patient patient = patientDAO.getPatientByDbId(id);
			return new PatientDto(patient);
		}catch(PatientExn e){
			throw new PatientServiceExn(e.toString()); 
		}
	}
	/**
	 * @see IPatientService#getPatientByNameDob(String, Date)
	 */
	public PatientDto[] getPatientByNameDob(String name, Date dob){
		List<Patient> patients = patientDAO.getPatientByNameDob(name, dob);
		PatientDto[] dto = new PatientDto[patients.size()];
		for(int i=0;i<patients.size();i++){
			dto[i] = new PatientDto(patients.get(i));
		}
		return dto;
	}

	/**
	 * @see IPatientService#getPatientByPatId(long)
	 */
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		try{
			Patient patient = patientDAO.getPatientByPatientId(pid);
			return new PatientDto(patient);
		}catch(PatientExn e){
			throw new PatientServiceExn(e.toString()); 
		}
	}

	/**
	 * @see IPatientService#deletePatient(String, long)
	 */
	public void deletePatient(String name, long id) throws PatientServiceExn{
		try {
			if(name.equals(patientDAO.getPatientByDbId(id).getName())){
				patientDAO.deletePatientByPrimaryKey(id);
			}else{
				throw new PatientServiceExn("Trid to delete wrong patient. name = "+ name+" id="+ id);
			}
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public  void addDrugTreatment(long id, Provider provider, 
			String diagnosis, String drug, float dosage) throws PatientNotFoundExn{
		//retrieve patient entity first: aggregate
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			patient.addDrugTrearment(provider, diagnosis, drug, dosage); 
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		}
	}

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

	@Override
	public TreatmentDto[] getTreatments(long id)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			List<Long> treatmentIds = patient.getTreatmentIds();
			TreatmentDto[] treatments = new TreatmentDto[treatmentIds.size()];
			//construct treatmentDTO
			TreatmentPDOtoDTO concreteTreatmentVisitor = new  TreatmentPDOtoDTO();
			for(int i=0;i<treatmentIds.size();i++){
				patient.visitTreatment(treatmentIds.get(i), concreteTreatmentVisitor);
				treatments[i] = concreteTreatmentVisitor.getDTO();
			}
			return treatments;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e){
			throw new PatientServiceExn(e.toString());
		}
	}
	@Override
	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		try {
			Patient patient = patientDAO.getPatientByDbId(id);
			TreatmentDto[] treatments = new TreatmentDto[tids.length];
			//construct treatmentDTO
			TreatmentPDOtoDTO concreteTreatmentVisitor = new  TreatmentPDOtoDTO();
			for(int i=0;i<tids.length;i++){
				patient.visitTreatment(tids[i], concreteTreatmentVisitor);
				treatments[i] = concreteTreatmentVisitor.getDTO();
			}
			return treatments;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e){
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn,
	TreatmentNotFoundExn, PatientServiceExn {
		try{
			Patient patient = patientDAO.getPatientByDbId(id);
			patient.deleteTreatment(tid);
		}catch(PatientExn e){
			throw new PatientNotFoundExn(e.toString());
		}catch(TreatmentExn e){
			throw new PatientServiceExn(e.toString());
		}
	}
	
	@Resource(name="SiteInfo")
	private String siteInformation;

	@Override
	public String siteInfo() {
		return siteInformation;
	}
}
