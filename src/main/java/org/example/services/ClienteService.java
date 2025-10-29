package org.example.services;

import java.util.List;
import org.example.entities.Cliente;
import org.example.repositories.ArticuloRepository;
import org.example.repositories.ClienteRepository;

public class ClienteService {

  public void listarClientes() {
    ClienteRepository repository = ClienteRepository.getInstance();
    List<Cliente> clientes = repository.findAll();

    mostrarClientes(clientes);
  }

  public void mostrarClienteConMasFacturas() {
    ClienteRepository repository = ClienteRepository.getInstance();
    List<Cliente> clientes = repository.getClienteConMasFacturas();

    mostrarClientes(clientes);
  }

  public void cerrarConexion() {
    ArticuloRepository repository = ArticuloRepository.getInstance();
    repository.closeEntityManager();
  }

  private void mostrarClientes(List<Cliente> cliente) {
    System.out.println("-".repeat(30));
    for (Cliente c : cliente) {
      System.out.println("Cuit: "  + c.getCuit());
      System.out.println("Razon social: "  + c.getRazonSocial());
    }
    System.out.println("-".repeat(30));
  }
}
