package Operators;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ErrorHandlingOperators {

    private Observable createObservableEmittingErrorAndItems() {
        return Observable.create(new ObservableOnSubscribe<String>() {
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
                        observableEmitter.onError(new Exception("Error"));
                    }
                }
            }
        });
    }

    /*
        OnErrorReturn will intercept an error from the source observable and convert it into an specific obervable and terminate the source observable by
        calling onComplete of the observer.
     */
    public void testUsingOnErrorReturn() {
        createObservableEmittingErrorAndItems().onErrorReturn(throwable -> "This is returned from onErrorReturn")
                .subscribe(s -> System.out.println("OnNext:" + s), throwable -> System.out.println("OnError:" + throwable.toString()), () -> System.out.println("Completed!"));
    }

    /*
        OnErrorResumeNext() operator will intercepts an error from the source observable and convert it into a specific obervable, then terminate the source
        observable by calling the onComplete() function of observer.
     */
    public void testUsingOnErrorResumeNext() {
        createObservableEmittingErrorAndItems().onErrorResumeNext(Observable.just(1, 2, 3))
                .subscribe(s -> System.out.println("OnNext:" + s), throwable -> System.out.println("OnError:" + throwable.toString()), () -> System.out.println("Completed!"));
    }

    /*
        This operator is much like the onErrorResumeNext. But this operator will check the type of Throwable. If the type is Exception, it will convert it into
        a specific observable and terminate the source observable by calling onComplete function. Otherwise, it will pass it to onError() of the observer.
     */
    public void testUsingOnExceptionResumeNext() {
        createObservableEmittingErrorAndItems().onExceptionResumeNext(Observable.just(1, 2, 3, 4, 5))
                .subscribe(s -> System.out.println("OnNext:" + s), throwable -> System.out.println("OnError:" + throwable.toString()), () -> System.out.println("Completed!"));
    }
}
