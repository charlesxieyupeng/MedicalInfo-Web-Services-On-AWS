package edu.cs548;

public interface IClinicGateway {
	
	 public IPatientFactory getPatientFactory();
	 
	 public IPatientDAO getPatientDAO();
	 
	 public IProviderDAO getProviderDAO();
	 
}
