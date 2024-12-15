package io.github.marcostota.libraryv2.controller.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

public record ErroResposta(int satatus, String mensagem, List<ErroCampo> erros) {

    public static ErroResposta respostaPadrao(String mensagem) {
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }

    public static ErroResposta conflito(String mensagaem) {
        return new ErroResposta(HttpStatus.CONFLICT.value(), mensagaem, List.of());
    }

}