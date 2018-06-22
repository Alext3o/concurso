package turnosVehiculos.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the personal database table.
 * 
 */
@Entity
@Table(name="personal")
@NamedQuery(name="Personal.findAll", query="SELECT p FROM Personal p")
public class Personal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="cedula_per", unique=true, nullable=false, length=10)
	private String cedulaPer;

	@Column(name="apellidos_per", length=50)
	private String apellidosPer;

	@Column(length=12)
	private String contrasenia;

	@Column(name="correo_per", length=40)
	private String correoPer;

	@Column(name="direccion_per", length=50)
	private String direccionPer;

	@Column(name="nombres_per", length=40)
	private String nombresPer;

	@Column(name="telefono_per", length=15)
	private String telefonoPer;

	//bi-directional many-to-one association to Asignacion
	@OneToMany(mappedBy="personal")
	private List<Asignacion> asignacions;

	//bi-directional many-to-one association to Rol
	@ManyToOne
	@JoinColumn(name="id_rol")
	private Rol rol;

	public Personal() {
	}

	public String getCedulaPer() {
		return this.cedulaPer;
	}

	public void setCedulaPer(String cedulaPer) {
		this.cedulaPer = cedulaPer;
	}

	public String getApellidosPer() {
		return this.apellidosPer;
	}

	public void setApellidosPer(String apellidosPer) {
		this.apellidosPer = apellidosPer;
	}

	public String getContrasenia() {
		return this.contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getCorreoPer() {
		return this.correoPer;
	}

	public void setCorreoPer(String correoPer) {
		this.correoPer = correoPer;
	}

	public String getDireccionPer() {
		return this.direccionPer;
	}

	public void setDireccionPer(String direccionPer) {
		this.direccionPer = direccionPer;
	}

	public String getNombresPer() {
		return this.nombresPer;
	}

	public void setNombresPer(String nombresPer) {
		this.nombresPer = nombresPer;
	}

	public String getTelefonoPer() {
		return this.telefonoPer;
	}

	public void setTelefonoPer(String telefonoPer) {
		this.telefonoPer = telefonoPer;
	}

	public List<Asignacion> getAsignacions() {
		return this.asignacions;
	}

	public void setAsignacions(List<Asignacion> asignacions) {
		this.asignacions = asignacions;
	}

	public Asignacion addAsignacion(Asignacion asignacion) {
		getAsignacions().add(asignacion);
		asignacion.setPersonal(this);

		return asignacion;
	}

	public Asignacion removeAsignacion(Asignacion asignacion) {
		getAsignacions().remove(asignacion);
		asignacion.setPersonal(null);

		return asignacion;
	}

	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}