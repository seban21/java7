package com.example.test.nio2;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.example.test.TestUtils;

/**
 * @author gimbyeongsu
 *
 */
public class PathTest {
	
	@Test
	public void test() throws Exception {
		
		// Path 클래스 두 가지 용도
		// 문법적인 용도로 파일시스템에 접근하지 않고 경로를 조작하는 작업에 주로 사용 (메모리상의 논리적인 조작)
		// 경로를 참조하는 파일에 대한 작업
		
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		File file = new File(dir + "/file/java7.txt");
		Path path = Paths.get(dir + "/file/java7.txt");
		File file2 = path.toFile();
		Path path2 = file.toPath();
		
		int nameCount = path.getNameCount();
		Path pathTmp = Paths.get(dir + "/file/tmp/README.txt");
		Path rootPath = Paths.get(dir + "/file/java7.txt");
		
		System.out.println(path.toString());
		System.out.println(path.getFileName().toString());
		System.out.println(path.toUri().toString()); // URI
		System.out.println(path.toAbsolutePath().toString()); // 절대 경로
		System.out.println(rootPath.getRoot().toString());
		
		System.out.println(nameCount);
		System.out.println(pathTmp.getNameCount());
		
		System.out.println();
		System.out.println(file);
		System.out.println(path);
		System.out.println(file2);
		System.out.println(path2);
	}
}
