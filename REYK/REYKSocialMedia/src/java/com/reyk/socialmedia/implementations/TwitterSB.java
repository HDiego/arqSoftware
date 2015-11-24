/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.socialmedia.implementations;

import com.reyk.socialmedia.interfaces.SocialMediaSBLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author MacAA
 */
@Stateful
public class TwitterSB implements SocialMediaSBLocal {

    private Twitter twitter;
    private RequestToken token;

    @Override
    public String connectToSocialMedia(String username) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("a7QcplYaabjAy7Qw6WsyNAkwy").setOAuthConsumerSecret("1xHSPlDU6eUp7YkkZL9oQioeQGuKiZHtwNuUCmTbARD2MoJLwV");
        twitter = new TwitterFactory(cb.build()).getInstance();
        try {
            token = twitter.getOAuthRequestToken();
        } catch (TwitterException ex) {
            return "Impossible to connect with Twitter";
        }
        return token.getAuthorizationURL();
    }

    @Override
    public String disconnectFromSocialMedia(String username) {
        String filename = "twitter-" + username + ".properties";
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
        assert (!file.exists());
        return "Your account information has been deleted. Delete from config settings your permissions.";
    }

    @Override
    public void addPin(String pin, String username) throws EJBException {
        String fileName = "twitter-" + username + ".properties";
        try {
            AccessToken accessToken = twitter.getOAuthAccessToken(token, pin);
            twitter.setOAuthAccessToken(accessToken);
            File file = new File(fileName);
            OutputStream outputStream = null;
            Properties prop = new Properties();
           
            prop.setProperty("oauth.consumerKey", "a7QcplYaabjAy7Qw6WsyNAkwy");
            prop.setProperty("oauth.consumerSecret", "1xHSPlDU6eUp7YkkZL9oQioeQGuKiZHtwNuUCmTbARD2MoJLwV");
            prop.setProperty("oauth.accessToken", accessToken.getToken());
            prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
            outputStream = new FileOutputStream(file);
            prop.store(outputStream, fileName);
            outputStream.close();

        } catch (FileNotFoundException fileEx) {
            throw new EJBException("Error while saving your account data. Try again.", fileEx);
        } catch (TwitterException twe) {
            throw new EJBException("Error while activating account. Try again.", twe);
        } catch (IOException io) {
            throw new EJBException("Error while saving your account data. Try again.", io);
        }
    }

    @Override
    public void postComment(String username, String post) throws EJBException {
        try {
            String fileName = "twitter-" + username + ".properties";
            File file = new File(fileName);
            OutputStream outputStream = null;
            Properties prop = new Properties();
            InputStream inputStream = null;
            

            try {
                if (file.exists()) {
                    inputStream = new FileInputStream(file);
                    prop.load(inputStream);
                } else {
                    throw new EJBException("There is no configuration int Twitter for " + username);
                }

            } catch (IOException ioEx) {
                ioEx.printStackTrace();
                System.exit(-1);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ignore) {
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException ignore) {
                    }
                }
            }
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey("a7QcplYaabjAy7Qw6WsyNAkwy")
                    .setOAuthConsumerSecret("1xHSPlDU6eUp7YkkZL9oQioeQGuKiZHtwNuUCmTbARD2MoJLwV")
                    .setOAuthAccessToken(prop.getProperty("oauth.accessToken"))
                    .setOAuthAccessTokenSecret(prop.getProperty("oauth.accessTokenSecret"));
            twitter = new TwitterFactory(cb.build()).getInstance();
            twitter.updateStatus(post);
        } catch (TwitterException e) {
            throw new EJBException("Error en el twitter sb al twittear", e);
        }
    }

}
