package edu.cs548;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Patient
 *
 */
@Entity
@Table(name = "PATIENT")
public class Patient implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		@Id
		@GeneratedValue
		private long id;
		private long patientId;
		private String name;
		@Temporal(TemporalType.DATE)
		private Date birthDate;

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

		/*
		 * TODO: Provide reference to related treatments, as a list
		 */
		@OneToMany(cascade = CascadeType.REMOVE,mappedBy = "patient")
		@OrderBy          //OrderBy default is primary key 
		private List<Treatment> treatments;
		
		public void setTreatment(List<Treatment> treatments){
			this.treatments = treatments;
		}
		public List<Treatment> getTreatment(){
			return treatments;
		}
		
		public Patient() {
			super();
			treatments = new ArrayList<Treatment>();
		}
	   
	}

