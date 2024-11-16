package CompletableFuture;

import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        CompletableFuture<String> future = new CompletableFuture<>();
//        future.complete("gngsn");

        CompletableFuture<String> future = CompletableFuture.completedFuture("gngsn");
        System.out.println(future.get());

        System.out.println("\n=== runAsync ===");
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
        });
        future1.get();

        System.out.println("\n=== supplyAsync ===");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });
        System.out.println(future2.get());

        System.out.println("\n=== thenApply - callback ===");
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenApply((s) -> {
            System.out.println(Thread.currentThread().getName());
            return s.toUpperCase();
        });

        System.out.println(future3.get());


        System.out.println("\n=== thenAccept - callback ===");
        CompletableFuture<Void> future4 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenAccept((s) -> {
            System.out.println(Thread.currentThread().getName());
        });

        System.out.println("\n=== thenRun - callback ===");
        CompletableFuture<Void> future5 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenRun(() -> {
            System.out.println(Thread.currentThread().getName());
        });

        System.out.println(future4.get());

        System.out.println("\n=== future6 ===");
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CompletableFuture<Void> future6 = CompletableFuture.runAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
        }, executorService);
        future6.get();

    }
}
