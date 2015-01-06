package com.poolingpeople.talks.controller;

import com.poolingpeople.talks.boundary.LoggedUser;
import com.poolingpeople.talks.entities.Talk;
import com.poolingpeople.talks.entities.TalkLog;
import com.poolingpeople.talks.entities.User;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by alacambra on 1/6/15.
 */
@Singleton
public class MessagePersister {

    final Map<Long, List<TalkLog>> logs = Collections.synchronizedMap(new HashMap<Long, List<TalkLog>>());

    @PersistenceContext
    EntityManager em;

    public void addLog(Long talkId, TalkLog log){
        if(!logs.containsKey(talkId)){
            logs.put(talkId, new LinkedList<TalkLog>());
        }

        logs.get(talkId).add(log);
    }

    @Schedule(second = "*/15")
    @Transactional
    public void persistLogs(){
        for(Map.Entry<Long, List<TalkLog>> talklog : logs.entrySet()){
            persistLogsOfTalk(talklog.getKey(), talklog.getValue());
        }
    }

    private void persistLogsOfTalk(Long talkId, List<TalkLog> talkLogs){

        if(talkLogs.isEmpty()) return;

        List<Talk> talks= em.createNamedQuery("talk.getById", Talk.class).setParameter("id", talkId).getResultList();
        Talk talk;
        if(talks.size() == 0){
            talk = new Talk();
            talk.setTitle("that is a new talk:" + new Date());
            talk.setCreationTime(Calendar.getInstance());
            em.persist(talk);
        } else {
            talk = talks.get(0);
        }

        talk.setTalkLogs(new ArrayList<TalkLog>());
        User currentAuthor = null;
        TalkLog currentTalkLog = null;

        for(TalkLog userlog : talkLogs){

            if(currentAuthor == null){

                currentAuthor = userlog.getAuthor();
                currentTalkLog = new TalkLog();
                currentTalkLog.append(userlog);

            } else if(currentAuthor.equals(userlog.getAuthor())){

                currentTalkLog.append(userlog);

            } else {

                talk.getTalkLogs().add(currentTalkLog);
                em.persist(talk);
                currentAuthor = userlog.getAuthor();
                currentTalkLog = new TalkLog();
                currentTalkLog.append(userlog);

            }
        }
    }
}
