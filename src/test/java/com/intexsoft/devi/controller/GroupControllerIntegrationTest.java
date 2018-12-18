package com.intexsoft.devi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.intexsoft.devi.config.DataConfigTest;
import com.intexsoft.devi.entity.Group;
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
 * @project SpringRESTDataJPA
 * Test for Controller Group Class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfigTest.class)
@WebAppConfiguration
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetups({
        @DatabaseSetup("/xml/teachers.xml"),
        @DatabaseSetup("/xml/groups.xml")
})
public class GroupControllerIntegrationTest {

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
     * @throws Exception
     */
    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/university/groups")
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].number", is("ПОИТ-21")));
    }

    /**
     * Will return a record by ID from tested db
     * @throws Exception
     */
    @Test
    public void getById() throws Exception {
        mockMvc.perform(get("/university/groups/{id}", 1)
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.number", is("ПОИТ-161")));
    }

    /**
     * Will save a record in tested db
     * @throws Exception
     */
    @Test
    public void save() throws Exception {
        Group group = new Group();
        group.setNumber("ЛОМП-212");

        mockMvc.perform(post("/university/groups")
                .header("Accept-language", "en")
                .param("curatorId", "3")
                .param("teacherIdList", "3")
                .contentType("application/json;charset=UTF-8")
                .content(json(group))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.number", is("ЛОМП-212")));
    }

    /**
     * Will update a record by ID from tested db
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        Group group = new Group();
        group.setNumber("ПОИТ-23");

        mockMvc.perform(put("/university/groups/{id}", 2)
                .header("Accept-language", "en")
                .param("curatorId", "2")
                .param("teacherIdList", "3")
                .contentType("application/json;charset=UTF-8")
                .content(json(group))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.number", is("ПОИТ-23")));
    }

    /**
     * Will delete a record by ID from tested db
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        mockMvc.perform(delete("/university/groups/{id}", 1)
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk());
    }

    private String json(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}