package edu.stevens.cs548.clinic.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class PatientDAO implements IPatientDAO {

	private EntityManager em;//is going to be used to get access to DB
	private TreatmentDAO treatmentDAO;
	
	private static Logger logger = Logger.getLogger("ClinicDomain - patient");
	
	public PatientDAO(EntityManager em){
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}
	
	@Override
	public Patient getPatientByDbId(long id) throws PatientExn {
		Patient p = em.find(Patient.class, id);
		if(p == null) 
			throw new PatientExn("Patient not found: primary key =" + id);
		else {
			p.setTreatmentDAO(treatmentDAO);
			return p;
		}
	}
	
	@Override
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		TypedQuery<Patient> query = 
				em.createNamedQuery("SearchPatientByPatientId", Patient.class)
				.setParameter("pid", pid);
		List<Patient> patients = query.getResultList();
		if(patients.size()>1){
			logger.info("Duplicate patient records:" + pid);
			throw new PatientExn("Duplicate patient records:" + pid );
		}
		else if(patients.size()<1)
			throw new PatientExn("Patient not found: patient id = "+ pid);
		else{
			Patient p = patients.get(0);
			p.setTreatmentDAO(treatmentDAO);
			//give the patient object the ability to access TreatmentDAO
			return p;
		}
	}

	@Override
	public List<Patient> getPatientByNameDob(String name, Date dob) {
		TypedQuery<Patient> query = 
				em.createNamedQuery("SearchPatientByNameDOB", Patient.class)
				.setParameter("name", name)
				.setParameter("dob", dob);
		List<Patient> patients = query.getResultList();
		for(Patient p: patients){
			p.setTreatmentDAO(treatmentDAO);
		}
		return patients;
	}

	@Override
	public void addPatient(Patient pat) throws PatientExn{
		//make sure this patient does not exist in DB
		long pid = pat.getPatientId();
		TypedQuery<Patient> query =
				em.createNamedQuery("SearchPatientByPatientId", Patient.class)
				.setParameter("pid", pid);
		List<Patient> patients = query.getResultList();
		if(patients.size()<1){
			em.persist(pat);
			pat.setTreatmentDAO(this.treatmentDAO);
		}else{
			throw new PatientExn("Insertion: Patien with patient id("+pid+") already exists.");
		}
	}

	@Override //HW5 - 1 the policy allows pid being null
	public Long addPatient(String name, Date dob, int age) 
			throws PatientExn {
		DateFormat dateformat = new SimpleDateFormat("YYYY");
		int birthyear = Integer.parseInt(dateformat.format(dob));
		int currentyear = Integer.parseInt(dateformat.format(new Date()));
		if(age!=(currentyear - birthyear)) throw new PatientExn("Inputed age conflic with birthday");
		Patient patient = new Patient(); //this is another patient factory method
		patient.setName(name);
		patient.setBirthDate(dob);
		em.persist(patient);
		em.flush();
		em.refresh(patient);//DB -> patient object
		long id = patient.getId();
		return id;
	}
	
	public Long addPatient(long pid, String name, Date dob, int age)
			throws PatientExn {
		//make sure pid is not exist in DB yet
		Patient patient = this.getPatientByPatientId(pid);
		if(patient!=null) throw new PatientExn("Insertion: Patien already exists.");
		Long id = addPatient(name,dob,age); 
		return id;
	}
	
	@Override
	public void deletePatient(Patient pat) throws PatientExn {
//		em.createQuery("delete from Treatment t where t.patient.id = :id")
//			.setParameter("id", pat.getId())
//			.executeUpdate();
		em.remove(pat); //will remove the treatment as well since  cascade = REMOVE
	}

	@Override //HW5 - 3
	public void deletePatientByPrimaryKey(long id) throws PatientExn {
		Patient pat = this.getPatientByDbId(id);
		this.deletePatient(pat);
	}
}
