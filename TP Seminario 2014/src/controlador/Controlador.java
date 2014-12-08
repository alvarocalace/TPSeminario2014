package controlador;

import implementacion.Cara;
import implementacion.Diente;
import implementacion.FichaPeriodontal;
import implementacion.HistoriaClinica;
import implementacion.Odontologo;
import implementacion.Paciente;
import implementacion.Prediccion;
import implementacion.Proyeccion;
import implementacion.Seccion;
import implementacion.Turno;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import persistencia.AdministradorPersistenciaHistoriasClinicas;
import persistencia.AdministradorPersistenciaOdontologos;
import persistencia.AdministradorPersistenciaPaciente;
import persistencia.AdministradorPersistenciaSintomas;
import persistencia.AdministradorPersistenciaTurnos;
import views.CaraView;
import views.DienteView;
import views.EspecialidadView;
import views.FichaPeriodontalView;
import views.HistoriaClinicaView;
import views.ObservacionView;
import views.OdontogramaView;
import views.OdontologoView;
import views.PacienteView;
import views.SeccionView;
import views.TurnoView;

public class Controlador {
	
	private Collection<Paciente> pacientes;
	private Collection<Turno> turnos;
	private Collection<Odontologo> odontologos;
	private Collection<HistoriaClinica> historiasClinicas;
	private Collection<String> sintomas;
	private static Controlador instancia;
		
	private void inicializar(){
		this.pacientes = AdministradorPersistenciaPaciente.getInstancia().buscarPacientes();
		this.turnos = AdministradorPersistenciaTurnos.getInstancia().buscarTurnos();
		this.odontologos = AdministradorPersistenciaOdontologos.getInstancia().buscarOdontologos();
		this.sintomas = AdministradorPersistenciaSintomas.getInstancia().buscarSintomas();
		this.historiasClinicas = AdministradorPersistenciaHistoriasClinicas.getInstancia().buscarHistorias();
	}
	
	private Controlador(){
		pacientes = new ArrayList<Paciente>();
		sintomas = new ArrayList<String>();
		odontologos = new ArrayList<Odontologo>();
		turnos = new ArrayList<Turno>();
		historiasClinicas = new ArrayList<HistoriaClinica>();
	}
	
	public static Controlador getInstancia(){
		if (instancia==null){
			instancia = new Controlador();
			instancia.inicializar();
		}
		return instancia;
	}
	
	public void altaPaciente(PacienteView pacienteView) {
		Paciente paciente = obtenerPaciente(pacienteView.getDni());
		if (paciente == null) {
			paciente = new Paciente(pacienteView.getDni(),
					pacienteView.getNombre(),
					pacienteView.getApellido(),
					pacienteView.getTelefono(),
					pacienteView.getEmail(),
					pacienteView.getFechaNacimiento(),
					pacienteView.getGenero(),
					pacienteView.getObraSocial(),
					pacienteView.getPlanObraSocial());
			this.pacientes.add(paciente);
		}
	}
	
	//ALTAS
	
	public void altaTurno(TurnoView turnoView) {
		Turno turno = obtenerTurno(turnoView.getPaciente().getDni(), turnoView.getOdontologo().getMatricula(), turnoView.getFecha());
		if (turno == null) {
			Paciente paciente = obtenerPaciente(turnoView.getPaciente().getDni());
			Odontologo odontologo = obtenerOdontologo(turnoView.getOdontologo().getMatricula());
			if (paciente != null && odontologo != null) {
				turno = new Turno(paciente, odontologo, turnoView.getDescripcion(), turnoView.getFecha());
				turnos.add(turno);
			}
		}
	}
	
	public void altaOdontologo(OdontologoView odontologoView) {
		Odontologo odontologo = obtenerOdontologo(odontologoView.getMatricula());
		if (odontologo == null) {
			odontologo = new Odontologo(odontologoView.getMatricula(), odontologoView.getNombre(), odontologoView.getApellido(), null);
			for (EspecialidadView espView : odontologoView.getEspecialidades()) {
				odontologo.agregarEspecialidad(espView.getDescripcion());
			}
			odontologos.add(odontologo);
		}
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
	
	public void altaSintoma(String sintoma) {
		if (!sintomas.contains(sintoma)) {
			AdministradorPersistenciaSintomas.getInstancia().insert(sintoma);
			sintomas.add(sintoma);
		}
	}
	
	public void altaOdontograma(String dni, String idOdontograma, String matricula){
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		Odontologo odontologo = obtenerOdontologo(matricula);
		if (historia != null && odontologo != null){
			historia.agregarOdontograma(idOdontograma, getFechaActualSQL(), odontologo);
		}
	}
	
	public void asignarFichaAHistoria(String dni, String matricula){
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		Odontologo odontologo = obtenerOdontologo(matricula);
		if (historia != null && odontologo != null){
			historia.asignarFichaPeriodontal(odontologo);
		}
	}
	
	public void altaObservacion(String dni, ObservacionView observacion){
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		Odontologo odontologo = obtenerOdontologo(observacion.getOdontologo().getMatricula());
		if (historia != null && odontologo != null){
			historia.agregarObservacion(odontologo, observacion.getFecha(), observacion.getDescripcion());
		}
	}
	
	//ACTUALIZACIONES
	
	public void actualizarHistoriaClinica(String dni, String matricula, Timestamp fecha, String descripcion) {
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		if (historia != null) {
			historia.setDescripcion(descripcion);
			AdministradorPersistenciaHistoriasClinicas.getInstancia().update(historia);
		}
	}
	
	public void actualizarOdontograma (String dni, OdontogramaView odontograma){
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		Odontologo odontologo = obtenerOdontologo(odontograma.getOdontologo().getMatricula());
		if (historia != null && odontologo != null){
			historia.actualizarOdontograma(odontograma.getIdOdontograma(), odontograma.getFecha(), odontologo, construirDientesDesdeView(odontograma.getDientes()));
		}
	}
	
	public void actualizarFicha(String dni, FichaPeriodontalView ficha){
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		Odontologo odontologo = obtenerOdontologo(ficha.getOdontologo().getMatricula());
		if (historia != null && odontologo != null){
			historia.actualizarFichaPeriodontal(odontologo, crearSeccionesDesdeView(ficha.getSecciones()));
		}
	}		

	//OBTENER OBJETO
	
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
	
	public Turno obtenerTurno(String dni, String matricula, Timestamp fecha) {
		Turno turno = null;
		for (Turno turn : turnos) {
			if (turn.sosElTurno(dni, matricula, fecha)) {
				turno = turn;
			}
		}
		if (turno == null) {
			turno = AdministradorPersistenciaTurnos.getInstancia().buscarTurno(matricula, dni, fecha);
			if (turno != null) {
				turnos.add(turno);
			}
		}
		return turno;
	}
	
	//OBTENER VIEW
	
	public OdontologoView obtenerOdontologoView (String matricula) {
		Odontologo odontologo = obtenerOdontologo(matricula);
		return odontologo != null ? odontologo.generarView() : null;
	}
	
	public PacienteView obtenerPacienteView(String dni) {
		Paciente paciente = obtenerPaciente(dni);
		return paciente != null? paciente.generarView() : null;
	}
	
	public HistoriaClinicaView obtenerHistoriaClinicaView(String dni) {
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		return historia != null ? historia.generarView() : null;
	}

	public FichaPeriodontalView obtenerFichaView(String dni) {
		FichaPeriodontal ficha = obtenerFicha(dni);
		return ficha != null? ficha.generarView() : null;
	}
	
	public FichaPeriodontal obtenerFicha(String dni) {
		HistoriaClinica historia = obtenerHistoriaClinica(dni);
		if (historia != null) {
			return historia.getFicha();
		}
		return null;
	}
	
	//OBTENER COLLECTION DE VIEWS
	
	public Collection<PacienteView> obtenerPacientesView() {
		Collection<PacienteView> pacientes = new ArrayList<PacienteView>();
		for (Paciente paciente : this.pacientes) {
			pacientes.add(paciente.generarView());
		}
		return pacientes;
	}	
	
	public Collection<OdontologoView> obtenerOdontologosView() {
		Collection<OdontologoView> odontologos = new ArrayList<OdontologoView>();
		for (Odontologo odontologo : this.odontologos) {
			odontologos.add(odontologo.generarView());
		}
		return odontologos;
	}
	
	public Collection<HistoriaClinicaView> obtenerHistoriasClinicasView() {
		Collection<HistoriaClinicaView> historias = new ArrayList<HistoriaClinicaView>();
		for (HistoriaClinica historia : this.historiasClinicas) {
			historias.add(historia.generarView());
		}
		return historias;
	}	
	
	public TurnoView obtenerTurnoView(String dni, String matricula, Timestamp fecha) {
		Turno turno = obtenerTurno(dni, matricula, fecha);
		return turno != null? turno.generarView() : null;
	}
	
	public Collection<TurnoView> obtenerTurnosView() {
		Collection<TurnoView> turnos = new ArrayList<TurnoView>();
		for (Turno turno : this.turnos) {
			turnos.add(turno.generarView());
		}
		return turnos;
	}	
	
	//ANALISIS PREDICTIVO
	
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
	
	//UTILITARIAS
	
	private java.sql.Timestamp getFechaActualSQL(){
		return new Timestamp(System.currentTimeMillis());
	}	
	
	private Collection<Diente> construirDientesDesdeView(Collection<DienteView> dientesView){
		Collection<Diente> dientes = new ArrayList<Diente>();
		Diente diente;
		for (DienteView d : dientesView){
			diente = new Diente(d.getPosicion());
			diente.setCaras(construirCarasDesdeView(d.getCaras()));
			diente.setEstado(d.getEstado());
			diente.setIdProtesis(d.getIdProtesis());
			diente.setIdPuente(d.getIdPuente());
			dientes.add(diente);
		}
		return dientes;
	}

	private Collection<Cara> construirCarasDesdeView(Collection<CaraView> carasView) {
		Collection<Cara> caras = new ArrayList<Cara>();
		Cara cara;
		for (CaraView c : carasView){
			cara = new Cara(c.getPosicion());
			cara.setEstado(c.getEstado());
			caras.add(cara);
		}
		return caras;
	}
	
	private Collection<Seccion> crearSeccionesDesdeView(Collection<SeccionView> seccionesView) {
		Collection<Seccion> secciones = new ArrayList<Seccion>();
		Seccion seccion;
		for (SeccionView s : seccionesView){
			seccion = new Seccion(s.getPosicionDiente(),s.getPosicionSeccion());
			seccion.setMargen(s.getMargen());
			seccion.setPlaca(s.isPlaca());
			seccion.setProfundidad(s.getProfundidad());
			seccion.setSangrado(s.isSangrado());
			secciones.add(seccion);
		}
		return secciones;
	}
}