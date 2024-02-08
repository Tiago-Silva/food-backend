package br.com.food.service;

import br.com.food.dto.*;
import br.com.food.entity.Estabelecimento;
import br.com.food.entity.User;
import br.com.food.infra.security.ResourceOwner;
import br.com.food.infra.security.TokenService;
import br.com.food.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Value("${google.client.id}")
    private String googleClientId;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository repository;

    private GoogleIdTokenVerifier googleIdTokenVerifier;
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void init() {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        this.googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    public void saveUser(UserRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.idestabelecimento() < 0) {
            throw new IllegalArgumentException("data = null ou id do estabelecimento menor que zero");
        }
        this.repository.save(new User(requestDTO, new Estabelecimento(requestDTO.idestabelecimento())));
    }

    public LoginResponseMobilleDTO saveUserRegister(RegisterDTO registerDTO) throws AuthenticationException {
        try {
            GoogleIdToken idToken = this.verifyGoogleToken(registerDTO.googleAccessToken());
            if (idToken != null) {
                registerDTO.validate();
                User user = this.getUserOrCreate(registerDTO);
                LoginResponseMobilleDTO loginResponseMobilleDTO = this.authenticateUserMobille(user.getLogin(), user.getEmail());
                this.updateUserWithRefreshToken(user, registerDTO, loginResponseMobilleDTO.refreshToken());
                return loginResponseMobilleDTO;
            } else {
                throw new IllegalArgumentException("Token google inválido");
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Falha ao verificar usuário google");
        }
    }

    private GoogleIdToken verifyGoogleToken(String googleAccessToken) throws GeneralSecurityException, IOException {
        return this.googleIdTokenVerifier.verify(googleAccessToken);
    }

    private User getUserOrCreate(RegisterDTO registerDTO) {
        User user = this.repository.getUserByLogin(registerDTO.email());
        if (user == null) {
            String encryptedPassword = this.passwordEncoder.encode(registerDTO.email());
            user = new User(registerDTO, encryptedPassword);
            this.repository.save(user);
        }
        return user;
    }

    private void updateUserWithRefreshToken(User user, RegisterDTO registerDTO, String refreshToken) {
        user = new User(user.getId(), registerDTO, user.getPassword(), refreshToken);
        this.repository.update(user);
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

    public String authenticatieUserGetToken(String login, String password) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return tokenService.generateTokenWithRotationKey((ResourceOwner) auth.getPrincipal());
    }

    public LoginResponseDTO authenticateUser(String login, String password) throws AuthenticationException {
        return new LoginResponseDTO(this.authenticatieUserGetToken(login, password));
    }

    public LoginResponseMobilleDTO authenticateUserMobille(String login, String password) throws AuthenticationException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateTokenWithRotationKey((ResourceOwner) auth.getPrincipal());
        return new LoginResponseMobilleDTO(token, this.tokenService.generateRefreshToken((ResourceOwner) auth.getPrincipal()));
    }

    public LoginResponseMobilleDTO refreshToken(String refreshToken) {
        if (refreshToken.isEmpty() || refreshToken.isBlank()) {
            throw new IllegalArgumentException("RefreshToken em branco ou null");
        }

        User user = this.repository.getUserByRefreshToken(refreshToken);

        if (user != null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(user.getEmail());
            return this.authenticateUserMobille(user.getLogin(), encryptedPassword);
        } else {
            throw new IllegalArgumentException("RefreshToken inválido");
        }
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
