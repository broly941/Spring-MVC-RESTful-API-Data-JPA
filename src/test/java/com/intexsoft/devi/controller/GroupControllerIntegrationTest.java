package com.intexsoft.devi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.intexsoft.devi.config.WebConfig;
import com.intexsoft.devi.entity.Group;
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
 * Test for Controller Group Class
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
@DatabaseSetup(".xml")
public class GroupControllerIntegrationTest {
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
        mockMvc.perform(get("/university/groups")
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].number", is("ПОИТ-162")));
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
                .andExpect(jsonPath("$.number", is("ПОИТ-162")));
    }

    /**
     * Will save a record in tested db
     * @throws Exception
     */
    @Test
    public void save() throws Exception {
        Group groupMax = new Group();
        groupMax.setNumber("MadMax");

        mockMvc.perform(post("/university/groups")
                .header("Accept-language", "en")
                .param("curatorId", "3")
                .param("teacherIdList", "3")
                .contentType("application/json;charset=UTF-8")
                .content(json(groupMax))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.number", is("MadMax")));
    }

    /**
     * Will update a record by ID from tested db
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        Group groupMax = new Group();
        groupMax.setNumber("MadMax2");

        mockMvc.perform(put("/university/groups/{id}", 7)
                .header("Accept-language", "en")
                .param("curatorId", "1")
                .param("teacherIdList", "3")
                .contentType("application/json;charset=UTF-8")
                .content(json(groupMax))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.number", is("MadMax2")));
    }

    /**
     * Will delete a record by ID from tested db
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        mockMvc.perform(delete("/university/groups/{id}", 7)
                .header("Accept-language", "en")
        )
                .andExpect(status().isOk());
    }

    private String json(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}