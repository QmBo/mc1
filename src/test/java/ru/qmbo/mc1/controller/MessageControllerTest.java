package ru.qmbo.mc1.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.qmbo.mc1.model.Message;
import ru.qmbo.mc1.service.CycleService;
import ru.qmbo.mc1.service.MessageService;
import ru.qmbo.mc1.service.WebsocketClientService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CycleService cycleService;
    @MockBean
    private WebsocketClientService websocketClientService;
    @Captor
    private ArgumentCaptor<Message> dataBaseCaptor;
    @MockBean
    private MessageService messageService;

    @BeforeEach
    public void cleanUp() {
        cycleService.stop();
    }

    @Test
    public void whenPostMessageThenSaveMessageAndDoCycle() throws Exception {
        long testStartTime = System.currentTimeMillis();
        MvcResult mvcResult = mockMvc.perform(get("/start"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).startsWith("Start cycle at:");
        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\": null, \"sessionId\": 7, \"id\": null, \"id\": null, \"id\": null, \"id\": null}"))
                .andExpect(status().isOk());
        verify(messageService).saveMessage(dataBaseCaptor.capture());
        assertThat(dataBaseCaptor.getValue().getSessionId()).isEqualTo(7);
        assertThat(dataBaseCaptor.getValue().getMc1Timestamp()).isNull();
        assertThat(dataBaseCaptor.getValue().getEndTimestamp().getTime())
                .isBetween(testStartTime, System.currentTimeMillis());
    }
}