package kr.co.bundletesthelper.application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kr.co.bundletesthelper.model.TestNodeType;
import kr.co.bundletesthelper.model.javafx.WordStyleManager;
import kr.co.bundletesthelper.model.javafx.testnode.TestNode;
import kr.co.bundletesthelper.model.javafx.testnode.TestTree;
import kr.co.bundletesthelper.model.yaml.Config;
import kr.co.bundletesthelper.model.yaml.exception.YamlException;
import kr.co.bundletesthelper.util.AlertUtil;
import kr.co.bundletesthelper.util.log.Logger;

/**
 * BorderPane
 *
 * @author homesking
 *
 */
public class Home implements Initializable {

	public static final URL FXML = Home.class.getClass().getResource("/home.fxml");
	public static final String TITLE = "Test Helper";
	private static final String YAML_EDITOR = "Yaml Editor";
	
	

	@FXML
	private MenuItem clear, refresh, text, yamlEditor, testCaseFolder, runMethod, addTestCaserMethod, addServerMethod,
			about;
	@FXML
	private TreeView<TestNode> testCaseTree;
	@FXML
	private ScrollPane logScrollPane;
	@FXML
	private TextFlow logBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Logger.init(logScrollPane, logBox);
		
		// 위

		// 왼

		// 중간

		// Default Root Path or selected path of User
		testCaseTree.setRoot(new TestTree());
	}

	public void clear(ActionEvent event) {
		boolean ok = AlertUtil.confirm("경고", "LOG가 지워집니다.", "지우시겠습니까?");
		if (ok) {
			Logger.clear();
		}
	}

	public void refresh(ActionEvent event) {
		((TestTree) testCaseTree.getRoot()).refresh();
		testCaseTree.refresh();
	}

	public void text(ActionEvent event) {

	}

	public void clickTestCaseFolder() {
		((TestTree) testCaseTree.getRoot()).setTestCaseFolder();
		refresh(null);
	}

	public void clickYamlEditor() {
		try {
			File initDir = new File(Config.TEST.getString(YAML_EDITOR));
			FileChooser chooser = new FileChooser();
			if (initDir != null && initDir.exists()) {
				chooser.setInitialDirectory(initDir.getParentFile()); // 초기 켜지는 화면 위치
			}

			File editor = chooser.showOpenDialog(new Stage());
			if (editor != null && editor.exists()) {
				Config.TEST.updateYaml(YAML_EDITOR, editor.getAbsolutePath());
			}
		} catch (YamlException e) {
			e.printStackTrace();
		}
	}

	public void clickEvent(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			questionTest();
		} else if (event.getButton() == MouseButton.MIDDLE) {
			openEditor();
		}
	}

	public void typing(KeyEvent event) {
		if (event.getCode().toString().equals("ENTER")) {
			if (event.isControlDown()) {
				openEditor();
			} else {
				TestNode node = testCaseTree.getSelectionModel().getSelectedItem().getValue();
				if (node.getType() == TestNodeType.TEST) {
					showTestCases();
				} else {
					expandNode();
				}
			}
		} else if (event.getCode().toString().equals("R") && event.isControlDown()) {
			questionTest();
		}
	}

	public void typingLogBox(KeyEvent event) {
		System.out.println(event);
		if (event.getCode().toString().equals("ENTER")) {
			Logger.debug("??");
		}
	}

	private void showTestCases() {
		// TODO Auto-generated method stub

	}

	private void openEditor() {
		TestNode node = null;
		try {
			node = testCaseTree.getSelectionModel().getSelectedItem().getValue();
		} catch (NullPointerException e) {
			node = (TestNode) testCaseTree.getRoot();
		}

		switch (node.getType()) {
		case FOLDER:
			try {
				Desktop.getDesktop().open(node.getFile());
			} catch (IOException e) {
				Logger.error(e);
				AlertUtil.error("열기 실패", "선택하신 폴더를 열기 위한 기본 프로그램이 없습니다.");
			}
			break;
		case FILE:
		case TEST:
			Runtime runtime = Runtime.getRuntime();
			String application;
			try {
				application = Config.TEST.getString(YAML_EDITOR);
				runtime.exec(application + " \"" + node.getFile().getAbsolutePath() + "\"");
			} catch (Exception e) {
				try {
					Desktop.getDesktop().edit(node.getFile());
				} catch (IOException e1) {
					Logger.error(e);
					AlertUtil.error("열기 실패", "선택하신 노드를 열기 위한 기본 프로그램이 없습니다.");
					File runProgram = new FileChooser().showOpenDialog(new Stage());
					try {
						runtime.exec(runProgram.getAbsolutePath() + " \"" + node.getFile().getAbsolutePath() + "\"");
						Config.TEST.updateYaml("Yaml Editor", runProgram.getAbsolutePath());
					} catch (IOException e2) {
						Logger.error(e);
						AlertUtil.error("열기 실패", "노드를 열기에 적합한 프로그램이 아닙니다.");
					}

				}
			}
			break;
		}

	}

	private void expandNode() {
		TestNode node = testCaseTree.getSelectionModel().getSelectedItem().getValue();
		testCaseTree.getRoot().setExpanded(true);
		node.setExpanded(true);
	}

	private void questionTest() {
		TestNode node = testCaseTree.getSelectionModel().getSelectedItem().getValue();
		boolean ok = AlertUtil.confirm("Test", node.toString(), "테스트를 진행하시겠습니까?");
		if (ok) {
			Logger.info("┌───────────────────────┐");
			Logger.info("      테스트 시작합니다                  ");
			Logger.info("└───────────────────────┘");
			node.test();
		}
	}

	public void addTestCaseMethod(ActionEvent event) {
		AlertUtil.info("테스트 케이스 추가 방법", "준비 중입니다.", "잠시만 기다려주세요.");
	}

	public void addServerMethod(ActionEvent event) {
		AlertUtil.info("테스트 할 서버 추가 방법", "준비 중입니다.", "잠시만 기다려주세요.");
	}

	public void runMethod(ActionEvent event) {
		String message = "1. 테스트 하고자 하는 폴더, 파일, TC를 선택\n";
		message += "2. Ctrl + R 혹은 마우스 우 클릭";
		AlertUtil.info("테스트 실행 방법", "T.C는 병렬 실행됩니다.", message);
	}

	public void about(ActionEvent event) {
		String message = "개발 : 최선재\n";
		message += "버그 발견 시 문의 부탁드립니다.";
		AlertUtil.info("About", "(주) 다날", message);
	}
	
	public void openTextSettingBox(ActionEvent event) {
		new WordStyleManager().showAndWait();
	}

}