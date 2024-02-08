package br.com.food.dto;

public record LoginResponseMobilleDTO(
    String token,
    String refreshToken
) {
}
