package io.github.marcostota.libraryv2.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.marcostota.libraryv2.entity.Livro;
import io.github.marcostota.libraryv2.exceptions.CampoInvalidoException;
import io.github.marcostota.libraryv2.exceptions.RegistroDuplicadoException;
import io.github.marcostota.libraryv2.repository.LivroRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LivroValidator {

    private static final int ANO_EXIGENCIA_PRECO = 2020;

    private final LivroRepository livroRepository;

    public void validar(Livro livro) {

        if (existeLivroComIsbn(livro)) {
            throw new RegistroDuplicadoException("ISBN ja cadastrado");
        }
        if (isPrecoObrigatorio(livro)) {
            throw new CampoInvalidoException("preco", "preco obrigatorio para ano 2020");
        }

    }

    private boolean isPrecoObrigatorio(Livro livro) {
        return livro.getPreco() == null && livro.getDataPublicacao().getYear() >= ANO_EXIGENCIA_PRECO;
    }

    private boolean existeLivroComIsbn(Livro livro) {

        Optional<Livro> livroOptional = livroRepository.findByIsbn(livro.getIsbn());

        if (livro.getId() == null) {
            return livroOptional.isPresent();
        }

        return livroOptional
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> id.equals(livro.getId()));
    }
}
