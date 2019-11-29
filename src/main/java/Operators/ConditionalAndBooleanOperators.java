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
}
