package com.poolingpeople.talks.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by alacambra on 1/6/15.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    Long id;
    String name;
    String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof User)){
            return false;
        }
        return ((User)obj).getId() == id;
    }
}
