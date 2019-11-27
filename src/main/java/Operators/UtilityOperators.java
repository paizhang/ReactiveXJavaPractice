package Operators;

import io.reactivex.Observable;

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

}
