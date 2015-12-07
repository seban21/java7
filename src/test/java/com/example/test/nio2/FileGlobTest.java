package com.example.test.nio2;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * @author gimbyeongsu
 *
 */
public class FileGlobTest {
	
	// Glob 패턴
	// * : 0개 이상의 문자
	// ** : * 과 비슷하지만, 디렉토리의 경계를 넘을 수 있음.
	// ? : 정확하게 1 문자
	// { } : 콤마로 나뉜 하위 패턴의 컬렉션. 예) {A,B,C}는 A,B,C와 일치함.
	// [ ] : 대체할 문자 집합 표현 한다. - 문자를 사용하면 문자의 범위를 표현한다.
	// [ ] 내부에서 *,?,\ 는 문자 그대로 매칭
	// *, ?를 비롯한 특수 문자와 일치하는 것을 찾으려면 역슬래시 문자를 사용해서 이스케이프 해야 한다.
	// \\는 \문자 하나와 매칭하면, \?는 ?문자 하나와 매칭한다.
	// 이외의 문자는 모두 문자 그래로 매칭
	
	/**
	 * 확장자 기준 필터
	 */
	@Test
	public void testFilter() throws Exception {
		
		String glob = "*.{java,class}";
		Path path = Paths.get("./src/test/java/com/example/test");
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, glob)) {
			for (Path file : ds) {
				System.out.println(file.getFileName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// AutoResCloseTest.java
		// BinaryTest.java
		// CatchingMultipleExceptionTest.java
		// ...
		
	}
}
