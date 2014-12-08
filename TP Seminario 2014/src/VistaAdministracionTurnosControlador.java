import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import views.OdontologoView;
import views.PacienteView;
import views.TurnoView;
import controlador.Controlador;


public class VistaAdministracionTurnosControlador implements Initializable {

	@FXML
	private TextField tDni, tDescripcion;
	@FXML
	private ComboBox<String> comboOdontologos, comboHora;
	@FXML
	private DatePicker fecha;
	@FXML
	private Button altaTurno;
	
	@SuppressWarnings("deprecation")
	public void agregarTurno(ActionEvent event) {
		if (tDni.getText()!=""){
			if(comboOdontologos.getValue().compareTo("Odontologo")!=0){
				if(fecha.getValue()!=null){
					if(comboHora.getValue().compareTo("Hora")!=0){
						if(tDescripcion.getText().compareTo("")!=0){
							Controlador con = Controlador.getInstancia();
							OdontologoView ov = new OdontologoView();
							PacienteView pv = new PacienteView();
							TurnoView tv = new TurnoView();
							SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
							String fechaT = fecha.getValue().toString()+' '+comboHora.getValue();
							Date fechaCompleta = null;
							try {
								fechaCompleta = new Date(format.parse(fechaT).getTime());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							pv = con.obtenerPacienteView(tDni.getText());
							ov = con.obtenerOdontologoView(comboOdontologos.getValue());
							tv.setPaciente(pv);
							tv.setOdontologo(ov);
							//tv.setFecha(fechaCompleta);
							tv.setDescripcion(tDescripcion.getText());
	
							con.altaTurno(tv);
							
							Stage dialogStage = new Stage();
							dialogStage.initModality(Modality.WINDOW_MODAL);
							dialogStage.setScene(new Scene(VBoxBuilder.create().
							    children(new Text("Turno registrado correctamente"), new Button("Continuar")).
							    alignment(Pos.CENTER).padding(new Insets(5)).build()));
							dialogStage.show();
 						}
					}
				}
			}
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		//Cargador odont�logos persistentes al combo//
		/*for (OdontologoView ov : Controlador.getInstancia().obtenerOdontologosView()){
			comboOdontologos.getItems().add(ov.getMatricula());
		}*/
		
		comboOdontologos.getItems().addAll("Nico Pickelny", "De Simone, Gabriel", "Lucas Aguirre", "C�mara");
		comboHora.getItems().addAll("8:30", "9:00", "10:00", "Nunca");
		
		}
}