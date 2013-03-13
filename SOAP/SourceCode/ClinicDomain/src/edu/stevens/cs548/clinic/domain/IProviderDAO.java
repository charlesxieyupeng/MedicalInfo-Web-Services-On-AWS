package edu.stevens.cs548.clinic.domain;

import java.util.List;

public interface IProviderDAO {
	
	public static class ProviderExn extends Exception{
		private static final long serialVersionUID = 1L;

		ProviderExn(String s){
			super(s);
		}
	}
	
	void addProvider(Provider provider) throws ProviderExn;
	
	void addProvider(long npi, String name) throws ProviderExn;
	
	Provider getProviderByNpi(long npi) throws ProviderExn;
	
	List<Provider> getProviderByName(String name) throws ProviderExn;
	
	void deleteProvider(Provider provider) throws ProviderExn;
	
}
