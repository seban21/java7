package com.example.test.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import org.junit.Test;

import com.example.util.TimeCheckedUtils;

/**
 * @author gimbyeongsu
 *
 */
public class ForkJoinTest {
	
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
