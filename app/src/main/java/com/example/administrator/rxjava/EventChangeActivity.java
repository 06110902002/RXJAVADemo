package com.example.administrator.rxjava;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/1/5.
 */

public class EventChangeActivity extends Activity {

    private TextView map;
    private TextView flatmap;
    private TextView flatmap2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initView();
    }

    private void initView(){
        map = (TextView) findViewById(R.id.map);
        flatmap = (TextView) findViewById(R.id.falt_map);
        flatmap2 = (TextView) findViewById(R.id.falt_map2);

        EventClick click = new EventClick();
        map.setOnClickListener(click);
        flatmap.setOnClickListener(click);
        flatmap2.setOnClickListener(click);
    }

    private class EventClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.map:
                    map();
                    break;
                case R.id.falt_map:
                    flat_map();
                    break;

                case R.id.falt_map2:
                    flat_map2();
                    break;
            }
        }
    }

    /**
     * 使用map函数将文件字符串变成，一张图片
     * 使用的场景可为在网络请求中，将输入的参数，得到请求结果之后，转换为想要的回传结果
     */
    private void map(){
        /*Observable.just("images/logo.png") // 输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) { // 参数类型 String
                        return getBitmapFromPath(filePath); // 返回类型 Bitmap
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) { // 参数类型 Bitmap
                        showBitmap(bitmap);
                    }
                });*/

    }

    /**
     * 假设有一个数据结构『学生』，现在需要打印出一组学生的名字。实现方式很简单：
     */
    private void flat_map(){

        Student s1 = new Student();
        s1.setName("东南大学");
        s1.setAge(116);

        Student s2 = new Student();
        s2.setName("南京大学");
        s2.setAge(1162);

        Student[] students = {s1,s2};
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String name) {
                System.out.println("99--------"+name);
            }

        };
        Observable.from(students)
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(subscriber);

    }

    /**
     * 使用场景一：如果要打印出每个学生所需要修的所有课程的名称呢？
     * （需求的区别在于，每个学生只有一个名字，但却有多个课程。）
     *
     *
     *
     * flatMap() 和 map() 有一个相同点：它也是把传入的参数转化之后返回另一个对象。
     * 但需要注意，和 map() 不同的是， flatMap() 中返回的是个 Observable 对象，
     * 并且这个 Observable 对象并不是被直接发送到了 Subscriber 的回调方法中。
     * flatMap() 的原理是这样的：
     * 1. 使用传入的事件对象创建一个 Observable 对象；
     * 2. 并不发送这个 Observable, 而是将它激活，于是它开始发送事件；
     * 3. 每一个创建出来的 Observable 发送的事件，都被汇入同一个 Observable ，
     * 而这个 Observable 负责将这些事件统一交给 Subscriber 的回调方法。
     * 这三个步骤，把事件拆成了两级，通过一组新创建的 Observable 将初始的对象『铺平』之后通过统一路径分发了下去。
     * 而这个『铺平』就是 flatMap() 所谓的 flat。
     */
    private void flat_map2(){


        Student s1 = new Student();
        s1.setName("张无忌");
        s1.setAge(116);

        List<Curse> curseList = new ArrayList<>();
        Curse curse = new Curse();
        curse.setCurse("数学");
        Curse curse2 = new Curse();
        curse2.setCurse("语文");
        curseList.add(curse);
        curseList.add(curse2);

        s1.setCurses(curseList);

        Student s2 = new Student();
        s2.setName("郭靖");
        s2.setAge(1162);

        List<Curse> curseList2 = new ArrayList<>();
        Curse curse3 = new Curse();
        curse3.setCurse("理综");
        Curse curse4 = new Curse();
        curse4.setCurse("英语");
        curseList2.add(curse3);
        curseList2.add(curse4);
        s2.setCurses(curseList2);


        Student[] students = {s1,s2};

        /**--------------实现方式一----------------***/
        /*Subscriber<Student> subscriber = new Subscriber<Student>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(Student student) {
                List<Curse> courses = student.getCurses();
                for (int i = 0; i < courses.size(); i++) {
                    Curse course = courses.get(i);
                    System.out.println("186-----------"+course.getCurse());
                }
            }

        };
        Observable.from(students)
                .subscribe(subscriber);*/
        /**--------------实现方式一完毕----------------***/

        /**--------------实现方式二----------------***/
        Subscriber<Curse> subscriber2 = new Subscriber<Curse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Curse course) {  //第二步：这里的对象，是由flatMap所激活的对象
                System.out.println("209-----------"+course.getCurse());
            }

        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Curse>>() {  //第一步：.flatMap激活一个Curse对象 ，通过激活的对象去发送事件
                    @Override
                    public Observable<Curse> call(Student student) {
                        return Observable.from(student.getCurses());
                    }
                })
                .subscribe(subscriber2);

    }
}
