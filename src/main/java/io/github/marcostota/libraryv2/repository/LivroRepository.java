package io.github.marcostota.libraryv2.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.github.marcostota.libraryv2.entity.Livro;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {

    Optional<Livro> findByIsbn(String isbn);
}