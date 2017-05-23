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
public class TahlilKayitJpaController implements Serializable {

    public TahlilKayitJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TahlilKayit tahlilKayit) throws PreexistingEntityException, Exception {
        if (tahlilKayit.getTahlilSonucCollection() == null) {
            tahlilKayit.setTahlilSonucCollection(new ArrayList<TahlilSonuc>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Randevu randevuTarih = tahlilKayit.getRandevuTarih();
            if (randevuTarih != null) {
                randevuTarih = em.getReference(randevuTarih.getClass(), randevuTarih.getTarih());
                tahlilKayit.setRandevuTarih(randevuTarih);
            }
            TahlilTur tahlilId = tahlilKayit.getTahlilId();
            if (tahlilId != null) {
                tahlilId = em.getReference(tahlilId.getClass(), tahlilId.getId());
                tahlilKayit.setTahlilId(tahlilId);
            }
            Collection<TahlilSonuc> attachedTahlilSonucCollection = new ArrayList<TahlilSonuc>();
            for (TahlilSonuc tahlilSonucCollectionTahlilSonucToAttach : tahlilKayit.getTahlilSonucCollection()) {
                tahlilSonucCollectionTahlilSonucToAttach = em.getReference(tahlilSonucCollectionTahlilSonucToAttach.getClass(), tahlilSonucCollectionTahlilSonucToAttach.getTarih());
                attachedTahlilSonucCollection.add(tahlilSonucCollectionTahlilSonucToAttach);
            }
            tahlilKayit.setTahlilSonucCollection(attachedTahlilSonucCollection);
            em.persist(tahlilKayit);
            if (randevuTarih != null) {
                randevuTarih.getTahlilKayitCollection().add(tahlilKayit);
                randevuTarih = em.merge(randevuTarih);
            }
            if (tahlilId != null) {
                tahlilId.getTahlilKayitCollection().add(tahlilKayit);
                tahlilId = em.merge(tahlilId);
            }
            for (TahlilSonuc tahlilSonucCollectionTahlilSonuc : tahlilKayit.getTahlilSonucCollection()) {
                TahlilKayit oldTahlilKayitTarihOfTahlilSonucCollectionTahlilSonuc = tahlilSonucCollectionTahlilSonuc.getTahlilKayitTarih();
                tahlilSonucCollectionTahlilSonuc.setTahlilKayitTarih(tahlilKayit);
                tahlilSonucCollectionTahlilSonuc = em.merge(tahlilSonucCollectionTahlilSonuc);
                if (oldTahlilKayitTarihOfTahlilSonucCollectionTahlilSonuc != null) {
                    oldTahlilKayitTarihOfTahlilSonucCollectionTahlilSonuc.getTahlilSonucCollection().remove(tahlilSonucCollectionTahlilSonuc);
                    oldTahlilKayitTarihOfTahlilSonucCollectionTahlilSonuc = em.merge(oldTahlilKayitTarihOfTahlilSonucCollectionTahlilSonuc);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTahlilKayit(tahlilKayit.getTarih()) != null) {
                throw new PreexistingEntityException("TahlilKayit " + tahlilKayit + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TahlilKayit tahlilKayit) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TahlilKayit persistentTahlilKayit = em.find(TahlilKayit.class, tahlilKayit.getTarih());
            Randevu randevuTarihOld = persistentTahlilKayit.getRandevuTarih();
            Randevu randevuTarihNew = tahlilKayit.getRandevuTarih();
            TahlilTur tahlilIdOld = persistentTahlilKayit.getTahlilId();
            TahlilTur tahlilIdNew = tahlilKayit.getTahlilId();
            Collection<TahlilSonuc> tahlilSonucCollectionOld = persistentTahlilKayit.getTahlilSonucCollection();
            Collection<TahlilSonuc> tahlilSonucCollectionNew = tahlilKayit.getTahlilSonucCollection();
            List<String> illegalOrphanMessages = null;
            for (TahlilSonuc tahlilSonucCollectionOldTahlilSonuc : tahlilSonucCollectionOld) {
                if (!tahlilSonucCollectionNew.contains(tahlilSonucCollectionOldTahlilSonuc)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TahlilSonuc " + tahlilSonucCollectionOldTahlilSonuc + " since its tahlilKayitTarih field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (randevuTarihNew != null) {
                randevuTarihNew = em.getReference(randevuTarihNew.getClass(), randevuTarihNew.getTarih());
                tahlilKayit.setRandevuTarih(randevuTarihNew);
            }
            if (tahlilIdNew != null) {
                tahlilIdNew = em.getReference(tahlilIdNew.getClass(), tahlilIdNew.getId());
                tahlilKayit.setTahlilId(tahlilIdNew);
            }
            Collection<TahlilSonuc> attachedTahlilSonucCollectionNew = new ArrayList<TahlilSonuc>();
            for (TahlilSonuc tahlilSonucCollectionNewTahlilSonucToAttach : tahlilSonucCollectionNew) {
                tahlilSonucCollectionNewTahlilSonucToAttach = em.getReference(tahlilSonucCollectionNewTahlilSonucToAttach.getClass(), tahlilSonucCollectionNewTahlilSonucToAttach.getTarih());
                attachedTahlilSonucCollectionNew.add(tahlilSonucCollectionNewTahlilSonucToAttach);
            }
            tahlilSonucCollectionNew = attachedTahlilSonucCollectionNew;
            tahlilKayit.setTahlilSonucCollection(tahlilSonucCollectionNew);
            tahlilKayit = em.merge(tahlilKayit);
            if (randevuTarihOld != null && !randevuTarihOld.equals(randevuTarihNew)) {
                randevuTarihOld.getTahlilKayitCollection().remove(tahlilKayit);
                randevuTarihOld = em.merge(randevuTarihOld);
            }
            if (randevuTarihNew != null && !randevuTarihNew.equals(randevuTarihOld)) {
                randevuTarihNew.getTahlilKayitCollection().add(tahlilKayit);
                randevuTarihNew = em.merge(randevuTarihNew);
            }
            if (tahlilIdOld != null && !tahlilIdOld.equals(tahlilIdNew)) {
                tahlilIdOld.getTahlilKayitCollection().remove(tahlilKayit);
                tahlilIdOld = em.merge(tahlilIdOld);
            }
            if (tahlilIdNew != null && !tahlilIdNew.equals(tahlilIdOld)) {
                tahlilIdNew.getTahlilKayitCollection().add(tahlilKayit);
                tahlilIdNew = em.merge(tahlilIdNew);
            }
            for (TahlilSonuc tahlilSonucCollectionNewTahlilSonuc : tahlilSonucCollectionNew) {
                if (!tahlilSonucCollectionOld.contains(tahlilSonucCollectionNewTahlilSonuc)) {
                    TahlilKayit oldTahlilKayitTarihOfTahlilSonucCollectionNewTahlilSonuc = tahlilSonucCollectionNewTahlilSonuc.getTahlilKayitTarih();
                    tahlilSonucCollectionNewTahlilSonuc.setTahlilKayitTarih(tahlilKayit);
                    tahlilSonucCollectionNewTahlilSonuc = em.merge(tahlilSonucCollectionNewTahlilSonuc);
                    if (oldTahlilKayitTarihOfTahlilSonucCollectionNewTahlilSonuc != null && !oldTahlilKayitTarihOfTahlilSonucCollectionNewTahlilSonuc.equals(tahlilKayit)) {
                        oldTahlilKayitTarihOfTahlilSonucCollectionNewTahlilSonuc.getTahlilSonucCollection().remove(tahlilSonucCollectionNewTahlilSonuc);
                        oldTahlilKayitTarihOfTahlilSonucCollectionNewTahlilSonuc = em.merge(oldTahlilKayitTarihOfTahlilSonucCollectionNewTahlilSonuc);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tahlilKayit.getTarih();
                if (findTahlilKayit(id) == null) {
                    throw new NonexistentEntityException("The tahlilKayit with id " + id + " no longer exists.");
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
            TahlilKayit tahlilKayit;
            try {
                tahlilKayit = em.getReference(TahlilKayit.class, id);
                tahlilKayit.getTarih();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tahlilKayit with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TahlilSonuc> tahlilSonucCollectionOrphanCheck = tahlilKayit.getTahlilSonucCollection();
            for (TahlilSonuc tahlilSonucCollectionOrphanCheckTahlilSonuc : tahlilSonucCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TahlilKayit (" + tahlilKayit + ") cannot be destroyed since the TahlilSonuc " + tahlilSonucCollectionOrphanCheckTahlilSonuc + " in its tahlilSonucCollection field has a non-nullable tahlilKayitTarih field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Randevu randevuTarih = tahlilKayit.getRandevuTarih();
            if (randevuTarih != null) {
                randevuTarih.getTahlilKayitCollection().remove(tahlilKayit);
                randevuTarih = em.merge(randevuTarih);
            }
            TahlilTur tahlilId = tahlilKayit.getTahlilId();
            if (tahlilId != null) {
                tahlilId.getTahlilKayitCollection().remove(tahlilKayit);
                tahlilId = em.merge(tahlilId);
            }
            em.remove(tahlilKayit);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TahlilKayit> findTahlilKayitEntities() {
        return findTahlilKayitEntities(true, -1, -1);
    }

    public List<TahlilKayit> findTahlilKayitEntities(int maxResults, int firstResult) {
        return findTahlilKayitEntities(false, maxResults, firstResult);
    }

    private List<TahlilKayit> findTahlilKayitEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TahlilKayit.class));
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

    public TahlilKayit findTahlilKayit(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TahlilKayit.class, id);
        } finally {
            em.close();
        }
    }

    public int getTahlilKayitCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TahlilKayit> rt = cq.from(TahlilKayit.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
