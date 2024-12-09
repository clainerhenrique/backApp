package com.api.app.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class ProdutoDto {

    private UUID id;

    @NotBlank
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String nome;

    @NotBlank
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private Double preco;

    private UUID lojaId; // Aqui está o campo lojaId

    // Getter para lojaId
    public UUID getLojaId() {
        return lojaId;
    }

    // Setter para lojaId
    public void setLojaId(UUID lojaId) {
        this.lojaId = lojaId;
    }
}
