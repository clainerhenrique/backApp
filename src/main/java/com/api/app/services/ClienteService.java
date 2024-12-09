package com.api.app.services;

import com.api.app.models.ClienteModel;
import com.api.app.models.PedidoModel;
import com.api.app.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteModel save(ClienteModel clienteModel) {
        // Associar cada pedido ao cliente
        for (PedidoModel pedido : clienteModel.getPedidos()) {
            pedido.setCliente(clienteModel);
        }
        return clienteRepository.save(clienteModel);
    }

    public List<ClienteModel> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<ClienteModel> findById(UUID id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    public void delete(UUID id) {
        clienteRepository.deleteById(id);
    }
}
