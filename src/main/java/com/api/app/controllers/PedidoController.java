package com.api.app.controllers;

import com.api.app.dtos.ProdutoDto;
import com.api.app.models.ProdutoModel;
import com.api.app.models.LojaModel;
import com.api.app.services.ProdutoService;
import com.api.app.services.LojaService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("produto")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final LojaService lojaService;  // Adicionado LojaService para acessar as lojas

    public ProdutoController(ProdutoService produtoService, LojaService lojaService) {
        this.produtoService = produtoService;
        this.lojaService = lojaService;  // Inicializa o LojaService
    }

    @GetMapping("/minharota")
    public String retornaTexto() {
        return "Minha requisição deu certo";
    }

    @PostMapping("/salvar")
    public ResponseEntity<Object> saveProduto(@RequestBody @Valid ProdutoDto produtoDto, BindingResult result) {
        // Verifica se há erros de validação
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors); // Retorna erro 400 com mensagens
        }

        // Busca a loja pelo ID recebido
        Optional<LojaModel> lojaOptional = lojaService.findById(produtoDto.getLojaId());
        if (!lojaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Loja não encontrada.");
        }

        // Cria o ProdutoModel e copia as propriedades do ProdutoDto
        var produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDto, produtoModel);

        // Associa a loja ao produto
        produtoModel.setLojaModel(lojaOptional.get());

        // Salva o produto
        ProdutoModel produtoSalvo = produtoService.save(produtoModel);

        // Adiciona o produto na lista de produtos da loja (opcional)
        lojaOptional.get().getProdutos().add(produtoSalvo);
        lojaService.save(lojaOptional.get());  // Atualiza a loja com o novo produto

        return ResponseEntity.ok().body(produtoSalvo); // Retorna o produto salvo
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ProdutoModel>> getAllProdutos() {
        return ResponseEntity.ok().body(produtoService.findAll());
    }

    @PostMapping("/editar")
    public ResponseEntity<Object> editarProduto(@RequestBody @Valid ProdutoDto produtoDto) {
        Optional<ProdutoModel> produtoModelOptional = produtoService.findById(produtoDto.getId());

        if (!produtoModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        var produtoModel = produtoModelOptional.get();
        BeanUtils.copyProperties(produtoDto, produtoModel);
        return ResponseEntity.ok().body(produtoService.save(produtoModel));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> apagarProduto(@PathVariable(value = "id") UUID id) {
        Optional<ProdutoModel> produtoModelOptional = produtoService.findById(id);

        if (!produtoModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        produtoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Produto removido com sucesso");
    }
}
