package com.example.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.util.ThreadFactoryUtils;

/**
 * @author gimbyeongsu
 * 
 */
public class Main {
	private ExecutorService acceptThreadExecutorService;
	private ServerSocketChannel ssc;
	
	public void start(int poolSize, String ip, int port) {
		try {
			ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.setOption(StandardSocketOptions.SO_REUSEADDR, false);
			ssc.setOption(StandardSocketOptions.SO_RCVBUF, 0);

			System.out.println("server " + ip + ":" + port);
			ServerSocket ss = ssc.socket();
			ss.bind(new InetSocketAddress(ip, port), 100);
			acceptThreadExecutorService = Executors.newSingleThreadExecutor(new ThreadFactoryUtils(
					AcceptThread.ACCETP_THREAD_NAME, false, Thread.NORM_PRIORITY));
			AcceptThread acceptThread = new AcceptThread(poolSize, ssc,
					AcceptThread.ACCETP_THREAD_NAME);
			acceptThreadExecutorService.execute(acceptThread);
		} catch (Exception e) {
			e.printStackTrace();
			acceptThreadExecutorService.shutdown();
			try {
				ssc.close();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		int poolSize = Integer.parseInt(args[2]);

		Main server = new Main();
		server.start(poolSize, ip, port);
	}
}
