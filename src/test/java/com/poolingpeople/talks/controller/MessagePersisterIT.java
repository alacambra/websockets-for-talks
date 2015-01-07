package com.poolingpeople.talks.controller;

import com.poolingpeople.talks.entities.Talk;
import com.poolingpeople.talks.entities.TalkLog;
import com.poolingpeople.talks.entities.User;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class MessagePersisterIT {

    private EntityManager em;
    private EntityTransaction tx;
    private User userA;
    private User userB;

    private MessagePersister messagePersister;

    @Before
    public void setUp() throws Exception {
        em = Persistence.createEntityManagerFactory("it").createEntityManager();
        tx = this.em.getTransaction();

        messagePersister = new MessagePersister();
        messagePersister.em = em;

        userA = new User();
        userA.setName("Alpha");
        userA.setPassword("a");

        userB = new User();
        userB.setName("Beta");
        userB.setPassword("b");

        tx.begin();
        em.persist(userA);
        em.persist(userB);
        tx.commit();
        tx = em.getTransaction();
    }



    @Test
    public void testAddLog() throws Exception {
        messagePersister.em = em;

        User user = new User();

        user.setName("AlbertTest");
        user.setPassword("a");

        TalkLog log1 = getRandomTalkLog();
        messagePersister.addLog(123L, log1);
        TalkLog log2 = getRandomTalkLog();
        messagePersister.addLog(123L, log2);
        TalkLog log3 = getRandomTalkLog();
        messagePersister.addLog(33L, log3);
        TalkLog log4 = getRandomTalkLog();
        messagePersister.addLog(33L, log4);
        TalkLog log5 = getRandomTalkLog();
        messagePersister.addLog(33L, log5);

        assertThat(messagePersister.logs.size(), Is.is(2));
        assertThat(messagePersister.logs.get(123L).size(), Is.is(2));
        assertThat(messagePersister.logs.get(33L).size(), Is.is(3));

    }

    @Test
    public void testPersistLogsOneUser() throws Exception {

        StringBuffer buffer1 = new StringBuffer();
        StringBuffer buffer2 = new StringBuffer();

        TalkLog log1 = getRandomTalkLog();
        messagePersister.addLog(123L, log1);
        buffer1.append(log1.getLog());

        TalkLog log2 = getRandomTalkLog();
        messagePersister.addLog(123L, log2);
        buffer1.append("\n" + log2.getLog());

        TalkLog log3 = getRandomTalkLog();
        messagePersister.addLog(33L, log3);
        buffer2.append(log3.getLog());

        TalkLog log4 = getRandomTalkLog();
        messagePersister.addLog(33L, log4);
        buffer2.append("\n" + log4.getLog());

        TalkLog log5 = getRandomTalkLog();
        messagePersister.addLog(33L, log5);
        buffer2.append("\n" + log5.getLog());

        tx.begin();
        messagePersister.persistLogs();
        tx.commit();
        tx = em.getTransaction();
        tx.begin();

        Talk t = em.createNamedQuery("talk.getById", Talk.class).setParameter("id", 123L).getSingleResult();
        assertThat(t.getUuid(), Is.is(123L));
        List<TalkLog> logs = t.getTalkLogs();
        assertThat(logs.size(), Is.is(1));
        assertThat(logs.get(0).getLog(), Is.is(buffer1.toString()));

        t = em.createNamedQuery("talk.getById", Talk.class).setParameter("id", 33L).getSingleResult();
        assertThat(t.getUuid(), Is.is(33L));
        logs = t.getTalkLogs();
        assertThat(logs.size(), Is.is(1));
        assertThat(logs.get(0).getLog(), Is.is(buffer2.toString()));

        tx.commit();
    }

    private TalkLog getRandomTalkLog(){
        return getRandomTalkLog(userA);
    }

    private TalkLog getRandomTalkLog(User u){
        TalkLog log = new TalkLog();
        log.setAuthor(u);
        Random r = new Random();
        Calendar.getInstance().setTime(new Date(r.nextLong()));
        log.setCreationTime(Calendar.getInstance());
        log.setLog("lalalala");
        return log;
    }
}