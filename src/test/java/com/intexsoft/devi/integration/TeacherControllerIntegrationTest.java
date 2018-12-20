package com.intexsoft.devi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.intexsoft.devi.config.DataConfigTest;
import com.intexsoft.devi.entity.Teacher;
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
 * @author DEVIAPHAN
 * Test for Controller Teacher Class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfigTest.class)
@WebAppConfiguration
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/xml/teachers.xml")
public class TeacherControllerIntegrationTest {

    private static final String ACCEPT_LANGUAGE = "Accept-language";
    private static final String EN = "en";
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";

    private static final String TEACHERS_ID = "/teachers/{id}";
    private static final String TEACHERS = "/teachers";
    private static final String $_ID = "$.id";
    private static final String $_FIRST_NAME = "$.firstName";
    private static final String $_LAST_NAME = "$.lastName";

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
        mockMvc.perform(get(TEACHERS)
                .header(ACCEPT_LANGUAGE, EN)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].firstName", is("Rob")))
                .andExpect(jsonPath("$[2].lastName", is("Stark")));
    }

    /**
     * Will return a record by ID from tested db
     *
     * @throws Exception
     */
    @Test
    public void getById() throws Exception {
        mockMvc.perform(get(TEACHERS_ID, 3)
                .header(ACCEPT_LANGUAGE, EN)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_ID, is(3)))
                .andExpect(jsonPath($_FIRST_NAME, is("Rob")))
                .andExpect(jsonPath($_LAST_NAME, is("Stark")));
    }

    /**
     * Will save a record in tested db
     *
     * @throws Exception
     */
    @Test
    public void save() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Yip");
        teacher.setLastName("Man");

        mockMvc.perform(post(TEACHERS)
                .header(ACCEPT_LANGUAGE, EN)
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(json(teacher))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_ID, is(4)))
                .andExpect(jsonPath($_FIRST_NAME, is("Yip")))
                .andExpect(jsonPath($_LAST_NAME, is("Man")));
    }

    /**
     * Will update a record by ID from tested db
     *
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Ilya");
        teacher.setLastName("Korzhavin");

        mockMvc.perform(put(TEACHERS_ID, 2)
                .header(ACCEPT_LANGUAGE, EN)
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(json(teacher))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_ID, is(2)))
                .andExpect(jsonPath($_FIRST_NAME, is("Ilya")))
                .andExpect(jsonPath($_LAST_NAME, is("Korzhavin")));
    }

    /**
     * Will delete a record by ID from tested db
     *
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        mockMvc.perform(delete(TEACHERS_ID, 3)
                .header(ACCEPT_LANGUAGE, EN)
        )
                .andExpect(status().isOk());
    }

    private String json(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}