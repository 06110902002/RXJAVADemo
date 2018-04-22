package com.example.administrator.rxjava;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    private TextView ObserverAbleText = null;
    private TextView ObserverText = null;
    private TextView result;
    private TextView customEvent;
    private TextView test1;
    private TextView test2;
    private ImageView img;

    private TextView eventChnage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);

        ObserverText = (TextView) findViewById(R.id.create_observer);
        ObserverAbleText = (TextView) findViewById(R.id.create_observerable);
        customEvent = (TextView) findViewById(R.id.observer_event);
        test1 = (TextView) findViewById(R.id.test1);
        test2 = (TextView) findViewById(R.id.test2);
        eventChnage= (TextView) findViewById(R.id.event_change);
        img = (ImageView)findViewById(R.id.img);

        EventClick click = new EventClick();
        ObserverText.setOnClickListener(click);
        ObserverAbleText.setOnClickListener(click);
        customEvent.setOnClickListener(click);
        test1.setOnClickListener(click);
        test2.setOnClickListener(click);
        eventChnage.setOnClickListener(click);

    }

    private class EventClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.create_observer:
                    createObserver();
                    break;

                case R.id.create_observerable:
                    createObserverAble();
                    break;

                case R.id.observer_event:
                    customObserverCallbackEvent();
                    break;

                case R.id.test1:
                    showTest();
                    break;

                case R.id.test2:
                    showImg();
                    break;

                case R.id.event_change:
                    Intent intent = new Intent(MainActivity.this,EventChangeActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    }

    /**
     * rxjava中:Observer对应观察者模式中的观察者
     */
    private Observer createObserver() {

        /**
         * 其中Observer泛型类型要求与被观察者传递过来的数据类型一致
         */
        Observer<String> observer = new Observer<String>() {
            StringBuilder sb = new StringBuilder();

            @Override
            public void onNext(String s) {
                sb.append("\nonNext:");
                sb.append(s);

            }

            @Override
            public void onCompleted() {
                sb.append(":onCompleted:");
                result.setText(sb);
            }

            @Override
            public void onError(Throwable e) {
                result.setText("Error!");
            }
        };

        return observer;

    }

    /**
     * rxjava中:Observable对应观察者模式中的被观察者，即观察者观察的主题
     */
    private Observable createObserverAble() {
        /**
         * 被观察者，创建一个可供观察的String 对象类型的数据
         */
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {

            /**
             * 发送事件
             * @param subscriber
             */
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(createObserver()); //将被观察者的对象传递给，观察者,,相当于，在被观察者中注册观察者信息

        return observable;
    }


    /**
     * 自定义Oberser中的回调事件
     */
    private void customObserverCallbackEvent() {

        /**
         * 泛型演示
         */
        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                System.out.println("136---------------onNextAction:  "+s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                System.out.println("143---------------onErrorAction");
            }
        };

        /**
         * 传递普通类型
         */
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                System.out.println("143---------------onCompletedAction");
            }
        };

        Observable observable = createObserverAble();

        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        //observable.subscribe(onNextAction);

        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        //observable.subscribe(onNextAction, onErrorAction);

        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    /**
     * 将字符串数组 names 中的所有字符串依次打印出来：
     * 在RxJava 中，Scheduler ——调度器，相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。
     * RxJava 已经内置了几个 Scheduler ，它们已经适合大多数的使用场景：
     * Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
     * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
     * Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，
     * 区别在于 io() 的内部实现是是用一个无数量上限的线程池，
     * 可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
     * Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
     * 另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
     * 有了这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。
     * * subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
     * * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
     */
    private void showTest(){

        String[] names = {"东南大学","南京大学","北京大学"};
        Observable.from(names)
                .subscribeOn(Schedulers.io())               // 指定 subscribe() 发生在 IO 线程
                //.observeOn(AndroidSchedulers.mainThread())  // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        System.out.println("187---------"+name);
                    }
                });

    }


    /**
     * 演示例子二
     */
    private void showImg(){
        final int drawableRes = R.mipmap.ic_launcher;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
               // Drawable drawable = getTheme().getDrawable(drawableRes);
               // subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onNext(Drawable drawable) {
                img.setImageDrawable(drawable);
            }

            @Override
            public void onCompleted() {

                Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
