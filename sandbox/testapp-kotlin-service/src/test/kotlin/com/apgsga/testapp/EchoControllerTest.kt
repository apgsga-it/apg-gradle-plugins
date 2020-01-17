package com.apgsga.testapp

import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
class EchoControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun `Default Echo Test`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/service/echo").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Hello World")))
    }

    @Test
    fun `Text provided Echo Test`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/service/echo?text=whatever").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.content().string(containsString("whatever")))
    }

    @Test
    fun `Default Echo Json Test`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/service/echo/json").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Hello World")))
    }

    @Test
    fun `Text provided Echo Json Test`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/service/echo/json?text=whatever").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(containsString("whatever")))
    }

}