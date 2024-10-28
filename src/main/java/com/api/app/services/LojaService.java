package com.api.app.services;

import com.api.app.models.LojaModel;
import com.api.app.models.ProdutoModel;
import com.api.app.repositories.LojaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LojaService {

    final LojaRepository lojaRepository;
    public LojaService(LojaRepository lojaRepository) {
        this.lojaRepository = lojaRepository;
    }

    @Transactional
    public LojaModel save(LojaModel lojaModel) {

        //associar cada produto a loja
        for (ProdutoModel produto: lojaModel.getProdutos()) {
            //Associa o produto
            produto.setLojaModel(lojaModel);
        }
        return lojaRepository.save(lojaModel);
    }

    public List<LojaModel> findAll() {
        return lojaRepository.findAll();
    }

    public Optional<LojaModel> findById(UUID id) {
        return lojaRepository.findById(id);
    }

    @Transactional
    public void delete(UUID id) {
        lojaRepository.deleteById(id);
    }

}
