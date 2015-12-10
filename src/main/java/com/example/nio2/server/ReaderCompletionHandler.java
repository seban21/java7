package com.example.nio2.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.example.Packet;

/**
 * @author gimbyeongsu
 * 
 */
public class ReaderCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
	private final AsynchronousSocketChannel channel;

	public ReaderCompletionHandler(AsynchronousSocketChannel channel) {
		this.channel = channel;
	}

	/**
	 * 전송 과정에서 생기는 패킷 단편화 처리 필요
	 * +----+-------+---+---+
	 * | AB | CDEFG | H | I |
	 * +----+-------+---+---+
	 */
	@Override
	public void completed(Integer readLength, ByteBuffer buffer) {
		
		if (readLength < 1 || readLength == Packet.BUFFER_SIZE_DEFAULT) {
			close();
			return;
		}
		System.out.println("readLength:" + readLength);
		
		buffer.flip();
		// byte startPacket = buffer.get(); // 시작 패킷 알림
		buffer.get();
		int length = buffer.getInt();
		byte[] body = new byte[length];
		buffer.get(body);
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(Packet.BUFFER_SIZE_DEFAULT);
		byteBuffer.put((byte) 0x07);
		byteBuffer.putInt(length);
		byteBuffer.put(body, 0, length);
		byteBuffer.flip();
		Future<Integer> future = channel.write(byteBuffer);
		try {
			System.out.println("write length:" + future.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			close();
			return;
		}

		ByteBuffer dst = ByteBuffer.allocate(Packet.BUFFER_SIZE_DEFAULT);
		channel.read(dst, dst, this); // callback
	}

	@Override
	public void failed(Throwable e, ByteBuffer buffer) {
		e.printStackTrace();
		close();
	}

	private void close() {
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
