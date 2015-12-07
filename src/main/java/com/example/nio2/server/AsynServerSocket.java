package com.example.nio2.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author gimbyeongsu
 * 
 */
public class AsynServerSocket implements Runnable {
	private final AsynchronousServerSocketChannel listener;
	private final AsynchronousChannelGroup channelGroup;

	public AsynServerSocket(AsynchronousChannelGroup channelGroup,
			AsynchronousServerSocketChannel listener) {
		this.channelGroup = channelGroup;
		this.listener = listener;
	}

	public SocketAddress getSocketAddress() throws IOException {
		return listener.getLocalAddress();
	}

	@Override
	public void run() {
		System.out.println("aio server start");
		AcceptCompletionHandler accept = new AcceptCompletionHandler(listener);
		listener.accept(null, accept); // callback
	}

	public void shutdown() throws InterruptedException, IOException {
		channelGroup.shutdownNow();
		channelGroup.awaitTermination(3, TimeUnit.SECONDS);
	}
}
