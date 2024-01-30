package br.com.food.controller;

import br.com.food.dto.AuthenticationDTO;
import br.com.food.dto.LoginResponseDTO;
import br.com.food.dto.RegisterDTO;
import br.com.food.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            return ResponseEntity.ok(this.userService.authenticateUser(data.login(), data.password()));
        } catch (BadCredentialsException ex) {
            logger.error("BadCredentialsException, significa que a senha está incorreta: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (UsernameNotFoundException ex) {
            logger.error("UsernameNotFoundException, significa que o usuário não foi encontrado: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AuthenticationException ex) {
            // Lidar com outras exceções de autenticação conforme necessário
            logger.error("Erro durante a autenticação: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO data) {
        try {
            return ResponseEntity.ok(this.userService.saveUserRegister(data));
        } catch (BadCredentialsException ex) {
            logger.error("BadCredentialsException, significa que a senha está incorreta: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (UsernameNotFoundException ex) {
            logger.error("UsernameNotFoundException, significa que o usuário não foi encontrado: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AuthenticationException ex) {
            // Lidar com outras exceções de autenticação conforme necessário
            logger.error("Erro durante a autenticação: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

