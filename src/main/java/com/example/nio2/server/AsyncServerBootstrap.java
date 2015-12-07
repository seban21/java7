package com.example.nio2.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * @author gimbyeongsu
 * 
 */
public class AsyncServerBootstrap {
	private AsynchronousServerSocketChannel listener;
	private AsynchronousChannelGroup channelGroup;
	private AsynServerSocket asynServerSocket;

	public AsyncServerBootstrap() {
	}

	public void group(AsynchronousChannelGroup channelGroup,
			AsynchronousServerSocketChannel listener) {
		this.channelGroup = channelGroup;
		this.listener = listener;
		this.asynServerSocket = new AsynServerSocket(this.channelGroup,
				this.listener);
	}

	public void bind(String ip, int port) {
		try {
			listener.bind(new InetSocketAddress(ip, port), 100);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Thread t = new Thread(asynServerSocket, "aioServerStart");
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	public void shuthdown() {
		try {
			asynServerSocket.shutdown();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	public AsynchronousServerSocketChannel getListener() {
		return listener;
	}

	public AsynchronousChannelGroup getChannelGroup() {
		return channelGroup;
	}
}
