package br.com.food.controller;

import br.com.food.dto.UserRequestDTO;
import br.com.food.dto.UserResponseDTO;
import br.com.food.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> saveUser(@RequestBody UserRequestDTO requestDTO) {
        this.service.saveUser(requestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> updateUser(@RequestBody UserResponseDTO responseDTO) {
        this.service.updateUser(responseDTO);
        return new ResponseEntity<>(HttpStatus.CREATED, HttpStatus.OK);
    }

    @RequestMapping(value = "/getUsers/{idestabelecimento}/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDTO>> getUsers(@PathVariable("idestabelecimento") int idestabelecimento,
                                                          @PathVariable("type") String type) {
        return new ResponseEntity<>(this.service.getUsersOfEstablishment(
                idestabelecimento,
                type),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/getUsers/{idestabelecimento}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@PathVariable("idestabelecimento") int idestabelecimento) {
        return new ResponseEntity<>(this.service.getAllUsersByEstablishment(idestabelecimento),HttpStatus.OK);
    }

    @RequestMapping(value = "/getById/{iduser}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> getUsersById(@PathVariable("iduser") String iduser) {
        return new ResponseEntity<>(this.service.getUserById(iduser),HttpStatus.OK);
    }

    @RequestMapping(value = "/getConsumidorFinal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> getConsumidorFinal() {
        return new ResponseEntity<>(this.service.getConsumidorFinal(),HttpStatus.OK);
    }
}
