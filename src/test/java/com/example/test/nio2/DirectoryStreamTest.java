package com.example.test.nio2;

import static com.example.test.TestUtils.DS;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * @author gimbyeongsu
 *
 */
public class DirectoryStreamTest {
	
	/**
	 * 디렉터리만 필터링
	 */
	@Test
	public void testDirFilter() throws Exception {
		
		Path path = Paths.get("." + DS);
		DirectoryStream.Filter<Path> dsFilter = new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path path) throws IOException {
				
				// 여기에서 조건에 해당하는 것만 수락
				return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
			}
		};
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, dsFilter)) {
			for (Path file : ds) {
				System.out.println(file.getFileName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// .settings
		// bin
		// doc
		// lib
		// scripts
		// src
	}
	
	/**
	 * 1KB보다 큰 파일, 디렉터리만 수락하는 필터
	 */
	@Test
	public void testFilter1K() throws Exception {
		
		Path path = Paths.get("." + DS);
		DirectoryStream.Filter<Path> dsFilter = new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path path) throws IOException {
				return Files.size(path) > 1024L;
			}
		};
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, dsFilter)) {
			for (Path file : ds) {
				System.out.println(file.getFileName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 현재 날짜로 수정된 파일만 수락하는 필터
	 */
	@Test
	public void testFilterToDay() throws Exception {
		
		Path path = Paths.get("." + DS);
		DirectoryStream.Filter<Path> dsFilter = new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path path) throws IOException {
				long currTimeMillis = FileTime.fromMillis(System.currentTimeMillis())
						.to(TimeUnit.DAYS);
				long modifiedTimeMillis = ((FileTime) Files.getAttribute(path,
						"basic:lastModifiedTime", LinkOption.NOFOLLOW_LINKS)).to(TimeUnit.DAYS);

				if (currTimeMillis == modifiedTimeMillis) {
					return true;
				}
				return false;
			}
		};
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, dsFilter)) {
			for (Path file : ds) {
				System.out.println(file.getFileName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
