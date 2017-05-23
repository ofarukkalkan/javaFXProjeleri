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
public class DoktorJpaController implements Serializable {

    public DoktorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Doktor doktor) throws PreexistingEntityException, Exception {
        if (doktor.getRandevuCollection() == null) {
            doktor.setRandevuCollection(new ArrayList<Randevu>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Klinik klinikId = doktor.getKlinikId();
            if (klinikId != null) {
                klinikId = em.getReference(klinikId.getClass(), klinikId.getId());
                doktor.setKlinikId(klinikId);
            }
            Collection<Randevu> attachedRandevuCollection = new ArrayList<Randevu>();
            for (Randevu randevuCollectionRandevuToAttach : doktor.getRandevuCollection()) {
                randevuCollectionRandevuToAttach = em.getReference(randevuCollectionRandevuToAttach.getClass(), randevuCollectionRandevuToAttach.getTarih());
                attachedRandevuCollection.add(randevuCollectionRandevuToAttach);
            }
            doktor.setRandevuCollection(attachedRandevuCollection);
            em.persist(doktor);
            if (klinikId != null) {
                klinikId.getDoktorCollection().add(doktor);
                klinikId = em.merge(klinikId);
            }
            for (Randevu randevuCollectionRandevu : doktor.getRandevuCollection()) {
                Doktor oldDoktorIdOfRandevuCollectionRandevu = randevuCollectionRandevu.getDoktorId();
                randevuCollectionRandevu.setDoktorId(doktor);
                randevuCollectionRandevu = em.merge(randevuCollectionRandevu);
                if (oldDoktorIdOfRandevuCollectionRandevu != null) {
                    oldDoktorIdOfRandevuCollectionRandevu.getRandevuCollection().remove(randevuCollectionRandevu);
                    oldDoktorIdOfRandevuCollectionRandevu = em.merge(oldDoktorIdOfRandevuCollectionRandevu);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDoktor(doktor.getId()) != null) {
                throw new PreexistingEntityException("Doktor " + doktor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Doktor doktor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Doktor persistentDoktor = em.find(Doktor.class, doktor.getId());
            Klinik klinikIdOld = persistentDoktor.getKlinikId();
            Klinik klinikIdNew = doktor.getKlinikId();
            Collection<Randevu> randevuCollectionOld = persistentDoktor.getRandevuCollection();
            Collection<Randevu> randevuCollectionNew = doktor.getRandevuCollection();
            List<String> illegalOrphanMessages = null;
            for (Randevu randevuCollectionOldRandevu : randevuCollectionOld) {
                if (!randevuCollectionNew.contains(randevuCollectionOldRandevu)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Randevu " + randevuCollectionOldRandevu + " since its doktorId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (klinikIdNew != null) {
                klinikIdNew = em.getReference(klinikIdNew.getClass(), klinikIdNew.getId());
                doktor.setKlinikId(klinikIdNew);
            }
            Collection<Randevu> attachedRandevuCollectionNew = new ArrayList<Randevu>();
            for (Randevu randevuCollectionNewRandevuToAttach : randevuCollectionNew) {
                randevuCollectionNewRandevuToAttach = em.getReference(randevuCollectionNewRandevuToAttach.getClass(), randevuCollectionNewRandevuToAttach.getTarih());
                attachedRandevuCollectionNew.add(randevuCollectionNewRandevuToAttach);
            }
            randevuCollectionNew = attachedRandevuCollectionNew;
            doktor.setRandevuCollection(randevuCollectionNew);
            doktor = em.merge(doktor);
            if (klinikIdOld != null && !klinikIdOld.equals(klinikIdNew)) {
                klinikIdOld.getDoktorCollection().remove(doktor);
                klinikIdOld = em.merge(klinikIdOld);
            }
            if (klinikIdNew != null && !klinikIdNew.equals(klinikIdOld)) {
                klinikIdNew.getDoktorCollection().add(doktor);
                klinikIdNew = em.merge(klinikIdNew);
            }
            for (Randevu randevuCollectionNewRandevu : randevuCollectionNew) {
                if (!randevuCollectionOld.contains(randevuCollectionNewRandevu)) {
                    Doktor oldDoktorIdOfRandevuCollectionNewRandevu = randevuCollectionNewRandevu.getDoktorId();
                    randevuCollectionNewRandevu.setDoktorId(doktor);
                    randevuCollectionNewRandevu = em.merge(randevuCollectionNewRandevu);
                    if (oldDoktorIdOfRandevuCollectionNewRandevu != null && !oldDoktorIdOfRandevuCollectionNewRandevu.equals(doktor)) {
                        oldDoktorIdOfRandevuCollectionNewRandevu.getRandevuCollection().remove(randevuCollectionNewRandevu);
                        oldDoktorIdOfRandevuCollectionNewRandevu = em.merge(oldDoktorIdOfRandevuCollectionNewRandevu);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = doktor.getId();
                if (findDoktor(id) == null) {
                    throw new NonexistentEntityException("The doktor with id " + id + " no longer exists.");
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
            Doktor doktor;
            try {
                doktor = em.getReference(Doktor.class, id);
                doktor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The doktor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Randevu> randevuCollectionOrphanCheck = doktor.getRandevuCollection();
            for (Randevu randevuCollectionOrphanCheckRandevu : randevuCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Doktor (" + doktor + ") cannot be destroyed since the Randevu " + randevuCollectionOrphanCheckRandevu + " in its randevuCollection field has a non-nullable doktorId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Klinik klinikId = doktor.getKlinikId();
            if (klinikId != null) {
                klinikId.getDoktorCollection().remove(doktor);
                klinikId = em.merge(klinikId);
            }
            em.remove(doktor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Doktor> findDoktorEntities() {
        return findDoktorEntities(true, -1, -1);
    }

    public List<Doktor> findDoktorEntities(int maxResults, int firstResult) {
        return findDoktorEntities(false, maxResults, firstResult);
    }

    private List<Doktor> findDoktorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Doktor.class));
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

    public Doktor findDoktor(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Doktor.class, id);
        } finally {
            em.close();
        }
    }

    public int getDoktorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Doktor> rt = cq.from(Doktor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
