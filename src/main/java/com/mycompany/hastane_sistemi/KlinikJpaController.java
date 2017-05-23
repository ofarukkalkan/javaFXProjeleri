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
public class KlinikJpaController implements Serializable {

    public KlinikJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Klinik klinik) throws PreexistingEntityException, Exception {
        if (klinik.getYoneticiCollection() == null) {
            klinik.setYoneticiCollection(new ArrayList<Yonetici>());
        }
        if (klinik.getDoktorCollection() == null) {
            klinik.setDoktorCollection(new ArrayList<Doktor>());
        }
        if (klinik.getSekreterCollection() == null) {
            klinik.setSekreterCollection(new ArrayList<Sekreter>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Yonetici> attachedYoneticiCollection = new ArrayList<Yonetici>();
            for (Yonetici yoneticiCollectionYoneticiToAttach : klinik.getYoneticiCollection()) {
                yoneticiCollectionYoneticiToAttach = em.getReference(yoneticiCollectionYoneticiToAttach.getClass(), yoneticiCollectionYoneticiToAttach.getId());
                attachedYoneticiCollection.add(yoneticiCollectionYoneticiToAttach);
            }
            klinik.setYoneticiCollection(attachedYoneticiCollection);
            Collection<Doktor> attachedDoktorCollection = new ArrayList<Doktor>();
            for (Doktor doktorCollectionDoktorToAttach : klinik.getDoktorCollection()) {
                doktorCollectionDoktorToAttach = em.getReference(doktorCollectionDoktorToAttach.getClass(), doktorCollectionDoktorToAttach.getId());
                attachedDoktorCollection.add(doktorCollectionDoktorToAttach);
            }
            klinik.setDoktorCollection(attachedDoktorCollection);
            Collection<Sekreter> attachedSekreterCollection = new ArrayList<Sekreter>();
            for (Sekreter sekreterCollectionSekreterToAttach : klinik.getSekreterCollection()) {
                sekreterCollectionSekreterToAttach = em.getReference(sekreterCollectionSekreterToAttach.getClass(), sekreterCollectionSekreterToAttach.getId());
                attachedSekreterCollection.add(sekreterCollectionSekreterToAttach);
            }
            klinik.setSekreterCollection(attachedSekreterCollection);
            em.persist(klinik);
            for (Yonetici yoneticiCollectionYonetici : klinik.getYoneticiCollection()) {
                Klinik oldKlinikIdOfYoneticiCollectionYonetici = yoneticiCollectionYonetici.getKlinikId();
                yoneticiCollectionYonetici.setKlinikId(klinik);
                yoneticiCollectionYonetici = em.merge(yoneticiCollectionYonetici);
                if (oldKlinikIdOfYoneticiCollectionYonetici != null) {
                    oldKlinikIdOfYoneticiCollectionYonetici.getYoneticiCollection().remove(yoneticiCollectionYonetici);
                    oldKlinikIdOfYoneticiCollectionYonetici = em.merge(oldKlinikIdOfYoneticiCollectionYonetici);
                }
            }
            for (Doktor doktorCollectionDoktor : klinik.getDoktorCollection()) {
                Klinik oldKlinikIdOfDoktorCollectionDoktor = doktorCollectionDoktor.getKlinikId();
                doktorCollectionDoktor.setKlinikId(klinik);
                doktorCollectionDoktor = em.merge(doktorCollectionDoktor);
                if (oldKlinikIdOfDoktorCollectionDoktor != null) {
                    oldKlinikIdOfDoktorCollectionDoktor.getDoktorCollection().remove(doktorCollectionDoktor);
                    oldKlinikIdOfDoktorCollectionDoktor = em.merge(oldKlinikIdOfDoktorCollectionDoktor);
                }
            }
            for (Sekreter sekreterCollectionSekreter : klinik.getSekreterCollection()) {
                Klinik oldKlinikIdOfSekreterCollectionSekreter = sekreterCollectionSekreter.getKlinikId();
                sekreterCollectionSekreter.setKlinikId(klinik);
                sekreterCollectionSekreter = em.merge(sekreterCollectionSekreter);
                if (oldKlinikIdOfSekreterCollectionSekreter != null) {
                    oldKlinikIdOfSekreterCollectionSekreter.getSekreterCollection().remove(sekreterCollectionSekreter);
                    oldKlinikIdOfSekreterCollectionSekreter = em.merge(oldKlinikIdOfSekreterCollectionSekreter);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKlinik(klinik.getId()) != null) {
                throw new PreexistingEntityException("Klinik " + klinik + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Klinik klinik) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Klinik persistentKlinik = em.find(Klinik.class, klinik.getId());
            Collection<Yonetici> yoneticiCollectionOld = persistentKlinik.getYoneticiCollection();
            Collection<Yonetici> yoneticiCollectionNew = klinik.getYoneticiCollection();
            Collection<Doktor> doktorCollectionOld = persistentKlinik.getDoktorCollection();
            Collection<Doktor> doktorCollectionNew = klinik.getDoktorCollection();
            Collection<Sekreter> sekreterCollectionOld = persistentKlinik.getSekreterCollection();
            Collection<Sekreter> sekreterCollectionNew = klinik.getSekreterCollection();
            List<String> illegalOrphanMessages = null;
            for (Yonetici yoneticiCollectionOldYonetici : yoneticiCollectionOld) {
                if (!yoneticiCollectionNew.contains(yoneticiCollectionOldYonetici)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Yonetici " + yoneticiCollectionOldYonetici + " since its klinikId field is not nullable.");
                }
            }
            for (Doktor doktorCollectionOldDoktor : doktorCollectionOld) {
                if (!doktorCollectionNew.contains(doktorCollectionOldDoktor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Doktor " + doktorCollectionOldDoktor + " since its klinikId field is not nullable.");
                }
            }
            for (Sekreter sekreterCollectionOldSekreter : sekreterCollectionOld) {
                if (!sekreterCollectionNew.contains(sekreterCollectionOldSekreter)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sekreter " + sekreterCollectionOldSekreter + " since its klinikId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Yonetici> attachedYoneticiCollectionNew = new ArrayList<Yonetici>();
            for (Yonetici yoneticiCollectionNewYoneticiToAttach : yoneticiCollectionNew) {
                yoneticiCollectionNewYoneticiToAttach = em.getReference(yoneticiCollectionNewYoneticiToAttach.getClass(), yoneticiCollectionNewYoneticiToAttach.getId());
                attachedYoneticiCollectionNew.add(yoneticiCollectionNewYoneticiToAttach);
            }
            yoneticiCollectionNew = attachedYoneticiCollectionNew;
            klinik.setYoneticiCollection(yoneticiCollectionNew);
            Collection<Doktor> attachedDoktorCollectionNew = new ArrayList<Doktor>();
            for (Doktor doktorCollectionNewDoktorToAttach : doktorCollectionNew) {
                doktorCollectionNewDoktorToAttach = em.getReference(doktorCollectionNewDoktorToAttach.getClass(), doktorCollectionNewDoktorToAttach.getId());
                attachedDoktorCollectionNew.add(doktorCollectionNewDoktorToAttach);
            }
            doktorCollectionNew = attachedDoktorCollectionNew;
            klinik.setDoktorCollection(doktorCollectionNew);
            Collection<Sekreter> attachedSekreterCollectionNew = new ArrayList<Sekreter>();
            for (Sekreter sekreterCollectionNewSekreterToAttach : sekreterCollectionNew) {
                sekreterCollectionNewSekreterToAttach = em.getReference(sekreterCollectionNewSekreterToAttach.getClass(), sekreterCollectionNewSekreterToAttach.getId());
                attachedSekreterCollectionNew.add(sekreterCollectionNewSekreterToAttach);
            }
            sekreterCollectionNew = attachedSekreterCollectionNew;
            klinik.setSekreterCollection(sekreterCollectionNew);
            klinik = em.merge(klinik);
            for (Yonetici yoneticiCollectionNewYonetici : yoneticiCollectionNew) {
                if (!yoneticiCollectionOld.contains(yoneticiCollectionNewYonetici)) {
                    Klinik oldKlinikIdOfYoneticiCollectionNewYonetici = yoneticiCollectionNewYonetici.getKlinikId();
                    yoneticiCollectionNewYonetici.setKlinikId(klinik);
                    yoneticiCollectionNewYonetici = em.merge(yoneticiCollectionNewYonetici);
                    if (oldKlinikIdOfYoneticiCollectionNewYonetici != null && !oldKlinikIdOfYoneticiCollectionNewYonetici.equals(klinik)) {
                        oldKlinikIdOfYoneticiCollectionNewYonetici.getYoneticiCollection().remove(yoneticiCollectionNewYonetici);
                        oldKlinikIdOfYoneticiCollectionNewYonetici = em.merge(oldKlinikIdOfYoneticiCollectionNewYonetici);
                    }
                }
            }
            for (Doktor doktorCollectionNewDoktor : doktorCollectionNew) {
                if (!doktorCollectionOld.contains(doktorCollectionNewDoktor)) {
                    Klinik oldKlinikIdOfDoktorCollectionNewDoktor = doktorCollectionNewDoktor.getKlinikId();
                    doktorCollectionNewDoktor.setKlinikId(klinik);
                    doktorCollectionNewDoktor = em.merge(doktorCollectionNewDoktor);
                    if (oldKlinikIdOfDoktorCollectionNewDoktor != null && !oldKlinikIdOfDoktorCollectionNewDoktor.equals(klinik)) {
                        oldKlinikIdOfDoktorCollectionNewDoktor.getDoktorCollection().remove(doktorCollectionNewDoktor);
                        oldKlinikIdOfDoktorCollectionNewDoktor = em.merge(oldKlinikIdOfDoktorCollectionNewDoktor);
                    }
                }
            }
            for (Sekreter sekreterCollectionNewSekreter : sekreterCollectionNew) {
                if (!sekreterCollectionOld.contains(sekreterCollectionNewSekreter)) {
                    Klinik oldKlinikIdOfSekreterCollectionNewSekreter = sekreterCollectionNewSekreter.getKlinikId();
                    sekreterCollectionNewSekreter.setKlinikId(klinik);
                    sekreterCollectionNewSekreter = em.merge(sekreterCollectionNewSekreter);
                    if (oldKlinikIdOfSekreterCollectionNewSekreter != null && !oldKlinikIdOfSekreterCollectionNewSekreter.equals(klinik)) {
                        oldKlinikIdOfSekreterCollectionNewSekreter.getSekreterCollection().remove(sekreterCollectionNewSekreter);
                        oldKlinikIdOfSekreterCollectionNewSekreter = em.merge(oldKlinikIdOfSekreterCollectionNewSekreter);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = klinik.getId();
                if (findKlinik(id) == null) {
                    throw new NonexistentEntityException("The klinik with id " + id + " no longer exists.");
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
            Klinik klinik;
            try {
                klinik = em.getReference(Klinik.class, id);
                klinik.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The klinik with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Yonetici> yoneticiCollectionOrphanCheck = klinik.getYoneticiCollection();
            for (Yonetici yoneticiCollectionOrphanCheckYonetici : yoneticiCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Klinik (" + klinik + ") cannot be destroyed since the Yonetici " + yoneticiCollectionOrphanCheckYonetici + " in its yoneticiCollection field has a non-nullable klinikId field.");
            }
            Collection<Doktor> doktorCollectionOrphanCheck = klinik.getDoktorCollection();
            for (Doktor doktorCollectionOrphanCheckDoktor : doktorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Klinik (" + klinik + ") cannot be destroyed since the Doktor " + doktorCollectionOrphanCheckDoktor + " in its doktorCollection field has a non-nullable klinikId field.");
            }
            Collection<Sekreter> sekreterCollectionOrphanCheck = klinik.getSekreterCollection();
            for (Sekreter sekreterCollectionOrphanCheckSekreter : sekreterCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Klinik (" + klinik + ") cannot be destroyed since the Sekreter " + sekreterCollectionOrphanCheckSekreter + " in its sekreterCollection field has a non-nullable klinikId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(klinik);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Klinik> findKlinikEntities() {
        return findKlinikEntities(true, -1, -1);
    }

    public List<Klinik> findKlinikEntities(int maxResults, int firstResult) {
        return findKlinikEntities(false, maxResults, firstResult);
    }

    private List<Klinik> findKlinikEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Klinik.class));
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

    public Klinik findKlinik(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Klinik.class, id);
        } finally {
            em.close();
        }
    }

    public int getKlinikCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Klinik> rt = cq.from(Klinik.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
