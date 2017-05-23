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
public class TahlilSonucJpaController implements Serializable {

    public TahlilSonucJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TahlilSonuc tahlilSonuc) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TahlilKayit tahlilKayitTarih = tahlilSonuc.getTahlilKayitTarih();
            if (tahlilKayitTarih != null) {
                tahlilKayitTarih = em.getReference(tahlilKayitTarih.getClass(), tahlilKayitTarih.getTarih());
                tahlilSonuc.setTahlilKayitTarih(tahlilKayitTarih);
            }
            em.persist(tahlilSonuc);
            if (tahlilKayitTarih != null) {
                tahlilKayitTarih.getTahlilSonucCollection().add(tahlilSonuc);
                tahlilKayitTarih = em.merge(tahlilKayitTarih);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTahlilSonuc(tahlilSonuc.getTarih()) != null) {
                throw new PreexistingEntityException("TahlilSonuc " + tahlilSonuc + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TahlilSonuc tahlilSonuc) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TahlilSonuc persistentTahlilSonuc = em.find(TahlilSonuc.class, tahlilSonuc.getTarih());
            TahlilKayit tahlilKayitTarihOld = persistentTahlilSonuc.getTahlilKayitTarih();
            TahlilKayit tahlilKayitTarihNew = tahlilSonuc.getTahlilKayitTarih();
            if (tahlilKayitTarihNew != null) {
                tahlilKayitTarihNew = em.getReference(tahlilKayitTarihNew.getClass(), tahlilKayitTarihNew.getTarih());
                tahlilSonuc.setTahlilKayitTarih(tahlilKayitTarihNew);
            }
            tahlilSonuc = em.merge(tahlilSonuc);
            if (tahlilKayitTarihOld != null && !tahlilKayitTarihOld.equals(tahlilKayitTarihNew)) {
                tahlilKayitTarihOld.getTahlilSonucCollection().remove(tahlilSonuc);
                tahlilKayitTarihOld = em.merge(tahlilKayitTarihOld);
            }
            if (tahlilKayitTarihNew != null && !tahlilKayitTarihNew.equals(tahlilKayitTarihOld)) {
                tahlilKayitTarihNew.getTahlilSonucCollection().add(tahlilSonuc);
                tahlilKayitTarihNew = em.merge(tahlilKayitTarihNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tahlilSonuc.getTarih();
                if (findTahlilSonuc(id) == null) {
                    throw new NonexistentEntityException("The tahlilSonuc with id " + id + " no longer exists.");
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
            TahlilSonuc tahlilSonuc;
            try {
                tahlilSonuc = em.getReference(TahlilSonuc.class, id);
                tahlilSonuc.getTarih();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tahlilSonuc with id " + id + " no longer exists.", enfe);
            }
            TahlilKayit tahlilKayitTarih = tahlilSonuc.getTahlilKayitTarih();
            if (tahlilKayitTarih != null) {
                tahlilKayitTarih.getTahlilSonucCollection().remove(tahlilSonuc);
                tahlilKayitTarih = em.merge(tahlilKayitTarih);
            }
            em.remove(tahlilSonuc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TahlilSonuc> findTahlilSonucEntities() {
        return findTahlilSonucEntities(true, -1, -1);
    }

    public List<TahlilSonuc> findTahlilSonucEntities(int maxResults, int firstResult) {
        return findTahlilSonucEntities(false, maxResults, firstResult);
    }

    private List<TahlilSonuc> findTahlilSonucEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TahlilSonuc.class));
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

    public TahlilSonuc findTahlilSonuc(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TahlilSonuc.class, id);
        } finally {
            em.close();
        }
    }

    public int getTahlilSonucCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TahlilSonuc> rt = cq.from(TahlilSonuc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
