package com.example.test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gimbyeongsu
 *
 */
public class TestUtils {
	
	public final static String TEST_DIR = System.getProperty("user.home") + "/tmp_java7";
	
	
	public static String toBinaryString(byte b) {
		String s = Integer.toBinaryString(b);
		return String.format("%08d", Integer.parseInt(s));
	}
	
	public static String toBinaryString(short b) {
		String s = Integer.toBinaryString(b);
		StringBuilder sb = new StringBuilder();
		for (int toPrepend = 16 - s.length(); toPrepend > 0; --toPrepend) {
			sb.append('0');
		}
		sb.append(s);
		return sb.toString();
	}
	
	public static String toBinaryString(int b) {
		String s = Integer.toBinaryString(b);
		StringBuilder sb = new StringBuilder();
		for (int toPrepend = 32 - s.length(); toPrepend > 0; --toPrepend) {
			sb.append('0');
		}
		sb.append(s);
		return sb.toString();
	}

	public static void fileTest() {
		String dir = TEST_DIR;
		
		try {
			if (Files.exists(Paths.get(dir)) == false) {
				Files.createDirectory(Paths.get(dir));
			}
			
			if (Files.exists(Paths.get(dir , "file"))) {
//			if (Files.exists(Paths.get(dir + "/file/tmp" + "empty.txt"),
//				new LinkOption[] { LinkOption.NOFOLLOW_LINKS }) == false) {
				// 하위 디렉터리 포함 삭제
				Files.walkFileTree(Paths.get(dir, "file"), new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
							throws IOException {
						Files.deleteIfExists(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path file, IOException e)
							throws IOException {
						Files.deleteIfExists(file);
						return FileVisitResult.CONTINUE;
					}
				});
			}
			
			Files.createDirectory(Paths.get(dir + "/file"));
			Files.createDirectory(Paths.get(dir + "/file/copy"));
			Files.createDirectory(Paths.get(dir + "/file/move"));
			Files.createDirectory(Paths.get(dir + "/file/tmp"));
			Files.createDirectory(Paths.get(dir + "/file/tmp/tmp2"));
			Files.createDirectory(Paths.get(dir + "/file/tmp/tmp2/tmp3"));
			
			Path path = Paths.get(dir + "/file", "java7.txt");
			List<String> list = new ArrayList<>();
			list.add("jdk1.7.0");
			list.add("qwerty");
			list.add("abc");
			list.add("0123456789");
			for (String s : list) {
				if (Files.exists(path)) {
					Files.write(path, s.getBytes("UTF-8"), StandardOpenOption.APPEND);
					Files.write(path, "\n".getBytes("UTF-8"), StandardOpenOption.APPEND);
				} else {
					Files.write(path, s.getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
				}
			}
			Files.write(Paths.get(dir + "/file/tmp/", "README.txt"), "TEST".getBytes("UTF-8"),
					StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
	public final static byte[] TMP_JPEG_BYTES = { (byte) -1, (byte) -40, (byte) -1, (byte) -32, (byte) 0,
		(byte) 16, (byte) 74, (byte) 70, (byte) 73, (byte) 70, (byte) 0, (byte) 1, (byte) 1,
		(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) -1,
		(byte) -37, (byte) 0, (byte) -124, (byte) 0, (byte) 9, (byte) 6, (byte) 7, (byte) 8,
		(byte) 18, (byte) 6, (byte) 20, (byte) 8, (byte) 7, (byte) 8, (byte) 22, (byte) 22,
		(byte) 20, (byte) 11, (byte) 24, (byte) 34, (byte) 25, (byte) 21, (byte) 23, (byte) 22,
		(byte) 13, (byte) 30, (byte) 20, (byte) 21, (byte) 26, (byte) 33, (byte) 32, (byte) 34,
		(byte) 31, (byte) 34, (byte) 30, (byte) 29, (byte) 31, (byte) 29, (byte) 29, (byte) 28,
		(byte) 40, (byte) 33, (byte) 27, (byte) 38, (byte) 37, (byte) 39, (byte) 42, (byte) 31,
		(byte) 33, (byte) 49, (byte) 33, (byte) 38, (byte) 42, (byte) 55, (byte) 46, (byte) 58,
		(byte) 58, (byte) 33, (byte) 31, (byte) 54, (byte) 56, (byte) 56, (byte) 45, (byte) 55,
		(byte) 52, (byte) 49, (byte) 46, (byte) 43, (byte) 1, (byte) 10, (byte) 10, (byte) 10,
		(byte) 14, (byte) 13, (byte) 14, (byte) 27, (byte) 16, (byte) 16, (byte) 26, (byte) 44,
		(byte) 35, (byte) 31, (byte) 37, (byte) 48, (byte) 52, (byte) 46, (byte) 45, (byte) 55,
		(byte) 55, (byte) 54, (byte) 44, (byte) 52, (byte) 52, (byte) 44, (byte) 55, (byte) 50,
		(byte) 56, (byte) 49, (byte) 44, (byte) 44, (byte) 46, (byte) 44, (byte) 53, (byte) 45,
		(byte) 44, (byte) 44, (byte) 52, (byte) 52, (byte) 44, (byte) 52, (byte) 54, (byte) 53,
		(byte) 45, (byte) 55, (byte) 45, (byte) 52, (byte) 50, (byte) 45, (byte) 45, (byte) 52,
		(byte) 44, (byte) 45, (byte) 44, (byte) 46, (byte) 44, (byte) 44, (byte) 44, (byte) 44,
		(byte) 44, (byte) 55, (byte) 45, (byte) 44, (byte) 45, (byte) -1, (byte) -64, (byte) 0,
		(byte) 17, (byte) 8, (byte) 0, (byte) 64, (byte) 0, (byte) 64, (byte) 3, (byte) 1,
		(byte) 17, (byte) 0, (byte) 2, (byte) 17, (byte) 1, (byte) 3, (byte) 17, (byte) 1,
		(byte) -1, (byte) -60, (byte) 0, (byte) 27, (byte) 0, (byte) 1, (byte) 0, (byte) 2,
		(byte) 3, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
		(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 3, (byte) 5,
		(byte) 2, (byte) 4, (byte) 6, (byte) 7, (byte) 1, (byte) -1, (byte) -60, (byte) 0,
		(byte) 51, (byte) 16, (byte) 0, (byte) 2, (byte) 1, (byte) 2, (byte) 3, (byte) 3,
		(byte) 9, (byte) 7, (byte) 5, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
		(byte) 0, (byte) 0, (byte) 1, (byte) 2, (byte) 0, (byte) 3, (byte) 17, (byte) 4,
		(byte) 5, (byte) 33, (byte) 49, (byte) 66, (byte) -127, (byte) 6, (byte) 19, (byte) 34,
		(byte) 65, (byte) 82, (byte) 97, (byte) -95, (byte) -79, (byte) -63, (byte) 18,
		(byte) 67, (byte) 81, (byte) -111, (byte) -62, (byte) -47, (byte) -31, (byte) 21,
		(byte) 98, (byte) 113, (byte) 114, (byte) -126, (byte) -1, (byte) -60, (byte) 0,
		(byte) 26, (byte) 1, (byte) 1, (byte) 0, (byte) 3, (byte) 1, (byte) 1, (byte) 1,
		(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
		(byte) 0, (byte) 0, (byte) 0, (byte) 3, (byte) 4, (byte) 6, (byte) 5, (byte) 2,
		(byte) 1, (byte) -1, (byte) -60, (byte) 0, (byte) 46, (byte) 17, (byte) 0, (byte) 2,
		(byte) 1, (byte) 2, (byte) 4, (byte) 3, (byte) 6, (byte) 6, (byte) 3, (byte) 0,
		(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1,
		(byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 17, (byte) 33, (byte) 49, (byte) 18,
		(byte) 65, (byte) -16, (byte) 6, (byte) 19, (byte) 50, (byte) 113, (byte) -127,
		(byte) -95, (byte) 34, (byte) 81, (byte) -111, (byte) -79, (byte) -63, (byte) -31,
		(byte) 66, (byte) 97, (byte) -47, (byte) -1, (byte) -38, (byte) 0, (byte) 12, (byte) 3,
		(byte) 1, (byte) 0, (byte) 2, (byte) 17, (byte) 3, (byte) 17, (byte) 0, (byte) 63,
		(byte) 0, (byte) -9, (byte) 24, (byte) 2, (byte) 0, (byte) -128, (byte) 98, (byte) -18,
		(byte) -95, (byte) 11, (byte) -69, (byte) 0, (byte) 23, (byte) 82, (byte) 73,
		(byte) -80, (byte) 19, (byte) -28, (byte) -92, (byte) -94, (byte) -77, (byte) 123,
		(byte) 31, (byte) 82, (byte) 109, (byte) -28, (byte) -123, (byte) 55, (byte) 70,
		(byte) 64, (byte) -12, (byte) -38, (byte) -31, (byte) -74, (byte) 17, (byte) 62,
		(byte) 70, (byte) 74, (byte) 75, (byte) -118, (byte) 47, (byte) 52, (byte) 26,
		(byte) 113, (byte) 121, (byte) 51, (byte) 41, (byte) -24, (byte) -8, (byte) 32,
		(byte) 8, (byte) 2, (byte) 0, (byte) -128, (byte) 32, (byte) 21, (byte) 57, (byte) -42,
		(byte) 35, (byte) -92, (byte) 48, (byte) -86, (byte) 127, (byte) 115, (byte) 122,
		(byte) 14, (byte) 39, (byte) 94, (byte) 19, (byte) 61, (byte) -113, (byte) -34,
		(byte) -9, (byte) 84, (byte) -69, (byte) -88, (byte) -18, (byte) -53, (byte) -74,
		(byte) -108, (byte) -9, (byte) -101, (byte) -14, (byte) 95, (byte) -109, (byte) -26,
		(byte) 77, (byte) 94, (byte) -43, (byte) 14, (byte) 25, (byte) -74, (byte) 55,
		(byte) 73, (byte) 126, (byte) -95, (byte) -21, (byte) -60, (byte) -56, (byte) -5,
		(byte) 61, (byte) 123, (byte) -34, (byte) 83, (byte) 116, (byte) 101, (byte) -70,
		(byte) -40, (byte) -5, (byte) 117, (byte) 79, (byte) 52, (byte) -89, (byte) -24,
		(byte) -1, (byte) 0, (byte) 31, (byte) -25, (byte) -48, (byte) -73, (byte) -102,
		(byte) 82, (byte) -120, (byte) -128, (byte) 32, (byte) 8, (byte) 6, (byte) -98,
		(byte) 43, (byte) 52, (byte) -62, (byte) 83, (byte) -88, (byte) 105, (byte) -44,
		(byte) -87, (byte) -46, (byte) 27, (byte) 64, (byte) 82, (byte) -57, (byte) -64,
		(byte) 105, (byte) -58, (byte) 84, (byte) -81, (byte) 125, (byte) 66, (byte) -117,
		(byte) -54, (byte) 114, (byte) -44, (byte) -98, (byte) -99, (byte) -75, (byte) 73,
		(byte) -84, (byte) -46, (byte) -48, (byte) -47, (byte) -87, (byte) -54, (byte) 4,
		(byte) -9, (byte) 56, (byte) 114, (byte) 127, (byte) -77, (byte) 5, (byte) -14,
		(byte) -71, (byte) -100, (byte) -54, (byte) -67, (byte) -96, (byte) -95, (byte) 31,
		(byte) 10, (byte) 111, (byte) -81, (byte) 82, (byte) -52, (byte) 108, (byte) 37,
		(byte) -4, (byte) -97, (byte) 94, (byte) -59, (byte) 99, (byte) 86, (byte) 102,
		(byte) -86, (byte) 106, (byte) -71, (byte) -43, (byte) -51, (byte) -49, (byte) -37,
		(byte) -128, (byte) -46, (byte) 100, (byte) 111, (byte) -18, (byte) -91, (byte) 115,
		(byte) 85, (byte) -44, (byte) 101, (byte) -40, (byte) -45, (byte) 81, (byte) -118,
		(byte) -118, (byte) -28, (byte) 102, (byte) -114, (byte) -63, (byte) -125, (byte) -95,
		(byte) -43, (byte) 13, (byte) -57, (byte) -13, (byte) -7, (byte) -39, (byte) -58,
		(byte) 71, (byte) 103, (byte) 115, (byte) 43, (byte) 106, (byte) -54, (byte) -94,
		(byte) -28, (byte) 120, (byte) -108, (byte) 83, (byte) 89, (byte) 50, (byte) -42,
		(byte) -98, (byte) 112, (byte) 125, (byte) -18, (byte) 20, (byte) -1, (byte) 0,
		(byte) -105, (byte) 13, (byte) -25, (byte) -20, (byte) -51, (byte) 125, (byte) 46,
		(byte) -47, (byte) -37, (byte) -53, (byte) -60, (byte) -102, (byte) -21, (byte) -48,
		(byte) -91, (byte) 43, (byte) 63, (byte) -108, (byte) -66, (byte) -67, (byte) 51,
		(byte) 106, (byte) -122, (byte) 99, (byte) -122, (byte) 119, (byte) 20, (byte) -43,
		(byte) -120, (byte) 39, (byte) 96, (byte) 100, (byte) 43, (byte) -30, (byte) 69,
		(byte) -113, (byte) 9, (byte) -44, (byte) -95, (byte) -120, (byte) 91, (byte) -41,
		(byte) 124, (byte) 48, (byte) -106, (byte) -92, (byte) 19, (byte) -73, (byte) -100,
		(byte) 86, (byte) 111, (byte) 99, (byte) 110, (byte) 93, (byte) 33, (byte) 16,
		(byte) 10, (byte) -116, (byte) -9, (byte) 45, (byte) 53, (byte) 19, (byte) -97,
		(byte) -96, (byte) -67, (byte) 53, (byte) -38, (byte) 59, (byte) 67, (byte) -17,
		(byte) 57, (byte) 24, (byte) -82, (byte) 28, (byte) -82, (byte) 97, (byte) -59,
		(byte) 21, (byte) -15, (byte) 47, (byte) 114, (byte) -11, (byte) -99, (byte) -49,
		(byte) 118, (byte) -8, (byte) 101, (byte) -73, (byte) -40, (byte) -26, (byte) 65,
		(byte) -42, (byte) 97, (byte) -86, (byte) 83, (byte) 112, (byte) 121, (byte) 51,
		(byte) -77, (byte) -71, (byte) 50, (byte) 60, (byte) -119, (byte) -94, (byte) 54,
		(byte) -119, (byte) -111, (byte) -25, (byte) -122, (byte) -120, (byte) -38, (byte) 38,
		(byte) 87, (byte) 22, (byte) -71, (byte) 49, (byte) 26, (byte) 110, (byte) 114,
		(byte) 81, (byte) 68, (byte) 109, (byte) 23, (byte) 25, (byte) 102, (byte) 5,
		(byte) -125, (byte) 12, (byte) 78, (byte) 36, (byte) 107, (byte) -70, (byte) -67,
		(byte) -98, (byte) -13, (byte) -34, (byte) 124, (byte) 54, (byte) 124, (byte) 102,
		(byte) -13, (byte) 8, (byte) -62, (byte) -107, (byte) -84, (byte) 120, (byte) -25,
		(byte) -30, (byte) 126, (byte) -33, (byte) -77, (byte) -97, (byte) 113, (byte) 93,
		(byte) 63, (byte) -126, (byte) 59, (byte) 115, (byte) -2, (byte) -1, (byte) 0,
		(byte) 69, (byte) -100, (byte) -18, (byte) 21, (byte) 4, (byte) 1, (byte) 0,
		(byte) -27, (byte) 121, (byte) 76, (byte) -108, (byte) -105, (byte) 26, (byte) 13,
		(byte) 53, (byte) -79, (byte) 117, (byte) -69, (byte) 119, (byte) -101, (byte) -38,
		(byte) -13, (byte) 37, (byte) -38, (byte) 10, (byte) 112, (byte) -116, (byte) -45,
		(byte) 75, (byte) 87, (byte) -71, (byte) -38, (byte) -61, (byte) -27, (byte) 41,
		(byte) 65, (byte) -25, (byte) -56, (byte) -85, (byte) 70, (byte) -103, (byte) -90,
		(byte) -117, (byte) -51, (byte) 19, (byte) 35, (byte) -49, (byte) 13, (byte) 17,
		(byte) -76, (byte) 89, (byte) -28, (byte) -88, (byte) -115, (byte) -113, (byte) 2,
		(byte) -90, (byte) -22, (byte) -106, (byte) 3, (byte) -86, (byte) -32, (byte) -127,
		(byte) -57, (byte) 108, (byte) -48, (byte) -10, (byte) 122, (byte) -108, (byte) 37,
		(byte) 89, (byte) -71, (byte) 45, (byte) 82, (byte) -51, (byte) 20, (byte) -82,
		(byte) -37, (byte) -115, (byte) 61, (byte) 60, (byte) -66, (byte) -25, (byte) 75,
		(byte) 54, (byte) -121, (byte) 32, (byte) 64, (byte) 16, (byte) 4, (byte) 3,
		(byte) -113, (byte) -27, (byte) 59, (byte) -109, (byte) -102, (byte) 21, (byte) 27,
		(byte) -86, (byte) 7, (byte) -103, (byte) -11, (byte) -103, (byte) 12, (byte) 121,
		(byte) -15, (byte) 87, (byte) 81, (byte) 92, (byte) -110, (byte) -21, (byte) -36,
		(byte) -17, (byte) 97, (byte) -47, (byte) -54, (byte) -114, (byte) 126, (byte) 102,
		(byte) -66, (byte) 3, (byte) 46, (byte) -59, (byte) 85, (byte) 55, (byte) -93,
		(byte) 79, (byte) 78, (byte) -47, (byte) -47, (byte) 126, (byte) 125, (byte) 124,
		(byte) 37, (byte) 27, (byte) 92, (byte) 42, (byte) -75, (byte) -58, (byte) -87,
		(byte) 100, (byte) -66, (byte) 100, (byte) -75, (byte) -18, (byte) 105, (byte) -45,
		(byte) -35, (byte) -22, (byte) 75, (byte) -117, (byte) -53, (byte) -15, (byte) 52,
		(byte) -115, (byte) -21, (byte) 38, (byte) -99, (byte) -95, (byte) -86, (byte) -4,
		(byte) -6, (byte) -72, (byte) -59, (byte) -26, (byte) 17, (byte) 94, (byte) -122,
		(byte) -71, (byte) 102, (byte) -113, (byte) 20, (byte) -82, (byte) 105, (byte) -43,
		(byte) -39, (byte) -22, (byte) 108, (byte) 100, (byte) 111, (byte) 108, (byte) -55,
		(byte) 1, (byte) -34, (byte) -72, (byte) -16, (byte) 39, (byte) -46, (byte) 79,
		(byte) -128, (byte) -73, (byte) 27, (byte) -98, (byte) 23, (byte) -49, (byte) 50,
		(byte) 43, (byte) -56, (byte) -25, (byte) 73, (byte) -99, (byte) 92, (byte) -37,
		(byte) 28, (byte) 65, (byte) 0, (byte) 64, (byte) 34, (byte) -85, (byte) -50,
		(byte) 110, (byte) 64, (byte) 43, (byte) 95, (byte) 43, (byte) 70, (byte) -59,
		(byte) -100, (byte) 77, (byte) 106, (byte) 119, (byte) 45, (byte) -44, (byte) 77,
		(byte) -58, (byte) -102, (byte) 108, (byte) -107, (byte) 37, (byte) 101, (byte) 70,
		(byte) 85, (byte) 123, (byte) -39, (byte) 44, (byte) -39, (byte) 97, (byte) 93,
		(byte) 84, (byte) -116, (byte) 56, (byte) 19, (byte) -55, (byte) 22, (byte) 116,
		(byte) -127, (byte) -10, (byte) 108, (byte) 70, (byte) -55, (byte) 111, (byte) 98,
		(byte) -71, (byte) -109, (byte) -34, (byte) -42, (byte) 16, (byte) 10, (byte) -65,
		(byte) -45, (byte) 80, (byte) 98, (byte) -123, (byte) 122, (byte) 116, (byte) -20,
		(byte) 87, (byte) 93, (byte) 13, (byte) -123, (byte) -2, (byte) 54, (byte) -107,
		(byte) 21, (byte) -115, (byte) 5, (byte) 85, (byte) 85, (byte) 81, (byte) -55,
		(byte) -94, (byte) 119, (byte) 115, (byte) 81, (byte) -61, (byte) -127, (byte) -67,
		(byte) 11, (byte) 10, (byte) 92, (byte) -18, (byte) -4, (byte) -74, (byte) 64,
		(byte) 77, (byte) 0, (byte) 64, (byte) 16, (byte) 4, (byte) 1, (byte) 0, (byte) 64,
		(byte) 16, (byte) 4, (byte) 1, (byte) 0, (byte) -1, (byte) -39 };
}
