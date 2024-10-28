package com.api.app.controllers.dtos;

import com.api.app.models.ProdutoModel;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProdutoDto
{
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 50) //delimita o tamanho da string
    private String nome;

    @NotBlank
    private String descricao;

    @NotNull(message = "Preco obrigatorio")
    @Positive(message = "Valor positivo apenas")
    private Double preco;


    @NotNull(message = "Id da loja obrigatorio")
    private UUID lojaId;

    private List<ProdutoModel> produtos;

}
