package br.com.food.controller;

import br.com.food.dto.ItemResponseDTO;
import br.com.food.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService service;
    public ItemController(ItemService service) {
        this.service = service;
    }

    @RequestMapping(value = "/getItemsByIdPedido", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemResponseDTO>> getItemsByIdPedido(@RequestHeader("idpedido") Long idpedido) {
        return new ResponseEntity<>(this.service.getItemsByIdPedido(idpedido), HttpStatus.OK);
    }
}
