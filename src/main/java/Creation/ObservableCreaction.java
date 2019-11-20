package Creation;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ObservableCreaction {

    private static void createdObservableUsingJust() {
        Observable<String> observable = Observable.just("This is my first obervable");
        observable.subscribe(s -> System.out.println(s));
    }

    private static void createdObservableFromArray() {
        Integer[] arr = new Integer[]{0, 1, 2, 3, 4, 5};
        Observable<Integer> observable = Observable.fromArray(arr);
        observable.subscribe(i -> System.out.println(i));
    }

    private static void createdObservableFromIterable() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        Observable<Integer> observable = Observable.fromIterable(list);
        observable.subscribe(i -> System.out.println("Item: " + i));
    }

    private static void createdObservableFromCallable() {
        Observable<String> observable = Observable.fromCallable(() -> {return "From callable.";});
        observable.subscribe(i -> System.out.println(i));
    }

    private static void createdObervableFromFuture() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<String> future = service.submit(() -> {Thread.sleep(5000); return "From future";});
        // This is still blocking. fromFuture will call future.get() and block the main thread.
        Observable<String> observable = Observable.fromFuture(future);
        observable.subscribe(s -> System.out.println(s), throwable -> System.out.println(throwable.toString()), () -> System.out.println("Complete from future."));
        System.out.println("Actions below...");
        service.shutdown();
    }

    public static void main(String[] args) {
        // createdUsingJust();
        // createdUsingFromArray();
        // createdUsingFromIterable();
        // createdCompletableFromRunnable();
        createdObervableFromFuture();
    }
}
