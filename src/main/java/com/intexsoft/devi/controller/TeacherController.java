package com.intexsoft.devi.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.intexsoft.devi.dto.TeacherDTO;
import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.service.ExcelFileService;
import com.intexsoft.devi.service.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
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
    ExcelFileService excelFileService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * @return getAll teacher entity in the database.
     */
    @GetMapping
    public List<TeacherDTO> getAll(Locale locale) {
        return teacherService.getAll(locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * @param id of teacher
     * @return teacher entity by ID in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @GetMapping("/{id}")
    public TeacherDTO getById(@PathVariable Long id, Locale locale) throws EntityNotFoundException{
        return convertToDto(teacherService.getById(id, locale));
    }

    /**
     * @param id
     * @param locale
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/getByGroupId/{id}")
    public List<TeacherDTO> getTeachersOfGroupById(@PathVariable Long id, Locale locale) throws EntityNotFoundException {
        return teacherService.getTeachersOfGroupById(id, locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * @param teacherDTO entity
     * @return added teacher entity in the database.
     */
    @PostMapping
    public TeacherDTO save(@RequestBody TeacherDTO teacherDTO, Locale locale) {
        return convertToDto(teacherService.save(convertToEntity(teacherDTO), locale));
    }

    /**
     * @param teacherDTO entity
     * @param id         of teachere database.
     *                   * @throws EntityNotFoundException if t
     * @return updated teacher entity in thhere is no value
     */
    @PutMapping("/{id}")
    public TeacherDTO updateById(@RequestBody TeacherDTO teacherDTO, @PathVariable Long id, Locale locale) throws
            EntityNotFoundException {
        Teacher teacher = teacherService.updateById(convertToEntity(teacherDTO), id, locale);
        return convertToDto(teacher);
    }

    /**
     * @param id the teacher entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, Locale locale) {
        teacherService.deleteById(id, locale);
    }

    /**
     * @param file
     * @param locale
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @PostMapping("/fileload")
    public String addGroupsToTeacher(@RequestParam(value = "file") MultipartFile file, @RequestParam Integer page, Locale locale) throws IOException, InvalidFormatException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        if (file.isEmpty()) {
            return "Unable to upload. File is empty.";
        } else {
            return excelFileService.createEntity(locale, file, page, teacherService::fileValidation, teacherService::fileSave);
        }
    }

    private TeacherDTO convertToDto(Teacher teacher) {
        return modelMapper.map(teacher, TeacherDTO.class);
    }

    private Teacher convertToEntity(TeacherDTO teacherDTO) {
        return modelMapper.map(teacherDTO, Teacher.class);
    }
}
