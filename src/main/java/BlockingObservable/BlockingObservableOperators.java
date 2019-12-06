package BlockingObservable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class BlockingObservableOperators {

    /*
        The blocking operator family will block the current thread until it returns the item with original type.
        Below is an example for using blockingFirst() which will returns the first item emit by the Observable with Integer type.
     */
    public void testUsingToBlockingAndFirst() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        Integer num = observable.blockingFirst();
        observable.subscribe(n -> System.out.println(n));
        System.out.println(num);
    }

    /*
        BlockingLast operator will block the main thread until it return the last item with its original type.
     */
    public void testUsingBlockingLast() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                Thread.sleep(2000);
                observableEmitter.onNext(2);
                Thread.sleep(2000);
                observableEmitter.onNext(3);
                Thread.sleep(2000);
                observableEmitter.onComplete();
            }
        });
        Integer lastNum = observable.blockingLast();
        System.out.println("Last item using blockingLast: " + lastNum);
        observable.subscribe(num -> System.out.println("In subscriber:" + num));
    }

    /*
        BlockingIterable will return an iterable for items emitted from the observable. 
     */
    public void testUsingBlockingIterator() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        Iterable it = observable.blockingIterable();
        it.forEach(num -> System.out.println(num));
    }
}
