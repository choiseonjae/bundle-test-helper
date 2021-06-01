package kr.co.bundletesthelper.util.log;

import javafx.scene.text.Text;

public class LogWord {
	
	private String message;
	private String color;
	
	public LogWord(String message, String color) {
		this.message = message;
		this.color = color;
	}
	
	public LogWord(String message) {
		this(message, "WHITE");
	}
	
	@Override
	public String toString() {
		return String.format("%s -> %s", message, color);
	}

	public String getMessage() {
		return message;
	}
	
	public String getColor() {
		return color;
	}
	
	public Text getText() {
		Text text = new Text(message);
		setTextStyle(text);
		return text;
	}
	
	private void setTextStyle(Text text) {
		StringBuilder style = new StringBuilder();
		if (color != null) {
			style.append("-fx-fill: " + color + "" );
		}
		text.setStyle(style.toString());
	}
}
