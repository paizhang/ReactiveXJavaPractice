package ConnectableObservable;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

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

    /*
        Replay(int buffer) operator generate a ConnectableObservable. It begins to emit items when connect() is called. Multiple observers will
        observe the same stream. When a new observer starts to observe the stream, it will receive all items in the buffer and following items after
        the time it subscribes.

        By default, all subscribers all run in a computation thread. We can assign each subscriber a distinct thread by specifying a scheduler in
        the parameters of the operator.

        TODO: It seems the reconnect part is not implemented in Rxjava. You can follow up with this feature. 
     */
    public void testUsingReplyAndSubscribeAndConnect() throws InterruptedException {
        ConnectableObservable<Long> connectableObservable = Observable.interval(1, TimeUnit.SECONDS).replay(2, Schedulers.computation());
        connectableObservable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " First subscriber:" + num),
                throwable -> {System.out.println(throwable.toString());},
                () -> System.out.println("Complete!"));
        connectableObservable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " Second subscriber:" + num),
                throwable -> {System.out.println(throwable.toString());},
                () -> System.out.println("Complete!"));
        Disposable disposable = connectableObservable.connect();
        Thread.sleep(2000);
        disposable.dispose();
        Thread.sleep(10000);
    }
}
