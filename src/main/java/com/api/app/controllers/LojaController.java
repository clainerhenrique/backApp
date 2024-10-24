package com.api.app.controllers;


import com.api.app.models.LojaModel;
import com.api.app.models.ProdutoModel;
import com.api.app.services.LojaService;
import com.api.app.services.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("loja")
public class LojaController {

    final private LojaService lojaService;

    public LojaController(LojaService lojaService) {
        this.lojaService = lojaService;
    }

    @PostMapping("/salvarloja")
    public ResponseEntity<Object> saveLoja
            (@RequestBody LojaModel lojaModel){
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
