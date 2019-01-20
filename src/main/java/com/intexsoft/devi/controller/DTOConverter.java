package com.intexsoft.devi.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author DEVIAPHAN on 16.01.2019
 * @project university
 * Class converts entities to DTO and DTO to entities
 */
@Component
public class DTOConverter {
    @Autowired
    ModelMapper modelMapper;

    /**
     * method convert entity to requiredType and return it
     *
     * @return requiredType
     */
    public Object convert(Object entity, Type requiredType) {
        return modelMapper.map(entity, requiredType);
    }
}
