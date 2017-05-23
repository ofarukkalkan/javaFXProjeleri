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
public class IlacJpaController implements Serializable {

    public IlacJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ilac ilac) throws PreexistingEntityException, Exception {
        if (ilac.getReceteCollection() == null) {
            ilac.setReceteCollection(new ArrayList<Recete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Recete> attachedReceteCollection = new ArrayList<Recete>();
            for (Recete receteCollectionReceteToAttach : ilac.getReceteCollection()) {
                receteCollectionReceteToAttach = em.getReference(receteCollectionReceteToAttach.getClass(), receteCollectionReceteToAttach.getTarih());
                attachedReceteCollection.add(receteCollectionReceteToAttach);
            }
            ilac.setReceteCollection(attachedReceteCollection);
            em.persist(ilac);
            for (Recete receteCollectionRecete : ilac.getReceteCollection()) {
                Ilac oldIlacIdOfReceteCollectionRecete = receteCollectionRecete.getIlacId();
                receteCollectionRecete.setIlacId(ilac);
                receteCollectionRecete = em.merge(receteCollectionRecete);
                if (oldIlacIdOfReceteCollectionRecete != null) {
                    oldIlacIdOfReceteCollectionRecete.getReceteCollection().remove(receteCollectionRecete);
                    oldIlacIdOfReceteCollectionRecete = em.merge(oldIlacIdOfReceteCollectionRecete);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIlac(ilac.getId()) != null) {
                throw new PreexistingEntityException("Ilac " + ilac + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ilac ilac) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ilac persistentIlac = em.find(Ilac.class, ilac.getId());
            Collection<Recete> receteCollectionOld = persistentIlac.getReceteCollection();
            Collection<Recete> receteCollectionNew = ilac.getReceteCollection();
            List<String> illegalOrphanMessages = null;
            for (Recete receteCollectionOldRecete : receteCollectionOld) {
                if (!receteCollectionNew.contains(receteCollectionOldRecete)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recete " + receteCollectionOldRecete + " since its ilacId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Recete> attachedReceteCollectionNew = new ArrayList<Recete>();
            for (Recete receteCollectionNewReceteToAttach : receteCollectionNew) {
                receteCollectionNewReceteToAttach = em.getReference(receteCollectionNewReceteToAttach.getClass(), receteCollectionNewReceteToAttach.getTarih());
                attachedReceteCollectionNew.add(receteCollectionNewReceteToAttach);
            }
            receteCollectionNew = attachedReceteCollectionNew;
            ilac.setReceteCollection(receteCollectionNew);
            ilac = em.merge(ilac);
            for (Recete receteCollectionNewRecete : receteCollectionNew) {
                if (!receteCollectionOld.contains(receteCollectionNewRecete)) {
                    Ilac oldIlacIdOfReceteCollectionNewRecete = receteCollectionNewRecete.getIlacId();
                    receteCollectionNewRecete.setIlacId(ilac);
                    receteCollectionNewRecete = em.merge(receteCollectionNewRecete);
                    if (oldIlacIdOfReceteCollectionNewRecete != null && !oldIlacIdOfReceteCollectionNewRecete.equals(ilac)) {
                        oldIlacIdOfReceteCollectionNewRecete.getReceteCollection().remove(receteCollectionNewRecete);
                        oldIlacIdOfReceteCollectionNewRecete = em.merge(oldIlacIdOfReceteCollectionNewRecete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ilac.getId();
                if (findIlac(id) == null) {
                    throw new NonexistentEntityException("The ilac with id " + id + " no longer exists.");
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
            Ilac ilac;
            try {
                ilac = em.getReference(Ilac.class, id);
                ilac.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ilac with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Recete> receteCollectionOrphanCheck = ilac.getReceteCollection();
            for (Recete receteCollectionOrphanCheckRecete : receteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ilac (" + ilac + ") cannot be destroyed since the Recete " + receteCollectionOrphanCheckRecete + " in its receteCollection field has a non-nullable ilacId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ilac);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ilac> findIlacEntities() {
        return findIlacEntities(true, -1, -1);
    }

    public List<Ilac> findIlacEntities(int maxResults, int firstResult) {
        return findIlacEntities(false, maxResults, firstResult);
    }

    private List<Ilac> findIlacEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ilac.class));
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

    public Ilac findIlac(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ilac.class, id);
        } finally {
            em.close();
        }
    }

    public int getIlacCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ilac> rt = cq.from(Ilac.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
