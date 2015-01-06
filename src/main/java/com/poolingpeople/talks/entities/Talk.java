package com.poolingpeople.talks.entities;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * Created by alacambra on 1/6/15.
 */

@NamedQueries({
        @NamedQuery(name = "talk.getById", query = "SELECT talk FROM Talk as talk where talk.id = :id ")
})
@Entity
public class Talk {

    @Id
    @GeneratedValue
    Long id;
    String title;

    @Temporal(TemporalType.DATE)
    Calendar creationTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "talk")
    List<TalkLog> talkLogs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TalkLog> getTalkLogs() {
        return talkLogs;
    }

    public void setTalkLogs(List<TalkLog> talkLogs) {
        this.talkLogs = talkLogs;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }
}
