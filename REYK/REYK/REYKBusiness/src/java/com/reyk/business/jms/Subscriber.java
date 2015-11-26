/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

/**
 *
 * @author diegorocca
 */
public class Subscriber implements MessageListener {

    private TopicSession session;
    private TopicSubscriber subscriber;
    private String nombre;

    public Subscriber(javax.jms.Topic topic, TopicSession sess, String nombre, String title) throws Exception {
        this.session = sess;//connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        this.subscriber = this.session.createSubscriber(topic, null, false);
        this.subscriber.setMessageListener(this);
        this.nombre = nombre;
    }

    public void close() throws Exception {
        subscriber.close();
        session.close();
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage text = (TextMessage) message;
            System.out.printf("Se aviso al suscriptor " + nombre +", el nuevo libro publicador es: " + text.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
