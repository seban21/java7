package com.example.test.nio2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import org.junit.Test;

import com.example.util.TimeCheckedUtils;

/**
 * @author gimbyeongsu
 *
 */
public class ForkJoinTest {
	
	// fork/join 프레임워크는 멀티프로세서의 성능을 이용할 수 있는 ExecutorService 인터페이스의 구현체입니다.
	// 반복적으로 작은 조각으로 작업을 나누어 수행 할 수 있게 설계 되었습니다.
	// 목표는 어플리케이션의 성능을 향상 시키기 위해 가능한 모든 프로세세를 이용하는 것입니다.
	// ExecutorServcie를 구현함으로써 fork/join 프레임워크는 Thread Pool안의 Worker Thread에게 작업들을 분배합니다.
	// fork/join 프레임워크는 Produce-Consumer 알고리즘과는 매우 다른 work-stealing 알고리즘을 이용합니다.
	// 할 작업이 없는 Worker Thread는 아직 바쁜 다른 Thread의 작업을 훔쳐 올 수 있습니다.
	// fork/join 프레임워크의 핵심은 AbstractExecutorService 클래스를 구현한 ForkJoinPool 클래스입니다.
	// ForkJoinPool은 핵심적인 work-stealing 알고리즘을 구현하고 ForkJoinTask 프로세스들을 실행 할 수 있습니다.
	// RecursiveTask(결과를 반환할 수 있는) 또는 RecursiveAction 같은 ForkJoinTask 하위 클래스를 랩핑할 수 있습니다.
	
	// http://www.oracle.com/technetwork/articles/java/fork-join-422606.html
	// http://www.javacodegeeks.com/2011/02/java-forkjoin-parallel-programming.html
	
	@Test
	public void testForkJoin() {
		TimeCheckedUtils check = new TimeCheckedUtils();
		check.checkedStart();
		
		int n = 50;
		
		int processors = Runtime.getRuntime().availableProcessors();
		System.out.println("No of processors: " + processors);
		
		FibonacciProblem bigProblem = new FibonacciProblem(n);
		
		FibonacciTask task = new FibonacciTask(bigProblem);
		ForkJoinPool pool = new ForkJoinPool(processors);
		pool.invoke(task);
		
		long result = task.getResult();
		System.out.println("Computed Result: " + result);
		System.out.println(check.getCheckedEndStr(""));
	}
	
	private class FibonacciProblem {
		private int n;

		public FibonacciProblem(int n) {
			this.n = n;
		}

		public long solve() {
			return fibonacci(n);
		}

		private long fibonacci(int n) {
			//System.out.println("Thread: " + Thread.currentThread().getName() + " calculates " + n);
			if (n <= 1) {
				return n;
			} else {
				return fibonacci(n - 1) + fibonacci(n - 2);
			}
		}
		
		public int getN() {
			return n;
		}
	}
	
	
	private class FibonacciTask extends RecursiveTask<Long> {
		private static final long serialVersionUID = 6136927121059165206L;

		private static final int THRESHOLD = 5;

		private FibonacciProblem problem;
		private long result;

		public FibonacciTask(FibonacciProblem problem) {
			this.problem = problem;
		}

		@Override
		public Long compute() {
			if (problem.getN() < THRESHOLD) {
				result = problem.solve();
			} else {
				FibonacciTask worker1 = new FibonacciTask(new FibonacciProblem(problem.getN() - 1));
				worker1.fork();
				FibonacciTask worker2 = new FibonacciTask(new FibonacciProblem(problem.getN() - 2));
				result = worker2.compute() + worker1.join();

			}
			return result;
		}
		
		public long getResult() {
			return result;
		}

	}
}
