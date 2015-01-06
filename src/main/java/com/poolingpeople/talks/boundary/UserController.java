package com.poolingpeople.talks.boundary;

import com.poolingpeople.talks.controller.DataMocker;
import com.poolingpeople.talks.controller.MessagePersister;
import com.poolingpeople.talks.entities.User;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by alacambra on 1/6/15.
 */
@ManagedBean
@RequestScoped
@Named
public class UserController {

    @PersistenceContext
    EntityManager em;

    @Inject
    DataMocker mocker;

    @Inject
    MessagePersister persister;

    public List<User> getUsers(){
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        return em.createQuery(cq).getResultList();
    }

    public void loadData(){
        mocker.createData();
    }

    public void persistLogsNow(){
        persister.persistLogs();
    }
}
