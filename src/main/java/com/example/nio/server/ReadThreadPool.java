package com.example.nio.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.util.ThreadFactoryUtils;

/**
 * @author gimbyeongsu
 * 
 */
public final class ReadThreadPool {
	public final static String READ_THREAD_NAME = "Read";
	private final ReadThread[] poolRead;
	private ExecutorService poolReadExecutorService;
	private int size = 0;
	private final AtomicInteger handlerID = new AtomicInteger(1);

	public ReadThreadPool(int size) {
		System.out.println("thread pool size : " + size);
		this.poolRead = new ReadThread[size];
		this.size = size;
	}

	public void startPool(String name, int priority) {
		poolReadExecutorService = Executors.newFixedThreadPool(size, new ThreadFactoryUtils(name,
				false, priority));
		for (int i = 0; i < size; ++i) {
			try {
				poolRead[i] = new ReadThread(handlerID, name + i, i);
				poolReadExecutorService.execute(poolRead[i]);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void stopPool() {
		poolReadExecutorService.shutdown();
	}

	public void accept(int readNum, SocketChannel a) {
		ReadThread r = poolRead[readNum];
		r.setAccept(a);
		r.wakeup();
	}
}
