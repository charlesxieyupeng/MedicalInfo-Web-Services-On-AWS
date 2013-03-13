package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;



/**
 * Entity implementation class for Entity: Patient
 *
 */
@Entity
@NamedQueries({
@NamedQuery(
		name = "SearchPatientByNameDOB",
		query = "select p from Patient p where p.name = :name and p.birthDate = :birthDate"),
@NamedQuery(
		name = "SearchPatientByPatientId",
		query = "select p from Patient p where p.patientId = :pid")
})
@Table(name = "PATIENT")
public class Patient implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		@Id
		@GeneratedValue
		private long id;
		private long patientId;//can be null. a Patient will be treat even without PID
		private String name;
		@Temporal(TemporalType.DATE)
		private Date birthDate;
		
		public Patient() {
			super();
			treatments = new ArrayList<Treatment>();
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public long getPatientId() {
			return patientId;
		}

		public void setPatientId(long patientId) {
			this.patientId = patientId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getBirthDate() {
			return birthDate;
		}

		public void setBirthDate(Date birthDate) {
			this.birthDate = birthDate; 
		}
		
		@OneToMany(cascade = CascadeType.REMOVE,mappedBy = "patient")
		@OrderBy          //OrderBy default is primary key 
		private List<Treatment> treatments;

		@Transient
		private ITreatmentDAO treatmentDAO;

		protected void setTreatment(List<Treatment> treatments){
			this.treatments = treatments;
		}

		protected List<Treatment> getTreatments(){
			return treatments;
		}
		
		public void setTreatmentDAO(ITreatmentDAO tdao){
			this.treatmentDAO = tdao;
		}
		
		void addTreatment (Treatment t){ //For concrete Treatment to invoke
			this.getTreatments().add(t);
			if(t.getPatient()!=this)
				t.setPatient(this);
			this.treatmentDAO.addTreatment(t); 
		}
		
/* 	the following addDrugTrearmen,
					addSurgeryTreatmentare,
					and addRadiologyTreatment are the factory methods to create treatment*/		
		public void addDrugTrearment(Provider provider, String diagnosis, String drug, float dosage){
			DrugTreatment dt = new DrugTreatment();
			dt.setProvider(provider);
			dt.setDiagnosis(diagnosis);
			dt.setDrug(drug);
			dt.setDosage(dosage);
			this.addTreatment(dt);
		}
		
		public void addSurgeryTreatment(Provider provider, String diagnosis, Date surgeryDate){
			SurgeryTreatment st = new SurgeryTreatment();
			st.setProvider(provider);
			st.setDiagnosis(diagnosis);
			st.setSurgeryDate(surgeryDate);
			this.addTreatment(st);
		}
		
		public void addRadiologyTreatment(Provider provider, String diagnosis, List<Date> radiologyDates){
			RadiologyTreatment rt = new RadiologyTreatment();
			rt.setProvider(provider);
			rt.setDiagnosis(diagnosis);
			rt.setRadiologyDates(radiologyDates);
			this.addTreatment(rt);
		}
		
		public List<Long> getTreatmentIds(){
			List<Long> tids = new ArrayList<Long>();
			for(Treatment t: this.getTreatments()){
				tids.add(t.getId());
			}
			return tids;
		}
		
		public void visitTreatment(long tid, ITreatmentVisitor visitor) throws TreatmentExn{
			Treatment t = treatmentDAO.getTreatmenByDbId(tid);
			if(t.getPatient() != this){
				throw new TreatmentExn("Inappropriate treatment access: patient =" + id
										+", treatment = " + tid);
			}
			t.accept(visitor);
		}
		
		public void visitTreatments(ITreatmentVisitor visitor){
			for(Treatment t : this.getTreatments()){
				t.accept(visitor);
			}
		}
		
		public void deleteTreatment(long tid) throws TreatmentExn{
			Treatment t = treatmentDAO.getTreatmenByDbId(tid);
			if(t.getPatient() != this){
				throw new TreatmentExn("Inappropriate treatment access: patient =" + id
										+", treatment = " + tid);
			}
			treatmentDAO.deleteTreatment(t);
		}
	}

