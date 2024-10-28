package com.api.app.controllers;


import com.api.app.models.LojaModel;

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
    public ResponseEntity<Object> saveLoja
            (@RequestBody @Valid com.api.app.dtos.LojaDto lojaDto, BindingResult result){
        //retorna lista de erros na validacao do dto
        if (result.hasErrors()) {

            List<String> messagemdeErrors = result.getAllErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(messagemdeErrors);
        }

        var lojaModel = new LojaModel();
        BeanUtils.copyProperties(lojaDto, lojaModel);
        return ResponseEntity.ok().body(
                lojaService.save(lojaModel));
    }


    @GetMapping("/listarloja")
    public ResponseEntity<List<LojaModel>> getAllLoja(){
        return ResponseEntity.ok().body(
                lojaService.findAll());
    }

    @PostMapping("/editarloja")
    public ResponseEntity<Object> editarLoja(
            @RequestBody LojaModel lojaModel)
    {
        //buscar um ProdutoModel no servico pelo ID recebido
        Optional<LojaModel> lojaModelOptional =
                lojaService.findById(lojaModel.getId());

        // verifica se o produto foi encontrado no banco de dados
        if(!lojaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Produto não encontrado"
            );
        }

        //retorna objeto que foi editado
        return ResponseEntity.status(HttpStatus.OK).body(
                lojaService.save(lojaModel)
        );

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
