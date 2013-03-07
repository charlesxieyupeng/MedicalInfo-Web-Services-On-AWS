package edu.cs548;

import javax.persistence.EntityManager;

public class TreatmentDAO implements ITreatmentDAO {
	
	private EntityManager em;
	
	public TreatmentDAO(EntityManager em){
		this.em = em;
	}
	@Override
	public Treatment getTreatmenByDbId(long id) throws TreatmentExn{
		Treatment t = em.find(Treatment.class, id);
		if(t == null)
			throw new TreatmentExn("Missing treatment. id = " + id);
		else
			return t;
	}

	@Override
	public long addTreatment(Treatment t) {
		em.persist(t);
		em.flush();
		long id = t.getId();
		return id;
	}
	
	@Override
	public void deleteTreatment(Treatment t) {
		em.remove(t);
	}
}
