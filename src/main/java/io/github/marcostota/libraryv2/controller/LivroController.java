package io.github.marcostota.libraryv2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.marcostota.libraryv2.controller.dto.CadastroLivroDTO;
import io.github.marcostota.libraryv2.controller.dto.ErroResposta;
import io.github.marcostota.libraryv2.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.marcostota.libraryv2.controller.mappers.LivroMapper;
import io.github.marcostota.libraryv2.entity.GeneroLivro;
import io.github.marcostota.libraryv2.entity.Livro;
import io.github.marcostota.libraryv2.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController {

    private final LivroMapper livroMapper;
    private final LivroService livroService;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO) {
        Livro livro = livroMapper.toEntity(cadastroLivroDTO);
        livroService.salvar(livro);
        URI url = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id) {
        var idLivro = UUID.fromString(id);
        return livroService.obterPorID(idLivro)
                .map(livro -> {
                    ResultadoPesquisaLivroDTO dto = livroMapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") String id) {
        return livroService.obterPorID(UUID.fromString(id))
                .map(livro -> {
                    livroService.deletar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nomeAutor", required = false) String nomeAutor,
            @RequestParam(value = "generoLivro", required = false) GeneroLivro generoLivro,
            @RequestParam(value = "anoPublicacao", required = false) Integer anoPublicaca) {

        var livrosResultados = livroService.pesquisa(isbn, titulo, nomeAutor, generoLivro, anoPublicaca);

        var livrosList = livrosResultados
                .stream()
                .map(livroMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(livrosList);
    }
}
