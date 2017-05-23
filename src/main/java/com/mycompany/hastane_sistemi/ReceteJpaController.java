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
public class ReceteJpaController implements Serializable {

    public ReceteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recete recete) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ilac ilacId = recete.getIlacId();
            if (ilacId != null) {
                ilacId = em.getReference(ilacId.getClass(), ilacId.getId());
                recete.setIlacId(ilacId);
            }
            Randevu randevuTarih = recete.getRandevuTarih();
            if (randevuTarih != null) {
                randevuTarih = em.getReference(randevuTarih.getClass(), randevuTarih.getTarih());
                recete.setRandevuTarih(randevuTarih);
            }
            em.persist(recete);
            if (ilacId != null) {
                ilacId.getReceteCollection().add(recete);
                ilacId = em.merge(ilacId);
            }
            if (randevuTarih != null) {
                randevuTarih.getReceteCollection().add(recete);
                randevuTarih = em.merge(randevuTarih);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRecete(recete.getTarih()) != null) {
                throw new PreexistingEntityException("Recete " + recete + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recete recete) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recete persistentRecete = em.find(Recete.class, recete.getTarih());
            Ilac ilacIdOld = persistentRecete.getIlacId();
            Ilac ilacIdNew = recete.getIlacId();
            Randevu randevuTarihOld = persistentRecete.getRandevuTarih();
            Randevu randevuTarihNew = recete.getRandevuTarih();
            if (ilacIdNew != null) {
                ilacIdNew = em.getReference(ilacIdNew.getClass(), ilacIdNew.getId());
                recete.setIlacId(ilacIdNew);
            }
            if (randevuTarihNew != null) {
                randevuTarihNew = em.getReference(randevuTarihNew.getClass(), randevuTarihNew.getTarih());
                recete.setRandevuTarih(randevuTarihNew);
            }
            recete = em.merge(recete);
            if (ilacIdOld != null && !ilacIdOld.equals(ilacIdNew)) {
                ilacIdOld.getReceteCollection().remove(recete);
                ilacIdOld = em.merge(ilacIdOld);
            }
            if (ilacIdNew != null && !ilacIdNew.equals(ilacIdOld)) {
                ilacIdNew.getReceteCollection().add(recete);
                ilacIdNew = em.merge(ilacIdNew);
            }
            if (randevuTarihOld != null && !randevuTarihOld.equals(randevuTarihNew)) {
                randevuTarihOld.getReceteCollection().remove(recete);
                randevuTarihOld = em.merge(randevuTarihOld);
            }
            if (randevuTarihNew != null && !randevuTarihNew.equals(randevuTarihOld)) {
                randevuTarihNew.getReceteCollection().add(recete);
                randevuTarihNew = em.merge(randevuTarihNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = recete.getTarih();
                if (findRecete(id) == null) {
                    throw new NonexistentEntityException("The recete with id " + id + " no longer exists.");
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
            Recete recete;
            try {
                recete = em.getReference(Recete.class, id);
                recete.getTarih();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recete with id " + id + " no longer exists.", enfe);
            }
            Ilac ilacId = recete.getIlacId();
            if (ilacId != null) {
                ilacId.getReceteCollection().remove(recete);
                ilacId = em.merge(ilacId);
            }
            Randevu randevuTarih = recete.getRandevuTarih();
            if (randevuTarih != null) {
                randevuTarih.getReceteCollection().remove(recete);
                randevuTarih = em.merge(randevuTarih);
            }
            em.remove(recete);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recete> findReceteEntities() {
        return findReceteEntities(true, -1, -1);
    }

    public List<Recete> findReceteEntities(int maxResults, int firstResult) {
        return findReceteEntities(false, maxResults, firstResult);
    }

    private List<Recete> findReceteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recete.class));
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

    public Recete findRecete(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recete.class, id);
        } finally {
            em.close();
        }
    }

    public int getReceteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recete> rt = cq.from(Recete.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
