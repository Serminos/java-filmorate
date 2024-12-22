package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase
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

    UserDto testUser;

    @BeforeEach
    void beforeEachTest() {
        testUser = UserDto.builder()
                .id(1L)
                .email("norm@email.ru")
                .name("name")
                .login("login")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        userService.clear();
    }

    private UserDto getNewUserDto() {
        String randomValue = UUID.randomUUID().toString();
        testUser = UserDto.builder()
                .id(1L)
                .email(randomValue + "@email.ru")
                .name("name")
                .login(randomValue + "login")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        return testUser;
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
        MvcResult userResult1 = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("norm@email.ru"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.birthday").value(LocalDate.now().minusYears(20).toString()))
                .andReturn();
        String jsonUser = userResult1.getResponse().getContentAsString();
        UserDto userDto = objectMapper.readValue(jsonUser, UserDto.class);
        userDto.setLogin("NewLogin");

        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(userDto))
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

    @Test
    void addUserFriend() throws Exception {
        MvcResult userResult1 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser = userResult1.getResponse().getContentAsString();
        UserDto userDto1 = objectMapper.readValue(jsonUser, UserDto.class);
        UserDto friend = getNewUserDto();
        MvcResult userResult2 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(friend))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser2 = userResult2.getResponse().getContentAsString();
        UserDto userDto2 = objectMapper.readValue(jsonUser2, UserDto.class);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", userDto1.getId(), userDto2.getId())
                )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void addUserFriend_GenerateExceptionWhenUserOrFriendNotExists() throws Exception {
        MvcResult userResult1 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser = userResult1.getResponse().getContentAsString();
        UserDto userDto1 = objectMapper.readValue(jsonUser, UserDto.class);
        UserDto friend = getNewUserDto();
        MvcResult userResult2 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(friend))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser2 = userResult2.getResponse().getContentAsString();
        UserDto userDto2 = objectMapper.readValue(jsonUser2, UserDto.class);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", userDto1.getId() + 9999,
                        userDto2.getId())
                )
                .andExpect(status().is4xxClientError());
        mockMvc.perform(put("/users/{id}/friends/{friendId}", userDto1.getId(),
                        userDto2.getId() + 9999)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteUserFriend() throws Exception {
        MvcResult userResult1 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser = userResult1.getResponse().getContentAsString();
        UserDto userDto1 = objectMapper.readValue(jsonUser, UserDto.class);
        UserDto friend = getNewUserDto();
        MvcResult userResult2 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(friend))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser2 = userResult2.getResponse().getContentAsString();
        UserDto userDto2 = objectMapper.readValue(jsonUser2, UserDto.class);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", userDto1.getId(), userDto2.getId())
                )
                .andExpect(status().isOk());
        mockMvc.perform(delete("/users/{id}/friends/{friendId}", userDto1.getId(), userDto2.getId())
                )
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserFriend_GenerateExceptionWhenUserOrFriendNotExists() throws Exception {
        MvcResult userResult1 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser = userResult1.getResponse().getContentAsString();
        UserDto userDto1 = objectMapper.readValue(jsonUser, UserDto.class);
        UserDto friend = getNewUserDto();
        MvcResult userResult2 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(friend))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser2 = userResult2.getResponse().getContentAsString();
        UserDto userDto2 = objectMapper.readValue(jsonUser2, UserDto.class);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", userDto1.getId(), userDto2.getId())
                )
                .andExpect(status().isOk());
        mockMvc.perform(delete("/users/{id}/friends/{friendId}",
                        userDto1.getId() + 9999, userDto2.getId())
                )
                .andExpect(status().is4xxClientError());
        mockMvc.perform(delete("/users/{id}/friends/{friendId}",
                        userDto1.getId(), userDto2.getId() + 9999)
                )
                .andExpect(status().is4xxClientError());
    }


    @Test
    void getUserFriends() throws Exception {
        MvcResult userResult1 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser = userResult1.getResponse().getContentAsString();
        UserDto userDto1 = objectMapper.readValue(jsonUser, UserDto.class);
        UserDto friend1 = getNewUserDto();
        MvcResult userResult2 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(friend1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser2 = userResult2.getResponse().getContentAsString();
        UserDto friend = objectMapper.readValue(jsonUser2, UserDto.class);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", userDto1.getId(), friend.getId())
                )
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/users/{id}/friends", userDto1.getId())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].id").value(friend.getId()));
    }

    @Test
    void commonFriends() throws Exception {
        MvcResult userResult1 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser = userResult1.getResponse().getContentAsString();
        UserDto userDto1 = objectMapper.readValue(jsonUser, UserDto.class);
        UserDto friend1 = getNewUserDto();
        MvcResult userResult2 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(friend1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser2 = userResult2.getResponse().getContentAsString();
        UserDto userDto2 = objectMapper.readValue(jsonUser2, UserDto.class);
        UserDto friend2 = getNewUserDto();
        MvcResult userResult3 = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(friend2))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String jsonUser3 = userResult3.getResponse().getContentAsString();
        UserDto commonFriend = objectMapper.readValue(jsonUser3, UserDto.class);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", userDto1.getId(), commonFriend.getId())
                )
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(put("/users/{id}/friends/{friendId}", userDto2.getId(), commonFriend.getId())
                )
                .andExpect(status().is2xxSuccessful());


        mockMvc.perform(get("/users/{id}/friends/common/{friendId}", userDto1.getId(), userDto2.getId())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(commonFriend.getId()));
    }
}