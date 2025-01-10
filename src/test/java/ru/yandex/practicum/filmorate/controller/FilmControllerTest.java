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
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmorateApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    FilmController filmController;
    @Autowired
    private UserController userController;
    @Autowired
    private FilmService filmService;
    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    FilmDto testFilm;
    UserDto testUser;

    @BeforeEach
    void beforeEachTest() {
        String randomValue = UUID.randomUUID().toString();
        testFilm = FilmDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.now().minusYears(1))
                .duration(120L)
                .mpa(new MpaDto(1L, "test", "test"))
                .build();
        testUser = UserDto.builder()
                .id(1L)
                .email(randomValue + "@email.ru")
                .name("name")
                .login(randomValue + "login")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        filmService.clear();
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
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(testFilm.getName()))
                .andExpect(jsonPath("$.description").value(testFilm.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(testFilm.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(testFilm.getDuration()));
    }

    @Test
    void update() throws Exception {
        MvcResult filmResult = mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(testFilm.getName()))
                .andExpect(jsonPath("$.description").value(testFilm.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(testFilm.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(testFilm.getDuration()))
                .andReturn();
        String jsonFilm = filmResult.getResponse().getContentAsString();
        FilmDto filmDto = objectMapper.readValue(jsonFilm, FilmDto.class);
        filmDto.setName("new Name");

        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(filmDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("new Name"))
                .andExpect(jsonPath("$.description").value(testFilm.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(testFilm.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(testFilm.getDuration()));
    }

    @Test
    void all() throws Exception {
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(testFilm))
                .contentType(MediaType.APPLICATION_JSON)
        );

        mockMvc.perform(get("/films"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].name").value(testFilm.getName()))
                .andExpect(jsonPath("$.[0].description").value(testFilm.getDescription()))
                .andExpect(jsonPath("$.[0].releaseDate").value(testFilm.getReleaseDate().toString()))
                .andExpect(jsonPath("$.[0].duration").value(testFilm.getDuration()));
    }

    @Test
    void create_shouldGenerateExceptionNameEmptyOrNull() throws Exception {
        testFilm.setName(" ");
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());

        testFilm.setName(null);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create_shouldGenerateExceptionWhenDescriptionLengthMoreThen200() throws Exception {
        StringBuilder description = new StringBuilder();
        for (int i = 0; i < 21; i++) {
            description.append("0123456789");
        }
        testFilm.setDescription(description.toString());
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create_shouldGenerateExceptionWhenReleaseDateIsEarly1895_12_28() throws Exception {
        testFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create_shouldNotGenerateExceptionWhenReleaseDateIs1895_12_28() throws Exception {
        testFilm.setReleaseDate(LocalDate.of(1895, 12, 28));
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void create_shouldGenerateExceptionWhenDurationNegativeOrZero() throws Exception {
        testFilm.setDuration(-1L);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());

        testFilm.setDuration(0L);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addLike() throws Exception {
        MvcResult userResult = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(getNewUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();
        String jsonUser = userResult.getResponse().getContentAsString();
        UserDto userDto = objectMapper.readValue(jsonUser, UserDto.class);

        MvcResult filmResult = mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();
        String jsonFilm = filmResult.getResponse().getContentAsString();
        FilmDto filmDto = objectMapper.readValue(jsonFilm, FilmDto.class);

        mockMvc.perform(put("/films/{id}/like/{userId}", filmDto.getId(), userDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void addLike_shouldGenerateExceptionWhenFilmOrUserNotFound() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(put("/films/{id}/like/{userId}",
                        testFilm.getId(), testUser.getId() + 9999))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(put("/films/{id}/like/{userId}",
                        testFilm.getId() + 9999, testUser.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteLike() throws Exception {
        MvcResult userResult = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();
        String jsonUser = userResult.getResponse().getContentAsString();
        UserDto userDto = objectMapper.readValue(jsonUser, UserDto.class);

        MvcResult filmResult = mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();
        String jsonFilm = filmResult.getResponse().getContentAsString();
        FilmDto filmDto = objectMapper.readValue(jsonFilm, FilmDto.class);
        mockMvc.perform(put("/films/{id}/like/{userId}", filmDto.getId(), userDto.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/films/{id}/like/{userId}", filmDto.getId(), userDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLike_shouldGenerateExceptionWhenFilmOrUserNotFound() throws Exception {
        MvcResult userResult = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();
        String jsonUser = userResult.getResponse().getContentAsString();
        UserDto userDto = objectMapper.readValue(jsonUser, UserDto.class);

        MvcResult filmResult = mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();
        String jsonFilm = filmResult.getResponse().getContentAsString();
        FilmDto filmDto = objectMapper.readValue(jsonFilm, FilmDto.class);
        mockMvc.perform(put("/films/{id}/like/{userId}", filmDto.getId(), userDto.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/films/{id}/like/{userId}",
                        filmDto.getId(), userDto.getId() + 9999))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(delete("/films/{id}/like/{userId}",
                        filmDto.getId() + 9999, userDto.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void popularFilms_shouldReturn10Items() throws Exception {
        for (int i = 1; i < 100; i++) {
            MvcResult userResult = mockMvc.perform(post("/users")
                            .content(objectMapper.writeValueAsString(getNewUserDto()))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk()).andReturn();
            String jsonUser = userResult.getResponse().getContentAsString();
            UserDto userDto = objectMapper.readValue(jsonUser, UserDto.class);

            MvcResult filmResult = mockMvc.perform(post("/films")
                            .content(objectMapper.writeValueAsString(testFilm))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk()).andReturn();
            String jsonFilm = filmResult.getResponse().getContentAsString();
            FilmDto filmDto = objectMapper.readValue(jsonFilm, FilmDto.class);

            mockMvc.perform(put("/films/{id}/like/{userId}", filmDto.getId(), userDto.getId()))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    void popularFilms_shouldReturn15Items() throws Exception {
        for (int i = 1; i < 100; i++) {
            MvcResult userResult = mockMvc.perform(post("/users")
                            .content(objectMapper.writeValueAsString(getNewUserDto()))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk()).andReturn();
            String jsonUser = userResult.getResponse().getContentAsString();
            UserDto userDto = objectMapper.readValue(jsonUser, UserDto.class);

            MvcResult filmResult = mockMvc.perform(post("/films")
                            .content(objectMapper.writeValueAsString(testFilm))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk()).andReturn();
            String jsonFilm = filmResult.getResponse().getContentAsString();
            FilmDto filmDto = objectMapper.readValue(jsonFilm, FilmDto.class);

            mockMvc.perform(put("/films/{id}/like/{userId}", filmDto.getId(), userDto.getId()))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/films/popular").param("count", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(15));
    }

    @Test
    void popularFilms_shouldReturnOnlyPopularFilms() throws Exception {
        List<Integer> popularFilmsIds = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            MvcResult userResult = mockMvc.perform(post("/users")
                            .content(objectMapper.writeValueAsString(getNewUserDto()))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk()).andReturn();
            String jsonUser = userResult.getResponse().getContentAsString();
            UserDto userDto = objectMapper.readValue(jsonUser, UserDto.class);

            MvcResult filmResult = mockMvc.perform(post("/films")
                            .content(objectMapper.writeValueAsString(testFilm))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk()).andReturn();
            String jsonFilm = filmResult.getResponse().getContentAsString();
            FilmDto filmDto = objectMapper.readValue(jsonFilm, FilmDto.class);

            if (i < 11) {
                mockMvc.perform(put("/films/{id}/like/{userId}", filmDto.getId(), userDto.getId()))
                        .andExpect(status().isOk());
                popularFilmsIds.add(filmDto.getId().intValue());
            }
        }

        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$.[*].id", containsInAnyOrder(popularFilmsIds.toArray())));

    }
}