package com.poolingpeople.talks.entities;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alacambra on 1/6/15.
 */
@Entity
public class TalkLog {

    @Id
    @GeneratedValue
    Long id;

    @OneToOne
    User author;

    @Temporal(TemporalType.DATE)
    Calendar creationTime;

    @Transient
    StringBuilder logBuilder;

    @ManyToOne
    Talk talk;

    String log;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    public void append(TalkLog log){

        if(logBuilder == null) {
            logBuilder = new StringBuilder(log.getLog());
            author = log.getAuthor();
            creationTime = log.getCreationTime();
            return;
        }

        logBuilder.append("\n" + log.getLog());
    }


}
