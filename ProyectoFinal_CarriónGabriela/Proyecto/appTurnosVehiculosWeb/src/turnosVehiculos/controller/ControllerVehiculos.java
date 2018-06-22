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
import turnosVehiculos.manager.ManagerVehiculos;
import turnosVehiculos.model.entities.Personal;
import turnosVehiculos.model.entities.Vehiculo;
import turnosVehiculos.view.util.JSFUtil;

@ManagedBean
@SessionScoped
public class ControllerVehiculos {

	private String placa;
	private int anio;
	private String color;
	private String marca;
	private String nro_motor;
	private String nro_chasis;
	private List<Vehiculo> lista;

	@EJB
	private ManagerVehiculos managerVehiculos;

	@PostConstruct
	public void iniciar() {
		lista = managerVehiculos.findAllVehiculos();
	}

	public String actionRegistraVehiculo() {
		try {

			managerVehiculos.registrarNuevoVehiculo(placa, anio, color, marca, nro_motor, nro_chasis);
			lista = managerVehiculos.findAllVehiculos();
			JSFUtil.crearMensajeInfo("Nuevo vehiculo ingresado.");
			return "MenuAdmin";
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public void actionListenerEliminarVe(String placa) {
		try {
			managerVehiculos.eliminarVehiculo(placa);

			lista = managerVehiculos.findAllVehiculos();
			JSFUtil.crearMensajeInfo("Vehiculo " + placa + " eliminado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionListenerCargarVe(Vehiculo vehiculo) {
		placa = vehiculo.getPlaca();
		anio = vehiculo.getAnio();
		color = vehiculo.getColor();
		marca = vehiculo.getMarca();
		nro_motor = vehiculo.getNroMotor();
		nro_chasis = vehiculo.getNroChasis();
	}

	public void actionListenerActualizarVehiculo() {
		try {
			managerVehiculos.editarVehiculo(placa, anio, color, marca, nro_motor, nro_chasis);
			lista = managerVehiculos.findAllVehiculos();
			JSFUtil.crearMensajeInfo("Actualización correcta.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	// ---REPORTES
	public String actionReporteVehiculos() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		/*
		 * parametros.put("p_titulo_principal",p_titulo_principal);
		 * parametros.put("p_titulo",p_titulo);
		 */ FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String ruta = servletContext.getRealPath("Administrador/vehiculos.jasper");
		System.out.println(ruta);
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename=ReporteVehiculos.pdf");
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

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNro_motor() {
		return nro_motor;
	}

	public void setNro_motor(String nro_motor) {
		this.nro_motor = nro_motor;
	}

	public String getNro_chasis() {
		return nro_chasis;
	}

	public void setNro_chasis(String nro_chasis) {
		this.nro_chasis = nro_chasis;
	}

	public List<Vehiculo> getLista() {
		return lista;
	}

	public void setLista(List<Vehiculo> lista) {
		this.lista = lista;
	}

}
