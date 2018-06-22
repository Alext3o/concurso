package turnosVehiculos.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the requisitos database table.
 * 
 */
@Entity
@Table(name="requisitos")
@NamedQuery(name="Requisito.findAll", query="SELECT r FROM Requisito r")
public class Requisito implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_requisito", unique=true, nullable=false, length=5)
	private String idRequisito;

	@Column(length=30)
	private String requisito;

	//bi-directional many-to-many association to Asignacion
	@ManyToMany
	@JoinTable(
		name="revision"
		, joinColumns={
			@JoinColumn(name="id_requisito", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_asignacion", nullable=false)
			}
		)
	private List<Asignacion> asignacions;

	public Requisito() {
	}

	public String getIdRequisito() {
		return this.idRequisito;
	}

	public void setIdRequisito(String idRequisito) {
		this.idRequisito = idRequisito;
	}

	public String getRequisito() {
		return this.requisito;
	}

	public void setRequisito(String requisito) {
		this.requisito = requisito;
	}

	public List<Asignacion> getAsignacions() {
		return this.asignacions;
	}

	public void setAsignacions(List<Asignacion> asignacions) {
		this.asignacions = asignacions;
	}

}