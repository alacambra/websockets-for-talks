package com.poolingpeople.talks.controller;

import com.poolingpeople.talks.entities.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sound.midi.Soundbank;
import java.util.Date;

/**
 * Created by alacambra on 1/6/15.
 */
@Singleton
public class DataMocker {

    @PersistenceContext
    EntityManager em;

    @PostConstruct
    private void init(){
        System.err.println("create user");
        createData();
    }

    public void createData(){
        User user = new User();
        user.setName("albert" + new Date());
        user.setPassword("a");
        em.persist(user);
    }
}
