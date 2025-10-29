package org.example.dto;

import lombok.Data;
import org.example.entities.Articulo;

@Data
public class DTOArticulosVendidos {
  private Articulo articulo;
  private Long cantidadVendida;

  public DTOArticulosVendidos(Articulo articulo, Long cantidadVendida) {
    this.articulo = articulo;
    this.cantidadVendida = cantidadVendida;
  }
}
