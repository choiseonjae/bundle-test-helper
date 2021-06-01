package kr.co.bundletesthelper.util.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import kr.co.bundletesthelper.model.yaml.Config;
import kr.co.bundletesthelper.util.AlertUtil;

public class Logger {

	private static List<Text> logs = new ArrayList<>();
	private static ObservableList<Node> textBoard;
	private static ScrollPane pane;
	private static TextFlow textField;

	public static void init(ScrollPane scrollPane, TextFlow field) {
		textField = field;
		textBoard = field.getChildren();
		pane = scrollPane;

		StringBuilder style = new StringBuilder();

		String size = Config.TEST.getOrDefault("Log.size", "15px;");
		String background = Config.TEST.getOrDefault("Log.background", "black;");

		style.append("-fx-font-size : " + size);
		style.append("-fx-background-color : " + background);

		textField.setStyle(style.toString());
		pane.setStyle(style.toString());

//		YamlConfig.TEST_COINFIG.updateYaml("Log.size", size);
//		YamlConfig.TEST_COINFIG.updateYaml("Log.background", background);
	}

	public static void debug(String... message) {
		String msg = "";
		for (String m : message) {
			msg += m;
		}
		logging(new Log(LogType.DEBUG, msg));
	}

	public static void warn(String... message) {
		String msg = "";
		for (String m : message) {
			msg += m;
		}
		logging(new Log(LogType.WARN, msg));
	}

	public static void error(String... message) {
		String msg = "";
		for (String m : message) {
			msg += m;
		}
		logging(new Log(LogType.ERROR, msg));
	}
	
	public static void error(Exception exception, String... message) {
		String msg = "";
		for (String m : message) {
			msg += m;
		}
		logging(new Log(LogType.ERROR, msg));
		logging(exception);
	}
	
// printStack 하지 않아서 주석처리 필요없을 거 같으면 삭제
//	public static void error(Exception e) {
//		error(getPrintStackTrace(e));
//	}

	public static void info(String message) {
		logging(new Log(LogType.INFO, message));
	}

	private static void logging(Log log) {
		// LOG LEVEL DEBUG, ERROR, INFO ...
		logging(new Text("\n"));
		logging(log.getType().getText());
		log.getMessage().forEach(msg -> logging(msg));
	}

	private static void logging(Text text) {
		logs.add(text);
		loggingTextBoard(text);

	}
	
	private static void logging(Exception exception) {
		Hyperlink errorView = new Hyperlink("Click Detail");
		errorView.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				AlertUtil.error(exception);
			}
		});
		loggingTextBoard(new Text(" "), errorView);
	}

	private static void loggingTextBoard(Node... elements) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				for(Node element : elements) {
					textBoard.add(element);
				}
				pane.vvalueProperty().bind(textField.heightProperty());
			}
		});
	}

	public static void clear() {
		textBoard.clear();
	}

	private static String getPrintStackTrace(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));

		return errors.toString();
	}

	public static void warn(Exception e) {
		warn(getPrintStackTrace(e));
	}
}
