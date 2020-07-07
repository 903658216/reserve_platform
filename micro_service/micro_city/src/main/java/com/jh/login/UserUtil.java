package com.jh.login;

import com.jh.entity.User;

/**
 * ThreadLocal
 */
public class UserUtil {

    private  static  ThreadLocal<User> user = new ThreadLocal<>();

    public   static  User getUser(){

        return  UserUtil.user.get();
    }

    public static void setUser(User user){

        UserUtil.user.set(user);
    }


    public  static void  main(String[] args){

        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("Hello");

        new Thread(){
            @Override
            public void run() {
//                super.run();
                threadLocal.set("hello");
                System.out.println("子线程"+threadLocal.get());
            }
        }.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String s = threadLocal.get();
        System.out.println(s);

    }
}
