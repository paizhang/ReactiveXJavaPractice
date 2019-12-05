package ConnectableObservable;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/*
    A nice article explaining ConnectableObservable: https://www.jianshu.com/p/575ce5b98389
 */
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

    public void testUsingRefCount() throws InterruptedException {
        /*
            This is an example showing how subscription works when two observers subscribe the same ordinary observable at different time. Basically
            two observers will receive different streams running in different threads started from the time they subscribe.
         */
        /*
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
        observable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " First observer:" + num));
        Thread.sleep(2000);
        observable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " Second observer:" + num));
        Thread.sleep(10000);
        */

        /*
            This is an example for converting a ConnectableObservable created using replay() into an Observable. The converted Observable is a cold
            observable. That means it will start emit items when the first observer starts to subscribe. But this converted observable is not a ordinary
            Observable that we see previously. The difference is that this converted observable still keeps the "replay property". When the second
            observer subscribes, it receives all items in the buffer along with the following items from the time it subscribes.
         */
        /*
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).replay(2).refCount();
        observable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " First observer:" + num));
        Thread.sleep(5000);
        observable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " Second observer:" + num));
        Thread.sleep(10000);
        */

        /*
            This is an example for converting a ConnectableObservable created using publish() into an Observable. The converted Observable is a cold
            observable. That means it will start emit items when the first observer starts to subscribe. But this converted observable is not a ordinary
            Observable that we see previously. The difference is that this converted observable still keeps the "publish property". When the second
            observer subscribes, it receives items from the time it subscribes.
         */
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).publish().refCount();
        observable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " First observer:" + num));
        Thread.sleep(5000);
        observable.subscribe(num -> System.out.println("Thread: " + Thread.currentThread().getName() + " Second observer:" + num));
        Thread.sleep(10000);
        
    }

}
