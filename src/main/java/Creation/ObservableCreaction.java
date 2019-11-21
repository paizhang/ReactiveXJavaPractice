package Creation;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ObservableCreaction {

    private static void createdObservableUsingJust() {
        Observable<String> observable = Observable.just("This is my first obervable");
        observable.subscribe(s -> System.out.println(s));
    }

    // Generate observable values from start to end - 1.
    private static void createdObservableUsingRange(int start, int end) {
        Observable.range(start, end).subscribe(num -> System.out.println(num));
    }

    // Repeat n times of the source observable
    private static void createdObservableUsingRepeat() {
        Observable.range(1, 6)
                .repeat(3)
                .subscribe(num -> System.out.println(num));
    }

    // By default Interval operator will run within a thread in a computation scheduler
    private static void createdObservableUsingInterval() throws InterruptedException {
        Observable.interval(3, TimeUnit.SECONDS)
                .doOnNext(s -> System.out.println(Thread.currentThread().getName()))
                .subscribe(num -> System.out.println(num));

        Thread.sleep(10000);
    }

    // By default Interval operator will run within a thread in a computation scheduler
    private static void createdObservableUsingTimer() throws InterruptedException {
        Observable.timer(3, TimeUnit.SECONDS)
                .doOnNext(s -> System.out.println(Thread.currentThread().getName()))
                .subscribe(num -> System.out.println(num));
        Thread.sleep(5000);
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

    public static void main(String[] args) throws InterruptedException {
        // createdUsingJust();
        // createdUsingFromArray();
        // createdUsingFromIterable();
        // createdCompletableFromRunnable();
        // createdObervableFromFuture();
        // createdObservableUsingRange(0, 10);
        // createdObservableUsingRepeat();
        // createdObservableUsingInterval();
        createdObservableUsingTimer();
    }
}
