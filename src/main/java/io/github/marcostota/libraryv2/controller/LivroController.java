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

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

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
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nomeAutor", required = false) String nomeAutor,
            @RequestParam(value = "generoLivro", required = false) GeneroLivro generoLivro,
            @RequestParam(value = "anoPublicacao", required = false) Integer anoPublicaca,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10") Integer tamanhoPagina

    ) {
        Page<Livro> paginaResultado = livroService.pesquisa(isbn, titulo, nomeAutor, generoLivro, anoPublicaca, pagina,
                tamanhoPagina);
        Page<ResultadoPesquisaLivroDTO> resultadoDto = paginaResultado.map(livroMapper::toDTO);

        return ResponseEntity.ok(resultadoDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") String id,
            @RequestBody @Valid CadastroLivroDTO cadastroLivroDTO) {

        return livroService.obterPorID(UUID.fromString(id))
                .map(livro -> {
                    Livro livroAux = livroMapper.toEntity(cadastroLivroDTO);
                    livro.setIsbn(livroAux.getIsbn());
                    livro.setPreco(livroAux.getPreco());
                    livro.setGeneroLivro(livroAux.getGeneroLivro());
                    livro.setTitulo(livroAux.getTitulo());
                    livro.setAutor(livroAux.getAutor());
                    livro.setDataPublicacao(livroAux.getDataPublicacao());
                    livroService.atualizar(livro);

                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
