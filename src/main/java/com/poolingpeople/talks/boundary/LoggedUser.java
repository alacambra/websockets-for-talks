package com.poolingpeople.talks.boundary;

import com.poolingpeople.talks.controller.DataMocker;
import com.poolingpeople.talks.entities.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Created by alacambra on 1/6/15.
 */
@Stateless
public class LoggedUser {
    @Inject
    DataMocker mocker;

    @PersistenceContext
    EntityManager em;
    User user;


    @PostConstruct
    public void init(){
        mocker.createData();
        CriteriaQuery<User> cq = em.getCriteriaBuilder().createQuery(User.class);
        cq.select(cq.from(User.class));
        user = em.createQuery(cq).getResultList().get(0);
    }

    public User getUser() {
        return user;
    }
}
