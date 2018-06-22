package turnosVehiculos.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import turnosVehiculos.manager.ManagerRequisitos;
import turnosVehiculos.model.entities.Asignacion;
import turnosVehiculos.model.entities.EstadoRevision;
import turnosVehiculos.model.entities.Personal;
import turnosVehiculos.model.entities.Requisito;
import turnosVehiculos.model.entities.Turno;
import turnosVehiculos.view.util.JSFUtil;

@ManagedBean
@SessionScoped
public class ControllerRequisitos {

	private String cedula_cli;
	private String nombres_cli;
	private String apellidos_cli;
	private String placa_cli;

	private String id_requisito;
	private String requisito;
	private List<Requisito> lista;

	private List<Asignacion> listaAsigna;
	private int id_asigna;
	private String cedula_asig_cli;
	private String cedula_asig_ope;
	private int id_turno_asig;
	private String placa_asig;
	private int codigoEstado_asig;

	private String estadoPorPlaca;
	private String placaParaEstado;

	private List<Personal> listaOperadores;

	private List<EstadoRevision> listaEstados;

	private List<Turno> listaTurnos;

	@EJB
	private ManagerRequisitos managerRequisitos;

	@PostConstruct
	public void iniciar() {
		lista = managerRequisitos.findAllRequisitos();
		listaAsigna = managerRequisitos.findAllAsignaciones();
		// listaOperadores = managerRequisitos.findAllOperadores();
		listaEstados = managerRequisitos.findAllEstados();
		listaTurnos = managerRequisitos.findAllTurnos();
	}

	public String actionRegistrarRequisito() {
		try {
			managerRequisitos.registrarNuevoRequisito(id_requisito, requisito);
			lista = managerRequisitos.findAllRequisitos();

			JSFUtil.crearMensajeInfo("Nuevo requisito ingresado.");
			return "MenuOper";
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public void actionListenerEliminarReequisito(String id_requisito) {
		try {
			managerRequisitos.eliminarRequisito(id_requisito);

			lista = managerRequisitos.findAllRequisitos();
			JSFUtil.crearMensajeInfo("Requisito " + id_requisito + " eliminado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionListenerCargaRe(Requisito re) {
		id_requisito = re.getIdRequisito();
		requisito = re.getRequisito();

	}

	public void actionListenerActualizarRe() {
		try {
			managerRequisitos.editarRequisito(id_requisito, requisito);
			lista = managerRequisitos.findAllRequisitos();
			JSFUtil.crearMensajeInfo("Actualización correcta.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}

	}

	// generacion turnos

	public void actionListenerGenerarTurno() {
		try {
			if (managerRequisitos.comprobarPlaca(placa_cli) == true) {
				managerRequisitos.ingresarCliente(cedula_cli, nombres_cli, apellidos_cli, placa_cli);
				managerRequisitos.GenerarTurno();
				managerRequisitos.Asignacion(cedula_cli, placa_cli);

				/////////////////////////////////////////////////////////////////

				Map<String, Object> parametros = new HashMap<String, Object>();
				/*
				 * parametros.put("p_titulo_principal",p_titulo_principal);
				 * parametros.put("p_titulo",p_titulo);
				 */
				parametros.put("idTurn", managerRequisitos.consultarUltimoTurno());
				// System.out.println("dddd" + idTurn);
				FacesContext context = FacesContext.getCurrentInstance();
				ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
				String ruta = servletContext.getRealPath("Turno.jasper");
				System.out.println(ruta);
				HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
				response.addHeader("Content-disposition", "attachment;filename=TurnoAsignado.pdf");
				response.setContentType("application/pdf");
				try {
					Class.forName("org.postgresql.Driver");
					Connection connection = null;
					connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/turnosVehiculos",
							"postgres", "root");
					JasperPrint impresion = JasperFillManager.fillReport(ruta, parametros, connection);
					JasperExportManager.exportReportToPdfStream(impresion, response.getOutputStream());
					context.getApplication().getStateManager().saveView(context);
					System.out.println("turno generado.");
					context.responseComplete();
				} catch (Exception e) {
					JSFUtil.crearMensajeError(e.getMessage());
					e.printStackTrace();
				}

				////////////////////////////////////////////////////////////////

				JSFUtil.crearMensajeInfo("turno exitoso.");
			} else {
				JSFUtil.crearMensajeInfo("la placa del vehículo no existe.");
			}

		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}

	}

	// asignaciones

	public void cargarAsignacion(int id_asignacion) {
		Asignacion as = managerRequisitos.findAsigById(id_asigna);
		id_asigna = as.getIdAsignacion();
		cedula_asig_cli = as.getCliente().getCedulaCli();
		id_turno_asig = as.getTurno().getIdTurno();
		placa_asig = as.getVehiculo().getPlaca();
		codigoEstado_asig = as.getEstadoRevision().getCodEstadorevision();
	}

	public void actionActualizarAasignacion() {

		try {
			managerRequisitos.editarAsigna(id_asigna, cedula_asig_ope);
			listaAsigna = managerRequisitos.findAllAsignaciones();
			JSFUtil.crearMensajeInfo("Asignación correcta.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	// revisiones

	public void cargarAsignacionEstadoRev(int id_asignacion) {
		Asignacion as = managerRequisitos.findAsigById(id_asignacion);
		id_asigna = as.getIdAsignacion();
		codigoEstado_asig = as.getEstadoRevision().getCodEstadorevision();
	}

	public void actionEditarEstado() {
		System.out.println("ppppppp" + id_asigna);
		try {

			managerRequisitos.editarEstadoRevision(id_asigna, codigoEstado_asig);
			listaAsigna = managerRequisitos.findAllAsignaciones();
			JSFUtil.crearMensajeInfo("Asignación correcta.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	//////////////// 7
	////////////////
	///////////// 7
	////////////
	// consultar estado por placa

	public void actionConsultarEstado(String placa) {
		// int temp = managerRequisitos.consultar(placaParaEstado);
		// System.out.println("fffffffffffffffffff " + temp);

		Asignacion as = managerRequisitos.findAsigById(managerRequisitos.consultar(placa));
		if (as != null)
			// as.getEstadoRevision().getEstado();

			estadoPorPlaca = as.getEstadoRevision().getEstado();

		JSFUtil.crearMensajeInfo("No existe trámite con esa placa.");
	}

	// ---REPORTES
	public String actionReporteRequisitos() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		/*
		 * parametros.put("p_titulo_principal",p_titulo_principal);
		 * parametros.put("p_titulo",p_titulo);
		 */ FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String ruta = servletContext.getRealPath("Operador/Requisitos.jasper");
		System.out.println(ruta);
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename=ReporteRequisitos.pdf");
		response.setContentType("application/pdf");
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/turnosVehiculos", "postgres",
					"root");
			JasperPrint impresion = JasperFillManager.fillReport(ruta, parametros, connection);
			JasperExportManager.exportReportToPdfStream(impresion, response.getOutputStream());
			context.getApplication().getStateManager().saveView(context);
			System.out.println("reporte generado.");
			context.responseComplete();
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	// ---REPORTE PDF PARA TURNO
	public String actionGenerarTurno(int idTurn) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		/*
		 * parametros.put("p_titulo_principal",p_titulo_principal);
		 * parametros.put("p_titulo",p_titulo);
		 */
		parametros.put("idTurn", idTurn);
		System.out.println("dddd" + idTurn);
		FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String ruta = servletContext.getRealPath("Turno.jasper");
		System.out.println(ruta);
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename=TurnoAsignado.pdf");
		response.setContentType("application/pdf");
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/turnosVehiculos", "postgres",
					"root");
			JasperPrint impresion = JasperFillManager.fillReport(ruta, parametros, connection);
			JasperExportManager.exportReportToPdfStream(impresion, response.getOutputStream());
			context.getApplication().getStateManager().saveView(context);
			System.out.println("turno generado.");
			context.responseComplete();
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public String getId_requisito() {
		return id_requisito;
	}

	public void setId_requisito(String id_requisito) {
		this.id_requisito = id_requisito;
	}

	public String getRequisito() {
		return requisito;
	}

	public void setRequisito(String requisito) {
		this.requisito = requisito;
	}

	public List<Requisito> getLista() {
		return lista;
	}

	public void setLista(List<Requisito> lista) {
		this.lista = lista;
	}

	public String getCedula_cli() {
		return cedula_cli;
	}

	public void setCedula_cli(String cedula_cli) {
		this.cedula_cli = cedula_cli;
	}

	public String getNombres_cli() {
		return nombres_cli;
	}

	public void setNombres_cli(String nombres_cli) {
		this.nombres_cli = nombres_cli;
	}

	public String getApellidos_cli() {
		return apellidos_cli;
	}

	public void setApellidos_cli(String apellidos_cli) {
		this.apellidos_cli = apellidos_cli;
	}

	public String getPlaca_cli() {
		return placa_cli;
	}

	public void setPlaca_cli(String placa_cli) {
		this.placa_cli = placa_cli;
	}

	public int getId_asigna() {
		return id_asigna;
	}

	public void setId_asigna(int id_asigna) {
		this.id_asigna = id_asigna;
	}

	public int getCodigoEstado_asig() {
		return codigoEstado_asig;
	}

	public void setCodigoEstado_asig(int codigoEstado_asig) {
		this.codigoEstado_asig = codigoEstado_asig;
	}

	public String getCedula_asig_cli() {
		return cedula_asig_cli;
	}

	public void setCedula_asig_cli(String cedula_asig_cli) {
		this.cedula_asig_cli = cedula_asig_cli;
	}

	public String getCedula_asig_ope() {
		return cedula_asig_ope;
	}

	public void setCedula_asig_ope(String cedula_asig_ope) {
		this.cedula_asig_ope = cedula_asig_ope;
	}

	public int getId_turno_asig() {
		return id_turno_asig;
	}

	public void setId_turno_asig(int id_turno_asig) {
		this.id_turno_asig = id_turno_asig;
	}

	public String getPlaca_asig() {
		return placa_asig;
	}

	public void setPlaca_asig(String placa_asig) {
		this.placa_asig = placa_asig;
	}

	public List<Personal> getListaOperadores() {
		return listaOperadores;
	}

	public void setListaOperadores(List<Personal> listaOperadores) {
		this.listaOperadores = listaOperadores;
	}

	public List<EstadoRevision> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<EstadoRevision> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public List<Asignacion> getListaAsigna() {
		return listaAsigna;
	}

	public void setListaAsigna(List<Asignacion> listaAsigna) {
		this.listaAsigna = listaAsigna;
	}

	public List<Turno> getListaTurnos() {
		return listaTurnos;
	}

	public void setListaTurnos(List<Turno> listaTurnos) {
		this.listaTurnos = listaTurnos;
	}

	public String getEstadoPorPlaca() {
		return estadoPorPlaca;
	}

	public void setEstadoPorPlaca(String estadoPorPlaca) {
		this.estadoPorPlaca = estadoPorPlaca;
	}

	public String getPlacaParaEstado() {
		return placaParaEstado;
	}

	public void setPlacaParaEstado(String placaParaEstado) {
		this.placaParaEstado = placaParaEstado;
	}

}
