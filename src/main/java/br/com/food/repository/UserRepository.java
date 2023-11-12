package br.com.food.repository;

import br.com.food.dto.UserResponseDTO;
import br.com.food.entity.User;
import br.com.food.enuns.UserType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends GenericRepository {

    public User getUserByLogin(String login) {
        return super.getEntityByProperty(User.class,login);
    }

    public User getUserById(String id) {
        return super.getEntityById(User.class, id);
    }

    public List<User> getUsersOfEstablishmentByType(int idestabelecimento,
                                                    String type) {
        return super.getEntitiesByForeignKeyAndWithConditional(
                User.class,
                "estabelecimento",
                "idestabelecimento",
                idestabelecimento,
                "nome",
                "type",
                UserType.valueOf(type)
        );
    }

    public List<User> getAllUsersByEstablishment(int idestabelecimento) {
        return super.getJoinColumn(
                User.class,
                "estabelecimento",
                "idestabelecimento",
                idestabelecimento,
                "nome");
    }
}
