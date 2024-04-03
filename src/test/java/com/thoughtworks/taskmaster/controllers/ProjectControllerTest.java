package com.thoughtworks.taskmaster.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.taskmaster.models.Project;
import com.thoughtworks.taskmaster.models.Token;
import com.thoughtworks.taskmaster.repositories.ProjectRepository;
import com.thoughtworks.taskmaster.repositories.TokenRepository;
import com.thoughtworks.taskmaster.repositories.UserRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Order(1)
    @WithMockUser("thoughtworks")
    @Test
    public void testCreateProject() throws Exception {
        String firstEmail = userRepository.findAll().get(0).getEmail();
        String jwt = "Bearer "+tokenRepository.findByUser_Email(firstEmail).getToken();
        Project project = new Project();

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/projects")
                        .header(HttpHeaders.AUTHORIZATION, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @WithMockUser("thoughtworks")
    @Test
    public void testGetAllProject() throws Exception {
        String firstEmail = userRepository.findAll().get(0).getEmail();
        String jwt = "Bearer "+tokenRepository.findByUser_Email(firstEmail).getToken();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/projects")
                        .header(HttpHeaders.AUTHORIZATION, jwt))
                .andExpect(status().isOk()).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @WithMockUser("thoughtworks")
    @Test
    public void testGetProjectById() throws Exception {
        String firstEmail = userRepository.findAll().get(0).getEmail();
        String jwt = "Bearer "+tokenRepository.findByUser_Email(firstEmail).getToken();
        Token token = tokenRepository.findByUser_Email(firstEmail);
        long id = token.getUser().getId();
        long projectId = projectRepository.findAllByUserId(id).get().get(0).getId();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/projects/"+projectId)
                        .header(HttpHeaders.AUTHORIZATION, jwt))
                .andExpect(status().isOk()).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @WithMockUser("thoughtworks")
    @Test
    public void testUpdateProject() throws Exception {
        String firstEmail = userRepository.findAll().get(0).getEmail();
        Token token = tokenRepository.findByUser_Email(firstEmail);
        String jwt = "Bearer "+token.getToken();
        long id = token.getUser().getId();
        long projectsId = projectRepository.findAllByUserId(id).get().get(0).getId();

        Project project = new Project();

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/projects/"+projectsId)
                        .header(HttpHeaders.AUTHORIZATION, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @WithMockUser("thoughtworks")
    @Test
    public void testDeleteProject() throws Exception {
        String firstEmail = userRepository.findAll().get(0).getEmail();
        Token token = tokenRepository.findByUser_Email(firstEmail);
        String jwt = "Bearer "+token.getToken();
        long id = token.getUser().getId();
        long projectId = projectRepository.findAllByUserId(id).get().get(0).getId();

        Project project = new Project();

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/projects/"+projectId)
                        .header(HttpHeaders.AUTHORIZATION, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

}
