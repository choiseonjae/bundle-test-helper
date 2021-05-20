package kr.co.danal.model.javafx.testnode;

import java.io.File;

import javafx.scene.control.TreeItem;
import kr.co.danal.model.TestNodeType;

abstract public class TestNode extends TreeItem<TestNode>{
	
	abstract public void test();
	
	abstract public TestNodeType getType();
	
	abstract public void print();
	
	abstract public File getFile();

}
