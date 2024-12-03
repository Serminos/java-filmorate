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
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmorateApplication.class)
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    FilmController filmController = new FilmController();

    @Autowired
    private ObjectMapper objectMapper;
    Film testFilm;

    @BeforeEach
    void beforeEachTest() {
        testFilm = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.now().minusYears(1))
                .duration(120L)
                .build();
        filmController.clear();
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

        testFilm.setName("new Name");

        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
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
    void create_shouldNotGenerateExceptionWhenDurationNegativeOrZero() throws Exception {
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
}