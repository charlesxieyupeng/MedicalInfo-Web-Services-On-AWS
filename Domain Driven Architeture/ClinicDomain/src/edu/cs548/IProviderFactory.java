package edu.cs548;

public interface IProviderFactory {
	public Provider createProvider(long npi, String name, String specialization);
}