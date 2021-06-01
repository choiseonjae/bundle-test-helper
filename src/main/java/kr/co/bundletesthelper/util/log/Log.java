package kr.co.bundletesthelper.util.log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalTime;

import javafx.scene.text.Text;
import kr.co.bundletesthelper.model.yaml.Config;
import kr.co.bundletesthelper.model.yaml.exception.YamlException;
import kr.co.bundletesthelper.util.AlertUtil;

public class Log {

	private LogType type;
	private String message;
	private LocalTime time;

	public Log(LogType type, String message) {
		super();
		this.type = type;
		this.message = message == null || message.isEmpty() ? "\n" : message;
		time = new LocalTime();
	}

	public LogType getType() {
		return type;
	}

	public List<Text> getMessage() {
		return getMessage(message);
	}

	private List<Text> getMessage(String message) {
		List<Text> msg = new ArrayList<>();
		List<LogWord> coloredWords = getColoredWordList();

		boolean isContains = false;
		for (LogWord coloredWord : coloredWords) {
			if (message.contains(coloredWord.getMessage())) {
				isContains = true;
				for (String word : message.split(coloredWord.getMessage())) {
					msg.addAll(getMessage(word));
					msg.add(coloredWord.getText());
				}
				msg.remove(msg.size() - 1);
				break;
			}
		}
		if (!isContains) {
			msg.add(new LogWord(message, "WHITE").getText());
		}
		return msg;

	}

	private List<LogWord> getColoredWordList() {
		try {
			return Config.TEST.getMap("Log.word").entrySet().stream()
					.map(e -> new LogWord(e.getKey(), e.getValue().toString())).collect(Collectors.toList());
		} catch (YamlException e) {
			AlertUtil.error(e);
			return new ArrayList<>();
		}
	}

	public Text getDate() {
		Text text = new Text();
		text.setText(time.toString());
		text.setStyle("-fx-fill: WHITE");
		return text;
	}
}
