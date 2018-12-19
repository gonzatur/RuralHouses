/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Zona;
import DTO.Alojamiento;
import DTO.Puebloszona;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author gonza
 */
public class PuebloszonaJpaController implements Serializable {

    public PuebloszonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Puebloszona puebloszona) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Alojamiento registroTurismoOrphanCheck = puebloszona.getRegistroTurismo();
        if (registroTurismoOrphanCheck != null) {
            Puebloszona oldPuebloszonaOfRegistroTurismo = registroTurismoOrphanCheck.getPuebloszona();
            if (oldPuebloszonaOfRegistroTurismo != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Alojamiento " + registroTurismoOrphanCheck + " already has an item of type Puebloszona whose registroTurismo column cannot be null. Please make another selection for the registroTurismo field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Zona codigoZona = puebloszona.getCodigoZona();
            if (codigoZona != null) {
                codigoZona = em.getReference(codigoZona.getClass(), codigoZona.getCodigoZona());
                puebloszona.setCodigoZona(codigoZona);
            }
            Alojamiento registroTurismo = puebloszona.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo = em.getReference(registroTurismo.getClass(), registroTurismo.getRegistroTurismo());
                puebloszona.setRegistroTurismo(registroTurismo);
            }
            em.persist(puebloszona);
            if (codigoZona != null) {
                codigoZona.getPuebloszonaList().add(puebloszona);
                codigoZona = em.merge(codigoZona);
            }
            if (registroTurismo != null) {
                registroTurismo.setPuebloszona(puebloszona);
                registroTurismo = em.merge(registroTurismo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Puebloszona puebloszona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puebloszona persistentPuebloszona = em.find(Puebloszona.class, puebloszona.getCodigoPueblosZona());
            Zona codigoZonaOld = persistentPuebloszona.getCodigoZona();
            Zona codigoZonaNew = puebloszona.getCodigoZona();
            Alojamiento registroTurismoOld = persistentPuebloszona.getRegistroTurismo();
            Alojamiento registroTurismoNew = puebloszona.getRegistroTurismo();
            List<String> illegalOrphanMessages = null;
            if (registroTurismoNew != null && !registroTurismoNew.equals(registroTurismoOld)) {
                Puebloszona oldPuebloszonaOfRegistroTurismo = registroTurismoNew.getPuebloszona();
                if (oldPuebloszonaOfRegistroTurismo != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Alojamiento " + registroTurismoNew + " already has an item of type Puebloszona whose registroTurismo column cannot be null. Please make another selection for the registroTurismo field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codigoZonaNew != null) {
                codigoZonaNew = em.getReference(codigoZonaNew.getClass(), codigoZonaNew.getCodigoZona());
                puebloszona.setCodigoZona(codigoZonaNew);
            }
            if (registroTurismoNew != null) {
                registroTurismoNew = em.getReference(registroTurismoNew.getClass(), registroTurismoNew.getRegistroTurismo());
                puebloszona.setRegistroTurismo(registroTurismoNew);
            }
            puebloszona = em.merge(puebloszona);
            if (codigoZonaOld != null && !codigoZonaOld.equals(codigoZonaNew)) {
                codigoZonaOld.getPuebloszonaList().remove(puebloszona);
                codigoZonaOld = em.merge(codigoZonaOld);
            }
            if (codigoZonaNew != null && !codigoZonaNew.equals(codigoZonaOld)) {
                codigoZonaNew.getPuebloszonaList().add(puebloszona);
                codigoZonaNew = em.merge(codigoZonaNew);
            }
            if (registroTurismoOld != null && !registroTurismoOld.equals(registroTurismoNew)) {
                registroTurismoOld.setPuebloszona(null);
                registroTurismoOld = em.merge(registroTurismoOld);
            }
            if (registroTurismoNew != null && !registroTurismoNew.equals(registroTurismoOld)) {
                registroTurismoNew.setPuebloszona(puebloszona);
                registroTurismoNew = em.merge(registroTurismoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = puebloszona.getCodigoPueblosZona();
                if (findPuebloszona(id) == null) {
                    throw new NonexistentEntityException("The puebloszona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puebloszona puebloszona;
            try {
                puebloszona = em.getReference(Puebloszona.class, id);
                puebloszona.getCodigoPueblosZona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puebloszona with id " + id + " no longer exists.", enfe);
            }
            Zona codigoZona = puebloszona.getCodigoZona();
            if (codigoZona != null) {
                codigoZona.getPuebloszonaList().remove(puebloszona);
                codigoZona = em.merge(codigoZona);
            }
            Alojamiento registroTurismo = puebloszona.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo.setPuebloszona(null);
                registroTurismo = em.merge(registroTurismo);
            }
            em.remove(puebloszona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Puebloszona> findPuebloszonaEntities() {
        return findPuebloszonaEntities(true, -1, -1);
    }

    public List<Puebloszona> findPuebloszonaEntities(int maxResults, int firstResult) {
        return findPuebloszonaEntities(false, maxResults, firstResult);
    }

    private List<Puebloszona> findPuebloszonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Puebloszona.class));
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

    public Puebloszona findPuebloszona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Puebloszona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuebloszonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Puebloszona> rt = cq.from(Puebloszona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Puebloszona> findByZona(Zona zona) {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Puebloszona.findByCodigoZona", Puebloszona.class);
            q.setParameter("codigoZona", zona);
            List <Puebloszona> listadoAlojZonas = q.getResultList();
            return listadoAlojZonas;
    }
    
}
