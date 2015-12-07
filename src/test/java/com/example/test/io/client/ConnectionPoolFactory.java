package com.example.test.io.client;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

/**
 * @author gimbyeongsu
 *
 */
public class ConnectionPoolFactory {
	private GenericObjectPool<IoSocket> pool;
	private NetworkConfig networkConfig;
	private InetSocketAddress address;

	public ConnectionPoolFactory(NetworkConfig networkConfig, Config config, String ip, int port) {
		this.address = new InetSocketAddress(ip, port);
		this.networkConfig = networkConfig;
		ConnectionFactory factory = new ConnectionFactory(networkConfig, ip, port);
		this.pool = new GenericObjectPool<IoSocket>(factory, config);
	}

	public IoSocket getConnection() throws Exception {
		IoSocket ioSocket = (IoSocket) pool.borrowObject();
		if (ioSocket == null || ioSocket.isClosed() == true) {
			System.out.println("reConnection");
			Socket socket = new Socket();
			socket.setSoTimeout(networkConfig.getReadTimeout());
			socket.setTcpNoDelay(networkConfig.isTcpNoDelay());
			socket.setReuseAddress(networkConfig.isReuseAddress());
			socket.setSoLinger(networkConfig.getSoLinger() > 0, networkConfig.getSoLinger());
			socket.setSendBufferSize(networkConfig.getSendBufferSize());
			socket.setReceiveBufferSize(networkConfig.getReceiveBufferSize());
			socket.setKeepAlive(networkConfig.isKeepAlive());
			socket.connect(address , networkConfig.getConnectTimeout());
			return new IoSocket(socket);
		}
		return ioSocket;
	}

	public void releaseConnection(IoSocket socket) {
		try {
			pool.returnObject(socket);
		} catch (Exception e) {
			e.printStackTrace();
			if (socket != null) {
				socket.close();
			}
		}
	}
}
