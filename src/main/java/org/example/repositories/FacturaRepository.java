package org.example.repositories;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import org.example.entities.Factura;

public class FacturaRepository extends GenericRepository<Factura, String> {

  public static FacturaRepository facturaRepository;

  public static FacturaRepository getInstance() {
    if (facturaRepository == null) {
      facturaRepository = new FacturaRepository();
    }
    return facturaRepository;
  }

  private FacturaRepository() {
    super(Factura.class);
  }

  public List<Factura> getFacturasUltimoMes() {
    try {
      LocalDate fechaInicio = LocalDate.now().minusMonths(1);
      String jpql = "SELECT f FROM Factura f WHERE f.fechaComprobante >= :fechaInicio";
      TypedQuery<Factura> query = this.em.createQuery(jpql, Factura.class);
      query.setParameter("fechaInicio", fechaInicio);

      return query.getResultList();
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  public List<Factura> getFacturasUltimos3MesesPorCliente(Long clienteId) {
    try {
      LocalDate fechaInicio = LocalDate.now().minusMonths(3);

      String jpql = "SELECT f FROM Factura f " +
          "WHERE f.cliente.id = :clienteId " +
          "AND f.fechaComprobante >= :fechaInicio " +
          "ORDER BY f.fechaComprobante DESC";

      TypedQuery<Factura> query = em.createQuery(jpql, Factura.class);
      query.setParameter("clienteId", clienteId);
      query.setParameter("fechaInicio", fechaInicio);

      return query.getResultList();
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  public Double getMontoTotalFacturadoPorCliente(Long clienteId) {
    try {
      String jpql = "SELECT SUM(f.total) " +
          "FROM Factura f " +
          "WHERE f.cliente.id = :clienteId";

      TypedQuery<Double> query = em.createQuery(jpql, Double.class);
      query.setParameter("clienteId", clienteId);

      Double resultado = query.getSingleResult();
      return resultado != null ? resultado : 0.0;
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  public Long contarTotalFacturas() {
    try {
      String jpql = "SELECT COUNT(f) FROM Factura f";

      TypedQuery<Long> query = em.createQuery(jpql, Long.class);

      return query.getSingleResult();
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  public List<Factura> getFacturasPorMontoMinimo(Double montoMinimo) {
    try {
      String jpql = "SELECT DISTINCT f FROM Factura f " +
          "LEFT JOIN FETCH f.cliente " +
          "LEFT JOIN FETCH f.detallesFactura df " +
          "LEFT JOIN FETCH df.articulo " +
          "WHERE f.total > :montoMinimo " +
          "ORDER BY f.total DESC";

      TypedQuery<Factura> query = em.createQuery(jpql, Factura.class);
      query.setParameter("montoMinimo", montoMinimo);

      return query.getResultList();
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  public List<Factura> getFacturasPorNombreArticulo(String nombreArticulo) {
    try {
      String jpql = "SELECT DISTINCT f FROM Factura f " +
          "JOIN f.detallesFactura fd " +
          "JOIN fd.articulo a " +
          "LEFT JOIN FETCH f.cliente " +
          "WHERE a.denominacion LIKE :nombreArticulo " +
          "ORDER BY f.fechaComprobante DESC";

      TypedQuery<Factura> query = em.createQuery(jpql, Factura.class);
      query.setParameter("nombreArticulo", "%" + nombreArticulo + "%");

      return query.getResultList();
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }
}
