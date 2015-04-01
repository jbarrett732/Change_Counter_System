import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

public class PrintPageTest {

	public File testFile;
	public File testAdd;
	public File testSub;
	public File testAddSub;
	public File emptyFile;
	public File largeFile;
	public PrintPage pp;
	
	@Before
	public void setUp() throws Exception {
		testFile = new File("/Users/johnbarrett/Desktop/GradSchool/SE580/Code/Change_Counter_System/file.txt");
		testAdd = new File("/Users/johnbarrett/Desktop/GradSchool/SE580/Code/Change_Counter_System/file3.txt");
		testSub = new File("/Users/johnbarrett/Desktop/GradSchool/SE580/Code/Change_Counter_System/file4.txt");
		testAddSub = new File("/Users/johnbarrett/Desktop/GradSchool/SE580/Code/Change_Counter_System/file2.txt");
		emptyFile = new File("/Users/johnbarrett/Desktop/GradSchool/SE580/Code/Change_Counter_System/empty_file.txt");
		largeFile = new File("/Users/johnbarrett/Desktop/GradSchool/SE580/Code/Change_Counter_System/large_file.txt");
		pp = new PrintPage();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void lineCounter_1() {
		int lineCount = pp.lineCount(testFile);
		assertEquals(3, lineCount);
	}

	@Test
	public void lineCounter_2() {
		int lineCount = pp.lineCount(emptyFile);
		assertEquals(0, lineCount);
	}
	
	@Test
	public void lineCounter_3() {
		int lineCount = pp.lineCount(largeFile);
		assertEquals(1075, lineCount);
	}
	
	@Test
	public void fileDiff_1() {
		ArrayList<String> actual = pp.differ(testFile,testAdd);
		String expected = "Added line";
		assertEquals(expected, actual.get(0));
	}
	
	@Test
	public void fileDiff_2() {
		ArrayList<String> actual = pp.differ(testSub,testFile);
		String expected = "This is a test file for SE580";
		assertEquals(expected, actual.get(0));
	}
	
	@Test
	public void fileDiff_3() {
		fail("Not yet implemented");
		
		ArrayList<String> actual1 = pp.differ(testFile, testAdd);
		ArrayList<String> actual2 = pp.differ(testAdd, testFile);
		//for String
		
		String expected = "Hello there\nCoding is fun\nHello\nMy Name is John\n";
		assertEquals(expected, actual);
		
		fail("Not yet implemented");
		
	}
}
