package turnosVehiculos.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the asignacion database table.
 * 
 */
@Entity
@Table(name = "asignacion")
@NamedQuery(name = "Asignacion.findAll", query = "SELECT a FROM Asignacion a")
public class Asignacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ASIGNACION_IDASIGNACION_GENERATOR", sequenceName = "SEQ_ASIGNACION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASIGNACION_IDASIGNACION_GENERATOR")
	@Column(name = "id_asignacion", unique = true, nullable = false)
	private Integer idAsignacion;

	// bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name = "cedula_cli")
	private Cliente cliente;

	// bi-directional many-to-one association to EstadoRevision
	@ManyToOne
	@JoinColumn(name = "cod_estadorevision")
	private EstadoRevision estadoRevision;

	// bi-directional many-to-one association to Personal
	@ManyToOne
	@JoinColumn(name = "cedula_per")
	private Personal personal;

	// bi-directional many-to-one association to Turno
	@ManyToOne
	@JoinColumn(name = "id_turno")
	private Turno turno;

	// bi-directional many-to-one association to Vehiculo
	@ManyToOne
	@JoinColumn(name = "placa")
	private Vehiculo vehiculo;

	// bi-directional many-to-many association to Requisito
	@ManyToMany(mappedBy = "asignacions")
	private List<Requisito> requisitos;

	// bi-directional many-to-one association to Revision
	@OneToMany(mappedBy = "asignacion")
	private List<Revision> revisions;

	public Asignacion() {
	}

	public Integer getIdAsignacion() {
		return this.idAsignacion;
	}

	public void setIdAsignacion(Integer idAsignacion) {
		this.idAsignacion = idAsignacion;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public EstadoRevision getEstadoRevision() {
		return this.estadoRevision;
	}

	public void setEstadoRevision(EstadoRevision estadoRevision) {
		this.estadoRevision = estadoRevision;
	}

	public Personal getPersonal() {
		return this.personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public Turno getTurno() {
		return this.turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public Vehiculo getVehiculo() {
		return this.vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public List<Requisito> getRequisitos() {
		return this.requisitos;
	}

	public void setRequisitos(List<Requisito> requisitos) {
		this.requisitos = requisitos;
	}

	public List<Revision> getRevisions() {
		return this.revisions;
	}

	public void setRevisions(List<Revision> revisions) {
		this.revisions = revisions;
	}

	public Revision addRevision(Revision revision) {
		getRevisions().add(revision);
		revision.setAsignacion(this);

		return revision;
	}

	public Revision removeRevision(Revision revision) {
		getRevisions().remove(revision);
		revision.setAsignacion(null);

		return revision;
	}

}