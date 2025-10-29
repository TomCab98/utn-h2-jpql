package org.example.services;

import java.util.List;
import org.example.dto.DTOArticuloPromedio;
import org.example.dto.DTOArticulosVendidos;
import org.example.entities.Articulo;
import org.example.repositories.ArticuloRepository;
import org.example.utils.Utils;

public class ArticuloService {

  public void listarArticulosMasVendidos() {
    ArticuloRepository repository = ArticuloRepository.getInstance();
    List<DTOArticulosVendidos> articulos = repository.getArticulosMasVendidos();

    mostrarArticulosMasVendidos(articulos);
  }

  public void mostrarArticulos(Long facturaId) {
    ArticuloRepository repository = ArticuloRepository.getInstance();
    List<Articulo> articulos = repository.getArticulosPorFactura(facturaId);

    System.out.println("\nArticulos vendidos en la factura " + facturaId + ":");
    for (Articulo art : articulos) {
      System.out.println("- " + art.getDenominacion() + " ($" + art.getPrecioVenta() + ")");
    }
    System.out.println();
  }

  public void mostrarArticuloMasCaro(Long facturaId) {
    ArticuloRepository repository = ArticuloRepository.getInstance();
    Articulo articuloMasCaro = repository.getArticuloMasCaroPorFactura(facturaId);

    if (articuloMasCaro != null) {
      System.out.println("\nArticulo mas caro de la factura " + facturaId + ":");
      System.out.println("- " + articuloMasCaro.getDenominacion() +
          " - Precio: $" + articuloMasCaro.getPrecioVenta());
    } else {
      System.out.println("La factura no tiene articulos");
    }
    System.out.println();
  }

  public void mostrarArticulosPorCodigoParcial(String codigoParcial) {
    ArticuloRepository repository = ArticuloRepository.getInstance();
    List<Articulo> articulos = repository.getArticulosPorCodigoParcial(codigoParcial);

    System.out.println("\nArticulos con codigo que contiene '" + codigoParcial + "':");
    for (Articulo art : articulos) {
      System.out.println("Codigo: " + art.getCodigo() +
          " - " + art.getDenominacion() +
          " - $" + art.getPrecioVenta());
    }
    System.out.println();
  }

  public void mostrarArticulosPorEncimaDelPromedio() {
    ArticuloRepository repository = ArticuloRepository.getInstance();
    DTOArticuloPromedio articulos = repository.getArticulosPorEncimaPrecioPromedio();

    System.out.println("\nPrecio promedio del sistema: $" + articulos.getPrecioPromedio());
    System.out.println("\nArticulos por encima del promedio:");
    for (Articulo art : articulos.getArticulos()) {
      System.out.println("- " + art.getDenominacion() + " - $" + art.getPrecioVenta());
    }
    System.out.println();
  }

  public void cerrarConexion() {
    ArticuloRepository repository = ArticuloRepository.getInstance();
    repository.closeEntityManager();
  }

  private void mostrarArticulosMasVendidos(List<DTOArticulosVendidos> articulos) {
    System.out.println("-".repeat(30));
    for (DTOArticulosVendidos a : articulos) {
      System.out.println("Cod: " + a.getArticulo().getCodigo());
      System.out.println("Denominacion: " + a.getArticulo().getDenominacion());
      System.out.println("Precio: $" + Utils.getFormatMilDecimal(a.getArticulo().getPrecioVenta(), 2));
      System.out.println("Totales vendidos: " + Utils.getFormatMilDecimal(a.getCantidadVendida(), 2));
      System.out.println();
    }
    System.out.println("-".repeat(30));
  }
}
