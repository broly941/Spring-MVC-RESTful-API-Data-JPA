package com.intexsoft.devi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.intexsoft.devi.config.DataConfigTest;
import com.intexsoft.devi.entity.Teacher;
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
 * @author DEVIAPHAN
 * Test for Controller Teacher Class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfigTest.class)
@WebAppConfiguration
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup({"/xml/teachers.xml"})

@DatabaseSetups({
        @DatabaseSetup("/xml/teachers.xml"),
        @DatabaseSetup("/xml/groups.xml"),
        @DatabaseSetup("/xml/groupteacher.xml")
})
public class TeacherControllerIntegrationTest {

    private static final String ACCEPT_LANGUAGE = "Accept-language";
    private static final String EN = "en";
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";

    private static final String TEACHERS_ID = "/teachers/{id}";
    private static final String TEACHERS = "/teachers";
    private static final String $_ID = "$.id";
    private static final String $_FIRST_NAME = "$.firstName";
    private static final String $_LAST_NAME = "$.lastName";
    private static final String $_2_ID = "$[2].id";
    private static final String $_2_FIRST_NAME = "$[2].firstName";
    private static final String $_2_LAST_NAME = "$[2].lastName";
    private static final String ROB = "Rob";
    private static final String STARK = "Stark";
    private static final String YIP = "Yip";
    private static final String MAN = "Man";
    private static final String ILYA = "Ilya";
    private static final String KORZHAVIN = "Korzhavin";

    private static final String DATA_SAVED_SUCCESSFULLY = "Data saved successfully.";
    private static final String VALIDATION_STATUS = "..::: validation status :::..\n";
    private static final String PAGE = "page";
    private static final String ROW_1_TEACHER_DOES_NOT_EXIST = "Row 1: teacher does not exist.\n";
    private static final String ROW_2_SOME_TYPE_IS_NOT_A_STRING = "Row 2: some type is not a string.\n";
    private static final String ROW_3_AT_LEAST_3_COLUMNS_REQUIRED = "Row 3: at least 3 columns required.\n";
    private static final String TEACHERS_FILELOAD = "/teachers/fileload";
    private static final String FILE = "file";
    private static final String FILE_XLSX = "file/xlsx";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * addGroupsToTeacher mockMvc
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
                .andExpect(jsonPath($_2_ID, is(3)))
                .andExpect(jsonPath($_2_FIRST_NAME, is(ROB)))
                .andExpect(jsonPath($_2_LAST_NAME, is(STARK)));
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
                .andExpect(jsonPath($_FIRST_NAME, is(ROB)))
                .andExpect(jsonPath($_LAST_NAME, is(STARK)));
    }

    /**
     * Will save a record in tested db
     *
     * @throws Exception
     */
    @Test
    public void save() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName(YIP);
        teacher.setLastName(MAN);

        mockMvc.perform(post(TEACHERS)
                .header(ACCEPT_LANGUAGE, EN)
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(json(teacher))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_ID, is(4)))
                .andExpect(jsonPath($_FIRST_NAME, is(YIP)))
                .andExpect(jsonPath($_LAST_NAME, is(MAN)));
    }

    /**
     * Will update a record by ID from tested db
     *
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName(ILYA);
        teacher.setLastName(KORZHAVIN);

        mockMvc.perform(put(TEACHERS_ID, 2)
                .header(ACCEPT_LANGUAGE, EN)
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(json(teacher))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath($_ID, is(2)))
                .andExpect(jsonPath($_FIRST_NAME, is(ILYA)))
                .andExpect(jsonPath($_LAST_NAME, is(KORZHAVIN)));
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

    @Test
    public void addGroupsToTeacher() throws Exception {
        StringBuilder validationStatus = new StringBuilder(VALIDATION_STATUS);
        validationStatus.append(DATA_SAVED_SUCCESSFULLY);

        MockMultipartFile file = getMultipartFile();
        mockMvc.perform(multipart(TEACHERS_FILELOAD)
                .file(file)
                .param(PAGE, "3")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(validationStatus.toString()));
    }

    @Test
    public void addGroupsToTeacher_Fall() throws Exception {
        StringBuilder validationStatus = new StringBuilder(VALIDATION_STATUS);
        validationStatus.append(ROW_1_TEACHER_DOES_NOT_EXIST);
        validationStatus.append(ROW_2_SOME_TYPE_IS_NOT_A_STRING);
        validationStatus.append(ROW_3_AT_LEAST_3_COLUMNS_REQUIRED);

        MockMultipartFile file = getMultipartFile();
        mockMvc.perform(multipart(TEACHERS_FILELOAD)
                .file(file)
                .param(PAGE, "2")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(validationStatus.toString()));
    }

    private MockMultipartFile getMultipartFile() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("IntegrationTest.xlsx")).getFile());
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile("file",
                file.getName(), "file/xlsx", IOUtils.toByteArray(input));
    }

    private String json(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}