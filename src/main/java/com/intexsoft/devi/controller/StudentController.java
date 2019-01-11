package com.intexsoft.devi.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.intexsoft.devi.beans.ValidationStatus;
import com.intexsoft.devi.dto.StudentDTO;
import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.service.ExcelFileService;
import com.intexsoft.devi.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    @Autowired
    ExcelFileService excelFileService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * @return getAll student entities in the database.
     */
    @GetMapping
    public List<StudentDTO> getAll(Locale locale) {
        return studentService.getAll(locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * @param id
     * @param locale
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/getByGroupId/{id}")
    public List<StudentDTO> getStudentsOfGroupById(@PathVariable Long id, Locale locale) throws EntityNotFoundException {
        return studentService.getStudentsOfGroupById(id, locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * @param id of student
     * @return student entity by ID in the database.
     */
    @GetMapping("/{id}")
    public StudentDTO getById(@PathVariable Long id, Locale locale) throws EntityNotFoundException {
        return convertToDto(studentService.getById(id, locale));
    }

    /**
     * @param studentDTO entity
     * @param groupId    of Group
     * @return added student entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @PostMapping
    public StudentDTO save(@RequestBody StudentDTO studentDTO, @RequestParam Long groupId, Locale locale) throws EntityNotFoundException {
        return convertToDto(studentService.save(convertToEntity(studentDTO), groupId, locale));
    }

    /**
     * @param studentDTO entity
     * @param studentId  of student
     * @param groupId    of group
     * @return updated student entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @PutMapping("/{studentId}")
    public StudentDTO updateById(@RequestBody StudentDTO studentDTO, @PathVariable Long studentId, @RequestParam Long groupId, Locale locale) throws EntityNotFoundException {
        return convertToDto(studentService.updateById(convertToEntity(studentDTO), studentId, groupId, locale));
    }

    /**
     * @param id the student entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, Locale locale) {
        studentService.deleteById(id, locale);
    }

    /**
     * @param file
     * @param locale
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @PostMapping("/fileload")
    public ResponseEntity<ValidationStatus> createStudent(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam Integer page, Locale locale) throws IOException, InvalidFormatException {
        return new ResponseEntity<>(excelFileService.createEntity(locale, file, page, studentService::fileValidation, studentService::fileSave), HttpStatus.OK);
    }

    private StudentDTO convertToDto(Student student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    private Student convertToEntity(StudentDTO studentDTO) {
        return modelMapper.map(studentDTO, Student.class);
    }
}