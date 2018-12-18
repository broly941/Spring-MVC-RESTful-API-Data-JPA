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
        mockMvc.perform(get("/students")
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
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
        mockMvc.perform(get("/students/{id}", 1)
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Lee")))
                .andExpect(jsonPath("$.lastName", is("Walker")));
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

        mockMvc.perform(post("/students/")
                .header("Accept-language", "en")
                .param("groupId", "1")
                .contentType("application/json;charset=UTF-8")
                .content(json(student))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.firstName", is("Tony")))
                .andExpect(jsonPath("$.lastName", is("Hawk")));
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

        mockMvc.perform(put("/students/{id}", 2)
                .header("Accept-language", "en")
                .param("groupId", "2")
                .contentType("application/json;charset=UTF-8")
                .content(json(student))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.firstName", is("Donald")))
                .andExpect(jsonPath("$.lastName", is("Trump")));
    }

    /**
     * Will delete a record by ID from tested db
     *
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        mockMvc.perform(delete("/students/{id}", 3)
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk());
    }

    private String json(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}