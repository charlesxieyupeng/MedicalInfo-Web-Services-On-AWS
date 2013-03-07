package edu.cs548;

import edu.cs548.Treatment;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

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
		// TODO Auto-generated method stub
		
	}
}
