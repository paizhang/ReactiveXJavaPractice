package Operators;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class UtilityOperators {
    /*
        This operator will time shift a specific amount of time to emit a observable. (Note that this will only work for onNext and onComplete observables.
        Not for onError observable.) By default it is run in onComputation scheduler.
     */
    public void testUsingDelay() throws InterruptedException {
        System.out.println("Started timestamp:" + new Timestamp(System.currentTimeMillis()));
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .delay(2, TimeUnit.SECONDS);
        System.out.println("Before sleep");
        Thread.sleep(3000);
        observable.subscribe(s -> {System.out.println(new Timestamp(System.currentTimeMillis()));System.out.println("OnNext:" + s);},
                           throwable -> System.out.println("OnError:" + throwable.toString()),
                           () -> System.out.println("Completed!"));
        Thread.sleep(10000);
    }

    /*
        TODO: Figure out what is the difference between delay and delaySubscription.
     */
    public void testUsingDelaySubscription() throws InterruptedException {
        System.out.println("Started timestamp:" + new Timestamp(System.currentTimeMillis()));
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .delaySubscription(2, TimeUnit.SECONDS);
        System.out.println("Before sleep");
        Thread.sleep(3000);
        observable.subscribe(s -> {System.out.println(new Timestamp(System.currentTimeMillis()));System.out.println("OnNext:" + s);},
                throwable -> System.out.println("OnError:" + throwable.toString()),
                () -> System.out.println("Completed!"));
        Thread.sleep(10000);
    }

    /*
        This is a good example showing what the order of the calls for do family operators.
     */
    public void testUsingDoOperators() {
        Observable.just(1, 2, 3)
                .doOnNext(num -> System.out.println("doOnNext:" + num))
                .doAfterNext(num -> System.out.println("doAfterNext:" + num))
                .doOnError(throwable -> {System.out.println("doOnError.");})
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnSubscribe((s) -> System.out.println("doOnSubscribe"))
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate"))
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));

    }

    /*
        doOnEach operator basically will be triggered every time before onNext or onError or onComplete is called.
        TODO: Investigate what the functionality of disposable is.
     */
    public void testUsingDoOnEach() {
        Observable.just(1, 2, 3)
                .doOnEach(new Observer<Integer>() {
                    @Override
                    public void onComplete() {
                        System.out.println("doOnEach onComplete. ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("doOnEach onNext.");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("doOnEach onError:" + throwable.toString());
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                       System.out.println("doOnEach onSubscribe.");
                    }
                }).subscribe(s -> {System.out.println("OnNext:" + s);},
                throwable -> System.out.println("OnError:" + throwable.toString()),
                () -> System.out.println("Completed!"));
    }

    /*
        This is operator is used to encapsulate emitted observables into notifications.
        onNext observable  ->  OnNextNotification
        onComplete observable -> OnCompleteNotification
        onError observable  -> OnErrorNotification.

        No matter what original type of observable is, it will only trigger the observer's onNext method. This make it constant for writing the
        handler functions for all kinds of original observables.

        After it completes, the observer's onComplete() function will be called.
        In the onNext(), we can call notification.getValue() to get the original value. We can also call isOnNext(), isOnError() and isOnComplete()
        to check the original observable type.

     */
    public void testUsingMaterialize() {
        Observable.range(0, 3)
                .materialize()
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));
    }

    /*
        This is the reverse process of the materialize operator. It convert a notification into original observable.
        TODO: Investigate what the functionality of the input function is.
     */
    public void testUsingDeMaterialize() {
        Observable.range(0, 3)
                .materialize()
                .dematerialize(notification -> notification)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));
    }

    /*
        This operator will convert the source observable which emits items into a observable which emits the time elapsed between emissions of
        the source items. By default it will return time interval using MILLISECONDS as time unit. But we can change it by specify the time unit
        in the input parameter.
     */
    public void testUsingTimeInterval() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .timeInterval(TimeUnit.SECONDS)
                .subscribe(s -> {System.out.println("OnNext:" + s);},
                        throwable -> System.out.println("OnError:" + throwable.toString()),
                        () -> System.out.println("Completed!"));

        Thread.sleep(10000);
    }

}
