package edu.cs548;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Treatment
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="TTYPE")
@Table(name="TREATMENT")

public abstract class Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	private String diagnosis;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	/*
	 * TODO: Use this in defining the discriminator column.
	 */
	@Column(name="TTYPE",length=2)
	private String treatmentType;
	
	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	/*
	 * TODO: Add backward references to patient and provider
	 */
	@ManyToOne
	@JoinColumn(name = "patient_fk",referencedColumnName = "id") //referencedColumnName - this "id" is the id field in Patient Class
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name = "provider_fk", referencedColumnName = "npi") // this npi is the nopi field in provider class 
	private Provider provider;
	
//	public Patient getPatient() {
//		return patient;
//	}
//
//	public void setPatient(Patient patient) {
//		this.patient = patient;
//		if (!patient.getTreatment().contains(this))
//			patient.setTreatment(this);
//	}
//	

	public Treatment() {
		super();
	}
   
}
