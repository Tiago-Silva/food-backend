package br.com.food.controller;

import br.com.food.dto.ProdutoRequestDTO;
import br.com.food.dto.ProdutoResponseDTO;
import br.com.food.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService service;
    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> saveProduct(@RequestBody ProdutoRequestDTO requestDTO) {
        this.service.saveProduct(requestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> updateProduct(@RequestBody ProdutoResponseDTO responseDTO) {
        this.service.updateProduct(responseDTO);
        return new ResponseEntity<>(HttpStatus.CREATED, HttpStatus.OK);
    }

    @RequestMapping(value = "/getProdutos/{idestabelecimento}/{category}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProdutoResponseDTO>> getProductsByCategory(
                                                        @PathVariable("idestabelecimento") int idestabelecimento,
                                                        @PathVariable("category") String category) {
        return new ResponseEntity<>(this.service.getProductsByCategory(
                idestabelecimento,
                category),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/getProdutos/{idestabelecimento}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProdutoResponseDTO>> getAllProductsByEstablishment(
            @PathVariable("idestabelecimento") int idestabelecimento) {
        return new ResponseEntity<>(this.service.getAllProductByEstablishment(idestabelecimento),HttpStatus.OK);
    }

    @RequestMapping(value = "/getById/{idproduto}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProdutoResponseDTO> getProductById(@PathVariable("idproduto") int idproduto) {
        return new ResponseEntity<>(this.service.getProductById(idproduto),HttpStatus.OK);
    }
}
