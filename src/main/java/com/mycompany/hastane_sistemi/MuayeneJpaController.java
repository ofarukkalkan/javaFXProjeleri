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
public class MuayeneJpaController implements Serializable {

    public MuayeneJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Muayene muayene) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hastalik hastalikId = muayene.getHastalikId();
            if (hastalikId != null) {
                hastalikId = em.getReference(hastalikId.getClass(), hastalikId.getId());
                muayene.setHastalikId(hastalikId);
            }
            Randevu randevuTarih = muayene.getRandevuTarih();
            if (randevuTarih != null) {
                randevuTarih = em.getReference(randevuTarih.getClass(), randevuTarih.getTarih());
                muayene.setRandevuTarih(randevuTarih);
            }
            em.persist(muayene);
            if (hastalikId != null) {
                hastalikId.getMuayeneCollection().add(muayene);
                hastalikId = em.merge(hastalikId);
            }
            if (randevuTarih != null) {
                randevuTarih.getMuayeneCollection().add(muayene);
                randevuTarih = em.merge(randevuTarih);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMuayene(muayene.getTarih()) != null) {
                throw new PreexistingEntityException("Muayene " + muayene + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Muayene muayene) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Muayene persistentMuayene = em.find(Muayene.class, muayene.getTarih());
            Hastalik hastalikIdOld = persistentMuayene.getHastalikId();
            Hastalik hastalikIdNew = muayene.getHastalikId();
            Randevu randevuTarihOld = persistentMuayene.getRandevuTarih();
            Randevu randevuTarihNew = muayene.getRandevuTarih();
            if (hastalikIdNew != null) {
                hastalikIdNew = em.getReference(hastalikIdNew.getClass(), hastalikIdNew.getId());
                muayene.setHastalikId(hastalikIdNew);
            }
            if (randevuTarihNew != null) {
                randevuTarihNew = em.getReference(randevuTarihNew.getClass(), randevuTarihNew.getTarih());
                muayene.setRandevuTarih(randevuTarihNew);
            }
            muayene = em.merge(muayene);
            if (hastalikIdOld != null && !hastalikIdOld.equals(hastalikIdNew)) {
                hastalikIdOld.getMuayeneCollection().remove(muayene);
                hastalikIdOld = em.merge(hastalikIdOld);
            }
            if (hastalikIdNew != null && !hastalikIdNew.equals(hastalikIdOld)) {
                hastalikIdNew.getMuayeneCollection().add(muayene);
                hastalikIdNew = em.merge(hastalikIdNew);
            }
            if (randevuTarihOld != null && !randevuTarihOld.equals(randevuTarihNew)) {
                randevuTarihOld.getMuayeneCollection().remove(muayene);
                randevuTarihOld = em.merge(randevuTarihOld);
            }
            if (randevuTarihNew != null && !randevuTarihNew.equals(randevuTarihOld)) {
                randevuTarihNew.getMuayeneCollection().add(muayene);
                randevuTarihNew = em.merge(randevuTarihNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = muayene.getTarih();
                if (findMuayene(id) == null) {
                    throw new NonexistentEntityException("The muayene with id " + id + " no longer exists.");
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
            Muayene muayene;
            try {
                muayene = em.getReference(Muayene.class, id);
                muayene.getTarih();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The muayene with id " + id + " no longer exists.", enfe);
            }
            Hastalik hastalikId = muayene.getHastalikId();
            if (hastalikId != null) {
                hastalikId.getMuayeneCollection().remove(muayene);
                hastalikId = em.merge(hastalikId);
            }
            Randevu randevuTarih = muayene.getRandevuTarih();
            if (randevuTarih != null) {
                randevuTarih.getMuayeneCollection().remove(muayene);
                randevuTarih = em.merge(randevuTarih);
            }
            em.remove(muayene);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Muayene> findMuayeneEntities() {
        return findMuayeneEntities(true, -1, -1);
    }

    public List<Muayene> findMuayeneEntities(int maxResults, int firstResult) {
        return findMuayeneEntities(false, maxResults, firstResult);
    }

    private List<Muayene> findMuayeneEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Muayene.class));
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

    public Muayene findMuayene(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Muayene.class, id);
        } finally {
            em.close();
        }
    }

    public int getMuayeneCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Muayene> rt = cq.from(Muayene.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
