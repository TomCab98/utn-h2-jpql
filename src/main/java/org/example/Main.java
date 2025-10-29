package org.example;

import org.example.services.ArticuloService;
import org.example.services.ClienteService;
import org.example.services.FacturaService;

public class Main {

  public static void main(String[] args) {
    ClienteService clienteService = new ClienteService();
    FacturaService facturaService = new FacturaService();
    ArticuloService articuloService = new ArticuloService();

    clienteService.listarClientes();
    clienteService.mostrarClienteConMasFacturas();
    facturaService.listarFacturasUltimoMes();
    articuloService.listarArticulosMasVendidos();
    facturaService.listarFacturasUltimoMes(1L);
    facturaService.montoFacturado(1L);
    articuloService.mostrarArticulos(1L);
    articuloService.mostrarArticuloMasCaro(1L);
    facturaService.mostrarTotalDeFacturas();
    facturaService.mostrarFacturasPorMontonMinimo(1000.0);
    facturaService.mostrarFacturaPorNombreArticulo("Hamburguesa");
    articuloService.mostrarArticulosPorCodigoParcial("123");
    articuloService.mostrarArticulosPorEncimaDelPromedio();

    clienteService.cerrarConexion();
    facturaService.cerrarConexion();
    articuloService.cerrarConexion();
  }
}
