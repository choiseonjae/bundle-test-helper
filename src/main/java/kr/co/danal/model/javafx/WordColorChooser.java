package kr.co.danal.model.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kr.co.danal.application.Home;
import kr.co.danal.util.log.LogWord;

public class WordColorChooser implements Initializable, View {

	@FXML
	private ColorPicker colorPicker;

	@FXML
	private TextField coloredWord;

	@FXML
	private Button setWordStyle;
	
	private static String word_t = null;
	private static String rgb_t = null;
	
	private String word = null;
	private String rgb = null;

	private String toRbgString(Color c) {
		return String.format("rgb(%d, %d, %d)", to255Int(c.getRed()), to255Int(c.getGreen()), to255Int(c.getBlue()));
	}

	private Object to255Int(double d) {
		return (int) (d * 255);
	}

	public void changeColor(ActionEvent e) {
		coloredWord.setStyle(String.format("-fx-text-inner-color: %s;", toRbgString(colorPicker.valueProperty().getValue())));
	}

	public void setWordStyle(ActionEvent e) {
		word_t = coloredWord.getText();
		rgb_t = toRbgString(colorPicker.valueProperty().getValue());
		((Stage) setWordStyle.getScene().getWindow()).close();
	}

	public String getRgb() {
		return rgb;
	}

	public String getWord() {
		return word;
	}

	public LogWord getLogWord() {
		if(isSelected()) {
			return new LogWord(word, rgb);
		}
		return null;
	}
	
	public boolean isSelected() {
		if(word != null && !word.isEmpty() && rgb != null && !rgb.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {


	}

	@Override
	public synchronized void showAndWait() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Home.class.getClass().getResource("/word_color_chooser.fxml"));
			AnchorPane colorPickerBox = (AnchorPane) loader.load();
			
			Stage stage = new Stage();
			stage.setTitle("단어 색상 설정");
			stage.setScene(new Scene(colorPickerBox));
			stage.showAndWait();
			rgb = rgb_t;
			word = word_t;
			
			rgb_t = null;
			word_t = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
