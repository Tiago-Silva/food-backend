package br.com.food.service;

import br.com.food.dto.*;
import br.com.food.entity.Item;
import br.com.food.entity.Pedido;
import br.com.food.entity.User;
import br.com.food.enuns.PedidoStatus;
import br.com.food.regras.DataFormat;
import br.com.food.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemService itemService;
    public PedidoService(PedidoRepository repository, ItemService itemService) {
        this.repository = repository;
        this.itemService = itemService;
    }

    private DataFormat dataFormat = new DataFormat();;

    public void savePedido(PedidoRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.itemRequestDTOS().isEmpty() || requestDTO.iduser() == null) {
            throw new IllegalArgumentException("Objeto null / iduser null ou itens vazio");
        }
        List<Item> itemList = new ArrayList<>();
        for (ItemRequestDTO itemRequestDTO : requestDTO.itemRequestDTOS()) {
            itemList.add(new Item(itemRequestDTO));
        }
        this.repository.save(new Pedido(requestDTO, new User(requestDTO.iduser()), itemList));
    }

    public void updatePedido(PedidoResponseDTO responseDTO) {
        if (responseDTO == null || responseDTO.idpedido() <= 0
                || responseDTO.itemsReponseDTO().isEmpty() || responseDTO.iduser() == null) {
            throw new IllegalArgumentException("Objeto null / iuser ou idpedido null ou itens vazio");
        }
        this.repository.update(
            new Pedido(
                responseDTO,
                new User(responseDTO.iduser()),
                    responseDTO.itemsReponseDTO().stream().map(
                        itemResponseDTO -> itemService.mapItemResponseDTOTOItem(
                            itemResponseDTO,
                            responseDTO.idpedido()
                        )
                    ).collect(Collectors.toList())
            )
        );
    }

    public void saveOrUpdate(PedidoResponseDTO responseDTO) {
        if (responseDTO.idpedido() != null && responseDTO.idpedido() > 0) {
            if (responseDTO.itemsReponseDTO().isEmpty()) {
                this.itemService.deleteItemsToPedido(responseDTO.idpedido());
                this.repository.delete(new Pedido(responseDTO.idpedido()));
            } else {
                Pedido pedido = new Pedido(responseDTO, new User(responseDTO.iduser()));
                this.repository.update(pedido);
                this.itemService.updateItemsToPedido(responseDTO.idpedido(), responseDTO.itemsReponseDTO());
            }
        } else {
            this.savePedido(this.mapPedidoResponseDTOToRequestDTO(responseDTO));
        }
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

    public PedidoPendenteReponseDTO getPedidoPendenteByIdUser(String iduser) {
        if (iduser == null) {
            throw new IllegalArgumentException("iduser inválido");
        }
        List<Pedido> pedidos = this.repository.getPedidoPendenteByIdUser(iduser, PedidoStatus.PENDENTE.toString());
        if (pedidos.isEmpty()) {
            return null;
        }
        return this.mapPedidoToResponseDTOWithItems(pedidos.get(0));
    }

    public List<PedidoResponseDTO> getPedidosEstablishmentByStatus(int idestabelecimento, String status) {
        if (idestabelecimento <= 0 || status == null) {
            throw new IllegalArgumentException("Argumentos inválidos");
        }
        return this.repository.getPedidosEstablishmentByStatus(idestabelecimento, status)
                .stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList());
    }

    public Page<PedidoResponseDTO> getPedidoById(Long idpedido) {
        if (idpedido <= 0) {
            throw new IllegalArgumentException("idpedido inválido ou null");
        }

        List<PedidoResponseDTO> list = new ArrayList<>();
        list.add(this.mapPedidoToResponseDTO(this.repository.getEntityById(
                Pedido.class,
                idpedido
        )));

        return new PageImpl<PedidoResponseDTO>(
                list,
                PageRequest.of(0, 5),
                1
        );
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

    public Page<PedidoResponseDTO> getAllPedidoEstablishmentWithDateAndPagination(
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
        Page<Pedido> page = this.repository.getAllPedidoEstablishmentWithDataAndPagination(
                idestabelecimento,
                this.dataFormat.FormatData(startDate, "dd/MM/yyyy"),
                this.dataFormat.FormatData(endDate, "dd/MM/yyyy"),
                pageNumber,
                pageSize
        );

        return new PageImpl<PedidoResponseDTO>(
                page.getContent().stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList()),
                PageRequest.of(page.getNumber(), page.getSize()),
                page.getTotalElements()
        );
    }

    public Page<PedidoResponseDTO> getPedidoByClientName(
            int idestabelecimento,
            String clientName,
            int pageNumber,
            int pageSize
    ) {

        if (idestabelecimento <= 0 || clientName == null) {
            throw new IllegalArgumentException("Parametros inválidos, verifique!!!");
        }

        Page<Pedido> page =  this.repository.getPedidoByClientName(idestabelecimento, clientName, pageNumber, pageSize);

        return new PageImpl<PedidoResponseDTO>(
                page.getContent().stream().map(this::mapPedidoToResponseDTO).collect(Collectors.toList()),
                PageRequest.of(page.getNumber(), pageSize),
                page.getTotalElements()
        );
    }

    public List<PedidoStatusDTO> getCountByStatusForEstablishment(int idestabelecimento) {
        if (idestabelecimento <= 0) {
            throw new IllegalArgumentException("argumento inválido ou null");
        }
        Map<String, Long> count = this.repository.getCountByStatusForEstablishment(idestabelecimento);

        EnumMap<PedidoStatus, String> statusToBackground = new EnumMap<>(PedidoStatus.class);
        statusToBackground.put(PedidoStatus.RECEBIDO, "title");
        statusToBackground.put(PedidoStatus.PREPARAÇÃO, "secondary");
        statusToBackground.put(PedidoStatus.PRONTO, "secondary_light");
        statusToBackground.put(PedidoStatus.ENVIADO, "success_light");
        statusToBackground.put(PedidoStatus.FINALIZADO, "success");
        statusToBackground.put(PedidoStatus.CANCELADO, "attention");
        statusToBackground.put(PedidoStatus.ENTREGUE, "primary");
        statusToBackground.put(PedidoStatus.PENDENTE, "attention_light");

        return Arrays.stream(PedidoStatus.values())
            .sorted(Comparator.comparingInt(PedidoStatus::getOrder))
            .map(status -> new PedidoStatusDTO(
            count.getOrDefault(status.name(), 0L).intValue(),
            status.name(),
            statusToBackground.get(status)
        ))
        .collect(Collectors.toList());
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
                pedido.getStatus(),
                new ArrayList<>()
        );
    }

    private PedidoPendenteReponseDTO mapPedidoToResponseDTOWithItems(Pedido pedido) {
        return new PedidoPendenteReponseDTO(
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
                pedido.getStatus(),
                pedido.getItems().stream().map(this::mapItemToResponseDTO).collect(Collectors.toList())
        );
    }

    private ItemResponseDTO mapItemToResponseDTO(Item item) {
        return new ItemResponseDTO(
                item.getIditem(),
                item.getQuantidade(),
                item.getDescricao(),
                item.getProduto().getValor(),
                item.getProduto().getValor().multiply(BigDecimal.valueOf(item.getQuantidade())),
                item.getProduto().getIdproduto(),
                item.getPedido().getIdpedido()
        );
    }

    private PedidoRequestDTO mapPedidoResponseDTOToRequestDTO (PedidoResponseDTO responseDTO) {
        return new PedidoRequestDTO(
                responseDTO.total(),
                responseDTO.iduser(),
                responseDTO.tipoPagamento(),
                responseDTO.status(),
                responseDTO.itemsReponseDTO().stream().map(this::mapItemResponseDTOToRequestDTO).collect(Collectors.toList())
        );
    }

    private ItemRequestDTO mapItemResponseDTOToRequestDTO (ItemResponseDTO item) {
        return new ItemRequestDTO(
                item.quantidade(),
                item.descricao(),
                item.idproduto(),
                item.idpedido()
        );
    }
}
