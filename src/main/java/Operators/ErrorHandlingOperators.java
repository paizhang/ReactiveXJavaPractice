package Operators;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ErrorHandlingOperators {
    /*
        OnErrorReturn will intercept an error from the source observable and convert it into an specific obervable and terminate the source observable by
        calling onComplete of the observer.  
     */
    public void testUsingOnErrorReturn() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                for (int i = 0; i < 6; i++) {
                    // isDisposed() will return true if the downstream disposed the sequence or the emitter was terminated via Emitter.onError(Throwable), Emitter.onComplete() or a successful tryOnError(Throwable).
                    if (observableEmitter.isDisposed()) {
                        return;
                    }
                    if (i % 2 == 0) {
                        observableEmitter.onNext("Item: " + String.valueOf(i));
                    } else {
                        observableEmitter.onError(new Throwable("Error"));
                    }
                }
            }
        }).onErrorReturn(throwable -> "This is returned from onErrorReturn")
                .subscribe(s -> System.out.println("OnNext:" + s), throwable -> System.out.println("OnError:" + throwable.toString()), () -> System.out.println("Completed!"));
    }
}
