package kr.co.bundletesthelper.util.log;

import javafx.scene.text.Text;

public enum LogType {

	DEBUG("GREEN"), ERROR("RED"),
	INFO("#4fc3f7"), WARN("YELLOW");

	private boolean isVisible = true;
	private String color;

	LogType(String color) {
		this.color = color;
	}

	public boolean isVisible() {
		return isVisible;
	}
	
	public Text getText() {
		Text text = new Text();
		
		text.setText(toString() + " ");
		text.setStyle("-fx-fill: " + color);
		
		return text;
	}

}
