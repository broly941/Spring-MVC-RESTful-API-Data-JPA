package com.intexsoft.devi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.intexsoft.devi.config.DataConfigTest;
import com.intexsoft.devi.entity.Student;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author DEVIAPHAN on 17.12.2018
 * Test for Controller Student Class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfigTest.class)
@WebAppConfiguration
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetups({
        @DatabaseSetup("/xml/teachers.xml"),
        @DatabaseSetup("/xml/groups.xml"),
        @DatabaseSetup("/xml/students.xml")
})
public class StudentControllerIntegrationTest {

    private static final String ACCEPT_LANGUAGE = "Accept-language";
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    private static final String EN = "en";

    private static final String STUDENTS = "/students";
    private static final String STUDENTS_ID = "/students/{id}";

    private static final String $_ID = "$.id";

    private static final String $_FIRST_NAME = "$.firstName";
    private static final String $_LAST_NAME = "$.lastName";
    private static final String GROUP_ID = "groupId";
    private static final String $_1_ID = "$[1].id";
    private static final String $_1_FIRST_NAME = "$[1].firstName";
    private static final String $_1_LAST_NAME = "$[1].lastName";
    private static final String THOMAS = "Thomas";
    private static final String MOORE = "Moore";
    private static final String LEE = "Lee";
    private static final String WALKER = "Walker";
    private static final String TONY = "Tony";
    private static final String HAWK = "Hawk";
    private static final String DONALD = "Donald";
    private static final String TRUMP = "Trump";
    private static final String STUDENTS_UPLOAD = "/students/upload";
    private static final String CREATE_STUDENT = "{\"rowCount\":3,\"validRow\":3,\"errorsCount\":0,\"errors\":[]}";
    private static final String FILE = "addStudentToGroup.xlsx";
    private static final String FILE_WORD = "file";
    private static final String FILE_XLSX = "file/xlsx";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * addGroupsToTeacher_Failed mockMvc
     */
    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Will return an all records from tested db
     *
     * @throws Exception in json parse or MockMvc.perform
     */
    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get(STUDENTS)
                .header(ACCEPT_LANGUAGE, EN)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_1_ID, is(2)))
                .andExpect(jsonPath($_1_FIRST_NAME, is(THOMAS)))
                .andExpect(jsonPath($_1_LAST_NAME, is(MOORE)));
    }

    /**
     * Will return a record by ID from tested db
     *
     * @throws Exception in json parse or MockMvc.perform
     */
    @Test
    public void getById() throws Exception {
        mockMvc.perform(get(STUDENTS_ID, 1)
                .header(ACCEPT_LANGUAGE, EN)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_ID, is(1)))
                .andExpect(jsonPath($_FIRST_NAME, is(LEE)))
                .andExpect(jsonPath($_LAST_NAME, is(WALKER)));
    }

    /**
     * Will save a record in tested db
     *
     * @throws Exception in json parse or MockMvc.perform
     */
    @Test
    public void save() throws Exception {
        Student student = new Student();
        student.setFirstName(TONY);
        student.setLastName(HAWK);

        mockMvc.perform(post(STUDENTS)
                .header(ACCEPT_LANGUAGE, EN)
                .param(GROUP_ID, "1")
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(json(student))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_ID, is(7)))
                .andExpect(jsonPath($_FIRST_NAME, is(TONY)))
                .andExpect(jsonPath($_LAST_NAME, is(HAWK)));
    }

    /**
     * Will update a record by ID from tested db
     *
     * @throws Exception in json parse or MockMvc.perform
     */
    @Test
    public void update() throws Exception {
        Student student = new Student();
        student.setFirstName(DONALD);
        student.setLastName(TRUMP);

        mockMvc.perform(put(STUDENTS_ID, 2)
                .header(ACCEPT_LANGUAGE, EN)
                .param(GROUP_ID, "2")
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(json(student))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath($_ID, is(2)))
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_FIRST_NAME, is(DONALD)))
                .andExpect(jsonPath($_LAST_NAME, is(TRUMP)));
    }

    /**
     * Will delete a record by ID from tested db
     *
     * @throws Exception in json parse or MockMvc.perform
     */
    @Test
    public void remove() throws Exception {
        mockMvc.perform(delete(STUDENTS_ID, 3)
                .header(ACCEPT_LANGUAGE, EN)
        )
                .andExpect(status().isOk());
    }

    /**
     * Will save a new entity and return validation status
     *
     * @throws Exception in json parse or MockMvc.perform
     */
    @Test
    public void addStudentToGroup_Failed() throws Exception {
        MockMultipartFile file = getMultipartFile();
        mockMvc.perform(multipart(STUDENTS_UPLOAD)
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(CREATE_STUDENT));
    }

    private MockMultipartFile getMultipartFile() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(FILE)).getFile());
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(FILE_WORD,
                file.getName(), FILE_XLSX, IOUtils.toByteArray(input));
    }

    private String json(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}