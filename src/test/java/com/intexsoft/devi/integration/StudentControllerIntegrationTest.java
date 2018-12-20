package com.intexsoft.devi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.intexsoft.devi.config.DataConfigTest;
import com.intexsoft.devi.entity.Student;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * initialization mockMvc
     */
    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Will return an all records from tested db
     *
     * @throws Exception
     */
    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get(STUDENTS)
                .header(ACCEPT_LANGUAGE, EN)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Thomas")))
                .andExpect(jsonPath("$[1].lastName", is("Moore")));
    }

    /**
     * Will return a record by ID from tested db
     *
     * @throws Exception
     */
    @Test
    public void getById() throws Exception {
        mockMvc.perform(get(STUDENTS_ID, 1)
                .header(ACCEPT_LANGUAGE, EN)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_ID, is(1)))
                .andExpect(jsonPath($_FIRST_NAME, is("Lee")))
                .andExpect(jsonPath($_LAST_NAME, is("Walker")));
    }

    /**
     * Will save a record in tested db
     *
     * @throws Exception
     */
    @Test
    public void save() throws Exception {
        Student student = new Student();
        student.setFirstName("Tony");
        student.setLastName("Hawk");

        mockMvc.perform(post(STUDENTS)
                .header(ACCEPT_LANGUAGE, EN)
                .param(GROUP_ID, "1")
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(json(student))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_ID, is(4)))
                .andExpect(jsonPath($_FIRST_NAME, is("Tony")))
                .andExpect(jsonPath($_LAST_NAME, is("Hawk")));
    }

    /**
     * Will update a record by ID from tested db
     *
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        Student student = new Student();
        student.setFirstName("Donald");
        student.setLastName("Trump");

        mockMvc.perform(put(STUDENTS_ID, 2)
                .header(ACCEPT_LANGUAGE, EN)
                .param(GROUP_ID, "2")
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(json(student))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath($_ID, is(2)))
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_FIRST_NAME, is("Donald")))
                .andExpect(jsonPath($_LAST_NAME, is("Trump")));
    }

    /**
     * Will delete a record by ID from tested db
     *
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        mockMvc.perform(delete(STUDENTS_ID, 3)
                .header(ACCEPT_LANGUAGE, EN)
        )
                .andExpect(status().isOk());
    }

    private String json(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}