package com.example.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

/**
 * @author gimbyeongsu
 * 
 */
public class AutoResCloseTest {

	/**
	 * java7 이전 자원 해제
	 */
	@Test
	public void test() throws Exception {

		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		FileInputStream fin = null;
		BufferedReader br = null;
		try {
			fin = new FileInputStream(dir + "/" + "file/" + "java7.txt");
			br = new BufferedReader(new InputStreamReader(fin));
			if (br.ready()) {
				String line1 = br.readLine();
				System.out.println(line1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}

	}

	/**
	 * java7 자원 해제
	 */
	@Test
	public void test7() throws Exception {
		
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		// AutoClosable, Closeable 인터페이스를 구현한 경우
		// try(resource)내의 resource들에 대해 close()를 수행
		try (FileInputStream fin = new FileInputStream(dir + "/" + "file/" + "java7.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(fin));) {
			if (br.ready()) {
				String line1 = br.readLine();
				System.out.println(line1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
