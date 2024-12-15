package io.github.marcostota.libraryv2.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.github.marcostota.libraryv2.controller.dto.AutorDTO;
import io.github.marcostota.libraryv2.controller.mappers.AutorMapper;
import io.github.marcostota.libraryv2.entity.Autor;
import io.github.marcostota.libraryv2.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;
    private final AutorMapper autorMapper;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid AutorDTO autorDTO) {
        // Autor autorEntidade = autorDTO.mapearParaAutor();
        Autor autorEntidade = autorMapper.toEntity(autorDTO);

        autorService.salvar(autorEntidade);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(autorEntidade.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> obterDetalher(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);

        return autorService.obterPorID(idAutor)
                .map(autor -> {
                    AutorDTO dto = autorMapper.toDTO(autor);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOPT = autorService.obterPorID(idAutor);

        if (autorOPT.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        autorService.deletar(autorOPT.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {
        List<Autor> resultAutors = autorService.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> autorDTOList = resultAutors
                .stream().map(autorMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(autorDTOList);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") String id, @RequestBody AutorDTO autorDTO) {

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOPT = autorService.obterPorID(idAutor);
        if (autorOPT.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var autor = autorOPT.get();
        autor.setNome(autorDTO.nome());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autor.setDataNascimento(autorDTO.dataNascimento());

        autorService.atualizar(autor);
        return ResponseEntity.noContent().build();
    }
}
