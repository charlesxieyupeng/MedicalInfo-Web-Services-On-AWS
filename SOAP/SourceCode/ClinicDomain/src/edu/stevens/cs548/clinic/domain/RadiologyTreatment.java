package edu.stevens.cs548.clinic.domain;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.Treatment;


/**
 * Entity implementation class for Entity: RadiologyTreatment
 *
 */
@Entity
@DiscriminatorValue("R")
@Table(name="TREATMENT_RadTreatment")
public class RadiologyTreatment extends Treatment implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Date> RadiologyDates;
	
	public RadiologyTreatment() {
		super();
		this.setTreatmentType("R");
	}

	public List<Date> getRadiologyDates() {
		return RadiologyDates;
	}

	public void setRadiologyDates(List<Date> radiologyDates) {
		RadiologyDates = radiologyDates;
	}

	@Override
	public void accept(ITreatmentVisitor visitor) {
		visitor.visitRadiology(this.getId(),
				this.getDiagnosis(), 
				RadiologyDates);
	}
}
