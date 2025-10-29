package org.example.repositories;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.example.entities.Cliente;

public class ClienteRepository extends GenericRepository<Cliente, String> {

  public static ClienteRepository clienteRepository;

  public static ClienteRepository getInstance() {
    if (clienteRepository == null) {
      clienteRepository = new ClienteRepository();
    }
    return clienteRepository;
  }

  private ClienteRepository() {
    super(Cliente.class);
  }

  public List<Cliente> getClienteConMasFacturas() {
    try {
      String jpql = "SELECT f.cliente " +
          "FROM Factura f " +
          "GROUP BY f.cliente " +
          "ORDER BY COUNT(f) DESC";

      TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
      query.setMaxResults(1);

      List<Cliente> resultados = query.getResultList();
      return resultados.isEmpty() ? null : resultados;
    } catch (Exception e) {
      throw new PersistenceException("No se pudieron obtener las entidades", e);
    }
  }
}
