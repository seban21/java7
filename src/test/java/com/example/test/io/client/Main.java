package com.example.test.io.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import com.example.util.ThreadFactoryUtils;

/**
 * @author gimbyeongsu
 * 
 */
public class Main {
	public static void main(String[] args) {
		System.out.println("start");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		int threadNumber = Integer.parseInt(args[2]);
		int workLoopNumber = Integer.parseInt(args[3]);
		//
		Config config = new Config();
		config.maxActive = threadNumber + 2;
		config.maxIdle = threadNumber + 2;
		config.minIdle = threadNumber + 1;
		config.maxWait = -1;
		config.testOnBorrow = false;
		config.testOnReturn = false;
		config.timeBetweenEvictionRunsMillis = -1; // 사용하지 않는 connection을 pool에서 제거하는 시간기준
		
		config.testWhileIdle = false; // 놀고 있는 connection의 제거 여부
		NetworkConfig networkConfig = new NetworkConfig()
			.setConnectTimeout(5 * 1000)
			.setReadTimeout(5 * 1000)
			.setSendBufferSize(1024)
			.setSoLinger(0)
			.setReceiveBufferSize(1024)
			.setTcpNoDelay(true)
			.setReuseAddress(false)
			.setKeepAlive(false);
		//
		ConnectionPoolFactory poolFactory = new ConnectionPoolFactory(networkConfig, config,
				ip, port);
		ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactoryUtils(
				"IO", false, 5));
		ArrayList<Future<Long>> list = new ArrayList<Future<Long>>();
		for (int i = 0; i < threadNumber; i++) {
			WorkThread task = new WorkThread(poolFactory, workLoopNumber);
			Future<Long> future = executorService.submit(task);
			list.add(future);
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		executorService.shutdown();
		//
		long nanos = 0L;
		for (Future<Long> future : list) {
			try {
				nanos = nanos + future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		//
		long sec = TimeUnit.MILLISECONDS.toSeconds(nanos);
		if (threadNumber == 1) {
			System.out.println("total sec:" + sec);
		} else {
			long sec1 = sec / threadNumber;
			System.out.println("total sec:" + sec1);
		}
		//
		String end = sdf.format(new Date());
		System.out.println(end);
	}
}
