package br.com.food.controller;

import br.com.food.dto.PedidoRequestDTO;
import br.com.food.dto.PedidoResponseDTO;
import br.com.food.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService service;
    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<HttpStatus> savePedido(@RequestBody PedidoRequestDTO requestDTO) {
        this.service.savePedido(requestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED,HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updatePedido(@RequestBody PedidoResponseDTO responseDTO) {
        this.service.updatePedido(responseDTO);
        return new ResponseEntity<>(HttpStatus.CREATED,HttpStatus.OK);
    }

    @GetMapping("/getPedidos/{idestabelecimento}")
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidoByIdEstablishment(@PathVariable("idestabelecimento")
                                                                                 int idestabelecimento) {
        return new ResponseEntity<>(this.service.getAllPedidoByIdEstablishment(idestabelecimento), HttpStatus.OK);
    }

    @GetMapping("/getPedidos/{idestabelecimento}/{paymentType}")
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidoByEstablishmentAndByPaymentType(
                                                                                @PathVariable("idestabelecimento")
                                                                                int idestabelecimento,
                                                                                @PathVariable("paymentType")
                                                                                String paymentType) {
        return new ResponseEntity<>(this.service.getAllPedidoByEstablishmentAndByPaymentType(
                idestabelecimento, paymentType), HttpStatus.OK);
    }

    @GetMapping("/getPedidosByUser/{iduser}")
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidoByUser(@PathVariable("iduser") String iduser) {
        return new ResponseEntity<>(this.service.getAllPedidoByUser(iduser), HttpStatus.OK);
    }

    @GetMapping("/getPedidosByUser/{iduser}/{paymentType}")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidoByUserAndByPaymentType(
            @PathVariable("iduser")
            String iduser,
            @PathVariable("paymentType")
            String paymentType) {
        return new ResponseEntity<>(this.service.getPedidoByUserAndByPaymentType(
                iduser, paymentType), HttpStatus.OK);
    }

    @GetMapping("/getById/{idpedido}")
    public ResponseEntity<PedidoResponseDTO> getPedidoById(@PathVariable("idpedido") Long idpedido) {
        return new ResponseEntity<>(this.service.getPedidoById(idpedido), HttpStatus.OK);
    }
}
