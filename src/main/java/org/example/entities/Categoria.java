package org.example.entities;

import lombok.*;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Categoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String denominacion;

  private boolean esInsumo;

  @OneToMany
  @JoinColumn(name = "categoria_fk")
  @Builder.Default
  private Set<Articulo> articulos = new HashSet<>();



}
