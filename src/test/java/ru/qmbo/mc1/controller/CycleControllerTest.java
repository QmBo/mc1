package ru.qmbo.mc1.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.qmbo.mc1.model.Message;
import ru.qmbo.mc1.service.CycleService;
import ru.qmbo.mc1.service.WebsocketClientService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CycleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CycleService cycleService;
    @MockBean
    private WebsocketClientService websocketClientService;
    @Captor
    private ArgumentCaptor<Message> captor;

    @BeforeEach
    public void cleanUp() {
        cycleService.stop();
    }

    @Test
    public void whenStartThenReturnStartMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/start"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).startsWith("Start cycle at:");
    }

    @Test
    public void whenStartThenSendMessage() throws Exception {
        long testStartTime = System.currentTimeMillis();
        mockMvc.perform(get("/start"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        verify(websocketClientService).sendMessage(captor.capture());
        assertThat(captor.getValue().getSessionId()).isEqualTo(1);
        assertThat(captor.getValue().getMc1Timestamp().getTime()).isBetween(testStartTime, System.currentTimeMillis());
    }

    @Test
    public void whenStopThenReturnStopMessage() throws Exception {
        mockMvc.perform(get("/start"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        MvcResult mvcResult = mockMvc.perform(get("/stop"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).startsWith("Cycle is stopped.");
    }
}