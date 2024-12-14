package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmorateApplication.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    User testUser;

    @BeforeEach
    void beforeEachTest() {
        testUser = User.builder()
                .id(1)
                .email("norm@email.ru")
                .name("name")
                .login("login")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        userService.clear();
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("norm@email.ru"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.birthday").value(LocalDate.now().minusYears(20).toString()));
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("norm@email.ru"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.birthday").value(LocalDate.now().minusYears(20).toString()));

        testUser.setLogin("NewLogin");

        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("norm@email.ru"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.login").value("NewLogin"))
                .andExpect(jsonPath("$.birthday").value(LocalDate.now().minusYears(20).toString()));
    }

    @Test
    void all() throws Exception {
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].email").value("norm@email.ru"))
                .andExpect(jsonPath("$.[0].name").value("name"))
                .andExpect(jsonPath("$.[0].login").value("login"))
                .andExpect(jsonPath("$.[0].birthday").value(LocalDate.now().minusYears(20).toString()));
    }

    @Test
    void create_shouldGenerateExceptionWhenWrongOrNullEmail() throws Exception {
        testUser.setEmail("wrong_emailru");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());

        testUser.setEmail(null);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create_shouldGenerateExceptionWhenWhiteSpaceInLogin() throws Exception {
        testUser.setLogin(" ");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());

        testUser.setLogin("wrong Login");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create_shouldGenerateExceptionWhenEmptyLogin() throws Exception {
        testUser.setLogin("");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create_shouldGenerateExceptionWhenNullLogin() throws Exception {
        testUser.setLogin(null);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create_shouldGenerateExceptionWhenBirthdayInFuture() throws Exception {
        testUser.setBirthday(LocalDate.now().plusMonths(20));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create_shouldReturnLoginInsteadofNameWhenNameIsNull() throws Exception {
        testUser.setName(null);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value(testUser.getLogin()));
    }

    @Test
    void get_shouldReturnLoginInsteadofNameWhenNameIsNull() throws Exception {
        testUser.setName(null);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value(testUser.getLogin()));

        mockMvc.perform(get("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.[0].name").value(testUser.getLogin()))
                .andExpect(jsonPath("$.[0].login").value(testUser.getLogin()))
                .andExpect(jsonPath("$.[0].birthday").value(LocalDate.now().minusYears(20).toString()));

    }
}