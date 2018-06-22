package turnosVehiculos.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estado_revision database table.
 * 
 */
@Entity
@Table(name="estado_revision")
@NamedQuery(name="EstadoRevision.findAll", query="SELECT e FROM EstadoRevision e")
public class EstadoRevision implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="cod_estadorevision", unique=true, nullable=false)
	private Integer codEstadorevision;

	@Column(length=25)
	private String estado;

	//bi-directional many-to-one association to Asignacion
	@OneToMany(mappedBy="estadoRevision")
	private List<Asignacion> asignacions;

	public EstadoRevision() {
	}

	public Integer getCodEstadorevision() {
		return this.codEstadorevision;
	}

	public void setCodEstadorevision(Integer codEstadorevision) {
		this.codEstadorevision = codEstadorevision;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Asignacion> getAsignacions() {
		return this.asignacions;
	}

	public void setAsignacions(List<Asignacion> asignacions) {
		this.asignacions = asignacions;
	}

	public Asignacion addAsignacion(Asignacion asignacion) {
		getAsignacions().add(asignacion);
		asignacion.setEstadoRevision(this);

		return asignacion;
	}

	public Asignacion removeAsignacion(Asignacion asignacion) {
		getAsignacions().remove(asignacion);
		asignacion.setEstadoRevision(null);

		return asignacion;
	}

}