package io.github.marcostota.libraryv2.repository.specs;

import org.springframework.data.jpa.domain.Specification;

import io.github.marcostota.libraryv2.entity.GeneroLivro;
import io.github.marcostota.libraryv2.entity.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class LivroSpecs {

    public static Specification<Livro> isbnEqual(String isbn) {
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro> tituloLike(String titulo) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    public static Specification<Livro> generoEqual(GeneroLivro generoLivro) {
        return (root, query, cb) -> cb.equal(root.get("generoLivro"), generoLivro);
    }

    public static Specification<Livro> anoPublicacao(Integer anoPulicacao) {
        return (root, query, cb) -> cb.equal(cb.function("to_char", String.class, root.get("dataPublicacao"),
                cb.literal("YYYY")), anoPulicacao.toString());
    }

    public static Specification<Livro> nomeAutorLike(String nome) {
        return (root, query, cb) -> {

            Join<Object, Object> joinAutor = root.join("autor", JoinType.LEFT);
            return cb.like(cb.upper(joinAutor.get("nome")), "%" + nome.toUpperCase() + "%");
            // return cb.like(cb.upper(root.get("autor").get("nome")), "%" +
            // nome.toUpperCase() + "%");
        };
    }
}
