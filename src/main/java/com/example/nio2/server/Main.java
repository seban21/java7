package com.example.nio2.server;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;

import com.example.util.ThreadFactoryUtils;

/**
 * @author gimbyeongsu
 * 
 */
public class Main {
	public static void main(String[] args) {
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		int channelThreadPoolSize = Integer.parseInt(args[2]);
		System.out.println("Running on port:" + port);
		AsyncServerBootstrap asyncServerBootstrap = new AsyncServerBootstrap();
		try {
			// 스레드 풀 지명
			AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
					channelThreadPoolSize, new ThreadFactoryUtils("CHANNEL", false,
							Thread.NORM_PRIORITY));
			AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel
					.open(channelGroup);
			listener.setOption(StandardSocketOptions.SO_REUSEADDR, false);
			listener.setOption(StandardSocketOptions.SO_RCVBUF, 0);
			
			asyncServerBootstrap.group(channelGroup, listener);
			asyncServerBootstrap.bind(ip, port);
		} catch (IOException e) {
			asyncServerBootstrap.shuthdown();
			e.printStackTrace();
		}
	}
}
