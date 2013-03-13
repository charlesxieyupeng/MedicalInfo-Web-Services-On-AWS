package edu.stevens.cs548.clinic.domain;

public interface ITreatmentDAO {
	public static class TreatmentExn extends Exception{

		private static final long serialVersionUID = 1L;

		public TreatmentExn(String msg){
			super(msg);
		}
	}
	public Treatment getTreatmenByDbId(long id) throws TreatmentExn;
	
	public long addTreatment(Treatment t);
	
	public void deleteTreatment(Treatment t);
}
