package io.github.marcostota.libraryv2.controller.dto;

import java.time.LocalDate;
import java.util.UUID;

import io.github.marcostota.libraryv2.entity.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record AutorDTO(
        UUID id,
        @NotBlank(message = "campo obrigatorio") @Size(min = 2, max = 100, message = "campo fora do tamanho") String nome,
        @NotNull(message = "campo obrigatorio") @Past(message = "nao pode ser data futura") LocalDate dataNascimento,
        @NotBlank(message = "campo obrigatorio") String nacionalidade) {

    public Autor mapearParaAutor() {
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }

}
