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
public class RandevuJpaController implements Serializable {

    public RandevuJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Randevu randevu) throws PreexistingEntityException, Exception {
        if (randevu.getMuayeneCollection() == null) {
            randevu.setMuayeneCollection(new ArrayList<Muayene>());
        }
        if (randevu.getTahlilKayitCollection() == null) {
            randevu.setTahlilKayitCollection(new ArrayList<TahlilKayit>());
        }
        if (randevu.getReceteCollection() == null) {
            randevu.setReceteCollection(new ArrayList<Recete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Doktor doktorId = randevu.getDoktorId();
            if (doktorId != null) {
                doktorId = em.getReference(doktorId.getClass(), doktorId.getId());
                randevu.setDoktorId(doktorId);
            }
            Hasta hastaId = randevu.getHastaId();
            if (hastaId != null) {
                hastaId = em.getReference(hastaId.getClass(), hastaId.getId());
                randevu.setHastaId(hastaId);
            }
            Collection<Muayene> attachedMuayeneCollection = new ArrayList<Muayene>();
            for (Muayene muayeneCollectionMuayeneToAttach : randevu.getMuayeneCollection()) {
                muayeneCollectionMuayeneToAttach = em.getReference(muayeneCollectionMuayeneToAttach.getClass(), muayeneCollectionMuayeneToAttach.getTarih());
                attachedMuayeneCollection.add(muayeneCollectionMuayeneToAttach);
            }
            randevu.setMuayeneCollection(attachedMuayeneCollection);
            Collection<TahlilKayit> attachedTahlilKayitCollection = new ArrayList<TahlilKayit>();
            for (TahlilKayit tahlilKayitCollectionTahlilKayitToAttach : randevu.getTahlilKayitCollection()) {
                tahlilKayitCollectionTahlilKayitToAttach = em.getReference(tahlilKayitCollectionTahlilKayitToAttach.getClass(), tahlilKayitCollectionTahlilKayitToAttach.getTarih());
                attachedTahlilKayitCollection.add(tahlilKayitCollectionTahlilKayitToAttach);
            }
            randevu.setTahlilKayitCollection(attachedTahlilKayitCollection);
            Collection<Recete> attachedReceteCollection = new ArrayList<Recete>();
            for (Recete receteCollectionReceteToAttach : randevu.getReceteCollection()) {
                receteCollectionReceteToAttach = em.getReference(receteCollectionReceteToAttach.getClass(), receteCollectionReceteToAttach.getTarih());
                attachedReceteCollection.add(receteCollectionReceteToAttach);
            }
            randevu.setReceteCollection(attachedReceteCollection);
            em.persist(randevu);
            if (doktorId != null) {
                doktorId.getRandevuCollection().add(randevu);
                doktorId = em.merge(doktorId);
            }
            if (hastaId != null) {
                hastaId.getRandevuCollection().add(randevu);
                hastaId = em.merge(hastaId);
            }
            for (Muayene muayeneCollectionMuayene : randevu.getMuayeneCollection()) {
                Randevu oldRandevuTarihOfMuayeneCollectionMuayene = muayeneCollectionMuayene.getRandevuTarih();
                muayeneCollectionMuayene.setRandevuTarih(randevu);
                muayeneCollectionMuayene = em.merge(muayeneCollectionMuayene);
                if (oldRandevuTarihOfMuayeneCollectionMuayene != null) {
                    oldRandevuTarihOfMuayeneCollectionMuayene.getMuayeneCollection().remove(muayeneCollectionMuayene);
                    oldRandevuTarihOfMuayeneCollectionMuayene = em.merge(oldRandevuTarihOfMuayeneCollectionMuayene);
                }
            }
            for (TahlilKayit tahlilKayitCollectionTahlilKayit : randevu.getTahlilKayitCollection()) {
                Randevu oldRandevuTarihOfTahlilKayitCollectionTahlilKayit = tahlilKayitCollectionTahlilKayit.getRandevuTarih();
                tahlilKayitCollectionTahlilKayit.setRandevuTarih(randevu);
                tahlilKayitCollectionTahlilKayit = em.merge(tahlilKayitCollectionTahlilKayit);
                if (oldRandevuTarihOfTahlilKayitCollectionTahlilKayit != null) {
                    oldRandevuTarihOfTahlilKayitCollectionTahlilKayit.getTahlilKayitCollection().remove(tahlilKayitCollectionTahlilKayit);
                    oldRandevuTarihOfTahlilKayitCollectionTahlilKayit = em.merge(oldRandevuTarihOfTahlilKayitCollectionTahlilKayit);
                }
            }
            for (Recete receteCollectionRecete : randevu.getReceteCollection()) {
                Randevu oldRandevuTarihOfReceteCollectionRecete = receteCollectionRecete.getRandevuTarih();
                receteCollectionRecete.setRandevuTarih(randevu);
                receteCollectionRecete = em.merge(receteCollectionRecete);
                if (oldRandevuTarihOfReceteCollectionRecete != null) {
                    oldRandevuTarihOfReceteCollectionRecete.getReceteCollection().remove(receteCollectionRecete);
                    oldRandevuTarihOfReceteCollectionRecete = em.merge(oldRandevuTarihOfReceteCollectionRecete);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRandevu(randevu.getTarih()) != null) {
                throw new PreexistingEntityException("Randevu " + randevu + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Randevu randevu) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Randevu persistentRandevu = em.find(Randevu.class, randevu.getTarih());
            Doktor doktorIdOld = persistentRandevu.getDoktorId();
            Doktor doktorIdNew = randevu.getDoktorId();
            Hasta hastaIdOld = persistentRandevu.getHastaId();
            Hasta hastaIdNew = randevu.getHastaId();
            Collection<Muayene> muayeneCollectionOld = persistentRandevu.getMuayeneCollection();
            Collection<Muayene> muayeneCollectionNew = randevu.getMuayeneCollection();
            Collection<TahlilKayit> tahlilKayitCollectionOld = persistentRandevu.getTahlilKayitCollection();
            Collection<TahlilKayit> tahlilKayitCollectionNew = randevu.getTahlilKayitCollection();
            Collection<Recete> receteCollectionOld = persistentRandevu.getReceteCollection();
            Collection<Recete> receteCollectionNew = randevu.getReceteCollection();
            List<String> illegalOrphanMessages = null;
            for (Muayene muayeneCollectionOldMuayene : muayeneCollectionOld) {
                if (!muayeneCollectionNew.contains(muayeneCollectionOldMuayene)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Muayene " + muayeneCollectionOldMuayene + " since its randevuTarih field is not nullable.");
                }
            }
            for (TahlilKayit tahlilKayitCollectionOldTahlilKayit : tahlilKayitCollectionOld) {
                if (!tahlilKayitCollectionNew.contains(tahlilKayitCollectionOldTahlilKayit)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TahlilKayit " + tahlilKayitCollectionOldTahlilKayit + " since its randevuTarih field is not nullable.");
                }
            }
            for (Recete receteCollectionOldRecete : receteCollectionOld) {
                if (!receteCollectionNew.contains(receteCollectionOldRecete)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recete " + receteCollectionOldRecete + " since its randevuTarih field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (doktorIdNew != null) {
                doktorIdNew = em.getReference(doktorIdNew.getClass(), doktorIdNew.getId());
                randevu.setDoktorId(doktorIdNew);
            }
            if (hastaIdNew != null) {
                hastaIdNew = em.getReference(hastaIdNew.getClass(), hastaIdNew.getId());
                randevu.setHastaId(hastaIdNew);
            }
            Collection<Muayene> attachedMuayeneCollectionNew = new ArrayList<Muayene>();
            for (Muayene muayeneCollectionNewMuayeneToAttach : muayeneCollectionNew) {
                muayeneCollectionNewMuayeneToAttach = em.getReference(muayeneCollectionNewMuayeneToAttach.getClass(), muayeneCollectionNewMuayeneToAttach.getTarih());
                attachedMuayeneCollectionNew.add(muayeneCollectionNewMuayeneToAttach);
            }
            muayeneCollectionNew = attachedMuayeneCollectionNew;
            randevu.setMuayeneCollection(muayeneCollectionNew);
            Collection<TahlilKayit> attachedTahlilKayitCollectionNew = new ArrayList<TahlilKayit>();
            for (TahlilKayit tahlilKayitCollectionNewTahlilKayitToAttach : tahlilKayitCollectionNew) {
                tahlilKayitCollectionNewTahlilKayitToAttach = em.getReference(tahlilKayitCollectionNewTahlilKayitToAttach.getClass(), tahlilKayitCollectionNewTahlilKayitToAttach.getTarih());
                attachedTahlilKayitCollectionNew.add(tahlilKayitCollectionNewTahlilKayitToAttach);
            }
            tahlilKayitCollectionNew = attachedTahlilKayitCollectionNew;
            randevu.setTahlilKayitCollection(tahlilKayitCollectionNew);
            Collection<Recete> attachedReceteCollectionNew = new ArrayList<Recete>();
            for (Recete receteCollectionNewReceteToAttach : receteCollectionNew) {
                receteCollectionNewReceteToAttach = em.getReference(receteCollectionNewReceteToAttach.getClass(), receteCollectionNewReceteToAttach.getTarih());
                attachedReceteCollectionNew.add(receteCollectionNewReceteToAttach);
            }
            receteCollectionNew = attachedReceteCollectionNew;
            randevu.setReceteCollection(receteCollectionNew);
            randevu = em.merge(randevu);
            if (doktorIdOld != null && !doktorIdOld.equals(doktorIdNew)) {
                doktorIdOld.getRandevuCollection().remove(randevu);
                doktorIdOld = em.merge(doktorIdOld);
            }
            if (doktorIdNew != null && !doktorIdNew.equals(doktorIdOld)) {
                doktorIdNew.getRandevuCollection().add(randevu);
                doktorIdNew = em.merge(doktorIdNew);
            }
            if (hastaIdOld != null && !hastaIdOld.equals(hastaIdNew)) {
                hastaIdOld.getRandevuCollection().remove(randevu);
                hastaIdOld = em.merge(hastaIdOld);
            }
            if (hastaIdNew != null && !hastaIdNew.equals(hastaIdOld)) {
                hastaIdNew.getRandevuCollection().add(randevu);
                hastaIdNew = em.merge(hastaIdNew);
            }
            for (Muayene muayeneCollectionNewMuayene : muayeneCollectionNew) {
                if (!muayeneCollectionOld.contains(muayeneCollectionNewMuayene)) {
                    Randevu oldRandevuTarihOfMuayeneCollectionNewMuayene = muayeneCollectionNewMuayene.getRandevuTarih();
                    muayeneCollectionNewMuayene.setRandevuTarih(randevu);
                    muayeneCollectionNewMuayene = em.merge(muayeneCollectionNewMuayene);
                    if (oldRandevuTarihOfMuayeneCollectionNewMuayene != null && !oldRandevuTarihOfMuayeneCollectionNewMuayene.equals(randevu)) {
                        oldRandevuTarihOfMuayeneCollectionNewMuayene.getMuayeneCollection().remove(muayeneCollectionNewMuayene);
                        oldRandevuTarihOfMuayeneCollectionNewMuayene = em.merge(oldRandevuTarihOfMuayeneCollectionNewMuayene);
                    }
                }
            }
            for (TahlilKayit tahlilKayitCollectionNewTahlilKayit : tahlilKayitCollectionNew) {
                if (!tahlilKayitCollectionOld.contains(tahlilKayitCollectionNewTahlilKayit)) {
                    Randevu oldRandevuTarihOfTahlilKayitCollectionNewTahlilKayit = tahlilKayitCollectionNewTahlilKayit.getRandevuTarih();
                    tahlilKayitCollectionNewTahlilKayit.setRandevuTarih(randevu);
                    tahlilKayitCollectionNewTahlilKayit = em.merge(tahlilKayitCollectionNewTahlilKayit);
                    if (oldRandevuTarihOfTahlilKayitCollectionNewTahlilKayit != null && !oldRandevuTarihOfTahlilKayitCollectionNewTahlilKayit.equals(randevu)) {
                        oldRandevuTarihOfTahlilKayitCollectionNewTahlilKayit.getTahlilKayitCollection().remove(tahlilKayitCollectionNewTahlilKayit);
                        oldRandevuTarihOfTahlilKayitCollectionNewTahlilKayit = em.merge(oldRandevuTarihOfTahlilKayitCollectionNewTahlilKayit);
                    }
                }
            }
            for (Recete receteCollectionNewRecete : receteCollectionNew) {
                if (!receteCollectionOld.contains(receteCollectionNewRecete)) {
                    Randevu oldRandevuTarihOfReceteCollectionNewRecete = receteCollectionNewRecete.getRandevuTarih();
                    receteCollectionNewRecete.setRandevuTarih(randevu);
                    receteCollectionNewRecete = em.merge(receteCollectionNewRecete);
                    if (oldRandevuTarihOfReceteCollectionNewRecete != null && !oldRandevuTarihOfReceteCollectionNewRecete.equals(randevu)) {
                        oldRandevuTarihOfReceteCollectionNewRecete.getReceteCollection().remove(receteCollectionNewRecete);
                        oldRandevuTarihOfReceteCollectionNewRecete = em.merge(oldRandevuTarihOfReceteCollectionNewRecete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = randevu.getTarih();
                if (findRandevu(id) == null) {
                    throw new NonexistentEntityException("The randevu with id " + id + " no longer exists.");
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
            Randevu randevu;
            try {
                randevu = em.getReference(Randevu.class, id);
                randevu.getTarih();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The randevu with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Muayene> muayeneCollectionOrphanCheck = randevu.getMuayeneCollection();
            for (Muayene muayeneCollectionOrphanCheckMuayene : muayeneCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Randevu (" + randevu + ") cannot be destroyed since the Muayene " + muayeneCollectionOrphanCheckMuayene + " in its muayeneCollection field has a non-nullable randevuTarih field.");
            }
            Collection<TahlilKayit> tahlilKayitCollectionOrphanCheck = randevu.getTahlilKayitCollection();
            for (TahlilKayit tahlilKayitCollectionOrphanCheckTahlilKayit : tahlilKayitCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Randevu (" + randevu + ") cannot be destroyed since the TahlilKayit " + tahlilKayitCollectionOrphanCheckTahlilKayit + " in its tahlilKayitCollection field has a non-nullable randevuTarih field.");
            }
            Collection<Recete> receteCollectionOrphanCheck = randevu.getReceteCollection();
            for (Recete receteCollectionOrphanCheckRecete : receteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Randevu (" + randevu + ") cannot be destroyed since the Recete " + receteCollectionOrphanCheckRecete + " in its receteCollection field has a non-nullable randevuTarih field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Doktor doktorId = randevu.getDoktorId();
            if (doktorId != null) {
                doktorId.getRandevuCollection().remove(randevu);
                doktorId = em.merge(doktorId);
            }
            Hasta hastaId = randevu.getHastaId();
            if (hastaId != null) {
                hastaId.getRandevuCollection().remove(randevu);
                hastaId = em.merge(hastaId);
            }
            em.remove(randevu);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Randevu> findRandevuEntities() {
        return findRandevuEntities(true, -1, -1);
    }

    public List<Randevu> findRandevuEntities(int maxResults, int firstResult) {
        return findRandevuEntities(false, maxResults, firstResult);
    }

    private List<Randevu> findRandevuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Randevu.class));
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

    public Randevu findRandevu(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Randevu.class, id);
        } finally {
            em.close();
        }
    }

    public int getRandevuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Randevu> rt = cq.from(Randevu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
