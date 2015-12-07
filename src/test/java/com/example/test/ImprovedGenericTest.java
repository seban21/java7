package com.example.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * @author gimbyeongsu
 *
 */
public class ImprovedGenericTest {
	
	/**
	 * 향상된 제너릭
	 */
	@Test
	public void test() throws Exception {
		
		Map<String, List<String>> java = new HashMap<String, List<String>>();
		// <> 사용
		Map<String, List<String>> java7 = new HashMap<>();
		
		System.out.println(java);
		System.out.println(java7);
		
	}
}
