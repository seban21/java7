package com.example.nio.server;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author gimbyeongsu
 * 
 */
public final class AcceptThread implements Runnable {
	public final static String ACCETP_THREAD_NAME = "Accept";
	private final ReadThreadPool readThreadPool;
	private final int readPoolSize;
	private final ServerSocketChannel ssc;

	public AcceptThread(int poolSize, ServerSocketChannel ssc, String name) {
		this.readThreadPool = new ReadThreadPool(poolSize);
		this.readThreadPool.startPool(ReadThreadPool.READ_THREAD_NAME, Thread.NORM_PRIORITY);
		this.ssc = ssc;
		this.readPoolSize = poolSize;
	}

	@Override
	public void run() {
		Selector s = null;
		try {
			s = Selector.open();
			ssc.register(s, SelectionKey.OP_ACCEPT);
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		//
		int ix = - 1;
		//
		while (true) {
			try {
				if (s.select(1000L) > 0) {
					s.selectedKeys().clear();
				}
				
				final SocketChannel sc = ssc.accept();
				if (sc != null) {
					sc.configureBlocking(false);
					sc.setOption(StandardSocketOptions.SO_KEEPALIVE, false);
					sc.setOption(StandardSocketOptions.SO_REUSEADDR, false);
					sc.setOption(StandardSocketOptions.TCP_NODELAY, false);
					sc.setOption(StandardSocketOptions.SO_LINGER, 0);
					sc.setOption(StandardSocketOptions.SO_SNDBUF, 1024 * 8);
					sc.setOption(StandardSocketOptions.SO_RCVBUF, 1024 * 8);
					final int readThreadPos = ix = (++ix) % readPoolSize;
					readThreadPool.accept(readThreadPos, sc);
				}
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
