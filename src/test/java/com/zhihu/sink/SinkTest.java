package com.zhihu.sink;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: shenchen
 * Date: 7/17/12
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinkTest {
    private Client subClient;
    private Client pubClient;
    private static ExecutorService pool;
    private String topic1;
    private String topic2;

    @Before
    public void setUp(){
        subClient = new Client("localhost");
        pubClient = new Client("localhost");
        topic1 = "topic"+Math.random();
        topic2 = "topic"+Math.random();
        pool = Executors.newFixedThreadPool(1);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try{
                    for( int i = 0 ; i < 10 ; i ++){
                        pubClient.publish(topic1,"hello"+i);
                    }

                    for( int i = 0 ; i < 10 ; i ++){
                        pubClient.publish(topic2,"world"+i);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void testPubSub() throws InterruptedException {
        PubSub pubsub = subClient.pubSub();
        pubsub.psubscribe("topic*");
        pubsub.psubscribe("*");


        for( int i = 0 ; i < 10 ; i ++ ){
            String s = pubsub.next();
            System.out.println(s);
            if(i == 4){
                subClient.disconnect();
            }
            assertEquals(s,"hello"+i);
        }
        for( int i = 0 ; i < 10 ; i ++ ){
            String s = pubsub.next();
            System.out.println(s);
            if(i == 4){
                subClient.disconnect();
            }
            assertEquals(s,"world"+i);
        }
    }

    @After
    public void tearDown(){
    }
}
