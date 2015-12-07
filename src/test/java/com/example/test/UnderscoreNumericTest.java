package com.example.test;

import org.junit.Test;

/**
 * @author gimbyeongsu
 *
 */
public class UnderscoreNumericTest {
	
	/**
	 * 숫자형(정수,실수)에 _(underscore) 문자열을 사용
	 */
	@Test
	public void test() throws Exception {
		
		int billion = 1_000_000_000; // 10^9
		long cardNum = 1234_4567_8901_2345L; // 16 digit number
		long ssn = 777_99_8888L;
		double pi = 3.1415_9265;
		float pif = 3.14_15_92_65f;
		
		// _ 위치가 다음과 같을 경우엔 컴파일 에러가 발생
		// double pi = 3._1415_9265; // 소수점 뒤에 _ 붙일 경우
		// long cardNum = 1234_4567_8901_2345_L; // 숫자 끝에 _ 붙일 경우
		// long ssn = _777_99_8888L; // 숫자 시작에 _ 붙일 경우
		
		System.out.println(billion);
		System.out.println(cardNum);
		System.out.println(ssn);
		System.out.println(pi);
		System.out.println(pif);
	}
}
