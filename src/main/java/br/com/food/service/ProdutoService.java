package br.com.food.service;

import br.com.food.dto.ProdutoRequestDTO;
import br.com.food.dto.ProdutoResponseDTO;
import br.com.food.entity.Estabelecimento;
import br.com.food.entity.Produto;
import br.com.food.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public void saveProduct(ProdutoRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.idestabelecimento() <= 0) {
            throw new IllegalArgumentException("objeto null ou idestabelecimento null");
        }
        this.repository.save(new Produto(requestDTO, new Estabelecimento(requestDTO.idestabelecimento())));
    }

    public void updateProduct(ProdutoResponseDTO responseDTO) {
        if (responseDTO == null) {
            throw new IllegalArgumentException("objeto null ou idestabelecimento null");
        }
        this.repository.update(new Produto(responseDTO, new Estabelecimento(responseDTO.idestabelecimento())));
    }

    public ProdutoResponseDTO getProductById(int idproduto) {
        if (idproduto <= 0) {
            throw new IllegalArgumentException("Idestabelecimento null / menor ou igual a zero");
        }
        return mapProductToResponseDTO(this.repository.getEntityById(Produto.class, idproduto));
    }

    public List<ProdutoResponseDTO> getProductsByCategory(int idestabelecimento, String category) {
        if (idestabelecimento <= 0 || category == null) {
            throw new IllegalArgumentException("Idestabelecimento null / menor ou igual a zero ou category = null");
        }
        return this.repository.getProductsByCategory(idestabelecimento,category)
                .stream().map(this::mapProductToResponseDTO).collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> getAllProductByEstablishment(int idestabelecimento) {
        if (idestabelecimento <= 0) {
            throw new IllegalArgumentException("Idestabelecimento null / menor ou igual a zero");
        }
        return this.repository.getAllProducts(idestabelecimento)
                .stream().map(this::mapProductToResponseDTO).collect(Collectors.toList());
    }

    private ProdutoResponseDTO mapProductToResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getIdproduto(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getValor(),
                produto.getCategoria(),
                produto.getStatus(),
                produto.getUrlImage(),
                produto.getEnablePromotions(),
                produto.getEstabelecimento().getIdestabelecimento()
        );
    }
}
