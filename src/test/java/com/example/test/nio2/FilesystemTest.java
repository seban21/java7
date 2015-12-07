package com.example.test.nio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.util.Set;

import org.junit.Test;

/**
 * @author gimbyeongsu
 * 
 */
public class FilesystemTest {
	
	/**
	 * 지원하는 FileSystem 확인
	 */
	@Test
	public void testFileSystem() throws Exception {
		
		// JVM에 기본 FileSystem(OS 기본 파일시스템)을 반환
		FileSystem fileSystem = FileSystems.getDefault();
		Set<String> fileSystemViews = fileSystem.supportedFileAttributeViews();
		for (String fileSystemView : fileSystemViews) {
			System.out.println(fileSystemView);
		}
		// basic
		// owner
		// unix
		// posix
		
	}
	
	/**
	 * 지원하는 FileAttributeView 확인
	 */
	@Test
	public void testSupportsFileAttributeView() throws Exception {
		
		FileSystem fileSystem = FileSystems.getDefault();
		Iterable<FileStore> fileStores = fileSystem.getFileStores();
		for (FileStore fileStore : fileStores) {
			System.out.println(String.format("Filestore %s supports (%s) : %s", fileStore,
					BasicFileAttributeView.class.getSimpleName(),
					fileStore.supportsFileAttributeView(BasicFileAttributeView.class)));
			System.out.println(String.format("Filestore %s supports (%s) : %s", fileStore,
					DosFileAttributeView.class.getSimpleName(),
					fileStore.supportsFileAttributeView(DosFileAttributeView.class)));
			System.out.println(String.format("Filestore %s supports (%s) : %s", fileStore,
					PosixFileAttributeView.class.getSimpleName(),
					fileStore.supportsFileAttributeView(PosixFileAttributeView.class)));
			System.out.println(String.format("Filestore %s supports (%s) : %s", fileStore,
					AclFileAttributeView.class.getSimpleName(),
					fileStore.supportsFileAttributeView(AclFileAttributeView.class)));
			System.out.println(String.format("Filestore %s supports (%s) : %s", fileStore,
					FileOwnerAttributeView.class.getSimpleName(),
					fileStore.supportsFileAttributeView(FileOwnerAttributeView.class)));
			System.out.println();
		}
		
	}
	
	/**
	 * 스토리지 정보 확인
	 */
	@Test
	public void testStores() throws Exception {
		
		FileSystem fileSystem = FileSystems.getDefault();
		StringBuilder sb = new StringBuilder();
		for (FileStore fileStore : fileSystem.getFileStores()) {
			try {
				sb.setLength(0);
				sb.append("File store - ");
				sb.append(fileStore).append("\n");

				sb.append("\t").append("Total space: ");
				sb.append(fileStore.getTotalSpace() / 1024).append("\n");

				sb.append("\t").append("Used space: ");
				sb.append((fileStore.getTotalSpace() - fileStore.getUnallocatedSpace()) / 1024);
				sb.append("\n");

				sb.append("\t").append("Available space: ");
				sb.append(fileStore.getUsableSpace() / 1024).append("\n");

				sb.append("\t").append("Read-Only: ");
				sb.append(fileStore.isReadOnly()).append("\n");

				System.out.println(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// File store - / (/dev/disk0s2)
		// Total space: 975922976
		// Used space: 212371832
		// Available space: 763295144
		// Read-Only: false
		// ...
		
	}
	
	/**
	 * jar 파일 내부 읽기
	 */
	@Test
	public void testJar() throws Exception {
		
		Path jarfile = Paths.get("./lib/junit-4.12.jar");
		FileSystem fs = FileSystems.newFileSystem(jarfile, null);
		
		Path mf = fs.getPath("/META-INF/MANIFEST.MF");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(Files.newInputStream(mf)));
			while (in.ready()) {
				System.out.println(in.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
		
	}
}
