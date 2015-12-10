package com.example.test.nio2;

import static com.example.test.TestUtils.DS;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

import com.example.test.TestUtils;

/**
 * @author gimbyeongsu
 * 
 */
public class FilesTest {
	
	// StandardCopyOption.ATOMIC_MOVE 복사 연산을 어토믹 하게 함
	// StandardCopyOption.COPY_ATTRIBUTES 원본파일의 속성까지 모두 복사
	// StandardCopyOption.REPLACE_EXISTING 복사대상파일이 존재하면 덮어쓰기를 함

	// StandardOpenOption.READ 읽기 전용으로 파일을 연다.
	// StandardOpenOption.WRITE 쓰기 전용으로 파일을 연다.
	// StandardOpenOption.CREATE 파일이 없다면 새 파일을 생성한다.
	// StandardOpenOption.CREATE_NEW 새 파일을 만든다. 파일이 있으면 예외와 함께 실패한다.
	// StandardOpenOption.APPEND 파일 끝에 데이터를 추가한다. (WRITE나 CREATE와 함께 사용됨)
	// StandardOpenOption.DELETE_ON_CLOSE 스트림을 닫을때 파일을 삭제한다. (임시파일을 삭제할때 사용)
	// StandardOpenOption.TRUNCATE_EXISTING 파일을 0바이트로 잘라낸다. (WRITE와 함께 사용)
	// StandardOpenOption.SPARSE 새로 생성하는 파일이 희소 할수있다.
	// StandardOpenOption.SYNC 파일 내용과 메타 데이터를 기반 저장소 디바이스와 동기화 한다.
	// StandardOpenOption.DSYN C파일 내용을 기반 저장소와 동기화 한다.

	/**
	 * 디렉터리 만들기
	 */
	@Test
	public void testCreateDirectory() throws Exception {
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Files.createDirectory(Paths.get(dir + DS + "file", "mkdir"));
	}
	
	/**
	 * 빈 파일 만들기
	 */
	@Test
	public void testCreateEmptyFile() throws Exception {
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Files.createFile(Paths.get(dir + DS + "file" + DS + "tmp", "java7.txt"));
	}
	
	/**
	 * 파일 쓰기
	 */
	@Test
	public void testFileWrite() throws Exception {
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();

		Path path = Paths.get(dir + DS + "file", "tmp.jpeg");
		Files.write(path, TestUtils.TMP_JPEG_BYTES, CREATE_NEW);
	}
	
	/**
	 * 파일 이동
	 */
	@Test
	public void testMove() throws Exception {
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Path sourcePath = Paths.get(dir + DS + "file", "java7.txt");
		Path targetPath = Paths.get(dir + DS + "file" + DS + "move", "java7.txt");

		Files.move(sourcePath, targetPath, REPLACE_EXISTING, ATOMIC_MOVE);
	}

	/**
	 * 파일 복사
	 */
	@Test
	public void testCopy() throws Exception {

		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Path sourcePath = Paths.get(dir + DS + "file", "java7.txt");
		Path targetPath = Paths.get(dir + DS + "file" + DS + "copy", "java7.txt");

		Files.copy(sourcePath, targetPath, COPY_ATTRIBUTES);
	}

	/**
	 * 디렉터리 및 파일 삭제
	 */
	@Test
	public void testDelete() throws Exception {

		String dir = TestUtils.TEST_DIR;
		testFileWrite();
		
		Path path = Paths.get(dir + DS + "file", "tmp.jpeg");
		// Files.delete(path);
		Files.deleteIfExists(path); // return boolean
		
		Path dirDel = Paths.get(dir + DS + "file", "copy");
		Files.delete(dirDel);
	}
	
	/**
	 * 심볼릭 링크 만들기
	 */
	@Test
	public void testCreateSymbolicLink() throws Exception {
		
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Path target = Paths.get(dir + DS + "file", "java7.txt");
		Path link = Paths.get(dir + DS + "file", "java7.txt.link");
		
		Files.createSymbolicLink(link, target);
		
		// lrwxr-xr-x 1 gimbyeongsu staff 43B 12 7 09:41 java7.txt.link -> /Users/gimbyeongsu/tmp_java7/file/java7.txt
	}
	
	/**
	 * 임시 파일 및 디렉터리 만들기
	 */
	@Test
	public void testTemp() throws Exception {
		
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Path tempDirectory = Files.createTempDirectory(Paths.get(dir + DS + "file"), "");
		String dirPath = tempDirectory.toString();
		System.out.println(dirPath);
        // /Users/gimbyeongsu/tmp_java7/file/7917053882407349208
        
		Path tempFile = Files.createTempFile(tempDirectory, "", "");
		String filePath = tempFile.toString();
		System.out.println(filePath);
        // /Users/gimbyeongsu/tmp_java7/file/7917053882407349208/3950731812345446005
	}
	
	// SimpleFileVisitor 클래스
	// 이미 FileVisitor의 모든 메소드를 구현한 클래스, 일반적으로 이것을 상속에서 사용한다.
	
	@Test
	public void testWalkFileTree() throws Exception {
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		Path path = Paths.get(dir + DS + "file");
		
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException {
				// 디렉토리에 있는 파일에 대해 호출된다.
				// 보통 CONTINUE나 TERMINATE를 결과로 반환한다.
				// 예를 들면 어떻 파일을 찾을 때까지는 CONTINUE, 파일을 발견한 다음에는 TERMINATE를 반환 한다.
				return super.visitFile(file, attrs);
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
					throws IOException {
				// 디렉토리에 있는 항목을 방문하기 전에 디렉토리에 대해 호출된다.
				return super.preVisitDirectory(dir, attrs);
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException ex) throws IOException {
				// 디렉토리에 있는 항목(후손까지)을 모두 방문하거나(IO 에러 발생해서 중단 시킨 경우) 방문이 갑자기 중단된 후에 호출된다.
				// 인자 IOException은 방문하는 에러가 없다면 nul 이다.
				return super.postVisitDirectory(dir, ex);
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException ex) throws IOException {
				// 파일을 어떤 이유로 접근할 수 없을 때 호출.
				return super.visitFileFailed(file, ex);
			}
		});
	}
	
	/**
	 * 하위 디렉터리 포함 삭제
	 */
	@Test
	public void testWalkFileTreeDelete() throws Exception {
		
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		Path path = Paths.get(dir + DS + "file");
		
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException {
				Files.deleteIfExists(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path file, IOException e) throws IOException {
				Files.deleteIfExists(file);
				return FileVisitResult.CONTINUE;
			}
		});
		
		// FileVisitResult.CONTINUE
		// 순회 과정이 계속 진행 중인 상태
		// FileVisitResult.TERMINATE
		// 순회 과정이 종료 되어야 함
		// FileVisitResult.SKIP_SIBLINGS
		// 방문 중인 파일이나 디렉토리의 형제를 방문하지 않아도 순회 과정은 지속
		// FileVisitResult.SKIP_SUBTREE
		// 방문 중인 디렉토리의 다른 항목을 방문하지 않아도 순회 과정이 지속
	}
	
	/**
	 * 파일 검색
	 */
	@Test
	public void testSearch() throws Exception {
		
		String dir = TestUtils.TEST_DIR;
		testCreateEmptyFile();
		
		final String findFile = File.separator + "java7.txt";
		Path path = Paths.get(dir + DS + "file");
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException {
				String fileStr = file.toAbsolutePath().toString();
				if (fileStr.endsWith(findFile)) {
					System.out.println("search file:" + fileStr);
				}
				return FileVisitResult.CONTINUE;
			}
		});
		// search file:/Users/gimbyeongsu/tmp_java7/file/java7.txt
		// search file:/Users/gimbyeongsu/tmp_java7/file/tmp/java7.txt
	}
}
