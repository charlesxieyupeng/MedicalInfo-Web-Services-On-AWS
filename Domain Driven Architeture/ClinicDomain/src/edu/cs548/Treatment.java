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

	@Column(name="TTYPE",length=2)
	private String treatmentType;

	@ManyToOne
	@JoinColumn(name = "patient_fk",referencedColumnName = "id") //referencedColumnName - this "id" is the id field(PK) in Patient Class
	private Patient patient;

	@ManyToOne
	@JoinColumn(name = "provider_fk", referencedColumnName = "npi") // this npi is the npi field in provider class 
	private Provider provider;

	public Treatment() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
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
	
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
		if(!provider.getTreatments().contains(this))
			provider.addTreatment(this);
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
		if(!patient.getTreatments().contains(this))
			patient.addTreatment(this);
	}
	
	//in pod-cast, professor named this function as visit(ItreatmentVisitor visitor)
	public abstract void accept(ITreatmentVisitor visitor);
}
