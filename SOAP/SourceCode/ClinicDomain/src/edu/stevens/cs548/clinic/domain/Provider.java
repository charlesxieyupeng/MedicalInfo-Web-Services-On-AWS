package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;



/**
 * Entity implementation class for Entity: Provider
 *
 */
@Entity
@NamedQuery(
		name = "SearchProviderByNpi",
		query = "select p from Provider p where p.npi = :npi")
@Table(name = "PROVIDER")
public class Provider implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	private long npi;
	private String name;

	private String Specialization;
	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();
	}

	public String getSpecialization() {
		return Specialization;
	}

	public void setSpecialization(String specialization) {
		Specialization = specialization;
	}

	public long getNpi() {
		return npi;
	}

	public void setNpi(long npi) {
		this.npi = npi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "provider") //this provider is the field in Treatment
	@OrderBy
	private List<Treatment> treatments;
	
	public List<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	@Transient 
	private TreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO(TreatmentDAO treatmentDAO) {
		this.treatmentDAO = treatmentDAO;
	}
	
	public Long addTreatment (Treatment t){ //For concrete Treatment to invoke
		this.getTreatments().add(t); //associate with provider object
		if(t.getProvider()!=this)
			t.setProvider(this);
		this.treatmentDAO.addTreatment(t); //persist to db
		Long id = t.getId();
		return id;
	}
	
	/* 	the following addDrugTrearmen,
	addSurgeryTreatmentare,
	and addRadiologyTreatment are the factory methods to create treatment*/		

	public void addDrugTrearment(Patient patient, String diagnosis, String drug, float dosage){
		DrugTreatment dt = new DrugTreatment();
		dt.setDiagnosis(diagnosis);
		dt.setPatient(patient);
		dt.setDrug(drug);
		dt.setDosage(dosage);
		this.addTreatment(dt);
	}
	public void addSurgeryTreatment(Patient patient, String diagnosis, Date surgeryDate){
		SurgeryTreatment st = new SurgeryTreatment();
		st.setPatient(patient);
		st.setDiagnosis(diagnosis);
		st.setSurgeryDate(surgeryDate);
		this.addTreatment(st);
	}
	
	public void addRadiologyTreatment(Patient patient, String diagnosis, List<Date> radiologyDates){
		RadiologyTreatment rt = new RadiologyTreatment();
		rt.setPatient(patient);
		rt.setDiagnosis(diagnosis);
		rt.setRadiologyDates(radiologyDates);
		this.addTreatment(rt);
	}
	
	
	
	public List<Long> getTreatmentIds(){
		List<Long> treatmentIds = new ArrayList<Long>();
		for(Treatment t : this.getTreatments()){
			treatmentIds.add(t.getId());
		}
		return treatmentIds;
	}
	public List<Long> getTreatmentIds(Patient patient){
		List<Long> treatmentIds = new ArrayList<Long>();
		List<Long> patientTreatmentIds = patient.getTreatmentIds();
		List<Long> providerTreatmentIds = this.getTreatmentIds();
		for(Long id : patientTreatmentIds){
			if(providerTreatmentIds.contains(id))
				treatmentIds.add(id);
		}
		return treatmentIds;
	}
	
	public void visitTreatment(long tid, ITreatmentVisitor visitor) throws TreatmentExn{
		Treatment treatment = treatmentDAO.getTreatmenByDbId(tid);
		if(treatment.getProvider()!=this)
			throw new TreatmentExn("This treatment (id = "+tid+") is not supervising by provider (npi = "+this.getNpi()+")");
		treatment.accept(visitor);
	}
	
	public void deleteTreatment(long tid) throws TreatmentExn{
		Treatment treatment = treatmentDAO.getTreatmenByDbId(tid);
		if(treatment.getProvider()!=this)
			throw new TreatmentExn("This treatment (id = "+tid+") is not supervising by provider (npi = "+this.getNpi()+")");
		treatmentDAO.deleteTreatment(treatment);
	}	
}
