package com.intexsoft.devi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.intexsoft.devi.config.WebConfig;
import com.intexsoft.devi.entity.Student;
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
 * @author DEVIAPHAN on 17.12.2018
 * @project SpringRESTDataJPA
 * Test for Controller Student Class
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
public class StudentControllerIntegrationTest {
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
        mockMvc.perform(get("/university/students")
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Михаил")))
                .andExpect(jsonPath("$[0].lastName", is("Черняк")));
    }

    /**
     * Will return a record by ID from tested db
     * @throws Exception
     */
    @Test
    @DatabaseSetup("Teacher.xml")
    public void getById() throws Exception {
        mockMvc.perform(get("/university/students/{id}", 1)
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Михаил")))
                .andExpect(jsonPath("$.lastName", is("Черняк")));
        //                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.firstName", is("Jackie")))
//                .andExpect(jsonPath("$.lastName", is("Chan")));
    }

    /**
     * Will save a record in tested db
     * @throws Exception
     */
    @Test
    public void save() throws Exception {
        Student studentMax = new Student();
        studentMax.setFirstName("Mad");
        studentMax.setLastName("Max");

        mockMvc.perform(post("/university/students/")
                .header("Accept-language", "en")
                .param("groupId", "1")
                .contentType("application/json;charset=UTF-8")
                .content(json(studentMax))
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
        Student studentMax = new Student();
        studentMax.setFirstName("Mad");
        studentMax.setLastName("Max2");

        mockMvc.perform(put("/university/students/{id}", 5)
                .header("Accept-language", "en")
                .param("groupId", "2")
                .contentType("application/json;charset=UTF-8")
                .content(json(studentMax))
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
        mockMvc.perform(delete("/university/students/{id}", 5)
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk());
    }

    private String json(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}