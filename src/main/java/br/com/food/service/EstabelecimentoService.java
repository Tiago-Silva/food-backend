package br.com.food.service;

import br.com.food.dto.EstabelecimentoRequestDTO;
import br.com.food.dto.EstabelecimentoResponseDTO;
import br.com.food.entity.Estabelecimento;
import br.com.food.repository.EstabelecimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstabelecimentoService {

    private final EstabelecimentoRepository repository;
    public EstabelecimentoService(EstabelecimentoRepository repository) {
        this.repository = repository;
    }

    public void saveEstabelecimento(EstabelecimentoRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw  new IllegalArgumentException("data = null");
        }
        this.repository.save(new Estabelecimento(requestDTO));
    }

    public void updateEstabelecimento(EstabelecimentoResponseDTO responseDTO) {
        if (responseDTO == null) {
            throw new IllegalArgumentException("Data igual a null");
        }
        this.repository.update(new Estabelecimento(responseDTO));
    }

    public EstabelecimentoResponseDTO getEstabelecimentoById(int idestabelecimento) {
        if (idestabelecimento < 0) {
            throw new IllegalArgumentException("id menor que zero");
        }
        return mapEstabelecimentoToResponseDTO(this.repository.getEntityById(Estabelecimento.class,idestabelecimento));
    }

    public List<EstabelecimentoResponseDTO> getAll() {
        return this.repository.getQuery(Estabelecimento.class).getResultList()
                .stream().map(this::mapEstabelecimentoToResponseDTO).collect(Collectors.toList());
    }

    public EstabelecimentoResponseDTO mapEstabelecimentoToResponseDTO(Estabelecimento estabelecimento) {
        return new EstabelecimentoResponseDTO(
                estabelecimento.getIdestabelecimento(),
                estabelecimento.getRazaoSocial(),
                estabelecimento.getNomeFantasia(),
                estabelecimento.getCnpj(),
                estabelecimento.getCpf(),
                estabelecimento.getPais(),
                estabelecimento.getEstado(),
                estabelecimento.getCidade(),
                estabelecimento.getBairro(),
                estabelecimento.getEndereco(),
                estabelecimento.getTelefone()
        );
    }
}
