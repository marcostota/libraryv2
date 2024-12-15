package io.github.marcostota.libraryv2.controller.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.marcostota.libraryv2.controller.dto.CadastroLivroDTO;
import io.github.marcostota.libraryv2.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.marcostota.libraryv2.entity.Livro;
import io.github.marcostota.libraryv2.repository.AutorRepository;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(source = "generoLivro", target = "generoLivro")
    @Mapping(target = "autor", expression = "java( autorRepository.findById(cadastroLivroDTO.idAutor()).orElse(null) )")
    public abstract Livro toEntity(CadastroLivroDTO cadastroLivroDTO);

    @Mapping(source = "generoLivro", target = "generoLivro")
    public abstract ResultadoPesquisaLivroDTO toDTO(Livro livro);
}
