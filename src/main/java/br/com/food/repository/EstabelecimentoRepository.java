package br.com.food.repository;

import br.com.food.dto.EstabelecimentoResponseDTO;
import org.springframework.stereotype.Repository;

@Repository
public class EstabelecimentoRepository extends GenericRepository {

    public EstabelecimentoResponseDTO getEstabelecimentoById(int idestabelecimento) {
        return super.getEntityById(EstabelecimentoResponseDTO.class, idestabelecimento);
    }
}
