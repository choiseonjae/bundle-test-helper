package kr.co.danal.model.javafx.testnode;

import java.io.File;
import java.util.Objects;

import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import kr.co.danal.model.TestNodeType;
import kr.co.danal.model.yaml.Config;
import kr.co.danal.util.AlertUtil;

public class TestTree extends TestNode {

	private static final String DEFAULT_PATH = "Test Folder Path";
	private File rootFile;

	/**
	 * 기본 설정된 config 정보를 바탕으로 생성.
	 * 설정된 정보가 없을 경우 사용자로부터 정보를 받는다.
	 */
	public TestTree() {
		super();
		rootFile = getRoot();
		
		if (!isDirectory(rootFile)) {
			String path = rootFile == null ? "null" : rootFile.getAbsolutePath();
			String errorMessage = String.format("(Path : %s)\n폴더 위치가 이상하거나 설정되지 않습니다.", path);
			AlertUtil.error("Not valid test case folder path", errorMessage);

			System.exit(0);
		}
		
		setValue(new TestNodeFolder(rootFile)); // 자신
		setGraphic(new ImageView(TestNodeFolder.FOLDER_IMAGE));
		setChildrenFolder(rootFile); // 새끼 등록
		savePath();
	}
	
	
	public void setTestCaseFolder() {
		rootFile = new DirectoryChooser().showDialog(new Stage());
		setValue(new TestNodeFolder(rootFile)); // 자신
		setGraphic(new ImageView(TestNodeFolder.FOLDER_IMAGE));
		setChildrenFolder(rootFile); // 새끼 등록
		savePath();
	}
	
	public void refresh() {
		getChildren().clear();
		setChildrenFolder(rootFile);
	}

	private File getRoot() {
		try {
			System.out.println("[TestTree] getRoot Start");

			String path = Config.TEST.getString(DEFAULT_PATH);
			Objects.requireNonNull(path, "Test Case 위치을 설정해주세요.");

			File folder = new File(path);
			if (!isDirectory(folder)) {
				throw new Exception("folder가 Null 또는 No exist 또는 Not Directory 입니다.");
			}

			return folder;
		} catch (NullPointerException e) {
			return new DirectoryChooser().showDialog(new Stage());
		} catch (Exception e) {
			e.printStackTrace();
			return new DirectoryChooser().showDialog(new Stage());
		} finally {
			System.out.println("[TestTree] getRoot End");
		}
	}

	private boolean isDirectory(File file) {
		if (file == null || !file.exists() || file.isFile()) {
			return false;
		}
		return true;
	}

	private void setChildrenFolder(File rootFolder) {
		getChildren().addAll(new TestNodeFolder(rootFolder).getChildren());
	}

	@Override
	public void test() {
		getChildren().forEach(node -> ((TestNode)node).test());
	}

	@Override
	public TestNodeType getType() {
		return TestNodeType.FOLDER;
	}

	@Override
	public void print() {
		
	}
	
	public void selectFolder() {
		rootFile = new DirectoryChooser().showDialog(new Stage());
		
		if (!isDirectory(rootFile)) {
			String path = rootFile == null ? "null" : rootFile.getAbsolutePath();
			String errorMessage = String.format("(Path : %s)\n폴더 위치가 이상하거나 설정되지 않습니다.", path);
			AlertUtil.error("Not valid test case folder path", errorMessage);

			System.exit(0);
		}
		
		setValue(new TestNodeFolder(rootFile)); // 자신
		setGraphic(new ImageView(TestNodeFolder.FOLDER_IMAGE));
		setChildrenFolder(rootFile); // 새끼 등록
		savePath();
	}
	
	private void savePath() {
		Config.TEST.updateYaml(DEFAULT_PATH, rootFile.getAbsolutePath());
	}

	@Override
	public File getFile() {
		return rootFile;
	}
}
