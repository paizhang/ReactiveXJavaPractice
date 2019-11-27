package Operators;

import io.reactivex.*;
import io.reactivex.functions.Function;

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
                        observableEmitter.onError(new Throwable("Error"));
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

    /*
        It will try to resubscribe the observable when getting a throwable from the source observable. It will keep retrying.
     */
    public void testUsingRetryWithoutParameter() {
        createObservableEmittingErrorAndItems().retry()
                .subscribe(s -> System.out.println("OnNext:" + s), throwable -> System.out.println("OnError:" + throwable.toString()), () -> System.out.println("Completed!"));
    }

    /*
        It will try to resubscribe the observable within the specific times.
     */
    public void testUsingRetryWithLimitedTimes() {
        createObservableEmittingErrorAndItems().retry(3)
                .subscribe(s -> System.out.println("OnNext:" + s), throwable -> System.out.println("OnError:" + throwable.toString()), () -> System.out.println("Completed!"));
    }

    /*
        This operator will apply a function to decide whether or not the error type thrown from the upstream is retryable.
        The input of this function is Observable<Throwable>. The output of this function is Observable<T>.
        This function will be triggered when there the upstream emit an error observable.
        Usually we apply a flatMap operator to the throwable observable to convert it into a target output observable.
        Nice article for explaining this operator with repeatWhen operator. 
     */
    public void testUsingRetryWhen() {
        createObservableEmittingErrorAndItems()
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(throwable -> {
                            if (throwable instanceof Exception)
                                return Observable.just("Retry");

                            return Observable.error(new Throwable("Terminate."));
                        });
                    }
                })
                .subscribe(s -> System.out.println("OnNext:" + s), throwable -> System.out.println("OnError:" + throwable.toString()), () -> System.out.println("Completed!"));
    }
}
