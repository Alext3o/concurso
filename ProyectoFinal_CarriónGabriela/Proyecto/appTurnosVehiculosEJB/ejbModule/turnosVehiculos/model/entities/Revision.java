package turnosVehiculos.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the revision database table.
 * 
 */
@Entity
@Table(name="revision")
@NamedQuery(name="Revision.findAll", query="SELECT r FROM Revision r")
public class Revision implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RevisionPK id;

	//bi-directional many-to-one association to Asignacion
	@ManyToOne
	@JoinColumn(name="id_asignacion", nullable=false, insertable=false, updatable=false)
	private Asignacion asignacion;

	public Revision() {
	}

	public RevisionPK getId() {
		return this.id;
	}

	public void setId(RevisionPK id) {
		this.id = id;
	}

	public Asignacion getAsignacion() {
		return this.asignacion;
	}

	public void setAsignacion(Asignacion asignacion) {
		this.asignacion = asignacion;
	}

}