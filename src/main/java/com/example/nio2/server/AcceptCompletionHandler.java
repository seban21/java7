package com.example.nio2.server;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.ReadPendingException;

import com.example.Packet;

/**
 * @author gimbyeongsu
 * 
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
	private final AsynchronousServerSocketChannel listener;

	public AcceptCompletionHandler(AsynchronousServerSocketChannel listener) {
		this.listener = listener;
	}

	@Override
	public void completed(AsynchronousSocketChannel channel, Void attachment) {
		listener.accept(null, this); // callback
		registerHandler(channel);
	}

	@Override
	public void failed(Throwable e, Void attachment) {
		e.printStackTrace();
	}

	private void registerHandler(AsynchronousSocketChannel channel) {
		try {
			channel.setOption(StandardSocketOptions.SO_REUSEADDR, false);
			channel.setOption(StandardSocketOptions.TCP_NODELAY, false);
			channel.setOption(StandardSocketOptions.SO_KEEPALIVE, false);
			channel.setOption(StandardSocketOptions.SO_SNDBUF, 1024 * 4);
			channel.setOption(StandardSocketOptions.SO_RCVBUF, 1024 * 4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("accept:" + channel);
		
		ByteBuffer dst = ByteBuffer.allocate(Packet.BUFFER_SIZE_DEFAULT);
		ReaderCompletionHandler reader = new ReaderCompletionHandler(channel);
		try {
			channel.read(dst, dst, reader); // callback
		} catch (ReadPendingException e) {
			e.printStackTrace();
			try {
				channel.close();
			} catch (IOException ie) {
			}
		} catch (NotYetConnectedException e) {
			e.printStackTrace();
		}
	}
}