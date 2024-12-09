package com.api.app.services;

import com.api.app.models.PedidoModel;
import com.api.app.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoService {

    final PedidoRepository pedidoRepository;

    // Construtor para injeção de dependência
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // Método para salvar um pedido
    @Transactional
    public PedidoModel save(PedidoModel pedidoModel) {
        return pedidoRepository.save(pedidoModel);
    }

    // Método para buscar todos os pedidos
    public List<PedidoModel> findAll() {
        return pedidoRepository.findAll();
    }

    // Método para buscar um pedido por ID
    public Optional<PedidoModel> findById(UUID id) {
        return pedidoRepository.findById(id);
    }

    // Método para deletar um pedido por ID
    @Transactional
    public void delete(UUID id) {
        pedidoRepository.deleteById(id);
    }
}
