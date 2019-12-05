package ConnectableObservable;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

public class ConnectableObservableOperator {

    /*
        The difference between ordinary observable and ConnectableObservable is that ConnectableObservable only emit items after calling the
        connect operator. And all observers will observe the same stream from the observable.
     */
    public void testUsingPublishAndSubscribeAndConnect() {
        ConnectableObservable<Integer> connectableObservable = Observable.range(1, 10).publish();
        connectableObservable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " Observer1:" + num));
        connectableObservable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " Observer2:" + num));
        connectableObservable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " Observer3:" + num));
        connectableObservable.connect();
    }
}
