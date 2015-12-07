package com.example.test.io.client;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.Callable;

import com.example.Packet;

/**
 * @author gimbyeongsu
 *
 */
public class WorkThread implements Callable<Long> {
	private final int workLoopNumber;
	private final ConnectionPoolFactory poolFactory;
	private boolean isRandomCloseTest = false;

	public WorkThread(ConnectionPoolFactory poolFactory, int workLoopNumber) {
		this.poolFactory = poolFactory;
		this.workLoopNumber = workLoopNumber;
	}

	@Override
	public Long call() throws Exception {
		long start = System.currentTimeMillis();

		Random random = new Random();
		for (int i = 0; i < workLoopNumber; ++i) {
			IoSocket socket = null;
			try {
				try {
					Thread.sleep(200L);
				} catch (InterruptedException e) {
				}
				socket = poolFactory.getConnection();

				ByteBuffer byteBuffer = ByteBuffer.allocate(Packet.HEAD_SIZE
						+ Packet.DUMMY_PACKET_LENGTH);
				byteBuffer.put((byte) 0x07);
				byteBuffer.putInt(Packet.DUMMY_PACKET_LENGTH);
				byteBuffer.put(Packet.DUMMY_PACKET);
				socket.write(byteBuffer.array());

				try {
					Thread.sleep(10L);
				} catch (InterruptedException e) {
				}

				socket.reader(i);
			} catch (Exception e) {
				System.err.println(e.getMessage());

				if (socket != null) {
					socket.close();
				}
			} finally {
				if (isRandomCloseTest) {
					if (random.nextInt(10) == 0) {
						if (socket != null) {
							socket.close();
						}
					}
				}
				poolFactory.releaseConnection(socket);
			}
		}

		return System.currentTimeMillis() - start;
	}
}
