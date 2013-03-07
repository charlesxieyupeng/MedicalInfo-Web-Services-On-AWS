package edu.cs548;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ProviderDAO implements IProviderDAO {
	
	private EntityManager em;
	private TreatmentDAO treatmentDAO;
	
	private static Logger logger = Logger.getLogger("ClinicDomain - provider");
	
	ProviderDAO(EntityManager em){
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

	@Override
	public void addProvider(Provider provider) throws ProviderExn {
		long npi = provider.getNpi();
		TypedQuery<Provider> query= em.createNamedQuery("SearchProviderByNPI", Provider.class)
										.setParameter("npi", npi);
		List<Provider> providers = query.getResultList();
		if(providers.size()<1){
			em.persist(provider);
			//might need -> TODO: provider.setTreatmentDAO(this.treatmentDAO);
		}
		else{
			throw new ProviderExn("Insertion: Provider with  npi("+npi+") already exists.");
		}
	}

	@Override
	public void addProvider(long npi, String name) throws ProviderExn {
		Provider p = this.getProviderByNpi(npi);
		if(p!=null) 
			throw new ProviderExn("Insertion: Provider with  npi("+npi+") already exists.");
		else{
			Provider provider = new Provider();
			provider.setName(name);
			provider.setNpi(npi);
			em.persist(provider);
		}
	}

	@Override
	public Provider getProviderByNpi(long npi) throws ProviderExn {
		TypedQuery<Provider> query = 
				em.createNamedQuery("SearchProviderByNpi", Provider.class)
				.setParameter("npi", npi);
		List<Provider> providers = query.getResultList();
		if(providers.size()>1){
			logger.info("Duplicate provider records:" + npi);
			throw new ProviderExn("Duplicate provider records:" + npi );
		}
		else if(providers.size()<1)
			throw new ProviderExn("provider not found: provider id = "+ npi);
		else{
			Provider p = providers.get(0);
			p.setTreatmentDAO(treatmentDAO);
			return p;
		}
	}

	@Override
	public List<Provider> getProviderByName(String name) throws ProviderExn {
		TypedQuery<Provider> query = 
				em.createNamedQuery("SearchProviderByName", Provider.class).setParameter("name", name);
		List<Provider> providers = query.getResultList();
		if(providers.size()<1){
			throw new ProviderExn("provider not found: provider name = "+ name);
		}
		for(Provider p: providers){
			p.setTreatmentDAO(treatmentDAO);
		}
		return providers;
	}

	@Override
	public void deleteProvider(Provider provider) throws ProviderExn {
		em.remove(provider);
		//the associated treatments will not be removed since the @OneToMany(mappedBy = "provider") 
	}
}
