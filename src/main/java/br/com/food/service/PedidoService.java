package br.com.food.service;

import br.com.food.dto.PedidoRequestDTO;
import br.com.food.dto.PedidoResponseDTO;
import br.com.food.entity.Pedido;
import br.com.food.entity.User;
import br.com.food.regras.DataFormat;
import br.com.food.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    public PedidoService(PedidoRepository repository) { this.repository = repository; }

    private DataFormat dataFormat = new DataFormat();;

    public void savePedido(PedidoRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.items().isEmpty() || requestDTO.iduser() == null) {
            throw new IllegalArgumentException("Objeto null / iduser null ou itens vazio");
        }
        this.repository.save(new Pedido(requestDTO, new User(requestDTO.iduser())));
    }

    public void updatePedido(PedidoResponseDTO responseDTO) {
        if (responseDTO == null || responseDTO.idpedido() <= 0
                || responseDTO.items().isEmpty() || responseDTO.iduser() == null) {
            throw new IllegalArgumentException("Objeto null / iuser ou idpedido null ou itens vazio");
        }
        this.repository.update(new Pedido(responseDTO, new User(responseDTO.iduser())));
    }

    public List<PedidoResponseDTO> getAllPedidoByIdEstablishment(int idestabelecimento) {
        if (idestabelecimento <= 0) {
            throw new IllegalArgumentException("idestabelecimento inválido ou null");
        }
        return this.repository.getAllPedidoByIdEstablishment(idestabelecimento)
                .stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> getAllPedidoByEstablishmentAndByPaymentType(int idestabelecimento, String paymentType) {
        if (idestabelecimento <= 0 || paymentType == null) {
            throw new IllegalArgumentException("idestabelecimento inválido ou null ou payment type null");
        }
        return this.repository.getAllPedidoByEstablishmentAndByPaymentType(idestabelecimento,paymentType)
                .stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> getAllPedidoByUser(String iduser) {
        if (iduser == null) {
            throw new IllegalArgumentException("iduser inválido ou null");
        }
        return this.repository.getAllPedidoByUser(iduser)
                .stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> getPedidoByUserAndByPaymentType(String iduser, String paymentType) {
        if (iduser == null || paymentType == null) {
            throw new IllegalArgumentException("iduser inválido ou null ou payment type null");
        }
        return this.repository.getPedidoByUserAndByPaymentType(iduser, paymentType)
                .stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> getPedidoById(Long idpedido) {
        if (idpedido <= 0) {
            throw new IllegalArgumentException("idpedido inválido ou null");
        }
        System.out.println("id: " + idpedido);
        List<PedidoResponseDTO> list = new ArrayList<>();
        list.add(this.mapPedidoToResponseDTO(this.repository.getEntityById(
                Pedido.class,
                idpedido
        )));

        return list;
    }

    public List<PedidoResponseDTO> getPedidoByUserAndByPaymentTypeWhitPagination(
            String iduser,
             String paymentType,
             int pageNumber,
             int pageSize
    ) {
        if (iduser == null || paymentType == null || pageNumber < 0 || pageSize < 0) {
            throw new IllegalArgumentException("Parametros inválidos, verifique!!!");
        }
        return this.repository.getPedidoByUserAndByPaymentTypeWhitPagination(
                iduser,
                paymentType,
                pageNumber,
                pageSize
        ).stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> getAllPedidoByUserWithDateAndPagination(
            String iduser,
            String startDate,
            String endDate,
            int pageNumber,
            int pageSize
    ) {
        if (iduser == null || startDate == null || endDate == null || pageNumber < 0 || pageSize < 0) {
            throw new IllegalArgumentException("Parametros inválidos, verifique!!!");
        }

        return this.repository.getAllPedidoByUserWithDataAndPagination(
                iduser,
                this.dataFormat.FormatData(startDate, "dd/MM/yyyy"),
                this.dataFormat.FormatData(endDate, "dd/MM/yyyy"),
                pageNumber,
                pageSize
        ).stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> getAllPedidoEstablishmentWithDateAndPagination(
            int idestabelecimento,
            String startDate,
            String endDate,
            int pageNumber,
            int pageSize
    ) {
        if (idestabelecimento <= 0 || startDate == null || endDate == null || pageNumber < 0 || pageSize < 0) {
            throw new IllegalArgumentException("Parametros inválidos, verifique!!!");
        }
        this.dataFormat = new DataFormat();
        return this.repository.getAllPedidoEstablishmentWithDataAndPagination(
                idestabelecimento,
                this.dataFormat.FormatData(startDate, "dd/MM/yyyy"),
                this.dataFormat.FormatData(endDate, "dd/MM/yyyy"),
                pageNumber,
                pageSize
        ).stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> getPedidoByClientName(int idestabelecimento, String clientName) {

        if (idestabelecimento <= 0 || clientName == null) {
            throw new IllegalArgumentException("Parametros inválidos, verifique!!!");
        }

        return this.repository.getPedidoByClientName(idestabelecimento, clientName)
                .stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList());
    }

    public PedidoResponseDTO mapPedidoToResponseDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getIdpedido(),
                pedido.getData(),
                pedido.getAno(),
                pedido.getMes(),
                pedido.getDia(),
                pedido.getHora(),
                pedido.getTotal(),
                pedido.getUser().getNome(),
                pedido.getUser().getId(),
                pedido.getTipoPagamento(),
                new ArrayList<>()
        );
    }
}
