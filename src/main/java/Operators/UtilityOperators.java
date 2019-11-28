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

}
