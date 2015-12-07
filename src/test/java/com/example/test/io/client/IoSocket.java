package com.example.test.io.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author gimbyeongsu
 *
 */
public class IoSocket {
	private Socket socket;
	private BufferedInputStream readerIo;
	private BufferedOutputStream writeIo;
	private boolean releaseConnection = true;

	public IoSocket(Socket socket) throws Exception {
		this.socket = socket;
		this.readerIo = new BufferedInputStream(socket.getInputStream());
		this.writeIo = new BufferedOutputStream(socket.getOutputStream());
	}

	public boolean isReleaseConnection() {
		return releaseConnection;
	}

	public boolean isConnected() {
		return socket.isConnected();
	}

	public boolean isClosed() {
		return socket.isClosed();
	}

	public void write(byte[] bytes) throws IOException {
		writeIo.write(bytes, 0, bytes.length);
		try {
			writeIo.flush();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public void reader(int num) throws IOException {
		byte[] sop = new byte[1];
		if (readerIo.read(sop, 0, 1) == -1) {
			close();
			return;
		}
		byte[] length = new byte[4];
		if (readerIo.read(length, 0, 4) == -1) {
			close();
			return;
		}
		int size = toInt(length);
		byte[] body = new byte[size];
		if (readerIo.read(body, 0, size) == -1) {
			close();
			return;
		}
		
		System.out.println(num + ":" + new String(body));
	}

	public void close() {
		releaseConnection = false;
		try {
			readerIo.close();
		} catch (IOException e) {}
		try {
			writeIo.close();
		} catch (IOException e) {}
		try {
			socket.close();
		} catch (IOException e) {}
	}

	public int toInt(byte[] bytes) {
		int v = 0;
		v |= ((bytes[0]) << 24) & 0xFF000000;
		v |= ((bytes[1]) << 16) & 0xFF0000;
		v |= ((bytes[2]) << 8) & 0xFF00;
		v |= (bytes[3]) & 0xFF;
		return v;
	}
}
