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
public class HastalikJpaController implements Serializable {

    public HastalikJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Hastalik hastalik) throws PreexistingEntityException, Exception {
        if (hastalik.getMuayeneCollection() == null) {
            hastalik.setMuayeneCollection(new ArrayList<Muayene>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Muayene> attachedMuayeneCollection = new ArrayList<Muayene>();
            for (Muayene muayeneCollectionMuayeneToAttach : hastalik.getMuayeneCollection()) {
                muayeneCollectionMuayeneToAttach = em.getReference(muayeneCollectionMuayeneToAttach.getClass(), muayeneCollectionMuayeneToAttach.getTarih());
                attachedMuayeneCollection.add(muayeneCollectionMuayeneToAttach);
            }
            hastalik.setMuayeneCollection(attachedMuayeneCollection);
            em.persist(hastalik);
            for (Muayene muayeneCollectionMuayene : hastalik.getMuayeneCollection()) {
                Hastalik oldHastalikIdOfMuayeneCollectionMuayene = muayeneCollectionMuayene.getHastalikId();
                muayeneCollectionMuayene.setHastalikId(hastalik);
                muayeneCollectionMuayene = em.merge(muayeneCollectionMuayene);
                if (oldHastalikIdOfMuayeneCollectionMuayene != null) {
                    oldHastalikIdOfMuayeneCollectionMuayene.getMuayeneCollection().remove(muayeneCollectionMuayene);
                    oldHastalikIdOfMuayeneCollectionMuayene = em.merge(oldHastalikIdOfMuayeneCollectionMuayene);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHastalik(hastalik.getId()) != null) {
                throw new PreexistingEntityException("Hastalik " + hastalik + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Hastalik hastalik) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hastalik persistentHastalik = em.find(Hastalik.class, hastalik.getId());
            Collection<Muayene> muayeneCollectionOld = persistentHastalik.getMuayeneCollection();
            Collection<Muayene> muayeneCollectionNew = hastalik.getMuayeneCollection();
            List<String> illegalOrphanMessages = null;
            for (Muayene muayeneCollectionOldMuayene : muayeneCollectionOld) {
                if (!muayeneCollectionNew.contains(muayeneCollectionOldMuayene)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Muayene " + muayeneCollectionOldMuayene + " since its hastalikId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Muayene> attachedMuayeneCollectionNew = new ArrayList<Muayene>();
            for (Muayene muayeneCollectionNewMuayeneToAttach : muayeneCollectionNew) {
                muayeneCollectionNewMuayeneToAttach = em.getReference(muayeneCollectionNewMuayeneToAttach.getClass(), muayeneCollectionNewMuayeneToAttach.getTarih());
                attachedMuayeneCollectionNew.add(muayeneCollectionNewMuayeneToAttach);
            }
            muayeneCollectionNew = attachedMuayeneCollectionNew;
            hastalik.setMuayeneCollection(muayeneCollectionNew);
            hastalik = em.merge(hastalik);
            for (Muayene muayeneCollectionNewMuayene : muayeneCollectionNew) {
                if (!muayeneCollectionOld.contains(muayeneCollectionNewMuayene)) {
                    Hastalik oldHastalikIdOfMuayeneCollectionNewMuayene = muayeneCollectionNewMuayene.getHastalikId();
                    muayeneCollectionNewMuayene.setHastalikId(hastalik);
                    muayeneCollectionNewMuayene = em.merge(muayeneCollectionNewMuayene);
                    if (oldHastalikIdOfMuayeneCollectionNewMuayene != null && !oldHastalikIdOfMuayeneCollectionNewMuayene.equals(hastalik)) {
                        oldHastalikIdOfMuayeneCollectionNewMuayene.getMuayeneCollection().remove(muayeneCollectionNewMuayene);
                        oldHastalikIdOfMuayeneCollectionNewMuayene = em.merge(oldHastalikIdOfMuayeneCollectionNewMuayene);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = hastalik.getId();
                if (findHastalik(id) == null) {
                    throw new NonexistentEntityException("The hastalik with id " + id + " no longer exists.");
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
            Hastalik hastalik;
            try {
                hastalik = em.getReference(Hastalik.class, id);
                hastalik.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hastalik with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Muayene> muayeneCollectionOrphanCheck = hastalik.getMuayeneCollection();
            for (Muayene muayeneCollectionOrphanCheckMuayene : muayeneCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Hastalik (" + hastalik + ") cannot be destroyed since the Muayene " + muayeneCollectionOrphanCheckMuayene + " in its muayeneCollection field has a non-nullable hastalikId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(hastalik);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Hastalik> findHastalikEntities() {
        return findHastalikEntities(true, -1, -1);
    }

    public List<Hastalik> findHastalikEntities(int maxResults, int firstResult) {
        return findHastalikEntities(false, maxResults, firstResult);
    }

    private List<Hastalik> findHastalikEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Hastalik.class));
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

    public Hastalik findHastalik(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Hastalik.class, id);
        } finally {
            em.close();
        }
    }

    public int getHastalikCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Hastalik> rt = cq.from(Hastalik.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
