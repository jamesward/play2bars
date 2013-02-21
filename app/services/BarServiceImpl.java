package services;


import models.Bar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import play.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
@Transactional
public class BarServiceImpl implements BarService {

    @PersistenceContext
    EntityManager em;

    @Override
    public void addBar(Bar bar) {
        em.persist(bar);
    }

    @Override
    public List<Bar> getAllBars() {
        CriteriaQuery<Bar> c = em.getCriteriaBuilder().createQuery(Bar.class);
        c.from(Bar.class);
        return em.createQuery(c).getResultList();
    }

}