package org.example.dto;

import java.util.List;
import lombok.Data;
import org.example.entities.Articulo;

@Data
public class DTOArticuloPromedio {
  private final List<Articulo> articulos;
  private final Double precioPromedio;
}
