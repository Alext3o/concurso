package turnosVehiculos.manager;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import turnosVehiculos.model.entities.Personal;
import turnosVehiculos.model.entities.Rol;
import turnosVehiculos.model.util.ModelUtil;

/**
 * Session Bean implementation class ManagerPersonal
 */
@Stateless
@LocalBean
public class ManagerPersonal {

	@PersistenceContext(unitName = "turnosVehiculosDS")
	private EntityManager em;

	public ManagerPersonal() {
	}

	// login del sistema

	public boolean comprobarUsuario(String cedula_per, String contrasenia) throws Exception {
		Personal p = em.find(Personal.class, cedula_per);
		if (p == null)
			throw new Exception("No existe el usuario " + cedula_per);
		if (p.getContrasenia().equals(contrasenia))
			return true;
		throw new Exception("Contraseña no válida.");
	}

	// metodos rol
	public List<Rol> findAllRoles() {
		Query q;
		List<Rol> listaRol;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM Rol o ";
		q = em.createQuery(sentenciaSQL);
		listaRol = q.getResultList();
		return listaRol;
	}

	public Rol findRolById(String id_rol) {
		Rol r = em.find(Rol.class, id_rol);
		return r;

	}

	// metodos para el crud de personal
	public List<Personal> findAllUsuarios() {
		Query q;
		List<Personal> listado;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM Personal o ";
		q = em.createQuery(sentenciaSQL);
		listado = q.getResultList();
		return listado;
	}

	public Personal findUsuarioByCedula(String cedula_per) {
		Personal p = em.find(Personal.class, cedula_per);
		return p;
	}

	public void registrarNuevoUsuario(String cedula_per, String nombres_per, String apellidos_per, String direccion_per,
			String telefono_per, String correo_per, String id_rol, String contrasenia) throws Exception {
		if (ModelUtil.isEmpty(cedula_per))
			throw new Exception("Debe especificar una cedula de usuario.");
		if (ModelUtil.isEmpty(nombres_per))
			throw new Exception("Debe especificar los nombres del usuario.");
		if (ModelUtil.isEmpty(apellidos_per))
			throw new Exception("Debe especificar los apellidos del usuario.");
		if (ModelUtil.isEmpty(direccion_per))
			throw new Exception("Debe especificar una direccion de usuario.");
		if (ModelUtil.isEmpty(telefono_per))
			throw new Exception("Debe especificar un telefono de usuario.");
		if (ModelUtil.isEmpty(correo_per))
			throw new Exception("Debe especificar un correo valido.");
		if (ModelUtil.isEmpty(contrasenia))
			throw new Exception("Debe especificar una contrasenia de usuario.");

		Personal u = findUsuarioByCedula(cedula_per);
		if (u != null)
			throw new Exception("Ya existe un usuario " + cedula_per);
		u = new Personal();
		u.setCedulaPer(cedula_per);
		u.setNombresPer(nombres_per);
		u.setApellidosPer(apellidos_per);
		u.setDireccionPer(direccion_per);
		u.setTelefonoPer(telefono_per);
		u.setCorreoPer(correo_per);
		Rol r = findRolById(id_rol);
		u.setRol(r);
		u.setContrasenia(contrasenia);
		em.persist(u);
	}

	public void eliminarUsuario(String cedula_per) throws Exception {
		Personal u = findUsuarioByCedula(cedula_per);
		if (u == null)
			throw new Exception("No existe el Usuario " + cedula_per);
		em.remove(u);

	}

	public void editarUsuario(String cedula_per, String nombres_per, String apellidos_per, String direccion_per,
			String telefono_per, String correo_per, String id_rol, String contrasenia) throws Exception {
		Personal u = findUsuarioByCedula(cedula_per);
		if (u == null)
			throw new Exception("No existe el automovil especificado.");

		u.setCedulaPer(cedula_per);
		u.setNombresPer(nombres_per);
		u.setApellidosPer(apellidos_per);
		u.setDireccionPer(direccion_per);
		u.setTelefonoPer(telefono_per);
		u.setCorreoPer(correo_per);
		Rol r = findRolById(id_rol);
		u.setRol(r);
		u.setContrasenia(contrasenia);
		em.merge(u);
	}

}
