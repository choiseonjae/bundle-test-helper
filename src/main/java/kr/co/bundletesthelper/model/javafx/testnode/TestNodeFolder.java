package kr.co.bundletesthelper.model.javafx.testnode;

import java.io.File;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.bundletesthelper.model.TestNodeType;

public class TestNodeFolder extends TestNode {

	private File dir;
	public static final Image FOLDER_IMAGE = new Image(TestNodeFolder.class.getClass().getResourceAsStream("/folder_img.png"));
	
	public TestNodeFolder(File dir) {
		super();
		Objects.requireNonNull(dir, "File 객체가 Null입니다.");
		this.dir = dir;
		setValue(this);
		setGraphic(new ImageView(FOLDER_IMAGE));
		setChildrenTestNode(dir);
	}

	@Override
	public void test() {
		getChildren().forEach(child -> child.getValue().test());
	}

	@Override
	public TestNodeType getType() {
		return TestNodeType.FOLDER;
	}

	@Override
	public String toString() {
		return dir.getName();
	}

	@Override
	public void print() {

	}

	private void setChildrenTestNode(File dir) {
		File[] fileList = dir.listFiles();
		for (File file : fileList) {
			if (file.isFile()) {
				if(file.getAbsolutePath().endsWith(".yaml")) {
					getChildren().add(new TestNodeFile(file));
				}
			} else if (file.isDirectory()) {
				getChildren().add(new TestNodeFolder(file));
			}
		}
	}
	
	@Override
	public File getFile() {
		return dir;
	}
}
