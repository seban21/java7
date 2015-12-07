package com.example.test.io.client;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * @author gimbyeongsu
 *
 */
public class ConnectionFactory extends BasePoolableObjectFactory<IoSocket> {
	private NetworkConfig networkConfig;
	private InetSocketAddress address;

	public ConnectionFactory(NetworkConfig networkConfig, String ip, int port) {
		this.networkConfig = networkConfig;
		this.address = new InetSocketAddress(ip, port);
	}

	@Override
	public IoSocket makeObject() throws Exception {
		Socket socket = new Socket();
		socket.setSoTimeout(networkConfig.getReadTimeout());
		socket.setTcpNoDelay(networkConfig.isTcpNoDelay());
		socket.setReuseAddress(networkConfig.isReuseAddress());
		socket.setSoLinger(networkConfig.getSoLinger() > 0, networkConfig.getSoLinger());
		socket.setSendBufferSize(networkConfig.getSendBufferSize());
		socket.setReceiveBufferSize(networkConfig.getReceiveBufferSize());
		socket.setKeepAlive(networkConfig.isKeepAlive());
		socket.connect(address, networkConfig.getConnectTimeout());
		return new IoSocket(socket);
	}

	@Override
	public void destroyObject(IoSocket socket) throws Exception {
		System.out.println("destroyObject");
		socket.close();
	}

	@Override
	public boolean validateObject(IoSocket socket) {
		if (socket.isConnected() == false) {
			return false;
		}
		if (socket.isClosed() == true) {
			return false;
		}
		return true;
	}
}
