package com.example.test;

import org.junit.Test;

/**
 * @author gimbyeongsu
 * 
 */
public class StringSwitchStatementsTest {
	
	/**
	 * 스위치문에 문자열 사용 가능
	 */
	@Test
	public void test() throws Exception {
		
		String s = "apple";
		switch (s) {
		case "apple":
			System.out.println(1);
			break;
		case "melon":
		case "orange":
			System.out.println(2);
			break;
		case "banana":
			System.out.println(3);
		default:
			System.out.println(4);
			break;
		}
		
	}
}
