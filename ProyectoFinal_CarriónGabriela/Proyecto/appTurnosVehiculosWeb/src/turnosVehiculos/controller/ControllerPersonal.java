package turnosVehiculos.controller;

import java.io.IOException;
import java.security.Permissions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import turnosVehiculos.model.util.ModelUtil;
import turnosVehiculos.manager.ManagerPersonal;
import turnosVehiculos.model.entities.Personal;
import turnosVehiculos.model.entities.Rol;
import turnosVehiculos.view.util.JSFUtil;

@ManagedBean
@SessionScoped

public class ControllerPersonal {

	private String cedula_per;
	private String nombres_per;
	private String apellidos_per;
	private String direccion_per;
	private String telefono_per;
	private String correo_per;
	private List<Personal> lista;
	private boolean confirmadoLogin;
	
	private String cedula_login;

	private List<Rol> listaRol;
	private String id_rol;
	private String contrasenia;

	@EJB
	private ManagerPersonal managerPersonal;

	@PostConstruct
	public void cargar() {
		lista = managerPersonal.findAllUsuarios();
		listaRol = managerPersonal.findAllRoles();
	}

	// ---LOGIN
	public String actionLogin() {
		try {
			confirmadoLogin = managerPersonal.comprobarUsuario(cedula_login, contrasenia);
			Personal p = managerPersonal.findUsuarioByCedula(cedula_login);
			JSFUtil.crearMensajeInfo("Login correcto");
			// verificamos si el acceso es con admin:
			if (p.getRol().getRol().equals("ADMINISTRADOR")) {
				return "Administrador/MenuAdmin";
			}
			return "Operador/MenuOper";
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public String actionSalir() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login?faces-redirect=true";
	}

	public void actionComprobarLogin() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			String path = ec.getRequestPathInfo();
			System.out.println("getRequestContextPath(): " + ec.getRequestContextPath());
			System.out.println("getRequestPathInfo(): " + path);
			System.out.println("Cédula personal: " + cedula_login);
			if (path.equals("/login.xhtml"))
				return;
			if (ModelUtil.isEmpty(cedula_login))
				ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
			if (!confirmadoLogin) {
				ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
			} else {

				Personal p = managerPersonal.findUsuarioByCedula(cedula_login);
				if (p.getRol().getRol().equals("ADMINISTRADOR")) {
					if (!path.contains("/Administrador/"))
						ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
					else
						return;
				}
				if (!path.contains("/Operador/"))
					ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// --PERSONAL
	public String actionRegistraPersonal() {
		try {

			managerPersonal.registrarNuevoUsuario(cedula_per, nombres_per, apellidos_per, direccion_per, telefono_per,
					correo_per, id_rol, contrasenia);
			lista = managerPersonal.findAllUsuarios();
			JSFUtil.crearMensajeInfo("Nuevo usuario registrado.");
			return "MenuAdmin";
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public void actionListenerEliminar(String cedula_per) {
		try {
			managerPersonal.eliminarUsuario(cedula_per);

			lista = managerPersonal.findAllUsuarios();
			JSFUtil.crearMensajeInfo("Usuario " + cedula_per + " eliminado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionListenerCargar(Personal personal) {
		cedula_per = personal.getCedulaPer();
		nombres_per = personal.getNombresPer();
		apellidos_per = personal.getApellidosPer();
		direccion_per = personal.getDireccionPer();
		telefono_per = personal.getTelefonoPer();
		correo_per = personal.getCorreoPer();
		id_rol = personal.getRol().getIdRol();
		contrasenia = personal.getContrasenia();

	}

	public void actionListenerActualizarUsuario() {
		try {
			managerPersonal.editarUsuario(cedula_per, nombres_per, apellidos_per, direccion_per, telefono_per,
					correo_per, id_rol, contrasenia);
			lista = managerPersonal.findAllUsuarios();
			JSFUtil.crearMensajeInfo("Actualización correcta.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	// ---REPORTES
	public String actionReportePersonal() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		/*
		 * parametros.put("p_titulo_principal",p_titulo_principal);
		 * parametros.put("p_titulo",p_titulo);
		 */ FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String ruta = servletContext.getRealPath("Administrador/personal.jasper");
		System.out.println(ruta);
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename=ReportePersonal.pdf");
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

	public String getCedula_per() {
		return cedula_per;
	}

	public void setCedula_per(String cedula_per) {
		this.cedula_per = cedula_per;
	}

	public String getNombres_per() {
		return nombres_per;
	}

	public void setNombres_per(String nombres_per) {
		this.nombres_per = nombres_per;
	}

	public String getApellidos_per() {
		return apellidos_per;
	}

	public void setApellidos_per(String apellidos_per) {
		this.apellidos_per = apellidos_per;
	}

	public String getDireccion_per() {
		return direccion_per;
	}

	public void setDireccion_per(String direccion_per) {
		this.direccion_per = direccion_per;
	}

	public String getTelefono_per() {
		return telefono_per;
	}

	public void setTelefono_per(String telefono_per) {
		this.telefono_per = telefono_per;
	}

	public String getCorreo_per() {
		return correo_per;
	}

	public void setCorreo_per(String correo_per) {
		this.correo_per = correo_per;
	}

	public String getId_rol() {
		return id_rol;
	}

	public void setId_rol(String id_rol) {
		this.id_rol = id_rol;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public List<Personal> getLista() {
		return lista;
	}

	public void setLista(List<Personal> lista) {
		this.lista = lista;
	}

	public boolean isConfirmadoLogin() {
		return confirmadoLogin;
	}

	public void setConfirmadoLogin(boolean confirmadoLogin) {
		this.confirmadoLogin = confirmadoLogin;
	}

	public List<Rol> getListaRol() {
		return listaRol;
	}

	public void setListaRol(List<Rol> listaRol) {
		this.listaRol = listaRol;
	}

	public String getCedula_login() {
		return cedula_login;
	}

	public void setCedula_login(String cedula_login) {
		this.cedula_login = cedula_login;
	}

}
