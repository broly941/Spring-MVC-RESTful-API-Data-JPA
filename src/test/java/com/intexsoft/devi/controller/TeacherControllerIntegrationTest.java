package com.intexsoft.devi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.intexsoft.devi.config.WebConfig;
import com.intexsoft.devi.entity.Teacher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
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
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DatabaseSetup("Teacher-create-expected.xml")
public class TeacherControllerIntegrationTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Will return an all records from tested db
     * @throws Exception
     */
    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/university/teachers")
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Иван")))
                .andExpect(jsonPath("$[0].lastName", is("Васильков")));
    }

    /**
     * Will return a record by ID from tested db
     * @throws Exception
     */
    @Test
    public void getById() throws Exception {
        mockMvc.perform(get("/university/teachers/{id}", 1)
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Иван")))
                .andExpect(jsonPath("$.lastName", is("Васильков")));
    }

    /**
     * Will save a record in tested db
     * @throws Exception
     */
    @Test
    public void save() throws Exception {
        Teacher teacherMax = new Teacher();
        teacherMax.setFirstName("Mad");
        teacherMax.setLastName("Max");

        mockMvc.perform(post("/university/teachers")
                .header("Accept-language", "en")
                .contentType("application/json;charset=UTF-8")
                .content(json(teacherMax))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.firstName", is("Mad")))
                .andExpect(jsonPath("$.lastName", is("Max")));
    }

    /**
     * Will update a record by ID from tested db
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        Teacher teacherMax = new Teacher();
        teacherMax.setFirstName("Mad");
        teacherMax.setLastName("Max2");

        mockMvc.perform(put("/university/teachers/{id}", 12)
                .header("Accept-language", "en")
                .contentType("application/json;charset=UTF-8")
                .content(json(teacherMax))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.firstName", is("Mad")))
                .andExpect(jsonPath("$.lastName", is("Max2")));
    }

    /**
     * Will delete a record by ID from tested db
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        mockMvc.perform(delete("/university/teachers/{id}", 13)
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk());
    }

    private String json(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

}