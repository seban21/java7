package com.example.nio.server;

import java.nio.channels.SelectionKey;

/**
 * @author gimbyeongsu
 * 
 */
public interface WorkHandler {
	public abstract void connect(int threadNumber, int handlerID);

	public abstract void received(SelectionKey selKey);

	public abstract void close();
}