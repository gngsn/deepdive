import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

class VirtualThread {
    public static void main(String[] args) {
        // 1. time : 1,024 ms
        long start = System.currentTimeMillis();
        try (var executor = Executors.newFixedThreadPool(10)) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("1. platform threads 10 - time : " + (elapsedTime) + "ms");

        // 2. time : 100,382 ms
        start = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }
        elapsedTime = System.currentTimeMillis() - start;
        System.out.println("2. virtual thread - time : " + (elapsedTime) + "ms");
    }
}
