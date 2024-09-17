package com.example.demolibrary.config;

import com.example.demolibrary.dto.BookStatusDto;
import com.example.demolibrary.models.BookStatus;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Настройка маппинга для BookStatus
        modelMapper.typeMap(BookStatus.class, BookStatusDto.class).addMappings(mapping -> {
            mapping.map(src -> src.getBook().getId(), BookStatusDto::setBookId); // Указываем, откуда брать bookId
        });

        return modelMapper;
    }
}