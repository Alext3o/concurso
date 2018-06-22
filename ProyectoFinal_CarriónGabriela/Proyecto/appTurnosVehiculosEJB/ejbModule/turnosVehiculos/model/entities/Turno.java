package turnosVehiculos.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the turno database table.
 * 
 */
@Entity
@Table(name = "turno")
@NamedQuery(name = "Turno.findAll", query = "SELECT t FROM Turno t")
public class Turno implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TURNO_IDTURNO_GENERATOR", sequenceName = "SEQ_TURNO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TURNO_IDTURNO_GENERATOR")
	@Column(name = "id_turno", unique = true, nullable = false)
	private Integer idTurno;

	@Column(name = "fecha_turno")
	private Timestamp fechaTurno;

	@Column(name = "tipo_tramite", length = 40)
	private String tipoTramite;

	// bi-directional many-to-one association to Asignacion
	@OneToMany(mappedBy = "turno")
	private List<Asignacion> asignacions;

	public Turno() {
	}

	public Integer getIdTurno() {
		return this.idTurno;
	}

	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}

	public Timestamp getFechaTurno() {
		return this.fechaTurno;
	}

	public void setFechaTurno(Timestamp fechaTurno) {
		this.fechaTurno = fechaTurno;
	}

	public String getTipoTramite() {
		return this.tipoTramite;
	}

	public void setTipoTramite(String tipoTramite) {
		this.tipoTramite = tipoTramite;
	}

	public List<Asignacion> getAsignacions() {
		return this.asignacions;
	}

	public void setAsignacions(List<Asignacion> asignacions) {
		this.asignacions = asignacions;
	}

	public Asignacion addAsignacion(Asignacion asignacion) {
		getAsignacions().add(asignacion);
		asignacion.setTurno(this);

		return asignacion;
	}

	public Asignacion removeAsignacion(Asignacion asignacion) {
		getAsignacions().remove(asignacion);
		asignacion.setTurno(null);

		return asignacion;
	}

}