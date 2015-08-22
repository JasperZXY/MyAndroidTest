// IMyAidlRemoteService.aidl
package com.jasper.myandroidtest.service.aidl;

//注意这里，虽然Person类跟这个是在同一个包，但还是要import进来
import com.jasper.myandroidtest.service.aidl.Person;

interface IMyAidlRemoteService {

    /**
     *  获取Service的pid
     */
    int getPid();

    /**
      *  把参数转为List
      */
    List toList(int anInt, boolean aBoolean, float aFloat);

    /**
     *  获取随机一个人
     */
    Person getSomeone();

    /**
     *  多线程
     */
    String multiThread(int i);

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
