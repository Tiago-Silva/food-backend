package br.com.food.service;

import br.com.food.dto.LoginResponseDTO;
import br.com.food.dto.RegisterDTO;
import br.com.food.dto.UserRequestDTO;
import br.com.food.dto.UserResponseDTO;
import br.com.food.entity.Estabelecimento;
import br.com.food.entity.User;
import br.com.food.infra.security.ResourceOwner;
import br.com.food.infra.security.TokenService;
import br.com.food.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository repository;
    public UserService(UserRepository repository, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public void saveUser(UserRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.idestabelecimento() < 0) {
            throw new IllegalArgumentException("data = null ou id do estabelecimento menor que zero");
        }
        this.repository.save(new User(requestDTO, new Estabelecimento(requestDTO.idestabelecimento())));
    }

    public LoginResponseDTO saveUserRegister(RegisterDTO registerDTO) throws AuthenticationException {
        registerDTO.validate();
        if(this.repository.getUserByLogin(registerDTO.email()) != null) {
            throw new IllegalArgumentException("Usuário já existente");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.email());
        User user = new User(registerDTO, encryptedPassword);
        this.repository.save(user);
        return this.authenticateUser(user.getLogin(), user.getEmail());
    }

    public UserResponseDTO updateUser(UserResponseDTO responseDTO) {
        if (responseDTO == null || responseDTO.id() == null) {
            throw new IllegalArgumentException("User = null ou id = null");
        }
        this.repository.update(new User(responseDTO, new Estabelecimento(responseDTO.idestabelecimento())));
        return responseDTO;
    }

    public List<UserResponseDTO> getUsersOfEstablishment(int idestabelecimento, String type) {
        if (idestabelecimento < 0 || type == null) {
            throw new IllegalArgumentException("Verique os parametros, algum está null");
        }
        return this.repository.getUsersOfEstablishmentByType(idestabelecimento, type)
                .stream().map(this::mapUserToResponseDTO).collect(Collectors.toList());
    }

    public List<UserResponseDTO> getAllUsersByEstablishment(int idestabelecimento) {
        if (idestabelecimento < 0) {
            throw new IllegalArgumentException("IdEstabelecimento = ou menor que zero ou null");
        }
        return this.repository.getAllUsersByEstablishment(idestabelecimento)
                .stream().map(this::mapUserToResponseDTO).collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(String iduser) {
        if (iduser == null || iduser.length() < 6) {
            throw new IllegalArgumentException("Iduser em blanco / null ou não é um UUID");
        }

        Optional<User> optional = Optional.ofNullable(this.repository.getUserById(iduser));

        if (optional.isPresent()) {
            return this.mapUserToResponseDTO(optional.get());
        } else {
            throw new IllegalArgumentException("UUID inválido / null ou user não encontrado");
        }
    }

    public LoginResponseDTO authenticateUser(String login, String password) throws AuthenticationException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateTokenWithRotationKey((ResourceOwner) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }

    private UserResponseDTO mapUserToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getSobreNome(),
                user.getLogin(),
                user.getPassword(),
                user.getTelefone(),
                user.getEndereco(),
                user.getCpf(),
                user.getEmail(),
                user.getType(),
                user.getRole(),
                user.getAccountNonExpired(),
                user.getAccountNonLocked(),
                user.getCredentialsNonExpired(),
                user.getEnabled(),
                user.getEstabelecimento().getIdestabelecimento()
        );
    }
}
