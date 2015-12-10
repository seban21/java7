package com.example.test.nio2;

import static com.example.test.TestUtils.DS;

import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

import com.example.test.TestUtils;

/**
 * @author gimbyeongsu
 * 
 */
public class SeekableByteChannelTest {

	/**
	 * 임위 위치 파일 읽어 들이기
	 */
	@Test
	public void testRandomAccessRead() throws Exception {
		
		String dir = TestUtils.TEST_DIR;
		int bufferSize = 8;
		Path path = Paths.get(dir + DS + "file", "java7.txt");

		try (SeekableByteChannel sbc = Files.newByteChannel(path)) {
			ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
			sbc.position(4);
			sbc.read(buffer);
			for (int i = 0; i < 5; i++) {
				System.out.print((char) buffer.get(i));
			}
			System.out.println();

			buffer.clear();
			sbc.position(0);
			sbc.read(buffer);
			for (int i = 0; i < 4; i++) {
				System.out.print((char) buffer.get(i));
			}
			System.out.println();
		}
		// .7.0q
		// jdk1
	}

	/**
	 * 임의 위치 파일 쓰기
	 */
	@Test
	public void testRandomAccessWrite() throws Exception {

		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();

		Path path = Paths.get(dir + DS + "file", "java7.txt");
		final String newLine = System.getProperty("line.separator");

		try (SeekableByteChannel sbc = Files.newByteChannel(path, StandardOpenOption.WRITE)) {
			long position = sbc.size();
			sbc.position(position);
			System.out.println("position:" + sbc.position());
			ByteBuffer buffer1 = ByteBuffer.wrap((newLine + "맨끝에추가1").getBytes());
			sbc.write(buffer1);
			System.out.println("position:" + sbc.position());
			ByteBuffer buffer2 = ByteBuffer.wrap((newLine + "맨끝에추가2").getBytes());
			sbc.write(buffer2);
			System.out.println("position:" + sbc.position());
			ByteBuffer buffer3 = ByteBuffer.wrap((newLine + "맨끝에추가3").getBytes());
			sbc.write(buffer3);
			System.out.println("position:" + sbc.position());
		}
		// $ cat java7.txt
		// jdk1.7.0qwerty
		// abc
		// 0123456789
		// 
		// 맨끝에추가1
		// 맨끝에추가2
		// 맨끝에추가3
	}
}
