package com.apgsga.testapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.containsString;

@WebMvcTest
public class EchoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Default Echo Test")
    @Test
    void testEchoDefault() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/service/echo").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Hello World")));
    }
    @DisplayName("Text provided Echo Test")
    @Test
    void testTextProvidedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/service/echo?text=whatever").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.content().string(containsString("whatever")));
    }

    @DisplayName("Default Echo Json Test")
    @Test
    void testJsonEchoDefault() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/service/echo/json").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Hello World")));
    }

    @DisplayName("Text provided Echo Json Test")
    @Test
    void testJsonEchoTextProvided() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/service/echo/json?text=whatever").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(containsString("whatever")));
    }

}
