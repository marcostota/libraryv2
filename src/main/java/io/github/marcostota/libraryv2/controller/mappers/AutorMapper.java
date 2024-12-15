package io.github.marcostota.libraryv2.controller.mappers;

import org.mapstruct.Mapper;

import io.github.marcostota.libraryv2.controller.dto.AutorDTO;
import io.github.marcostota.libraryv2.entity.Autor;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    Autor toEntity(AutorDTO autorDTO);

    AutorDTO toDTO(Autor autor);
}
