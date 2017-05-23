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
public class TahlilTurJpaController implements Serializable {

    public TahlilTurJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TahlilTur tahlilTur) throws PreexistingEntityException, Exception {
        if (tahlilTur.getTahlilKayitCollection() == null) {
            tahlilTur.setTahlilKayitCollection(new ArrayList<TahlilKayit>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TahlilKayit> attachedTahlilKayitCollection = new ArrayList<TahlilKayit>();
            for (TahlilKayit tahlilKayitCollectionTahlilKayitToAttach : tahlilTur.getTahlilKayitCollection()) {
                tahlilKayitCollectionTahlilKayitToAttach = em.getReference(tahlilKayitCollectionTahlilKayitToAttach.getClass(), tahlilKayitCollectionTahlilKayitToAttach.getTarih());
                attachedTahlilKayitCollection.add(tahlilKayitCollectionTahlilKayitToAttach);
            }
            tahlilTur.setTahlilKayitCollection(attachedTahlilKayitCollection);
            em.persist(tahlilTur);
            for (TahlilKayit tahlilKayitCollectionTahlilKayit : tahlilTur.getTahlilKayitCollection()) {
                TahlilTur oldTahlilIdOfTahlilKayitCollectionTahlilKayit = tahlilKayitCollectionTahlilKayit.getTahlilId();
                tahlilKayitCollectionTahlilKayit.setTahlilId(tahlilTur);
                tahlilKayitCollectionTahlilKayit = em.merge(tahlilKayitCollectionTahlilKayit);
                if (oldTahlilIdOfTahlilKayitCollectionTahlilKayit != null) {
                    oldTahlilIdOfTahlilKayitCollectionTahlilKayit.getTahlilKayitCollection().remove(tahlilKayitCollectionTahlilKayit);
                    oldTahlilIdOfTahlilKayitCollectionTahlilKayit = em.merge(oldTahlilIdOfTahlilKayitCollectionTahlilKayit);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTahlilTur(tahlilTur.getId()) != null) {
                throw new PreexistingEntityException("TahlilTur " + tahlilTur + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TahlilTur tahlilTur) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TahlilTur persistentTahlilTur = em.find(TahlilTur.class, tahlilTur.getId());
            Collection<TahlilKayit> tahlilKayitCollectionOld = persistentTahlilTur.getTahlilKayitCollection();
            Collection<TahlilKayit> tahlilKayitCollectionNew = tahlilTur.getTahlilKayitCollection();
            List<String> illegalOrphanMessages = null;
            for (TahlilKayit tahlilKayitCollectionOldTahlilKayit : tahlilKayitCollectionOld) {
                if (!tahlilKayitCollectionNew.contains(tahlilKayitCollectionOldTahlilKayit)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TahlilKayit " + tahlilKayitCollectionOldTahlilKayit + " since its tahlilId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TahlilKayit> attachedTahlilKayitCollectionNew = new ArrayList<TahlilKayit>();
            for (TahlilKayit tahlilKayitCollectionNewTahlilKayitToAttach : tahlilKayitCollectionNew) {
                tahlilKayitCollectionNewTahlilKayitToAttach = em.getReference(tahlilKayitCollectionNewTahlilKayitToAttach.getClass(), tahlilKayitCollectionNewTahlilKayitToAttach.getTarih());
                attachedTahlilKayitCollectionNew.add(tahlilKayitCollectionNewTahlilKayitToAttach);
            }
            tahlilKayitCollectionNew = attachedTahlilKayitCollectionNew;
            tahlilTur.setTahlilKayitCollection(tahlilKayitCollectionNew);
            tahlilTur = em.merge(tahlilTur);
            for (TahlilKayit tahlilKayitCollectionNewTahlilKayit : tahlilKayitCollectionNew) {
                if (!tahlilKayitCollectionOld.contains(tahlilKayitCollectionNewTahlilKayit)) {
                    TahlilTur oldTahlilIdOfTahlilKayitCollectionNewTahlilKayit = tahlilKayitCollectionNewTahlilKayit.getTahlilId();
                    tahlilKayitCollectionNewTahlilKayit.setTahlilId(tahlilTur);
                    tahlilKayitCollectionNewTahlilKayit = em.merge(tahlilKayitCollectionNewTahlilKayit);
                    if (oldTahlilIdOfTahlilKayitCollectionNewTahlilKayit != null && !oldTahlilIdOfTahlilKayitCollectionNewTahlilKayit.equals(tahlilTur)) {
                        oldTahlilIdOfTahlilKayitCollectionNewTahlilKayit.getTahlilKayitCollection().remove(tahlilKayitCollectionNewTahlilKayit);
                        oldTahlilIdOfTahlilKayitCollectionNewTahlilKayit = em.merge(oldTahlilIdOfTahlilKayitCollectionNewTahlilKayit);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tahlilTur.getId();
                if (findTahlilTur(id) == null) {
                    throw new NonexistentEntityException("The tahlilTur with id " + id + " no longer exists.");
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
            TahlilTur tahlilTur;
            try {
                tahlilTur = em.getReference(TahlilTur.class, id);
                tahlilTur.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tahlilTur with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TahlilKayit> tahlilKayitCollectionOrphanCheck = tahlilTur.getTahlilKayitCollection();
            for (TahlilKayit tahlilKayitCollectionOrphanCheckTahlilKayit : tahlilKayitCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TahlilTur (" + tahlilTur + ") cannot be destroyed since the TahlilKayit " + tahlilKayitCollectionOrphanCheckTahlilKayit + " in its tahlilKayitCollection field has a non-nullable tahlilId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tahlilTur);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TahlilTur> findTahlilTurEntities() {
        return findTahlilTurEntities(true, -1, -1);
    }

    public List<TahlilTur> findTahlilTurEntities(int maxResults, int firstResult) {
        return findTahlilTurEntities(false, maxResults, firstResult);
    }

    private List<TahlilTur> findTahlilTurEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TahlilTur.class));
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

    public TahlilTur findTahlilTur(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TahlilTur.class, id);
        } finally {
            em.close();
        }
    }

    public int getTahlilTurCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TahlilTur> rt = cq.from(TahlilTur.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
