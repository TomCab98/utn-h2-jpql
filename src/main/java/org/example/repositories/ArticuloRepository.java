package org.example.repositories;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.example.dto.DTOArticuloPromedio;
import org.example.dto.DTOArticulosVendidos;
import org.example.entities.Articulo;

public class ArticuloRepository extends GenericRepository<Articulo, Long> {

  public static ArticuloRepository articuloRepository;

  public static ArticuloRepository getInstance() {
    if (articuloRepository == null) {
      articuloRepository = new ArticuloRepository();
    }
    return articuloRepository;
  }

  private ArticuloRepository() {
    super(Articulo.class);
  }

  public List<DTOArticulosVendidos> getArticulosMasVendidos() {
    try {
      String jpql = "SELECT fd.articulo, SUM(fd.cantidad) " +
          "FROM FacturaDetalle fd " +
          "GROUP BY fd.articulo " +
          "ORDER BY SUM(fd.cantidad) DESC";

      TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
      List<Object[]> resultados = query.getResultList();

      return resultados.stream()
          .map(row -> new DTOArticulosVendidos(
              (Articulo) row[0],
              ((Number) row[1]).longValue()
          ))
          .toList();
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  public List<Articulo> getArticulosPorFactura(Long facturaId) {
    try {
      String jpql = "SELECT fd.articulo " +
          "FROM FacturaDetalle fd " +
          "WHERE fd.factura.id = :facturaId";

      TypedQuery<Articulo> query = em.createQuery(jpql, Articulo.class);
      query.setParameter("facturaId", facturaId);

      return query.getResultList();
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  public Articulo getArticuloMasCaroPorFactura(Long facturaId) {
    try {
      String jpql = "SELECT fd.articulo " +
          "FROM FacturaDetalle fd " +
          "WHERE fd.factura.id = :facturaId " +
          "ORDER BY fd.articulo.precioVenta DESC";

      TypedQuery<Articulo> query = em.createQuery(jpql, Articulo.class);
      query.setParameter("facturaId", facturaId);
      query.setMaxResults(1);

      List<Articulo> resultados = query.getResultList();
      return resultados.isEmpty() ? null : resultados.getFirst();
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  public List<Articulo> getArticulosPorCodigoParcial(String codigoParcial) {
    try {
      String jpql = "SELECT a FROM Articulo a " +
          "WHERE a.codigo LIKE :codigoParcial " +
          "ORDER BY a.codigo ASC";

      TypedQuery<Articulo> query = em.createQuery(jpql, Articulo.class);
      query.setParameter("codigoParcial", "%" + codigoParcial + "%");

      return query.getResultList();
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }

  public DTOArticuloPromedio getArticulosPorEncimaPrecioPromedio() {
    try {
      // Primero obtenemos el promedio
      String jpqlPromedio = "SELECT AVG(a.precioVenta) FROM Articulo a";
      TypedQuery<Double> queryPromedio = em.createQuery(jpqlPromedio, Double.class);
      Double precioPromedio = queryPromedio.getSingleResult();

      // Luego obtenemos los artículos por encima del promedio
      String jpql = "SELECT a FROM Articulo a " +
          "WHERE a.precioVenta > :precioPromedio " +
          "ORDER BY a.precioVenta DESC";

      TypedQuery<Articulo> query = em.createQuery(jpql, Articulo.class);
      query.setParameter("precioPromedio", precioPromedio);

      List<Articulo> articulos = query.getResultList();

      return new DTOArticuloPromedio(articulos, precioPromedio);
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }
}
