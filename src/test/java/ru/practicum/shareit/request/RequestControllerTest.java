package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutput;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class RequestControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService requestService;
    @Autowired
    private MockMvc mvc;

    private final ItemRequestDto requestDto = ItemRequestDto.builder()
            .id(1L)
            .description("Request text_1")
            .created(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
            .requester(new ItemRequestDto.Requester(new User(1L, "Name", "qwer@mail.ru")))
            .build();

    private final ItemRequestOutput itemRequestOutput = ItemRequestOutput.builder()
            .id(2L)
            .description("Request text_2")
            .created(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
            .requester(new ItemRequestOutput.Requester(new User(1L, "Name", "qwer@mail.ru")))
            .items(List.of(ItemByRequestDto.builder().id(1L).build()))
            .build();

    @Test
    void createRequestTest() throws Exception {
        when(requestService.createRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(String.valueOf(requestDto.getDescription()))))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester()),
                        ItemRequestDto.Requester.class))
                .andExpect(jsonPath("$.created", is(String.valueOf(requestDto.getCreated()))));
    }

    @Test
    void getRequestsByUserIdTest() throws Exception {
        when(requestService.getRequestsByUserId(anyLong()))
                .thenReturn(List.of(itemRequestOutput));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestOutput.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestOutput.getDescription())))
                .andExpect(jsonPath("$[0].requester", is(itemRequestOutput.getRequester()),
                        ItemRequestOutput.Requester.class))
                .andExpect(jsonPath("$[0].created", is(String.valueOf(itemRequestOutput.getCreated()))));
    }

    @Test
    void getAllRequestsTest() throws Exception {
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestOutput));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestOutput.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestOutput.getDescription())))
                .andExpect(jsonPath("$[0].requester", is(itemRequestOutput.getRequester()),
                        ItemRequestOutput.Requester.class))
                .andExpect(jsonPath("$[0].created", is(String.valueOf(itemRequestOutput.getCreated()))));
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestOutput);

        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestOutput.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestOutput.getDescription())))
                .andExpect(jsonPath("$.requester", is(itemRequestOutput.getRequester()),
                        ItemRequestOutput.Requester.class))
                .andExpect(jsonPath("$.created", is(String.valueOf(itemRequestOutput.getCreated()))));
    }
}
