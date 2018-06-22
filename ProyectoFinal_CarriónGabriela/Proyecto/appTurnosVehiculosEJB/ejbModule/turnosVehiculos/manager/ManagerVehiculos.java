package turnosVehiculos.manager;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import turnosVehiculos.model.entities.Vehiculo;
import turnosVehiculos.model.util.ModelUtil;

/**
 * Session Bean implementation class ManagerVehiculos
 */
@Stateless
@LocalBean
public class ManagerVehiculos {

	@PersistenceContext(unitName = "turnosVehiculosDS")
	private EntityManager em;

	public ManagerVehiculos() {
	}

	public List<Vehiculo> findAllVehiculos() {
		Query q;
		List<Vehiculo> listado;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM Vehiculo o ";
		q = em.createQuery(sentenciaSQL);
		listado = q.getResultList();
		return listado;
	}

	public Vehiculo findVehiculoByPlaca(String placa) {
		Vehiculo p = em.find(Vehiculo.class, placa);
		return p;
	}

	public void registrarNuevoVehiculo(String placa, int anio, String color, String marca, String nro_motor,
			String nro_chasis) throws Exception {
		if (ModelUtil.isEmpty(placa))
			throw new Exception("Debe especificar una placa del vehiculo.");
		if (ModelUtil.isEmpty(anio + ""))
			throw new Exception("Debe especificar el anio del vehiculo.");
		if (ModelUtil.isEmpty(color))
			throw new Exception("Debe especificar el color del vehiculo.");
		if (ModelUtil.isEmpty(marca))
			throw new Exception("Debe especificar una marca de vehiculo.");
		if (ModelUtil.isEmpty(nro_motor))
			throw new Exception("Debe especificar un nro de motor válido.");
		if (ModelUtil.isEmpty(nro_chasis))
			throw new Exception("Debe especificar nro de chasis válido.");

		Vehiculo u = findVehiculoByPlaca(placa);
		if (u != null)
			throw new Exception("Ya existe un vehiculo " + placa);
		u = new Vehiculo();
		u.setPlaca(placa);
		u.setAnio(anio);
		u.setColor(color);
		u.setMarca(marca);
		u.setNroMotor(nro_motor);
		u.setNroChasis(nro_chasis);
		em.persist(u);
	}

	public void eliminarVehiculo(String placa) throws Exception {
		Vehiculo u = findVehiculoByPlaca(placa);
		if (u == null)
			throw new Exception("No existe el Usuario " + placa);
		em.remove(u);

	}

	public void editarVehiculo(String placa, int anio, String color, String marca, String nro_motor, String nro_chasis)
			throws Exception {
		Vehiculo u = findVehiculoByPlaca(placa);
		if (u == null)
			throw new Exception("No existe el vehiculo especificado.");

		u.setPlaca(placa);
		u.setAnio(anio);
		u.setColor(color);
		u.setMarca(marca);
		u.setNroMotor(nro_motor);
		u.setNroChasis(nro_chasis);

		em.merge(u);
	}

}
