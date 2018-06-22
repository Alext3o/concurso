package turnosVehiculos.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@Table(name="cliente")
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="cedula_cli", unique=true, nullable=false, length=10)
	private String cedulaCli;

	@Column(name="apellidos_cli", length=50)
	private String apellidosCli;

	@Column(name="nombres_cli", length=40)
	private String nombresCli;

	@Column(length=7)
	private String placa;

	//bi-directional many-to-one association to Asignacion
	@OneToMany(mappedBy="cliente")
	private List<Asignacion> asignacions;

	public Cliente() {
	}

	public String getCedulaCli() {
		return this.cedulaCli;
	}

	public void setCedulaCli(String cedulaCli) {
		this.cedulaCli = cedulaCli;
	}

	public String getApellidosCli() {
		return this.apellidosCli;
	}

	public void setApellidosCli(String apellidosCli) {
		this.apellidosCli = apellidosCli;
	}

	public String getNombresCli() {
		return this.nombresCli;
	}

	public void setNombresCli(String nombresCli) {
		this.nombresCli = nombresCli;
	}

	public String getPlaca() {
		return this.placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public List<Asignacion> getAsignacions() {
		return this.asignacions;
	}

	public void setAsignacions(List<Asignacion> asignacions) {
		this.asignacions = asignacions;
	}

	public Asignacion addAsignacion(Asignacion asignacion) {
		getAsignacions().add(asignacion);
		asignacion.setCliente(this);

		return asignacion;
	}

	public Asignacion removeAsignacion(Asignacion asignacion) {
		getAsignacions().remove(asignacion);
		asignacion.setCliente(null);

		return asignacion;
	}

}