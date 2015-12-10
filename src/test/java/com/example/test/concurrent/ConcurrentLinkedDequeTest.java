package com.example.test.concurrent;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author gimbyeongsu
 * 
 */
public class ConcurrentLinkedDequeTest {
	private final static ConcurrentLinkedDeque<Item> DEQUE = new ConcurrentLinkedDeque<>();

	private static class Item {
		private String description;
		private int itemId;

		public String getDescription() {
			return description;
		}

		public int getItemId() {
			return itemId;
		}

		public Item(String description, int itemId) {
			this.description = description;
			this.itemId = itemId;
		}
	}

	private static class ItemProducer implements Runnable {
		private boolean isRun = true;

		@Override
		public void run() {
			String itemName = "";
			int itemId = 0;
			for (int x = 1; x < 1000000; x++) {
				itemName = "Item" + x;
				itemId = x;
				DEQUE.add(new Item(itemName, itemId));
				System.out.println("신규 아이템 추가:" + itemName + " " + itemId);
				if (isRun == false) {
					break;
				}
			}
		}

		public void workStop() {
			isRun = false;
		}
	}

	private static class ItemConsumer implements Runnable {
		private boolean isRun = true;
		private String name;
		private long cnt;

		@Override
		public void run() {
			this.name = Thread.currentThread().getName();

			while (isRun) {
				Item item = DEQUE.pollFirst();
				if (item != null) {
					process(item);
				}
			}
		}

		private void process(Item item) {
			System.out.println(name + "가 신규 아이템 처리 description: " + item.getDescription()
					+ " itemId:" + item.getItemId());
			item.getItemId();
			item.getDescription();
			cnt += item.getItemId();
		}

		public void print() {
			System.out.println(name + "의 처리량:" + cnt);
		}

		public void shutdown() {
			isRun = false;
		}
	}

	public static void main(String[] args) {
		ItemProducer producer = new ItemProducer();
		Thread producerThread = new Thread(producer);
		producerThread.start();

		ItemConsumer consumer1 = new ItemConsumer();
		ItemConsumer consumer2 = new ItemConsumer();
		ItemConsumer consumer3 = new ItemConsumer();
		Thread consumerThread1 = new Thread(consumer1, "CONSUMER_1");
		Thread consumerThread2 = new Thread(consumer2, "CONSUMER_2");
		Thread consumerThread3 = new Thread(consumer3, "CONSUMER_3");
		consumerThread1.start();
		consumerThread2.start();
		consumerThread3.start();

		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		producer.workStop();
		System.out.println("======================");
		System.out.println("작업 추가 종료");
		System.out.println("======================");

		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		consumer1.shutdown();
		consumer2.shutdown();
		consumer3.shutdown();

		consumer1.print();
		consumer2.print();
		consumer3.print();

		System.out.println("======================");
		System.out.println("소비 작업 종료");
		System.out.println("======================");
	}
}
