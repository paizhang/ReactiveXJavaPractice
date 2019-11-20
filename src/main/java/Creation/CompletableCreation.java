package Creation;

import io.reactivex.Completable;

public class CompletableCreation {

    private static void createdCompletableFromRunnable() {
        Completable completable = Completable.fromRunnable(() -> System.out.println("From Runnable. "));
        completable.subscribe(() -> System.out.println("Complete!"), throwable -> {System.out.println(throwable.toString());});
    }

    public static void main(String[] args) {
        createdCompletableFromRunnable();
    }
}
