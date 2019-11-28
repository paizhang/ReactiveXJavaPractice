package Operators;

import io.reactivex.Observable;

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
}
