package org.example.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class ArticuloInsumo extends Articulo {

  private Double precioCompra;

  private Integer stockActual;

  private Integer stockMaximo;

  private Boolean esParaElaborar;

  @Transient
  public double getGanancia(){
    return this.getPrecioVenta() - this.getPrecioCompra();
  }
}
