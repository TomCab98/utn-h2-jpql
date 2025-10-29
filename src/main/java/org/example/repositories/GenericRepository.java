package org.example.repositories;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase genérica CRUD para operaciones de persistencia con JPA.
 * Proporciona métodos estándar para crear, leer, actualizar y eliminar entidades.
 *
 * @param <T> el tipo de entidad a persistir
 * @param <ID> el tipo del identificador único de la entidad
 */
public abstract class GenericRepository<T, ID> {

  private static final Logger LOGGER = Logger.getLogger(GenericRepository.class.getName());

  public final EntityManagerFactory emf;
  public final Class<T> entityClass;
  public final EntityManager em;

  /**
   * Constructor que inicializa el repositorio con la fábrica de EntityManager
   * y la clase de la entidad.
   *
   * @param entityClass la clase de la entidad a gestionar
   * @throws IllegalArgumentException si emf o entityClass es null
   */
  public GenericRepository(Class<T> entityClass) {
    this.emf = Persistence.createEntityManagerFactory("db-persistence-unit");
    this.em = emf.createEntityManager();
    this.entityClass = Objects.requireNonNull(entityClass, "entityClass no puede ser null");
  }

  /**
   * Crea y persiste una nueva entidad en la base de datos.
   *
   * @param entity la entidad a crear
   * @return la entidad persistida
   * @throws IllegalArgumentException si entity es null
   * @throws PersistenceException si ocurre un error durante la persistencia
   */
  public T create(T entity) {
    Objects.requireNonNull(entity, "La entidad no puede ser null");

    try {
      em.getTransaction().begin();
      em.persist(entity);
      em.getTransaction().commit();
      LOGGER.log(Level.INFO, "Entidad creada exitosamente: {0}", entityClass.getSimpleName());
      return entity;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      LOGGER.log(Level.SEVERE, "Error al crear la entidad: " + entityClass.getSimpleName(), e);
      throw new PersistenceException("No se pudo crear la entidad", e);
    }
  }

  /**
   * Busca una entidad por su identificador único.
   *
   * @param id el identificador de la entidad
   * @return un Optional que contiene la entidad si existe, vacío en caso contrario
   * @throws IllegalArgumentException si id es null
   */
  public Optional<T> findById(ID id) {
    Objects.requireNonNull(id, "El ID no puede ser null");

    try {
      T entity = em.find(entityClass, id);
      return Optional.ofNullable(entity);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error al buscar entidad por ID: " + id, e);
      throw new PersistenceException("No se pudo buscar la entidad por ID", e);
    }
  }

  /**
   * Obtiene todas las entidades del tipo especificado.
   *
   * @return una lista de todas las entidades, o lista vacía si no hay resultados
   */
  public List<T> findAll() {
    try {
      String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
      return em.createQuery(jpql, entityClass).getResultList();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error al obtener todas las entidades de " +
          entityClass.getSimpleName(), e);
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  /**
   * Actualiza una entidad existente.
   *
   * @param entity la entidad con los datos actualizados
   * @return la entidad actualizada
   * @throws IllegalArgumentException si entity es null
   * @throws PersistenceException si ocurre un error durante la actualización
   */
  public T update(T entity) {
    Objects.requireNonNull(entity, "La entidad no puede ser null");

    try {
      em.getTransaction().begin();
      T merged = em.merge(entity);
      em.getTransaction().commit();
      LOGGER.log(Level.INFO, "Entidad actualizada exitosamente: {0}", entityClass.getSimpleName());
      return merged;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      LOGGER.log(Level.SEVERE, "Error al actualizar la entidad: " + entityClass.getSimpleName(), e);
      throw new PersistenceException("No se pudo actualizar la entidad", e);
    }
  }

  /**
   * Elimina una entidad existente por su identificador.
   *
   * @param id el identificador de la entidad a eliminar
   * @return true si la entidad fue eliminada, false si no existía
   * @throws IllegalArgumentException si id es null
   * @throws PersistenceException si ocurre un error durante la eliminación
   */
  public boolean deleteById(ID id) {
    Objects.requireNonNull(id, "El ID no puede ser null");

    try {
      em.getTransaction().begin();
      T entity = em.find(entityClass, id);
      if (entity == null) {
        em.getTransaction().commit();
        LOGGER.log(Level.WARNING, "Entidad no encontrada para ID: {0}", id);
        return false;
      }
      em.remove(entity);
      em.getTransaction().commit();
      LOGGER.log(Level.INFO, "Entidad eliminada exitosamente: {0}", entityClass.getSimpleName());
      return true;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      LOGGER.log(Level.SEVERE, "Error al eliminar la entidad con ID: " + id, e);
      throw new PersistenceException("No se pudo eliminar la entidad", e);
    }
  }

  /**
   * Elimina una entidad existente.
   *
   * @param entity la entidad a eliminar
   * @return true si la entidad fue eliminada, false en caso contrario
   * @throws IllegalArgumentException si entity es null
   * @throws PersistenceException si ocurre un error durante la eliminación
   */
  public boolean delete(T entity) {
    Objects.requireNonNull(entity, "La entidad no puede ser null");

    try {
      em.getTransaction().begin();
      T managed = em.merge(entity);
      em.remove(managed);
      em.getTransaction().commit();
      LOGGER.log(Level.INFO, "Entidad eliminada exitosamente: {0}", entityClass.getSimpleName());
      return true;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      LOGGER.log(Level.SEVERE, "Error al eliminar la entidad: " + entityClass.getSimpleName(), e);
      throw new PersistenceException("No se pudo eliminar la entidad", e);
    }
  }

  /**
   * Cuenta el número total de entidades del tipo especificado.
   *
   * @return la cantidad de entidades
   */
  public long count() {
    EntityManager em = emf.createEntityManager();
    try {
      String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
      return em.createQuery(jpql, Long.class).getSingleResult();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error al contar entidades de " +
          entityClass.getSimpleName(), e);
      throw new PersistenceException("No se pudo contar las entidades", e);
    }
  }

  /**
   * Verifica si existe una entidad con el identificador especificado.
   *
   * @param id el identificador a verificar
   * @return true si la entidad existe, false en caso contrario
   * @throws IllegalArgumentException si id es null
   */
  public boolean existsById(ID id) {
    Objects.requireNonNull(id, "El ID no puede ser null");
    return findById(id).isPresent();
  }

  public void closeEntityManager() {
    em.close();
    emf.close();
  }
}
