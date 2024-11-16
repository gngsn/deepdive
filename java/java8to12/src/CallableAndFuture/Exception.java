package CallableAndFuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Exception {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        boolean throwError = true;

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            if (throwError) {
                System.out.println("이거 왜 됨");
                throw new IllegalArgumentException();
            }
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).exceptionally(ex -> {
            System.out.println(ex);
            return "Error!";
        });

//        System.out.println(hello.get());

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            if (throwError) {
                throw new IllegalArgumentException();
            }
            System.out.println("World" + Thread.currentThread().getName());
            return "World";
        }).handle((result, ex) -> {
            if (ex != null) {
                System.out.println(ex);
                return "Error!";
            }
            return result;
        });

        System.out.println(world.get());
    }
}
