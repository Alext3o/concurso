package turnosVehiculos.model.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the revision database table.
 * 
 */
@Embeddable
public class RevisionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_asignacion", insertable=false, updatable=false, unique=true, nullable=false)
	private Integer idAsignacion;

	@Column(name="id_requisito", insertable=false, updatable=false, unique=true, nullable=false, length=5)
	private String idRequisito;

	public RevisionPK() {
	}
	public Integer getIdAsignacion() {
		return this.idAsignacion;
	}
	public void setIdAsignacion(Integer idAsignacion) {
		this.idAsignacion = idAsignacion;
	}
	public String getIdRequisito() {
		return this.idRequisito;
	}
	public void setIdRequisito(String idRequisito) {
		this.idRequisito = idRequisito;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RevisionPK)) {
			return false;
		}
		RevisionPK castOther = (RevisionPK)other;
		return 
			this.idAsignacion.equals(castOther.idAsignacion)
			&& this.idRequisito.equals(castOther.idRequisito);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idAsignacion.hashCode();
		hash = hash * prime + this.idRequisito.hashCode();
		
		return hash;
	}
}