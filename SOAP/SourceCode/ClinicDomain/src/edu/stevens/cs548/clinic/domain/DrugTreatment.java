package edu.stevens.cs548.clinic.domain;


import java.io.Serializable;
import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.Treatment;


/**
 * Entity implementation class for Entity: DrugTreatment
 * 
 */
@Entity
@DiscriminatorValue("D")
@Table(name="TREATMENT_DrugTreatment")
public class DrugTreatment extends Treatment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String drug;
	private float dosage;

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public float getDosage() {
		return dosage;
	}

	public void setDosage(float dosage) {
		this.dosage = dosage;
	}

	public DrugTreatment() {
		super();
		this.setTreatmentType("D");
	}

	@Override
	public void accept(ITreatmentVisitor visitor) {
		visitor.visitDrugTreatment(this.getId(), 
								   this.getDiagnosis(),
								   drug, 
								   dosage);
	}

}
