package br.com.food.controller;

import br.com.food.dto.EstabelecimentoRequestDTO;
import br.com.food.dto.EstabelecimentoResponseDTO;
import br.com.food.service.EstabelecimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empress")
public class EstabelecimentoController {

    private final EstabelecimentoService service;
    public EstabelecimentoController(EstabelecimentoService service) {
        this.service = service;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> saveEstabelecimento(@RequestBody EstabelecimentoRequestDTO requestDTO) {
        this.service.saveEstabelecimento(requestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> updateEstabelecimento(@RequestBody EstabelecimentoResponseDTO responseDTO) {
        this.service.updateEstabelecimento(responseDTO);
        return new ResponseEntity<>(HttpStatus.CREATED, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById/{idestabelecimento}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EstabelecimentoResponseDTO> getEstabelecimentoById(
            @PathVariable("idestabelecimento") int idestabelecimento) {
        return new ResponseEntity<>(this.service.getEstabelecimentoById(idestabelecimento), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EstabelecimentoResponseDTO>> getAll() {
        return new ResponseEntity<>(this.service.getAll(), HttpStatus.OK);
    }
}
