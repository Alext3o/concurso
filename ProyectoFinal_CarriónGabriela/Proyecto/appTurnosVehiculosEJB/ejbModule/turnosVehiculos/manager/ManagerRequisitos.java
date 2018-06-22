package turnosVehiculos.manager;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.taglibs.standard.lang.jstl.BooleanLiteral;

import com.sun.xml.internal.ws.runtime.config.TubelineFeatureReader;

import turnosVehiculos.model.entities.Asignacion;
import turnosVehiculos.model.entities.Cliente;
import turnosVehiculos.model.entities.EstadoRevision;
import turnosVehiculos.model.entities.Personal;
import turnosVehiculos.model.entities.Requisito;
import turnosVehiculos.model.entities.Turno;
import turnosVehiculos.model.entities.Vehiculo;
import turnosVehiculos.model.util.ModelUtil;

/**
 * Session Bean implementation class ManagerRequisitos
 */
@Stateless
@LocalBean
public class ManagerRequisitos {

	@PersistenceContext(unitName = "turnosVehiculosDS")
	private EntityManager em;

	public ManagerRequisitos() {
	}

	// REQUISITOS
	public List<Requisito> findAllRequisitos() {
		Query q;
		List<Requisito> listado;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM Requisito o ";
		q = em.createQuery(sentenciaSQL);
		listado = q.getResultList();
		return listado;
	}

	public Requisito findRequisitoById(String id_requisito) {
		Requisito r = em.find(Requisito.class, id_requisito);
		return r;
	}

	public void registrarNuevoRequisito(String id_requisito, String requisito) throws Exception {
		if (ModelUtil.isEmpty(id_requisito + ""))
			throw new Exception("Debe especificar un id del requisito.");
		if (ModelUtil.isEmpty(requisito + ""))
			throw new Exception("Debe especificar el requisito.");

		Requisito u = findRequisitoById(id_requisito);
		if (u != null)
			throw new Exception("Ya existe un requisito " + id_requisito);
		u = new Requisito();
		u.setIdRequisito(id_requisito);
		u.setRequisito(requisito);
		em.persist(u);
	}

	public void eliminarRequisito(String id_requisito) throws Exception {
		Requisito r = findRequisitoById(id_requisito);
		if (r == null)
			throw new Exception("No existe el requisito " + id_requisito);
		em.remove(r);
	}

	public void editarRequisito(String id_requisito, String requisito) throws Exception {
		Requisito r = findRequisitoById(id_requisito);
		if (r == null)
			throw new Exception("No existe el requisito");
		r.setIdRequisito(id_requisito);
		r.setRequisito(requisito);

		em.merge(r);
	}

	// GENERAR TURNOS
	// **ingresar datos cliente

	public Cliente findClienteByCedula(String cedula_cli) {
		Cliente cl = em.find(Cliente.class, cedula_cli);
		return cl;
	}

	public Vehiculo findVehiculoByPlaca(String placa) {
		Vehiculo ve = em.find(Vehiculo.class, placa);
		return ve;
	}

	public EstadoRevision findRevisionByEstado(int codigo_estado) {
		EstadoRevision esta = em.find(EstadoRevision.class, codigo_estado);
		return esta;
	}

	public Turno findTurnoById(int id_turno) {
		Turno tur = em.find(Turno.class, id_turno);
		return tur;
	}

	public void ingresarCliente(String cedula_cli, String nombres_cli, String apellidos_cli, String placa_cli)
			throws Exception {
		if (ModelUtil.isEmpty(cedula_cli + ""))
			throw new Exception("Debe especificar la cedula.");
		if (ModelUtil.isEmpty(nombres_cli + ""))
			throw new Exception("Debe especificar los nombres.");
		if (ModelUtil.isEmpty(apellidos_cli + ""))
			throw new Exception("Debe especificar los apellidos.");
		if (ModelUtil.isEmpty(placa_cli + ""))
			throw new Exception("Debe especificar la placa del vehiculo.");

		Cliente cl = findClienteByCedula(cedula_cli);
		if (cl != null)
			System.out.println("ya esta");

		cl = new Cliente();
		cl.setCedulaCli(cedula_cli);
		cl.setNombresCli(nombres_cli);
		cl.setApellidosCli(apellidos_cli);
		Vehiculo ve = findVehiculoByPlaca(placa_cli);
		if (ve != null) {
			cl.setPlaca(placa_cli);
		}
		em.persist(cl);
	}

	public void GenerarTurno() {
		Calendar calendar = Calendar.getInstance();
		Turno turnoActual = findTurnoById(consultarUltimoTurno());
		calendar.setTime(turnoActual.getFechaTurno());
		calendar.add(calendar.MINUTE, 30);
		Timestamp tm = new Timestamp(calendar.getTimeInMillis());

		Turno tu = new Turno();

		tu.setFechaTurno(tm);
		tu.setTipoTramite("REVISIÓN VEHICULAR");
		em.persist(tu);
	}

	public void Asignacion(String cedula_cli, String placa_cli) {
		Asignacion as = new Asignacion();

		Cliente cl = findClienteByCedula(cedula_cli);
		if (cl != null)
			as.setCliente(cl);
		Turno tur = findTurnoById(consultarUltimoTurno());
		as.setTurno(tur);
		Vehiculo ve = findVehiculoByPlaca(placa_cli);
		if (ve != null)
			as.setVehiculo(ve);
		EstadoRevision estado = findRevisionByEstado(100);
		as.setEstadoRevision(estado);

		em.persist(as);

	}

	public int consultarUltimoTurno() {
		Query q;
		int listado;
		String sentenciaSQL;
		sentenciaSQL = "SELECT MAX(o.idTurno) FROM Turno o ";
		q = em.createQuery(sentenciaSQL);
		listado = (int) q.getSingleResult();
		return listado;
	}

	public Date sumarRestarHorasFecha(Date fecha, int horas) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.HOUR, horas); // numero de horas a añadir, o
											// restar en caso de horas<0
		return calendar.getTime(); // Devuelve el objeto Date con las nuevas
									// horas añadidas

	}

	// ASIGNACIONES

	public Asignacion findAsigById(int id_asigna) {
		Asignacion as = em.find(Asignacion.class, id_asigna);
		return as;
	}

	public Personal findPersonalByCedula(String cedula_oper) {
		Personal as = em.find(Personal.class, cedula_oper);
		return as;
	}

	public List<Asignacion> findAllAsignaciones() {
		Query q;
		List<Asignacion> listado;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM Asignacion o ";
		q = em.createQuery(sentenciaSQL);
		listado = q.getResultList();
		return listado;
	}

	public List<Personal> findAllOperadores() {
		Query q;
		List<Personal> listado;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM Personal o WHERE o.Rol.idRol='OPE02' ";
		q = em.createQuery(sentenciaSQL);
		listado = q.getResultList();
		return listado;
	}

	public void editarAsigna(int id_asignacion, String cedula_oper) throws Exception {
		Asignacion as = findAsigById(id_asignacion);
		if (as == null)
			throw new Exception("No existe el requisito");
		Personal pe = findPersonalByCedula(cedula_oper);
		if (pe != null)
			as.setPersonal(pe);

		em.merge(as);
	}

	// REALIZAR REVISION

	public void editarEstadoRevision(int id_asignacion, int codigo_EstadoAsignacion) throws Exception {
		Asignacion as = findAsigById(id_asignacion);
		if (as == null)
			throw new Exception("No existe la asignacion");
		EstadoRevision es = findRevisionByEstado(codigo_EstadoAsignacion);
		as.setEstadoRevision(es);

		em.merge(as);
	}

	public List<EstadoRevision> findAllEstados() {
		Query q;
		List<EstadoRevision> listado;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM EstadoRevision o";
		q = em.createQuery(sentenciaSQL);
		listado = q.getResultList();
		return listado;

	}

	public List<Turno> findAllTurnos() {
		Query q;
		List<Turno> listado;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM Turno o";
		q = em.createQuery(sentenciaSQL);
		listado = q.getResultList();
		return listado;
	}

	// comprobar placa vehiculo

	public Boolean comprobarPlaca(String placa) {
		Vehiculo vehiculo = findVehiculoByPlaca(placa);
		if (vehiculo != null) {
			return true;
		}
		return false;
	}

	// consultar estado del vehiculo

	public int consultar(String placa) {
		Query q;
		int listado;
		String sentenciaSQL;
		sentenciaSQL = "select MAX(o.idAsignacion) from Asignacion o where o.Vehiculo.placa ='" + placa + "'";
		q = em.createQuery(sentenciaSQL);
		listado = (int) q.getSingleResult();
		return listado;
	}
}
