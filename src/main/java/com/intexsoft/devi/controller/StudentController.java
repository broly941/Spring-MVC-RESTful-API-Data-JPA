package com.intexsoft.devi.controller;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.dto.StudentDTO;
import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.service.FileService;
import com.intexsoft.devi.service.StudentService;
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
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    FileService fileService;

    @Autowired
    DTOConverter dtoConverter;

    /**
     * method return all student
     *
     * @param locale of message
     * @return getAll student entities in the database.
     */
    @GetMapping
    public List<StudentDTO> getAll(Locale locale) {
        return studentService.getAll(locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * method return all student by group id
     *
     * @param id     of group
     * @param locale of message
     * @return all student by group id
     */
    @GetMapping("/getByGroupId/{id}")
    public List<StudentDTO> getStudentsOfGroupById(@PathVariable Long id, Locale locale) {
        return studentService.getStudentsOfGroupById(id, locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * method return student by id
     *
     * @param id of student
     * @return student entity by ID in the database.
     */
    @GetMapping("/{id}")
    public StudentDTO getById(@PathVariable Long id, Locale locale) {
        return convertToDto(studentService.getById(id, locale));
    }

    /**
     * method save student
     *
     * @param studentDTO entity
     * @param groupId    of Group
     * @return added student entity in the database.
     */
    @PostMapping
    public StudentDTO save(@RequestBody StudentDTO studentDTO, @RequestParam Long groupId, Locale locale) {
        return convertToDto(studentService.save(convertToEntity(studentDTO), groupId, locale));
    }

    /**
     * method update student by id
     *
     * @param studentDTO entity
     * @param studentId  of student
     * @param groupId    of group
     * @return updated student entity in the database.
     */
    @PutMapping("/{studentId}")
    public StudentDTO updateById(@RequestBody StudentDTO studentDTO, @PathVariable Long studentId, @RequestParam Long groupId, Locale locale) {
        return convertToDto(studentService.updateById(convertToEntity(studentDTO), studentId, groupId, locale));
    }

    /**
     * method delete student by id
     *
     * @param id     the student entity to be removed from the database
     * @param locale of message
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, Locale locale) {
        studentService.deleteById(id, locale);
    }

    /**
     * method save entities in database from excel file
     *
     * @param file   excel
     * @param locale of message
     * @return validation status
     * @throws IOException if an exception occurred in the file parsing
     */
    @PostMapping("/upload")
    public ValidationStatus addStudentsToGroup(@RequestParam(value = "file") MultipartFile file, Locale locale) throws IOException {
        return fileService.parse(locale, file.getInputStream(), file.getOriginalFilename().split("\\.")[1], studentService::validate, studentService::save);
    }


    private StudentDTO convertToDto(Student student) {
        return (StudentDTO) dtoConverter.convert(student, StudentDTO.class);
    }

    private Student convertToEntity(StudentDTO studentDTO) {
        return (Student) dtoConverter.convert(studentDTO, Student.class);
    }
}