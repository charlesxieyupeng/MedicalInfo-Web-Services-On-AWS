package edu.cs548;

public class ProviderFactory implements IProviderFactory {

	@Override
	public Provider createProvider(long npi, String name, String specialization) {
		Provider provider = new Provider();
		provider.setNpi(npi);
		provider.setName(name);
		provider.setSpecialization(specialization);
		return provider;
	}

}
