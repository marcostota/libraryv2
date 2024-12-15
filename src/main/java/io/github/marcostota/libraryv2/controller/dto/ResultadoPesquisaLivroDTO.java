package io.github.marcostota.libraryv2.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import io.github.marcostota.libraryv2.entity.GeneroLivro;

public record ResultadoPesquisaLivroDTO(
                UUID id,
                String isbn,
                String titulo,
                LocalDateTime dataPublicacao,
                GeneroLivro generoLivro,
                BigDecimal preco,
                AutorDTO autor) {
}