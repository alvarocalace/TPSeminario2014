package controlador;

import implementacion.FichaPeriodontal;
import implementacion.HistoriaClinica;
import implementacion.Odontologo;
import implementacion.Paciente;
import implementacion.Prediccion;
import implementacion.Proyeccion;
import implementacion.Turno;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import persistencia.AdministradorPersistenciaHistoriasClinicas;
import persistencia.AdministradorPersistenciaOdontologos;
import persistencia.AdministradorPersistenciaPaciente;
import persistencia.AdministradorPersistenciaSintomas;
import persistencia.AdministradorPersistenciaTurnos;
import views.SeccionView;

public class Controlador {
	
	private Collection<Paciente> pacientes;
	private Collection<Turno> turnos;
	private Collection<Odontologo> odontologos;
	private Collection<HistoriaClinica> historiasClinicas;
	private Collection<String> sintomas;
	private static Controlador instancia;
	
	private Controlador(){
		this.pacientes = AdministradorPersistenciaPaciente.getInstancia().buscarPacientes();
		this.turnos = AdministradorPersistenciaTurnos.getInstancia().buscarTurnos();
		this.odontologos = AdministradorPersistenciaOdontologos.getInstancia().buscarOdontologos();
		this.historiasClinicas = AdministradorPersistenciaHistoriasClinicas.getInstancia().buscarHistorias();
		this.sintomas = AdministradorPersistenciaSintomas.getInstancia().buscarSintomas();
	}
	
	public static Controlador getInstancia(){
		if (instancia==null)
			instancia = new Controlador();
		return instancia;
	}
	
	public void altaHistoriaClinica(String dni, String descripcion) {
		Paciente paciente = obtenerPaciente(dni);
		if (paciente != null) {
			HistoriaClinica historia = obtenerHistoriaClinica(dni);
			if (historia == null) {
				historia = new HistoriaClinica(paciente, descripcion);
				historiasClinicas.add(historia);
			}
		}
	}
	
	public Collection<Proyeccion> analisisPredictivoHistoriaClinica(String dni) {
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		Collection<Proyeccion> proyecciones = new ArrayList<Proyeccion>();
		if (historia != null){
			Collection<String> sintomasDetectados = historia.detectarSintomas(sintomas);
			Prediccion prediccion;
			Collection<HistoriaClinica> historiasSinContarLaAnalizada = this.historiasClinicas;
			historiasSinContarLaAnalizada.remove(historia);
			for (String sintoma : sintomasDetectados){
				prediccion = new Prediccion(sintoma);
				for (HistoriaClinica h : historiasSinContarLaAnalizada)
					if (h.tenesElSintoma(sintoma)){
						for (String sintomaAnalisis : h.detectarSintomas(sintomas))
							prediccion.agregarItemPrediccion(sintomaAnalisis);
						prediccion.aumentarCantidad();
					}
				proyecciones.add(prediccion.generarProyeccion());
			}				
		}
		return proyecciones;
	}
	
	public void modificarSeccionHistoriaFicha(String dni, SeccionView seccionView) {
		FichaPeriodontal ficha = obtenerFicha(dni);
		if (ficha != null) {
			ficha.modificarSeccion(seccionView.getPosicionSeccion(), seccionView.getPosicionDiente(), seccionView.isSangrado(), seccionView.isPlaca(), seccionView.getMargen());
		}
	}
	
	public void actualizarHistoriaClinica(String dni, String matricula, Date fecha, String descripcion) {
	
	}
	
	public Odontologo buscarOdontologo(String dni) {
		return null;
	}
	
	public HistoriaClinica buscarHistoriaClinica(String dni) {
		return null;
	}
	
	public void ingresarEstadoDiente(int dni, int idDiente, String estado) {
	
	}
	
	public void ingresarProtesis(int dni, int idDiente) {
	
	}
	
	public void ingresarEstadoCara(int dni, int idDiente, int idCara, String estadoCara) {
	
	}
	
	public void ingresarDientesPuente(int dni, int[] idDientes) {
	
	}
	
	public Prediccion analisisPredictivoHistoriaClinica(int dni) {
		return null;
	}
	
	public float[] calcularProbabilidadesHistorias(Collection<String> Sintomas) {
		return null;
	}
	
	public float[] calcularProbabilidades(Collection<String> Sintomas, Collection<String> SintomasOtros) {
		return null;
	}

	public Collection<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(Collection<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public Collection<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(Collection<Turno> turnos) {
		this.turnos = turnos;
	}

	public Collection<Odontologo> getOdontologos() {
		return odontologos;
	}

	public void setOdontologos(Collection<Odontologo> odontologos) {
		this.odontologos = odontologos;
	}

	public Collection<HistoriaClinica> getHistoriasClinicas() {
		return historiasClinicas;
	}

	public void setHistoriasClinicas(Collection<HistoriaClinica> historiasClinicas) {
		this.historiasClinicas = historiasClinicas;
	}

	public Odontologo obtenerOdontologo(String matricula) {
		Odontologo odontologo = null;
		for (Odontologo odon : odontologos) {
			if (odon.sosElOdontologo(matricula)) {
				odontologo = odon;
			}
		}
		if (odontologo == null) {
			odontologo = AdministradorPersistenciaOdontologos.getInstancia().buscarOdontologo(matricula);
			if (odontologo != null) {
				odontologos.add(odontologo);
			}
		}
		return odontologo;
	}

	public Paciente obtenerPaciente(String dni) {
		Paciente paciente = null;
		for (Paciente pac : pacientes) {
			if (pac.sosElPaciente(dni)) {
				paciente = pac;
			}
		}
		if (paciente == null) {
			paciente = AdministradorPersistenciaPaciente.getInstancia().buscarPaciente(dni);
			if (paciente != null) {
				pacientes.add(paciente);
			}
		}
		return paciente;
	}
	
	public HistoriaClinica obtenerHistoriaClinica(String dni) {
		HistoriaClinica historia = null;
		for (HistoriaClinica hist : historiasClinicas) {
			if (hist.sosLaHistoria(dni)) {
				historia = hist;
			}
		}
		if (historia == null) {
			historia = AdministradorPersistenciaHistoriasClinicas.getInstancia().buscarHistoria(dni);
			if (historia != null) {
				historiasClinicas.add(historia);
			}
		}
		return historia;
	}

	public FichaPeriodontal obtenerFicha(String dni) {
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		if (historia != null) {
			return historia.getFicha();
		}
		return null;
	}
	
	//metodos para el test
	
	public void altaPacienteTest(String dni){
		Paciente paciente = new Paciente();
		paciente.setDni(dni);
		this.pacientes.add(paciente);
	}
	

	public HistoriaClinica obtenerHistoriaClinicaTest(String dni) {
		HistoriaClinica historia = null;
		for (HistoriaClinica hist : historiasClinicas) {
			if (hist.sosLaHistoria(dni)) {
				historia = hist;
			}
		}
		return historia;
	}
	
	public void altaHistoriaClinicaTest(String dni) {
		Paciente paciente = obtenerPaciente(dni);
		if (paciente != null) {
			HistoriaClinica historia = obtenerHistoriaClinicaTest(dni);
			if (historia == null) {
				historia = new HistoriaClinica();
				historia.setPaciente(paciente);
				this.historiasClinicas.add(historia);
			}
		}
	}
}