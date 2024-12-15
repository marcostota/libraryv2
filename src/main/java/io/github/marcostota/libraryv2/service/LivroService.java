package io.github.marcostota.libraryv2.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.github.marcostota.libraryv2.entity.GeneroLivro;
import io.github.marcostota.libraryv2.entity.Livro;
import io.github.marcostota.libraryv2.repository.LivroRepository;
import io.github.marcostota.libraryv2.repository.specs.LivroSpecs;
import io.github.marcostota.libraryv2.validator.LivroValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroValidator livroValidator;

    public Livro salvar(Livro livro) {
        livroValidator.validar(livro);
        return livroRepository.save(livro);

    }

    public Optional<Livro> obterPorID(UUID id) {
        return livroRepository.findById(id);
    }

    public void deletar(Livro livro) {
        livroRepository.delete(livro);
    }

    public Page<Livro> pesquisa(String isbn, String titulo, String nomeAutor, GeneroLivro generoLivro,
            Integer anoPublicacao, Integer pagina, Integer tamanhoPagina) {

        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (isbn != null) {
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }

        if (titulo != null) {
            specs = specs.and(LivroSpecs.tituloLike(titulo));
        }

        if (generoLivro != null) {
            specs = specs.and(LivroSpecs.generoEqual(generoLivro));
        }

        if (anoPublicacao != null) {
            specs = specs.and(LivroSpecs.anoPublicacao(anoPublicacao));
        }

        if (nomeAutor != null) {
            specs = specs.and(LivroSpecs.nomeAutorLike(nomeAutor));
        }

        Pageable pageableRequest = PageRequest.of(pagina, tamanhoPagina);

        return livroRepository.findAll(specs, pageableRequest);

    }

    public void atualizar(Livro livro) {
        if (livro.getId() == null) {
            throw new IllegalArgumentException("para atualizar e necessario livro cadastrado");
        }
        livroValidator.validar(livro);
        livroRepository.save(livro);
    }

}
