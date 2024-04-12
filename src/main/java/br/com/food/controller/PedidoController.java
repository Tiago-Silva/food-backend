package br.com.food.controller;

import br.com.food.dto.PedidoPendenteReponseDTO;
import br.com.food.dto.PedidoRequestDTO;
import br.com.food.dto.PedidoResponseDTO;
import br.com.food.dto.PedidoStatusDTO;
import br.com.food.service.PedidoService;
import org.springframework.data.domain.Page;
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

    @PostMapping("/saveOrUpdate")
    public ResponseEntity<HttpStatus> saveOrUpdate(@RequestBody PedidoResponseDTO responseDTO) {
        this.service.saveOrUpdate(responseDTO);
        return new ResponseEntity<>(HttpStatus.CREATED,HttpStatus.OK);
    }

    @GetMapping("/getPedidosByIdEstablishment")
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidoByIdEstablishment(@RequestHeader("idestabelecimento")
                                                                                 int idestabelecimento) {
        return new ResponseEntity<>(this.service.getAllPedidoByIdEstablishment(idestabelecimento), HttpStatus.OK);
    }

    @GetMapping("/getPedidosByIdEstablishmentPayment")
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidoByEstablishmentAndByPaymentType(
                                                                                @RequestHeader("idestabelecimento")
                                                                                int idestabelecimento,
                                                                                @RequestHeader("paymentType")
                                                                                String paymentType) {
        return new ResponseEntity<>(this.service.getAllPedidoByEstablishmentAndByPaymentType(
                idestabelecimento, paymentType), HttpStatus.OK);
    }

    @GetMapping("/getPedidosByUser")
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidoByUser(@RequestHeader("iduser") String iduser) {
        return new ResponseEntity<>(this.service.getAllPedidoByUser(iduser), HttpStatus.OK);
    }

    @GetMapping("/getPedidosByUserPayment")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidoByUserAndByPaymentType(
            @RequestHeader("iduser")
            String iduser,
            @RequestHeader("paymentType")
            String paymentType) {
        return new ResponseEntity<>(this.service.getPedidoByUserAndByPaymentType(
                iduser, paymentType), HttpStatus.OK);
    }

    @GetMapping("/getPedidosByUserPaymentPagination")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidoByUserAndByPaymentTypeWithPagination(
            @RequestHeader("iduser")
            String iduser,
            @RequestHeader("paymentType")
            String paymentType,
            @RequestHeader("pageNumber")
            int pageNumber,
            @RequestHeader("pageSize")
            int pageSize) {
        return new ResponseEntity<>(this.service.getPedidoByUserAndByPaymentTypeWhitPagination(
                iduser, paymentType, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/getPedidosByUserWithDatePagination")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidoByUserWithDatePagination(
            @RequestHeader("iduser")
            String iduser,
            @RequestHeader("startDate")
            String startDate,
            @RequestHeader("endDate")
            String endDate,
            @RequestHeader("pageNumber")
            int pageNumber,
            @RequestHeader("pageSize")
            int pageSize) {
        return new ResponseEntity<>(this.service.getAllPedidoByUserWithDateAndPagination(
                iduser, startDate, endDate, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<Page<PedidoResponseDTO>> getPedidoById(@RequestHeader("idpedido") Long idpedido) {
        return new ResponseEntity<>(this.service.getPedidoById(idpedido), HttpStatus.OK);
    }

    @GetMapping("/getPendenteByIdUser/{iduser}")
    public ResponseEntity<PedidoPendenteReponseDTO> getPedidoPendenteByIdUser(@PathVariable("iduser") String iduser) {
        return new ResponseEntity<>(this.service.getPedidoPendenteByIdUser(iduser), HttpStatus.OK);
    }

    @GetMapping("/getEstablishmentByStatus/{idestabelecimento}/{status}")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosEstablishmentByStatus(
            @PathVariable("idestabelecimento") int idestabelecimento,
            @PathVariable("status") String status
    ) {
        return new ResponseEntity<>(this.service.getPedidosEstablishmentByStatus(idestabelecimento, status), HttpStatus.OK);
    }

    @GetMapping("/getTotalOrdesByStatus/{idestabelecimento}")
    public ResponseEntity<List<PedidoStatusDTO>> getCountByStatusForEstablishment(@PathVariable("idestabelecimento") int idestabelecimento) {
        return new ResponseEntity<>(this.service.getCountByStatusForEstablishment(idestabelecimento), HttpStatus.OK);
    }

    @GetMapping("/getPedidoByEstablishmentWithDatePagination")
    public ResponseEntity<Page<PedidoResponseDTO>> getPedidoByEstablishmentWithDatePagination(
            @RequestHeader("idestabelecimento")
            int idestabelecimento,
            @RequestHeader("startDate")
            String startDate,
            @RequestHeader("endDate")
            String endDate,
            @RequestHeader("pageNumber")
            int pageNumber,
            @RequestHeader("pageSize")
            int pageSize) {
        return new ResponseEntity<>(this.service.getAllPedidoEstablishmentWithDateAndPagination(
                idestabelecimento, startDate, endDate, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/getPedidosByClientName")
    public ResponseEntity<Page<PedidoResponseDTO>> getPedidoByClientName(
            @RequestHeader("idestabelecimento")
            int idestabelecimento,
            @RequestHeader("clientName")
            String clientName,
            @RequestHeader("pageNumber")
            int pageNumber,
            @RequestHeader("pageSize")
            int pageSize
    ) {
          return new ResponseEntity<>(this.service.getPedidoByClientName(
                  idestabelecimento, clientName, pageNumber, pageSize), HttpStatus.OK);
    }
}
