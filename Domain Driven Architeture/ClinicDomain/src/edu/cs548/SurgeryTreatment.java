package edu.cs548;

import edu.cs548.Treatment;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: SurgeryTreatment
 *
 */
@Entity
@DiscriminatorValue("S")
@Table(name="TREATMENT_SurgeryTreatment")
public class SurgeryTreatment extends Treatment implements Serializable {

	private static final long serialVersionUID = 1L;
//	
//	private long npi_surgeon;
//	
//	public long getNpi_surgeon() {
//		return npi_surgeon;
//	}
//	public void setNpi_surgeon(long npi_surgeon) {
//		this.npi_surgeon = npi_surgeon;
//	}

	@Temporal(TemporalType.DATE)
	private Date surgeryDate;
	
	public Date getSurgeryDate() {
		return surgeryDate;
	}
	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	public SurgeryTreatment() {
		super();
		this.setTreatmentType("S");
	}
	@Override
	public void accept(ITreatmentVisitor visitor) {
		// TODO Auto-generated method stub
		
	}
}
