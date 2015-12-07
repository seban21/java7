package com.example.test.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

/**
 * @author gimbyeongsu
 * 
 */
public class PhaserTest {

	private static abstract class Entity implements Runnable {
		public abstract void run();
	}

	private static class Player extends Entity implements Runnable {
		private final static AtomicInteger ID_SOURCE = new AtomicInteger();
		private final int id = ID_SOURCE.incrementAndGet();

		public void run() {
			System.out.println(toString() + " 시작");
			try {
				// 작업중 ...
				Thread.sleep(ThreadLocalRandom.current().nextInt(200, 600));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(toString() + " 완료");
		}

		@Override
		public String toString() {
			return "Player #" + id;
		}
	}

	private static class Zombie extends Entity implements Runnable {
		private final static AtomicInteger ID_SOURCE = new AtomicInteger();
		private final int id = ID_SOURCE.incrementAndGet();

		public void run() {
			System.out.println(toString() + " 시작");
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(400, 800));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(toString() + " 완료");
		}

		@Override
		public String toString() {
			return "Zombie #" + id;
		}
	}

	private void execute(int type) {
		List<Entity> entities = new ArrayList<>();
		entities = new ArrayList<>();
		entities.add(new Player());
		entities.add(new Zombie());
		entities.add(new Zombie());
		entities.add(new Zombie());
		if (type == 1) {
			gameEngine1(entities);
		} else {
			gameEngine2(entities);
		}
	}

	private void gameEngine1(List<Entity> entities) {
		final Phaser phaser = new Phaser(1);
		for (final Entity entity : entities) {
			final String member = entity.toString();
			System.out.println(member + " 게임에 조인함");
			phaser.register(); // 참여자를 등록함
			new Thread() { // 참여자의 Runnable 오브젝트로 새로운 스레드를 생성
				@Override
				public void run() {
					System.out.println(member + " 다른 플레이어가 조인하기를 기다림...");
					phaser.arriveAndAwaitAdvance(); // 다른 task가 생성 될때까지 기다림
					System.out.println(member + " 달리기 준비...");
					entity.run();
				}
			}.start();
		}
		phaser.arriveAndDeregister(); // 참여자를 실행 시킴
		System.out.println("다음 단계로 진행...");
	}
	
	private void gameEngine2(List<Entity> entities) {
		final int iterations = 3; // 반복 작업 개수
		
		final Phaser phaser = new Phaser(1) {
			protected boolean onAdvance(int phase, int registeredParties) {
				System.out.println("해당 단계 넘버:" + phase + " 완료됨\n");
				return phase >= iterations - 1 || registeredParties == 0;
			}
		};
		for (final Entity entity : entities) {
			final String member = entity.toString();
			System.out.println(member + " 게임에 조인함");
			phaser.register();
			new Thread() {

				@Override
				public void run() {
					do {
						System.out.println(member + " 달리기 준비...");
						entity.run();
						System.out.println(member
								+ " 달리기 종료 분할 작업 넘버:"
								+ phaser.getPhase());
						phaser.arriveAndAwaitAdvance(); // 다른 task가 생성 될때까지 기다림
					} while (!phaser.isTerminated());
				}
			}.start();
		}

		while (!phaser.isTerminated()) { // 모든 참여자가 도착할때 까지 기다림
			phaser.arriveAndAwaitAdvance(); // 다른 task가 생성 될때까지 기다림
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("다음 단계로 진행...");
	}

	/**
	 * Phaser
	 */
	@Test
	public void test1() {
		new PhaserTest().execute(1);
	}
	
	/**
	 * 일련의 작업 반복
	 */
	@Test
	public void test2() {
		new PhaserTest().execute(2);
	}
}
