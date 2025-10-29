package org.example.services;

import java.util.List;
import org.example.entities.Factura;
import org.example.entities.FacturaDetalle;
import org.example.repositories.ArticuloRepository;
import org.example.repositories.FacturaRepository;
import org.example.utils.Utils;

public class FacturaService {

  public void listarFacturasUltimoMes() {
    FacturaRepository repository = FacturaRepository.getInstance();
    List<Factura> facturas = repository.getFacturasUltimoMes();

    mostrarFacturas(facturas);
  }

  public void listarFacturasUltimoMes(Long clienteId) {
    FacturaRepository repository = FacturaRepository.getInstance();
    List<Factura> facturas = repository.getFacturasUltimos3MesesPorCliente(clienteId);

    mostrarFacturas(facturas);
  }

  public void montoFacturado(Long clienteId) {
    FacturaRepository repository = FacturaRepository.getInstance();
    Double monto = repository.getMontoTotalFacturadoPorCliente(clienteId);

    System.out.println("\nMonto total facturado al cliente " + clienteId + ": $" + monto + "\n");
  }

  public void mostrarTotalDeFacturas() {
    FacturaRepository repository = FacturaRepository.getInstance();
    Long totalFacturas = repository.contarTotalFacturas();

    System.out.println("\nTotal de facturas en el sistema: " + totalFacturas + "\n");
  }

  public void mostrarFacturasPorMontonMinimo(Double monto) {
    FacturaRepository repository = FacturaRepository.getInstance();
    List<Factura> facturas = repository.getFacturasPorMontoMinimo(monto);

    System.out.println("\nFacturas con monto mayor a $" + monto + ":");
    for (Factura f : facturas) {
      System.out.println("Factura: " + f.getStrProVentaNroComprobante() +
          " - Cliente: " + f.getCliente().getRazonSocial() +
          " - Total: $" + f.getTotal());
    }
    System.out.println();
  }

  public void mostrarFacturaPorNombreArticulo(String nombreArticulo) {
    FacturaRepository repository = FacturaRepository.getInstance();
    List<Factura> facturas = repository.getFacturasPorNombreArticulo(nombreArticulo);

    System.out.println("\nFacturas que contienen articulos con '" + nombreArticulo + "':");
    for (Factura f : facturas) {
      System.out.println("Factura: " + f.getStrProVentaNroComprobante() +
          " - Cliente: " + f.getCliente().getRazonSocial() +
          " - Fecha: " + f.getFechaComprobante());
    }
    System.out.println();
  }

  public void cerrarConexion() {
    ArticuloRepository repository = ArticuloRepository.getInstance();
    repository.closeEntityManager();
  }

  private void mostrarFacturas(List<Factura> facturas) {
    System.out.println("-".repeat(30));
    for (Factura f : facturas) {
      System.out.println("N. Comp: " + f.getStrProVentaNroComprobante());
      System.out.println("Fecha: " + Utils.formatLocalDateToString(f.getFechaComprobante()));
      System.out.println("CUIT Cliente: " + Utils.formatterCuit(f.getCliente().getCuit()));
      System.out.println("Cliente: " + f.getCliente().getRazonSocial() + " ("+f.getCliente().getId() + ")");
      System.out.println("------Articulos------");
      for(FacturaDetalle detalle : f.getDetallesFactura()){
        System.out.println(detalle.getArticulo().getDenominacion() + ", " + detalle.getCantidad() + " unidades, $" + Utils.getFormatMilDecimal(detalle.getSubTotal(), 2));
      }
      System.out.println("Total: $" + Utils.getFormatMilDecimal(f.getTotal(),2));
      System.out.println("*************************");
    }
    System.out.println("-".repeat(30));
  }
}
