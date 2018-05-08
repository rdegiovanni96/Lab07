package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PowerOutagesController {
	
	private Model model = new Model();
	
	public void setModel(Model model) {
		this.model = model;
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> btnNerc;

    @FXML
    private TextField txtYears;

    @FXML
    private TextField txtHours;

    @FXML
    private TextArea txtResult;

    @FXML
    void doWorstCase(ActionEvent event) {
    	
    		txtResult.clear();
    	
    		int maxYears = Integer.parseInt(txtYears.getText());
    		int maxHours = Integer.parseInt(txtHours.getText());
    		
    		String nerc = btnNerc.getValue();
    		
    		txtResult.appendText(model.worstCase(nerc, maxYears, maxHours));

    }

    @FXML
    void initialize() {
        assert btnNerc != null : "fx:id=\"btnNerc\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        this.setModel(model);
        btnNerc.getItems().addAll(model.getNomiNerc());
    }
}
