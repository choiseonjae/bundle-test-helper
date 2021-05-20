package kr.co.danal.model.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import kr.co.danal.application.Home;
import kr.co.danal.model.yaml.Config;
import kr.co.danal.model.yaml.exception.YamlException;
import kr.co.danal.util.AlertUtil;
import kr.co.danal.util.log.LogWord;
import kr.co.danal.util.log.Logger;

public class WordStyleManager implements Initializable, View {

	@FXML
	private ListView<LogWord> wordStyleList;

	@FXML
	private Button addBtn;
	@FXML
	private Button deleteBtn;
	@FXML
	private Button dropBtn;
	@FXML
	private Button saveBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		wordStyleList.setItems(FXCollections.observableArrayList());
		try {
			List<LogWord> recordStyle = Config.TEST.getMap("Log.word").entrySet().stream()
					.map(e -> new LogWord(e.getKey(), e.getValue().toString())).collect(Collectors.toList());
			wordStyleList.getItems().addAll(recordStyle);
		} catch (YamlException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			// nothing...
		}

	}

	@Override
	public void showAndWait() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Home.class.getClass().getResource("/word_style_manager.fxml"));
		try {
			TabPane settingBox = (TabPane) loader.load();
			Stage stage = new Stage();
			stage.setTitle("설정");
			stage.setScene(new Scene(settingBox));

			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addLogWord(ActionEvent e) {
		WordColorChooser wordColorChooser = new WordColorChooser();
		wordColorChooser.showAndWait();

		if (wordColorChooser.isSelected()) {
			wordStyleList.getItems().add(wordColorChooser.getLogWord());
		}
	}

	public void deleteLogWord(ActionEvent e) {
		wordStyleList.getItems().remove(wordStyleList.getSelectionModel().getSelectedIndex());
	}

	public void deleteAllLogWord(ActionEvent e) {
		wordStyleList.getItems().clear();
	}

	public void save(ActionEvent event) {
		Map<String, String> logWordMap = new HashMap<>();
		wordStyleList.getItems().forEach(word -> logWordMap.put(word.getMessage(), word.getColor()));
		try {
			Config.TEST.getMap("Log.word").clear();
			Config.TEST.getMap("Log.word").putAll(logWordMap);
			Config.TEST.saveYaml();
			AlertUtil.info("설정", "저장 완료", "단어 색상에 대해서 저장 완료했습니다.");
		} catch (YamlException e) {
			AlertUtil.error(e);
		}
	}

}
