package com.example.test.nio2;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;

import org.junit.Test;

import com.example.test.TestUtils;

/**
 * @author gimbyeongsu
 *
 */
public class MetaDataTest {
	
	/**
	 * 파일 시스템에서 지원하는 뷰 목록
	 * @throws Exception
	 */
	@Test
	public void testSupportedFileAttributeViews() throws Exception {
		Set<String> attrsSet = FileSystems.getDefault().supportedFileAttributeViews();
		for (String s : attrsSet) {
			System.out.println(s);
		}
		// basic
		// owner
		// unix
		// posix
		
		// java.nio.file.attribute.BasiceFileAttributeView
		// 모든 파일시스템 구현물에서 지원해야 하는 기본 속성에 대한 뷰를 제공
		// 속성 뷰의 이름은 basic
		// java.nio.file.attribute.DosFileAttributeView
		// 도스(DOS)속성을 지원하는 파일시스템에서 지원하는 네 가지 표준 속성에 대한 뷰를 제공
		// 이 속성 뷰의 이름은 dos다.
		// java.nio.file.attribute.PosixFileAttributeView
		// basic 속성 뷰를 확장한 뷰로 유닉스 같은 POSIX 표준을 지원하는 파일시스템의 속성을 보여줌
		// 이 속성 뷰의 이름은 posix다
		// java.nio.file.attribute.FileOwnerAttributeView
		// 파일 소유자 개념을 지원하는 파일시스템 구현물에서 지원하는 뷰
		// 속성 뷰의 이름은 owner
		// java.nio.file.attribute.AclFileAttributeView
		// 파일은 ACL 읽기나 업데이트를 지원하는 뷰
		// NFSv4 ACL 모델을 지원한다.
		// 이 속성 뷰의 이름은 acl
		// java.nio.file.attribute.UserDefinedFileAttributeView
		// 사용자가 정의한 메타데이터를 지원하는 뷰
	}
	
	/**
	 * BasicFileAttributes 뷰
	 */
	@Test
	public void testBasicFileAttributes() throws Exception {
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Path path = Paths.get(dir + "/file", "java7.txt");
		BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

		StringBuilder sb = new StringBuilder();
		sb.append("File: " + path);
		sb.append("\n");
		sb.append("\t").append("tsize: " + attrs.size());
		sb.append("\n");
		sb.append("\t").append("creationTime: " + attrs.creationTime());
		sb.append("\n");
		sb.append("\t").append("lastAccessTime: " + attrs.lastAccessTime());
		sb.append("\n");
		sb.append("\t").append("lastModifiedTime: " + attrs.lastModifiedTime());
		sb.append("\n");
		sb.append("\t").append("isRegularFile: " + attrs.isRegularFile());
		sb.append("\n");
		sb.append("\t").append("isDirectory: " + attrs.isDirectory());
		sb.append("\n");
		sb.append("\t").append("isSymbolicLink: " + attrs.isSymbolicLink());
		sb.append("\n");
		sb.append("\t").append("isOther: " + attrs.isOther());
		sb.append("\n");
		System.out.println(sb.toString());
		//    File: /Users/gimbyeongsu/tmp_java7/java7.txt
		//    tsize: 30
		//    creationTime: 2015-11-24T08:39:37Z
		//    lastAccessTime: 2015-11-25T02:41:03Z
		//    lastModifiedTime: 2015-11-24T08:39:37Z
		//    isRegularFile: true
		//    isDirectory: false
		//    isSymbolicLink: false
		//    isOther: false
	}
	
	/**
	 * 파일 소유자 확인
	 */
	@Test
	public void testFileOwnerAttributeView() throws Exception {
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Path path = Paths.get(dir + "/file", "java7.txt");
		FileOwnerAttributeView attrs = Files.getFileAttributeView(path,
				FileOwnerAttributeView.class);
		UserPrincipal owner = attrs.getOwner();
		System.out.println("owner:" + owner.getName());
	}
	
	/**
	 * 파일 권한 확인
	 * @throws Exception
	 */
	@Test
	public void testPosixFileAttributes() throws Exception {
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Path path = Paths.get(dir + "/file", "java7.txt");
		
		PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(path,
				PosixFileAttributeView.class);
		if (fileAttributeView != null) {
			PosixFileAttributes readAttributes = fileAttributeView.readAttributes();
			String owner = readAttributes.owner().getName();
			Set<PosixFilePermission> permissions = readAttributes.permissions();
			System.out.format("%s %s%n", owner, permissions);
			permissions.add(PosixFilePermission.OWNER_EXECUTE);
			fileAttributeView.setPermissions(permissions);
			System.out.format("%s %s%n", owner, permissions);
			System.out.println(PosixFilePermissions.toString(permissions));
		}
	}
	
	/**
	 * 유닉스 기반 OS 파일 권한 설정 및 파일 속성 지정
	 * @throws Exception
	 */
	@Test
	public void testAttributes() throws Exception {
		String dir = TestUtils.TEST_DIR;
		TestUtils.fileTest();
		
		Path path = Paths.get(dir + "/file", "java7.txt");
		Path newPath = Paths.get(dir + "/file" ,"java7.new.txt");
		
		// 1970년 1월 1일 오전 09:00:00
		Files.setLastModifiedTime(path, FileTime.fromMillis(0));
		
		Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
		Files.createFile(newPath, PosixFilePermissions.asFileAttribute(perms));
		// -rwxr-x---   1 gimbyeongsu  staff     0B 11 25 13:46 java7.new.txt
		// -rw-r--r--   1 gimbyeongsu  staff    30B  1  1  1970 java7.txt
	}
}
