package turnosVehiculos.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the rol database table.
 * 
 */
@Entity
@Table(name="rol")
@NamedQuery(name="Rol.findAll", query="SELECT r FROM Rol r")
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_rol", unique=true, nullable=false, length=5)
	private String idRol;

	@Column(length=20)
	private String rol;

	//bi-directional many-to-one association to Personal
	@OneToMany(mappedBy="rol")
	private List<Personal> personals;

	public Rol() {
	}

	public String getIdRol() {
		return this.idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public List<Personal> getPersonals() {
		return this.personals;
	}

	public void setPersonals(List<Personal> personals) {
		this.personals = personals;
	}

	public Personal addPersonal(Personal personal) {
		getPersonals().add(personal);
		personal.setRol(this);

		return personal;
	}

	public Personal removePersonal(Personal personal) {
		getPersonals().remove(personal);
		personal.setRol(null);

		return personal;
	}

}