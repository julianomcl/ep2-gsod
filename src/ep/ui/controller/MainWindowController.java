package ep.ui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import ep.hadoop.startup.HadoopLauncher;
import ep.helper.DateHelper;
import ep.helper.UiExceptionHelper;
import ep.ui.vo.ComboBoxKVP;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class MainWindowController implements Initializable{

    @FXML
    private JFXComboBox<ComboBoxKVP> cmbMetodo;
    
    @FXML
    private Label lblProgress;

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
    void btnExecutarClick(ActionEvent event) {
    	
    	//TODO: VALIDAÇÃO AQUI PF
    	
    	try{
        	launcher.LaunchHadoop(
        			DateHelper.LocalDateToDate(dtInicio.getValue()),
        			DateHelper.LocalDateToDate(dtTermino.getValue()),
        			cmbMetodo.getValue().toString(),
        			cmbMetodo.getValue().toString());
    	}catch(Exception e){
    		Alert alert = UiExceptionHelper.getAlertForException(e);
    		alert.showAndWait();
    	}
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
				lblProgress.setText("Map: " + launcher.getMapProgress() + "| Reduce: " + launcher.getReduceProgress());
			}
		};
    }
}
