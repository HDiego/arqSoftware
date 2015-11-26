/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.jms;

import javax.jms.Message;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

/**
 *
 * @author diegorocca
 */
public class Publisher implements Runnable {

    private TopicSession session;
    private TopicPublisher publisher;
    private String message;

    public Publisher(javax.jms.Topic topic, TopicSession sess, String message) throws Exception {
        this.session = sess; //connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        this.publisher = this.session.createPublisher(topic);
        this.message = message;
    }

    public void close() throws Exception {
        publisher.close();
        session.close();
    }

    @Override
    public void run() {
        try {
            Message mensaje = session.createTextMessage(String.format(message));
            publisher.publish(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
