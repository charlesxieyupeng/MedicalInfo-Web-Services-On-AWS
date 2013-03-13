package edu.stevens.cs548.clinic.domain;

public interface IClinicGateway {
	
	 public IPatientFactory getPatientFactory();
	 
	 public IPatientDAO getPatientDAO();
	 
	 public IProviderDAO getProviderDAO();
	 
}
