/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hastane_sistemi;

import com.mycompany.hastane_sistemi.exceptions.NonexistentEntityException;
import com.mycompany.hastane_sistemi.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author omerfaruk
 */
public class YoneticiJpaController implements Serializable {

    public YoneticiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Yonetici yonetici) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Klinik klinikId = yonetici.getKlinikId();
            if (klinikId != null) {
                klinikId = em.getReference(klinikId.getClass(), klinikId.getId());
                yonetici.setKlinikId(klinikId);
            }
            em.persist(yonetici);
            if (klinikId != null) {
                klinikId.getYoneticiCollection().add(yonetici);
                klinikId = em.merge(klinikId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findYonetici(yonetici.getId()) != null) {
                throw new PreexistingEntityException("Yonetici " + yonetici + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Yonetici yonetici) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Yonetici persistentYonetici = em.find(Yonetici.class, yonetici.getId());
            Klinik klinikIdOld = persistentYonetici.getKlinikId();
            Klinik klinikIdNew = yonetici.getKlinikId();
            if (klinikIdNew != null) {
                klinikIdNew = em.getReference(klinikIdNew.getClass(), klinikIdNew.getId());
                yonetici.setKlinikId(klinikIdNew);
            }
            yonetici = em.merge(yonetici);
            if (klinikIdOld != null && !klinikIdOld.equals(klinikIdNew)) {
                klinikIdOld.getYoneticiCollection().remove(yonetici);
                klinikIdOld = em.merge(klinikIdOld);
            }
            if (klinikIdNew != null && !klinikIdNew.equals(klinikIdOld)) {
                klinikIdNew.getYoneticiCollection().add(yonetici);
                klinikIdNew = em.merge(klinikIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = yonetici.getId();
                if (findYonetici(id) == null) {
                    throw new NonexistentEntityException("The yonetici with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Yonetici yonetici;
            try {
                yonetici = em.getReference(Yonetici.class, id);
                yonetici.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The yonetici with id " + id + " no longer exists.", enfe);
            }
            Klinik klinikId = yonetici.getKlinikId();
            if (klinikId != null) {
                klinikId.getYoneticiCollection().remove(yonetici);
                klinikId = em.merge(klinikId);
            }
            em.remove(yonetici);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Yonetici> findYoneticiEntities() {
        return findYoneticiEntities(true, -1, -1);
    }

    public List<Yonetici> findYoneticiEntities(int maxResults, int firstResult) {
        return findYoneticiEntities(false, maxResults, firstResult);
    }

    private List<Yonetici> findYoneticiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Yonetici.class));
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

    public Yonetici findYonetici(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Yonetici.class, id);
        } finally {
            em.close();
        }
    }

    public int getYoneticiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Yonetici> rt = cq.from(Yonetici.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
