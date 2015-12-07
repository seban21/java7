package com.example.test;

import org.junit.Test;

/**
 * @author gimbyeongsu
 *
 */
public class BinaryTest {
	
	/**
	 * 숫자형에 '0B' 또는 '0b'를 앞에 붙임으로써 이진법 표현이 가능
	 */
	@Test
	public void test() throws Exception {
		
		byte bit1 = 0B1;
		System.out.println(TestUtils.toBinaryString(bit1));
		// 00000001
		
		short bit16 = 0B0101101100001101;
		System.out.println(TestUtils.toBinaryString(bit16));
		// 0101101100001101
		
		int mask = 0b01010000101;
		System.out.println(TestUtils.toBinaryString(mask));
		// 00000000000000000000001010000101
		
		// _를 이용한 가독성 향상
		byte bit8 = 0B0101_1011;
		System.out.println(TestUtils.toBinaryString(bit8));
		// 01011011
		
		int binary = 0B0101_0000_1010_0010_1101_0000_1010_0010;
		System.out.println(TestUtils.toBinaryString(binary));
		// 01010000101000101101000010100010
		
		// int binary100 = 0B0000_0000_0000_0000_0000_0000_0110_0100;
		int binary100 = 100;
		System.out.println(TestUtils.toBinaryString(binary100));
	}
}
