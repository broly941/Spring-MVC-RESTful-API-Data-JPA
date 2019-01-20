package com.intexsoft.devi.controller;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.dto.TeacherDTO;
import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.service.FileService;
import com.intexsoft.devi.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author DEVIAPHAN
 * Processes the request and returns the response as JSON.
 */
@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @Autowired
    FileService fileService;

    @Autowired
    DTOConverter dtoConverter;

    /**
     * method return all teachers
     *
     * @param locale of message
     * @return getAll teacher entity in the database.
     */
    @GetMapping
    public List<TeacherDTO> getAll(Locale locale) {
        return teacherService.getAll(locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * method return teacher by id
     *
     * @param id of teacher
     * @return teacher entity by ID in the database.
     */
    @GetMapping("/{id}")
    public TeacherDTO getById(@PathVariable Long id, Locale locale) {
        return convertToDto(teacherService.getById(id, locale));
    }

    /**
     * method return all teachers by group id
     *
     * @param id     of group
     * @param locale of message
     * @return all teachers by group id
     */
    @GetMapping("/getByGroupId/{id}")
    public List<TeacherDTO> getTeachersOfGroupById(@PathVariable Long id, Locale locale) {
        return teacherService.getTeachersOfGroupById(id, locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * method save teacher
     *
     * @param teacherDTO entity
     * @param locale     of message
     * @return added teacher entity in the database.
     */
    @PostMapping
    public TeacherDTO save(@RequestBody TeacherDTO teacherDTO, Locale locale) {
        return convertToDto(teacherService.save(convertToEntity(teacherDTO), locale));
    }

    /**
     * update teacher by id
     *
     * @param teacherDTO entity
     * @param id         of teacher
     * @param locale     of message
     * @return updated group entity in the database.
     */
    @PutMapping("/{id}")
    public TeacherDTO updateById(@RequestBody TeacherDTO teacherDTO, @PathVariable Long id, Locale locale) {
        Teacher teacher = teacherService.updateById(convertToEntity(teacherDTO), id, locale);
        return convertToDto(teacher);
    }

    /**
     * method delete teacher by id
     *
     * @param id     the teacher entity to be removed from the database
     * @param locale of message
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, Locale locale) {
        teacherService.deleteById(id, locale);
    }

    /**
     * method save entities in database from excel file
     *
     * @param file excel
     * @param locale of message
     * @return validation status
     * @throws IOException if an exception occurred in the file parsing
     */
    @PostMapping("/upload")
    public ValidationStatus addGroupsToTeacher(@RequestParam(value = "file") MultipartFile file, Locale locale) throws IOException {
        return fileService.parse(locale, file.getInputStream(),file.getOriginalFilename().split("\\.")[1], teacherService::validate, teacherService::save);
    }

    private TeacherDTO convertToDto(Teacher teacher) {
        return (TeacherDTO) dtoConverter.convert(teacher, TeacherDTO.class);
    }

    private Teacher convertToEntity(TeacherDTO teacherDTO) {
        return (Teacher) dtoConverter.convert(teacherDTO, Teacher.class);
    }
}
