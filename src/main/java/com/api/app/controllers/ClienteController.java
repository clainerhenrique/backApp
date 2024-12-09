package com.api.app.controllers;


import com.api.app.models.LojaModel;

import com.api.app.models.ProdutoModel;
import com.api.app.services.LojaService;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import  com.api.app.dtos.LojaDto;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("loja")
public class LojaController {

    final private LojaService lojaService;
    private final View error;

    public LojaController(LojaService lojaService, View error) {
        this.lojaService = lojaService;
        this.error = error;
    }

    @PostMapping("/salvarloja")
    public ResponseEntity<Object> saveLoja(@RequestBody @Valid LojaDto lojaDto, BindingResult result) {
        // Verifica os erros de validação
        if (result.hasErrors()) {
            List<String> messagemdeErrors = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(messagemdeErrors);
        }

        // Converte LojaDto para LojaModel
        var lojaModel = new LojaModel();
        BeanUtils.copyProperties(lojaDto, lojaModel);

        // Converte cada ProdutoDto para ProdutoModel e associa com LojaModel
        List<ProdutoModel> produtos = lojaDto.getProdutos().stream().map(produtoDto -> {
            ProdutoModel produtoModel = new ProdutoModel();
            BeanUtils.copyProperties(produtoDto, produtoModel);
            produtoModel.setLojaModel(lojaModel);  // Associa cada produto à loja
            return produtoModel;
        }).collect(Collectors.toList());

        lojaModel.setProdutos(produtos); // Define a lista de produtos no LojaModel

        // Salva a loja e produtos associados no banco de dados
        return ResponseEntity.ok().body(lojaService.save(lojaModel));
    }




    @GetMapping("/listarloja")
    public ResponseEntity<List<LojaModel>> getAllLoja(){
        return ResponseEntity.ok().body(
                lojaService.findAll());
    }

    @PostMapping("/editarloja")
    public ResponseEntity<Object> editarLoja(@RequestBody LojaModel lojaModel) {
        // Buscar a loja pelo ID recebido
        Optional<LojaModel> lojaModelOptional = lojaService.findById(lojaModel.getId());

        // Verifica se a loja foi encontrada no banco de dados
        if (!lojaModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loja não encontrada");
        }

        // Se a loja for encontrada, vamos atualizar os dados da loja
        LojaModel lojaExistente = lojaModelOptional.get();

        // Atualiza os dados da loja, incluindo a lista de produtos
        lojaExistente.setCnpj(lojaModel.getCnpj());
        lojaExistente.setRazaosocial(lojaModel.getRazaosocial());
        lojaExistente.setProdutos(lojaModel.getProdutos());  // Atualiza a lista de produtos

        // Salva a loja com a lista atualizada de produtos
        lojaService.save(lojaExistente);

        // Retorna a loja atualizada com sucesso
        return ResponseEntity.ok().body(lojaExistente);
    }

    @GetMapping("/buscarlojaid/{id}")
    public ResponseEntity<Object> getLojaById(@PathVariable UUID id) {
        // Tenta buscar a loja no banco de dados usando o ID
        Optional<LojaModel> lojaModelOptional = lojaService.findById(id);

        // Verifica se a loja foi encontrada
        if (!lojaModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loja não encontrada");
        }

        // Se a loja for encontrada, retorna os dados da loja
        return ResponseEntity.ok().body(lojaModelOptional.get());
    }




    @PostMapping("/deleteloja/{id}")
    public ResponseEntity<Object> apagarProduto(
            @PathVariable(value = "id") UUID id) {
        Optional<LojaModel> lojaModelOptional =
                lojaService.findById(id);

        // verifica se o produto foi encontrado no banco de dados
        if (!lojaModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Produto não encontrado"
            );

        }
        //se existir vai no service e chama para remover
        lojaService.delete(id);
        //retorna resposta de removido com sucesso
        return ResponseEntity.status(HttpStatus.OK).body(
                "produto removido com sucesso"
        );
    }

}
