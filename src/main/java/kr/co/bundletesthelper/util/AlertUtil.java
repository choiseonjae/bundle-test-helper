package kr.co.bundletesthelper.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AlertUtil {
	
	public static void error(String errorName, String errMessage) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText(errorName);
		errorAlert.setContentText(errMessage);
		errorAlert.showAndWait();
	}
	
	public static void error(Exception e) {
		error(e, e.getMessage());
	}
	
	public static void error(Exception e, String contentText) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setHeaderText(String.format("It's %s.", e.getClass().getSimpleName()));
		alert.setContentText("Error : " + contentText);
		
		Label label = new Label("The exception stacktrace was:");
		
		TextArea textArea = new TextArea(getPrintStackTrace(e));
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		
		alert.getDialogPane().setExpandableContent(expContent);
		alert.getDialogPane().setExpanded(true);
		alert.showAndWait();
	}
	
	private static String getPrintStackTrace(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		
		return errors.toString();
	}
	
	public static boolean confirm(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		
		return alert.showAndWait().get() == ButtonType.OK;
	}
	
	public static void info(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
	
	public static String input(String message) {
		return input("", message);
	}
	
	public static String input(String defaultValue, String message) {
		TextInputDialog dialog = new TextInputDialog(defaultValue);
		dialog.setTitle("사용자 입력");
		dialog.setHeaderText("입력하지 않으면 테스트는 실패처리 됩니다.");
		dialog.setContentText(message);
		

		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			return result.get();
		}
		throw new NullPointerException("Cancel Click in Input Box");
	}

}
