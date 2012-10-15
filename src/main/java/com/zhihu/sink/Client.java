package com.zhihu.sink;

import com.zhihu.sink.exception.SinkDataException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhihu.sink.Protocol.Command.*;

/**
 * Created with IntelliJ IDEA.
 * User: shenchen
 * Date: 7/13/12
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client extends Connection{
    private String     session;

    public Client(final String host){
        super(host);
    }

    public Client(final String host, int port){
        super(host, port);
    }

    public Client(final String host, int port, int retry){
        super(host, port, retry);
    }

    public void connect(){
        super.connect();
        if(session == null)
            mksession();
        else
            ldsession();
    }

    public void publish(String topic, String message){
        super.connect();
        sendCommand(PUBLISH,topic,message);
        List<String> reply = getStringReply();
        if(reply.get(0).equals("ERR"))
            throw new SinkDataException("ERR");;
    }

    private void mksession(){
        sendCommand(MKSESSION);
        List<String> reply = getStringReply();
        if(reply.get(0).equals("ERR"))
            throw new SinkDataException("ERR");
        session = reply.get(1);
    }

    private void ldsession(){
        sendCommand(LDSESSION,session);
        List<String> reply = getStringReply();
        if(reply.get(0).equals("ERR"))
            throw new SinkDataException("ERR");
    }

    public PubSub pubSub(){
        return new CPubSub(this);
    }

    static class Topic{
        String topic;
        Protocol.Command cmd;
        Protocol.Command unCmd;
        int offset;

        public Topic(String topic, Protocol.Command cmd){
            this.topic = topic;
            this.cmd = cmd;
            if(cmd == PSUBSCRIBE) {
                this.offset = 1;
                unCmd = PUNSUBSCRIBE;
            }
            else {
                this.offset = 0;
                unCmd = UNSUBSCRIBE;
            }
        }
    }

    static class CPubSub implements PubSub{
        private final Client client;
        private final List<Topic> topics;
        private final Map<String,Topic> topicMaps;

        CPubSub(Client client) {
            this.client = client;
            this.topics = new ArrayList<Topic>();
            this.topicMaps = new HashMap<String,Topic>();
        }

        public void addTopic(String topic, Protocol.Command cmd){
            Topic tp = new Topic(topic, cmd);
            this.topics.add(tp);
            this.topicMaps.put(topic, tp);
            subTopics();
        }

        void subTopics(){
            client.connect();
            for(Topic topic:topics){
                client.sendCommand(topic.cmd,topic.topic);
                List<String> reply = client.getStringReply();
                if(reply.get(0).equals("ERR") || !reply.get(1).equals(topic.topic))
                    throw new SinkDataException("ERR");
            }
        }

        @Override
        public String next() {
            if(!client.isConnected()){
                subTopics();
            }

            client.sendCommand(NEXT);
            client.setTimeoutInfinite();
            try{
                List<String> reply = client.getStringReply();
                if(reply.get(0).equals("ERR"))
                    throw new SinkDataException("ERR");
                Topic tp = this.topicMaps.get(reply.get(2));
                return reply.get(3+tp.offset);
            }
            finally {
                client.rollbackTimeout();
            }
        }

        @Override
        public void stop() {
            client.connect();
            for(Topic topic:topics){
                client.sendCommand(topic.unCmd,topic.topic);
                List<String> reply = client.getStringReply();
                if(reply.get(0).equals("ERR"))
                    throw new SinkDataException("ERR");
            }
        }

        @Override
        public void psubscribe(String s) {
            addTopic(s, PSUBSCRIBE);
        }

        @Override
        public void subscribe(String s) {
            addTopic(s, SUBSCRIBE);
        }


    }

}
