package br.com.food.controller;

import br.com.food.dto.AuthenticationDTO;
import br.com.food.dto.LoginResponseDTO;
import br.com.food.dto.RegisterDTO;
import br.com.food.entity.Estabelecimento;
import br.com.food.entity.User;
import br.com.food.infra.security.ResourceOwner;
import br.com.food.infra.security.TokenService;
import br.com.food.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository criteriaRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateTokenWithRotationKey((ResourceOwner) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (BadCredentialsException ex) {
            // Se a exceção for do tipo BadCredentialsException, significa que a senha está incorreta
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (UsernameNotFoundException ex) {
            // Se a exceção for do tipo UsernameNotFoundException, significa que o usuário não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AuthenticationException ex) {
            // Lidar com outras exceções de autenticação conforme necessário
            logger.error("Erro durante a autenticação: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid RegisterDTO data){
        if(this.criteriaRepository.getUserByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
//        User newUser = new User(data, encryptedPassword, new Estabelecimento(1));

//        this.criteriaRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}

