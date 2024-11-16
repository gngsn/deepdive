package CallableAndFuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class App2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("==== thenCompose ====");
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<String> future = hello.thenCompose(App2::getWorld);
        System.out.println(future.get());

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return "World";
        });

        System.out.println("\n==== thenCombine ====");

        // BiFunction
        CompletableFuture<String> future2 = hello.thenCombine(world, (h, w)-> h + " " + w);
        System.out.println(future2.get());

        System.out.println("\n==== allOf ====");

        // 모든 task의 결과 타입이 동일하지 않을 수 있고, task 중 하나에서 오류가 나올 수 있다. -> result 가 null
        CompletableFuture.allOf(hello, world)
                .thenAccept(System.out::println);

        // 모든 결과값을 받고 싶다면 복잡하지만 아래와 같이 사용
        List<CompletableFuture<String>> futures = Arrays.asList(hello, world);
        CompletableFuture[] futuresArray = futures.toArray(new CompletableFuture[futures.size()]);

        CompletableFuture<List<String>> results = CompletableFuture.allOf(futuresArray)
                .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));

        results.get().forEach(System.out::println);

        System.out.println("\n==== allOf ====");

        CompletableFuture.anyOf(hello, world).thenAccept(System.out::println);
        future.get();
    }

    private static CompletableFuture<String> getWorld(String message) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return message + " World";
        });
    }
}
