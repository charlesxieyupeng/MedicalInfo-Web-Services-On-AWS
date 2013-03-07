package edu.cs548;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Provider
 *
 */
@Entity
@Table(name = "PROVIDER")
public class Provider implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	private long npi;
	public String getSpecialization() {
		return Specialization;
	}

	public void setSpecialization(String specialization) {
		Specialization = specialization;
	}

	private String name;
	private String Specialization;
	
	@OneToMany(mappedBy = "provider") //this provider is the field in Treatment
	@OrderBy
	private List<Treatment> treatments;
	
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

	public List<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();
	}
   
}
