/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hastane_sistemi;

import com.mycompany.hastane_sistemi.exceptions.IllegalOrphanException;
import com.mycompany.hastane_sistemi.exceptions.NonexistentEntityException;
import com.mycompany.hastane_sistemi.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omerfaruk
 */
public class HastaJpaController implements Serializable {

    public HastaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Hasta hasta) throws PreexistingEntityException, Exception {
        if (hasta.getRandevuCollection() == null) {
            hasta.setRandevuCollection(new ArrayList<Randevu>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Randevu> attachedRandevuCollection = new ArrayList<Randevu>();
            for (Randevu randevuCollectionRandevuToAttach : hasta.getRandevuCollection()) {
                randevuCollectionRandevuToAttach = em.getReference(randevuCollectionRandevuToAttach.getClass(), randevuCollectionRandevuToAttach.getTarih());
                attachedRandevuCollection.add(randevuCollectionRandevuToAttach);
            }
            hasta.setRandevuCollection(attachedRandevuCollection);
            em.persist(hasta);
            for (Randevu randevuCollectionRandevu : hasta.getRandevuCollection()) {
                Hasta oldHastaIdOfRandevuCollectionRandevu = randevuCollectionRandevu.getHastaId();
                randevuCollectionRandevu.setHastaId(hasta);
                randevuCollectionRandevu = em.merge(randevuCollectionRandevu);
                if (oldHastaIdOfRandevuCollectionRandevu != null) {
                    oldHastaIdOfRandevuCollectionRandevu.getRandevuCollection().remove(randevuCollectionRandevu);
                    oldHastaIdOfRandevuCollectionRandevu = em.merge(oldHastaIdOfRandevuCollectionRandevu);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHasta(hasta.getId()) != null) {
                throw new PreexistingEntityException("Hasta " + hasta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Hasta hasta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hasta persistentHasta = em.find(Hasta.class, hasta.getId());
            Collection<Randevu> randevuCollectionOld = persistentHasta.getRandevuCollection();
            Collection<Randevu> randevuCollectionNew = hasta.getRandevuCollection();
            List<String> illegalOrphanMessages = null;
            for (Randevu randevuCollectionOldRandevu : randevuCollectionOld) {
                if (!randevuCollectionNew.contains(randevuCollectionOldRandevu)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Randevu " + randevuCollectionOldRandevu + " since its hastaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Randevu> attachedRandevuCollectionNew = new ArrayList<Randevu>();
            for (Randevu randevuCollectionNewRandevuToAttach : randevuCollectionNew) {
                randevuCollectionNewRandevuToAttach = em.getReference(randevuCollectionNewRandevuToAttach.getClass(), randevuCollectionNewRandevuToAttach.getTarih());
                attachedRandevuCollectionNew.add(randevuCollectionNewRandevuToAttach);
            }
            randevuCollectionNew = attachedRandevuCollectionNew;
            hasta.setRandevuCollection(randevuCollectionNew);
            hasta = em.merge(hasta);
            for (Randevu randevuCollectionNewRandevu : randevuCollectionNew) {
                if (!randevuCollectionOld.contains(randevuCollectionNewRandevu)) {
                    Hasta oldHastaIdOfRandevuCollectionNewRandevu = randevuCollectionNewRandevu.getHastaId();
                    randevuCollectionNewRandevu.setHastaId(hasta);
                    randevuCollectionNewRandevu = em.merge(randevuCollectionNewRandevu);
                    if (oldHastaIdOfRandevuCollectionNewRandevu != null && !oldHastaIdOfRandevuCollectionNewRandevu.equals(hasta)) {
                        oldHastaIdOfRandevuCollectionNewRandevu.getRandevuCollection().remove(randevuCollectionNewRandevu);
                        oldHastaIdOfRandevuCollectionNewRandevu = em.merge(oldHastaIdOfRandevuCollectionNewRandevu);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = hasta.getId();
                if (findHasta(id) == null) {
                    throw new NonexistentEntityException("The hasta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hasta hasta;
            try {
                hasta = em.getReference(Hasta.class, id);
                hasta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hasta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Randevu> randevuCollectionOrphanCheck = hasta.getRandevuCollection();
            for (Randevu randevuCollectionOrphanCheckRandevu : randevuCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Hasta (" + hasta + ") cannot be destroyed since the Randevu " + randevuCollectionOrphanCheckRandevu + " in its randevuCollection field has a non-nullable hastaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(hasta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Hasta> findHastaEntities() {
        return findHastaEntities(true, -1, -1);
    }

    public List<Hasta> findHastaEntities(int maxResults, int firstResult) {
        return findHastaEntities(false, maxResults, firstResult);
    }

    private List<Hasta> findHastaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Hasta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Hasta findHasta(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Hasta.class, id);
        } finally {
            em.close();
        }
    }

    public int getHastaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Hasta> rt = cq.from(Hasta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
