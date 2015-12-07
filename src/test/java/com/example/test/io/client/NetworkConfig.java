package com.example.test.io.client;

/**
 * @author gimbyeongsu
 * 
 */
public class NetworkConfig {
	private boolean KeepAlive = false;
	private int connectTimeout = 10 * 1000;
	private boolean tcpNoDelay = true;
	private boolean reuseAddress = true;
	private int soLinger = -1;
	private int sendBufferSize = 256;
	private int receiveBufferSize = 1024;
	private int readTimeout = 20 * 1000;

	public boolean isKeepAlive() {
		return KeepAlive;
	}

	public NetworkConfig setKeepAlive(boolean keepAlive) {
		KeepAlive = keepAlive;
		return this;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public NetworkConfig setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public NetworkConfig setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
		return this;
	}

	public boolean isReuseAddress() {
		return reuseAddress;
	}

	public NetworkConfig setReuseAddress(boolean reuseAddress) {
		this.reuseAddress = reuseAddress;
		return this;
	}

	public int getSoLinger() {
		return soLinger;
	}

	public NetworkConfig setSoLinger(int soLinger) {
		this.soLinger = soLinger;
		return this;
	}

	public int getSendBufferSize() {
		return sendBufferSize;
	}

	public NetworkConfig setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
		return this;
	}

	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public NetworkConfig setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
		return this;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public NetworkConfig setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

}
