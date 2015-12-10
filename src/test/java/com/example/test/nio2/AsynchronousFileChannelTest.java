package com.example.test.nio2;

import static com.example.test.TestUtils.DS;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.example.test.TestUtils;
import com.example.util.ThreadFactoryUtils;

/**
 * @author gimbyeongsu
 * 
 */
public class AsynchronousFileChannelTest {

	/**
	 * 작은 파일 읽고 쓰기
	 */
	@Test
	public void test() throws Exception {
		
		String fileName = "smallFile.txt";
		
		String dir = TestUtils.TEST_DIR;
		Path path = Paths.get(dir + DS + fileName);

		try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"),
				StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
			for (int i = 0; i < 100; i++) {
				writer.write("안녕하세요.");
			}
			writer.flush();
		}

		List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
		for (String line : lines) {
			System.out.println(line);
		}

		Files.deleteIfExists(path);
	}
	
	@Test
	public void testSimple() throws Exception {
		
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Path file = Paths.get(dir + DS + "file" , "simple.txt");
		String s = "DEADBEEF";
		byte data[] = s.getBytes();
		// write
		try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(file,
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE)) {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			Future<Integer> pendingResult = fileChannel.write(buffer, 0);
			pendingResult.get();
			fileChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// read
		try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(file)) {
			ByteBuffer buffer = ByteBuffer.allocate(100);
			Future<Integer> pendingResult = fileChannel.read(buffer, 0);
			pendingResult.get();
			buffer.rewind();
			if (pendingResult.isDone()) {
				byte[] bytes = new byte[buffer.position()];
				buffer.get(bytes);
				System.out.print(Charset.forName("UTF-8").decode(buffer));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 대용량 파일 쓰기
	 */
	@Test
	public void testWrite() throws Exception {
		final Thread current = Thread.currentThread();
		
		String fileName = "bigFile.txt";
		
		byte[] bytes = {65,65,65,65,65,65};
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		String dir = TestUtils.TEST_DIR;
		Path path = Paths.get(dir + DS + fileName);
		
		ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactoryUtils(
				"WRITE", false, 5));
		
		Set<OpenOption> opts =  new HashSet<OpenOption>(2);
		opts.add(StandardOpenOption.CREATE);
		opts.add(StandardOpenOption.WRITE);
		AsynchronousFileChannel channel = AsynchronousFileChannel.open(path,
				opts, executor, new FileAttribute[0]);
		CompletionHandler<Integer, String> handler = new CompletionHandler<Integer, String>() {

			@Override
			public void completed(Integer result, String attachment) {
				System.out.println(attachment + " 파일 쓰기 완료 " + result + " bytes");
				current.interrupt();
			}

			@Override
			public void failed(Throwable e, String attachment) {
				e.printStackTrace();
			}
		};
		
		int position = 0;
		String attachment = fileName;
		channel.write(buffer, position, attachment, handler);
		
		System.out.println("파일을 다 쓸때가지 대기");
		try {
			current.join();
		} catch (InterruptedException e) {
			// ignored
		}
		
		channel.close();
	}
	
	/**
	 * 대용량 파일 읽기
	 */
	@Test
	public void testRead() throws Exception {

		final Thread current = Thread.currentThread();
		String fileName = "bigFile.txt";
		
		String dir = TestUtils.TEST_DIR;
		Path path = Paths.get(dir + DS + fileName);
		
		ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactoryUtils(
				"READ", false, 5));
		final AsynchronousFileChannel channel = AsynchronousFileChannel.open(path,
				new HashSet<OpenOption>(0), executor, new FileAttribute[0]);
		final ByteBuffer buffer = ByteBuffer.allocate(1024);
		final StringBuilder content = new StringBuilder();
		channel.read(buffer, 0, 0, new CompletionHandler<Integer, Integer>() {
			
			@Override
			public void completed(Integer length, Integer totalLength) {
				if (length == -1) {
					try {
						channel.close();
					} catch (IOException e) {
						// ignored
					}
					current.interrupt();
					return;
				}
				buffer.flip();
				byte[] bytes = new byte[length];
				buffer.get(bytes, 0, length);
				content.append(new String(bytes));
				buffer.clear();
				channel.read(buffer, totalLength, totalLength + length, this);
			}

			@Override
			public void failed(Throwable e, Integer length) {
				e.printStackTrace();
			}
		});
		
		System.out.println("파일을 다 읽을때가지 대기");
		try {
			current.join();
		} catch (InterruptedException e) {
			// ignored
		}
		System.out.println(content.toString());
	}
}
