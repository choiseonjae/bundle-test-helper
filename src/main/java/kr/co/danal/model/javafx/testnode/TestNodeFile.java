package kr.co.danal.model.javafx.testnode;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.danal.model.TestNodeType;
import kr.co.danal.util.AlertUtil;

public class TestNodeFile extends TestNode {

	private File file;
	// 파일에 있는 실행 가능한 테스트 프로세스들
	private List<Thread> runnableProcessors = new ArrayList<>();
	private static final Image FILE_IMAGE = new Image(TestNodeFolder.class.getClass().getResourceAsStream("/file_img.png"));

	public TestNodeFile(File file) {
		super();
		setValue(this);
		setGraphic(new ImageView(FILE_IMAGE));
		this.file = file;
		List<TestNodeProcessor> testProcessors = getTestNodeProcessors(file);
		testProcessors.forEach(node -> runnableProcessors.add(new Thread(node)));
	}

	@Override
	public void test() {
		runnableProcessors.forEach(node -> node.start());
		runnableProcessors.clear();
		getChildren().forEach(node -> runnableProcessors.add(new Thread((TestNodeProcessor)node)));
	}

	@Override
	public TestNodeType getType() {
		return TestNodeType.FILE;
	}

	@Override
	public String toString() {
		return file.getName();
	}

	@Override
	public void print() {
		joinAllTestNode();
		getChildren().forEach(node -> ((TestNode)node).print());
	}

	private void joinAllTestNode() {
		for(Thread node : runnableProcessors) {
			try {
				node.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				AlertUtil.error(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<TestNodeProcessor> getTestNodeProcessors(File file) {
		try {
			List<TestNodeProcessor> testProcesses = new ArrayList<>();
			List<Object> everyProcess = new Yaml().loadAs(new FileInputStream(file), ArrayList.class);

			// 하나 파일 중 가능한 프로세스들만 추출
			Function<Object, List<Object>> objectToList = process -> (List<Object>) process;
			Predicate<List<Object>> isNotNull = process -> process.stream().allMatch(Objects::nonNull);
			Function<List<Object>, TestNodeProcessor> listToNode = process -> new TestNodeProcessor(this, process);
			for (Object process : everyProcess) {

				Objects.requireNonNull(process);
				if ("STOP".equalsIgnoreCase(process.toString())) {
					break;
				}

				if (isNotNull.test(objectToList.apply(process))) {
					testProcesses.add(listToNode.apply(objectToList.apply(process)));
				}
			}
			getChildren().addAll(testProcesses);
			return testProcesses;
		} catch (ParserException e) {
			AlertUtil.error(e, String.format("%s 파일에서 Yaml 문법이 잘못됐습니다.", file.getName()));
			return new ArrayList<>();
		} catch (Exception e) {
			AlertUtil.error(e);
			return new ArrayList<>();
		}
	}

	@Override
	public File getFile() {
		return file;
	}
}
