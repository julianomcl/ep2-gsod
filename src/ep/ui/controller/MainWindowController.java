package ep.ui.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;

import ep.hadoop.startup.HadoopLauncher;
import ep.helper.DateHelper;
import ep.helper.UiExceptionHelper;
import ep.ui.vo.ComboBoxKVP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class MainWindowController implements Initializable{

    @FXML
    private JFXComboBox<ComboBoxKVP> cmbMetodo;
    
    @FXML
    private AnchorPane ancMain;

    @FXML
    private JFXComboBox<ComboBoxKVP> cmbAtributo;

    @FXML
    private JFXButton btnExecutar;

    @FXML
    private DatePicker dtInicio;

    @FXML
    private DatePicker dtTermino;
    
    @FXML
    private JFXProgressBar progress;

    @FXML
    void btnExecutarClick(ActionEvent event) {
    	
    	//TODO: VALIDAÇÃO AQUI PF
    	
		Task<Void> task = new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				String sDtInicio = dtInicio.getEditor().getText();
				String sDtTermino = dtTermino.getEditor().getText();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				progress.setVisible(true);
				
				launcher.LaunchHadoop(
						sdf.parse(sDtInicio),
						sdf.parse(sDtTermino),
						cmbMetodo.getValue().getValue(),
						cmbAtributo.getValue().getValue());
				return null;
			}
		};
		
		task.setOnFailed(e -> {
			progress.setVisible(false);
			progress.setProgress(0.0);
			Alert alert = UiExceptionHelper.getAlertForException(task.getException());
    		alert.showAndWait();
		});
		
		task.setOnSucceeded(e ->{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Hadoop");
			alert.setContentText("Execução finalizada");
			progress.setVisible(false);
			progress.setProgress(0.0);
		});
		
		new Thread(task).run();
    }
    
    private HadoopLauncher launcher;
    
    
    private final ObservableList<ComboBoxKVP> metodoList = FXCollections.observableArrayList(
    		new ComboBoxKVP("Anual", "YA"),
    		new ComboBoxKVP("Mensal", "MA"),
    		new ComboBoxKVP("Semanal", "WA"),
    		new ComboBoxKVP("Dia da semana", "WDA"));
    
    private final ObservableList<ComboBoxKVP> atributoList = FXCollections.observableArrayList(
    		new ComboBoxKVP("Temperatura (TEMP)", "TEMP"),
    		new ComboBoxKVP("Temperatura do ponto de orvalho (DEWP)", "DEWP"),
    		new ComboBoxKVP("Pressão Nível do Mar (SLP)", "SLP"),
    		new ComboBoxKVP("Pressão na estação (STP)", "STP"),
    		new ComboBoxKVP("Visibilidade em décimos de milhas (VISIB)", "VISIB"),
    		new ComboBoxKVP("Velocidade do vento (WDSP)", "WDSP"),
    		new ComboBoxKVP("Velocidade Máxima do vento sustentada (MXSPD)", "MXSPD"),
    		new ComboBoxKVP("Velocidade Máxima de rajada de vento (GUST)", "GUST"),
    		new ComboBoxKVP("Profundidade da neve (SNDP)", "SNDP"),
    		new ComboBoxKVP("Precipitação Total (PRCP)", "PRCP"),
    		new ComboBoxKVP("Temperatura Máxima (MAX)", "MAX"),
    		new ComboBoxKVP("Temperatura Mínima (MIN)", "MIN"));
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	dtInicio.setValue(LocalDate.of(2016, 1, 1));
    	dtTermino.setValue(LocalDate.now());
    	
    	cmbMetodo.setItems(metodoList);
    	cmbAtributo.setItems(atributoList);
    	
    	launcher = new HadoopLauncher(getProgressAction());
    }

    
    private EventHandler<ActionEvent> getProgressAction(){
    	return new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				progress.setProgress((launcher.getMapProgress() * 0.5) + (launcher.getReduceProgress() * 0.5));
			}
		};
    }
}
