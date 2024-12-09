package com.api.app.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class LojaDto {

    private UUID id;

    @NotBlank(message = "Razão Social é obrigatória")
    private String razaosocial;

    @NotBlank(message = "CNPJ é obrigatório")
    private String cnpj;

    // Lista de produtos
    private List<ProdutoDto> produtos = new ArrayList<>();
}
