package com.api.app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TB_PEDIDO")
@Data
public class PedidoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    @NotNull(message = "O valor do pedido não pode ser nulo.")
    private BigDecimal valor;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)  // Relacionamento com ClienteModel
    @JsonBackReference  // Garante que a serialização de pedidos não cause loop com clientes
    private ClienteModel cliente;
}
