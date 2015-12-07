package com.example.test.nio2;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.example.util.ThreadFactoryUtils;

/**
 * @author gimbyeongsu
 * 
 */
public class WatchServiceTest {

	// http://freeminderhuni.blogspot.kr/2013/09/7-nio2-6.html

	// ENTRY_CREATE : 디렉토리 항목이 생성됐다.
	// 해당 디렉토리에 있는 파일 이름 바뀌거나 이동한 경우에도 발생
	// ENTRY_DELETE : 디렉토리 항목이 삭제.
	// 해당 디렉토리에 있는 파일 이름 바뀌거나 파일이 디렉토리 바깥으로 이동한 경우에도 발생
	// ENTRY_MODIFY : 디렉토리 항목이 수정됐다.
	// 어느 이벤트가 수정인지는 플랫폼에 따라 다소 다를 수 있음.
	// 실제로 파일의 내용이 수정됐을 때 항상 수정 이벤트가 발생한다.
	// 일부 플랫폼에서는 파일의 속성이 변경해도 이 이벤트가 발생
	// OVERFLOW : 이벤트가 소실됐거나 버려진 경우. 이 이벤트를 받으려 등록할 필요는 없다.

	// poll() : 이용할 수 있는 키가 없으면 즉시 null 값을 반환한다.
	// poll(long,TimeUnit) : 이용할 수 있는 키가 없으면 지저된 시간동안 대기한 다음 다시 시도한다.
	// 다시 시도 하였을 때도 이용할 수 있는 키가 없으면 null을 반환 한다.
	// long 대기시간 , TimeUnit 시간 단위
	// take() : 이용할 수 있는 키가 없으면 큐에 키가 들어올 때까지 대기한다.
	// 몇가지 조건에 대해서는 무한 루프를 중지 한다.
	
	@Test
	public void test() throws Exception {
		
		ExecutorService service = Executors.newSingleThreadExecutor(new ThreadFactoryUtils("WATCH",
				false, Thread.NORM_PRIORITY));
		final FileSystem fs = FileSystems.getDefault();
		final WatchService ws = fs.newWatchService();
		final Map<WatchKey, Path> keys = new ConcurrentHashMap<>();

		reg(fs.getPath("bin/com/example/test"), keys, ws);
		reg(fs.getPath("lib"), keys, ws);

		service.execute(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("START");
				while (Thread.interrupted() == false) {
					WatchKey key;
					try {
						key = ws.poll(10, TimeUnit.MILLISECONDS);
					} catch (InterruptedException | ClosedWatchServiceException e) {
						break;
					}
					if (key != null) {
						Path path = keys.get(key);
						for (WatchEvent<?> i : key.pollEvents()) {
							WatchEvent<Path> event = cast(i);
							WatchEvent.Kind<Path> kind = event.kind();
							Path name = event.context();
							Path child = path.resolve(name);
							System.out.printf("%s: %s %s%n", kind.name(), path, child);
							if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
								if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
									try {
										walk(child, keys, ws);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
						if (key.reset() == false) {
							System.out.printf("%s is invalid %n", key);
							keys.remove(key);
							if (keys.isEmpty()) {
								break;
							}
						}
					}
				}
				System.out.println("END");
			}
		});
		while (true) {
			Path p = fs.getPath("bin/com/example/test/end");
			if (Files.isReadable(p)) {
				ws.close();
				service.shutdownNow();
				break;
			}
			Thread.sleep(200L);
		}
	}

	private void walk(Path root, final Map<WatchKey, Path> keys, final WatchService ws)
			throws IOException {
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
					throws IOException {
				reg(dir, keys, ws);
				return super.preVisitDirectory(dir, attrs);
			}
		});
	}

	private void reg(Path dir, Map<WatchKey, Path> keys, WatchService ws) throws IOException {
		System.out.println(dir.toAbsolutePath());
		WatchKey key = dir.register(ws, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
	}

	@SuppressWarnings("unchecked")
	private WatchEvent<Path> cast(WatchEvent<?> event) {
		return (WatchEvent<Path>) event;
	}
}
