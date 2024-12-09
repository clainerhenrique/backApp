package com.api.app.controllers;

import com.api.app.dtos.PedidoDto;
import com.api.app.models.PedidoModel;
import com.api.app.models.ClienteModel;
import com.api.app.services.PedidoService;
import com.api.app.services.ClienteService;
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
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    public PedidoController(PedidoService pedidoService, ClienteService clienteService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }

    @GetMapping("/status")
    public String retornaTexto() {
        return "Requisição realizada com sucesso!";
    }

    @PostMapping("/salvar")
    public ResponseEntity<Object> salvarPedido(@RequestBody @Valid PedidoDto pedidoDto, BindingResult result) {
        // Verifica se há erros de validação
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        // Busca o cliente pelo ID recebido
        Optional<ClienteModel> clienteOptional = clienteService.findById(pedidoDto.getClienteId());
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente não encontrado.");
        }

        // Cria o PedidoModel e copia as propriedades do PedidoDto
        var pedidoModel = new PedidoModel();
        BeanUtils.copyProperties(pedidoDto, pedidoModel);

        // Associa o cliente ao pedido
        pedidoModel.setCliente(clienteOptional.get());

        // Salva o pedido
        PedidoModel pedidoSalvo = pedidoService.save(pedidoModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PedidoModel>> listarPedidos() {
        return ResponseEntity.ok().body(pedidoService.findAll());
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Object> editarPedido(@PathVariable UUID id, @RequestBody @Valid PedidoDto pedidoDto) {
        Optional<PedidoModel> pedidoModelOptional = pedidoService.findById(id);

        if (!pedidoModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }

        // Atualiza os dados do pedido
        var pedidoModel = pedidoModelOptional.get();
        BeanUtils.copyProperties(pedidoDto, pedidoModel, "id");
        pedidoModel.setCliente(clienteService.findById(pedidoDto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado.")));

        return ResponseEntity.ok().body(pedidoService.save(pedidoModel));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Object> deletarPedido(@PathVariable UUID id) {
        Optional<PedidoModel> pedidoModelOptional = pedidoService.findById(id);

        if (!pedidoModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }

        pedidoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Pedido removido com sucesso.");
    }
}
