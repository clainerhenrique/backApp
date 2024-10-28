package com.api.app.dtos;
import com.api.app.controllers.dtos.ProdutoDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
public class LojaDto {

    private UUID id;

    @NotBlank(message = "Razão Social é obrigatória")
    private String razaoSocial;

    private List<ProdutoDto> produtos =new ArrayList<>();



}