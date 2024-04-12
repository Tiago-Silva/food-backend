package br.com.food.dto;

public record PedidoStatusDTO(int total, String title, String background) {
    public PedidoStatusDTO(String title, String background) {
        this(0, title, background);
    }
}
