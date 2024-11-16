package Executors;

import java.lang.reflect.Executable;
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Thread: " + Thread.currentThread().getName());
//            }
//        });

//        executorService.submit(() -> System.out.println("Thread: " + Thread.currentThread().getName()));
//        executorService.shutdown();

//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        executorService.submit(getRunnable("Hello"));
//        executorService.submit(getRunnable("gngsn"));
//        executorService.submit(getRunnable("park"));
//        executorService.submit(getRunnable("the"));
//        executorService.submit(getRunnable("java"));
//        executorService.submit(getRunnable("thread"));
//
//        executorService.shutdown();

        // Thread Pool
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(getRunnable("Hello"), 1, 2, TimeUnit.SECONDS);

//        executorService.shutdown();
    }

    private static Runnable getRunnable(String message) {
        return () -> System.out.println(message + " : " + Thread.currentThread().getName());
    }
}
