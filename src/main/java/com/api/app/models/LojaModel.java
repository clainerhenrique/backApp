package com.api.app.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_LOJA")
@Data
public class LojaModel implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @Column(nullable = false)
        private String cnpj;

        @Column(nullable = false)
        private String razaosocial;

        @OneToMany(mappedBy = "lojaModel", cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<ProdutoModel> produtos = new ArrayList<>();


}


