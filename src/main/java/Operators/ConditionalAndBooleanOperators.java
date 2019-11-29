package Operators;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ConditionalAndBooleanOperators {

    /*
        This operator will evaluate all items emitted by the source observable based on the specified condition.
        The return value is a Single<Boolean> observable which is true in case the observable terminates normally and all items are evaluated as true,
        and false otherwise.
     */
    public void testUsingAll() {
        Observable.range(1, 5)
                .all(num -> num > 6)
                .subscribe(s -> {System.out.println("Complete with:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()));
    }

    /*
        Given two or more observables, the function of this operator is to only emit items of the observable who emit the first item among all
        source observables.
     */
    public void testUsingAmb() throws InterruptedException {
        Observable<Long> ob1 = Observable.interval(1, TimeUnit.SECONDS).delay(1, TimeUnit.SECONDS);
        Observable<Long> ob2 = Observable.interval(2, TimeUnit.SECONDS);

        Observable.ambArray(ob1, ob2)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));

        Thread.sleep(10000);
    }

    /*
       This is a simple operation which will check whether or not the source observable emits items that contain the specific item.
       It will return a Single<Boolean> observable which will emit true if it contains, and false if not.
     */
    public void testUsingContains() {
        Observable.range(1, 6)
                .contains(6)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()));
    }

    /*
        This operator will mirror the source observable exactly if the source observable emit any items. If the source observable completes normally
        without emitting any items. Then it will emit a default item and terminate the observable.
     */
    public void testUsingDefaultIfEmpty() {
        Observable.empty()
                .defaultIfEmpty(1)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));
    }

    /*
        This operator will compare two input observables. If these two observables emit the same items in the same order with the same termination
        state, then it will emit a true item. Otherwise, it will return a false item.
        The type of return is Observable<Boolean>.
     */
    public void testUsingSequenceEqual() {
        Observable<Integer> ob1 = Observable.just(1, 2, 3, 4, 5);
        Observable<Integer> ob2 = Observable.just(1, 2, 3, 4, 5, 6);

        Observable.sequenceEqual(ob1, ob2)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()));
    }

    /*
        This operator will skip items emitted by an observable until another observable starts to emit items.
     */
    public void testUsingSkipUntil() throws InterruptedException {
        Observable<Integer> obs = Observable.just(1, 2, 3).delay(3, TimeUnit.SECONDS);
        Observable.interval(1, TimeUnit.SECONDS)
                .skipUntil(obs)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));

        Thread.sleep(10000);
    }

    /*
        This operator will ignore the items emitted from the source observable until such time as the condition you specify becomes false. Then
        it will start to mirror the source observable.
     */
    public void testUsingSkipWhile() {
        Observable.range(1, 5)
                .skipWhile(num -> num < 3)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                    throwable -> System.out.println("OnError:" + throwable.toString()),
                    () -> System.out.println("Completed!"));
    }

    /*
        This operator will emit items from the source observable until the specified observable starts to emit items or terminate with error or complete notification.
     */
    public void testUsingTakeUntilWithObservable() throws InterruptedException {
        Observable<Integer> obs = Observable.just(1, 2, 3).delay(3, TimeUnit.SECONDS);
        Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(obs)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));
        Thread.sleep(10000);
    }

    /*
        This operator will emit items until the specified condition return false.
        TODO: Investigate why this operator does not emit item as expected.
     */
    public void testUsingTakeUntilWithCondition() {
        Observable.range(1, 5)
                .takeUntil(num -> num <= 3)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));
    }

    /*
        This operator will emit items from the source observable until the specified condition return false.
     */
    public void testUsingTakeWhile() {
        Observable.range(1, 5)
                .takeWhile(num -> num <= 3)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));
    }
}
