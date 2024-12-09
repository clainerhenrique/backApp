package com.api.app.repositories;

import com.api.app.models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutoRepository extends
        JpaRepository<ProdutoModel, UUID> {

}
