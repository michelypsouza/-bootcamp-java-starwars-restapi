package com.bootcampjava.starwars.controller;

import com.bootcampjava.starwars.model.Jedi;
import com.bootcampjava.starwars.service.JediService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JediControllerTest {

    @MockBean
    private JediService jediService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /jedi/1 - SUCCESS")
    public void testGetJediByIdWithSuccess() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "HanSolo", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);

        // execucao
        mockMvc.perform(get("/jedi/{id}",1))

                // asserts
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("HanSolo")))
                .andExpect(jsonPath("$.strength", is(10)))
                .andExpect(jsonPath("$.version", is(1)));

    }

    @Test
    @DisplayName("GET /jedi/1 - Not Found")
    public void testGetJediByIdNotFound() throws Exception {

        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);

        mockMvc.perform(get("/jedi/{1}",1))
                .andExpect(status().isNotFound());

    }

    //TODO: Teste do POST com sucesso
    @Test
    @DisplayName("POST /jedi - SUCCESS")
    public void testPostJediWithSucess() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "Mace Windu", 400, 1);
        Mockito.doReturn(mockJedi).when(jediService).save(any());

        // execucao
        mockMvc.perform(post("/jedi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockJedi)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.LOCATION,"/jedi".concat("/"+mockJedi.getId())))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mace Windu")))
                .andExpect(jsonPath("$.strength", is(400)))
                .andExpect(jsonPath("$.version", is(1)));

    }

    //TODO: Teste do PUT com sucesso
    @Test
    @DisplayName("PUT /jedi/1 - SUCCESS")
    public void testPutJediWithSuccess() throws Exception {

        Jedi putJedi = new Jedi("Princess XXXX", 1);
        Jedi mockJedi = new Jedi(1, "Princess Leia", 1, 1);

        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);
        Mockito.doReturn(true).when(jediService).update(any());

        mockMvc.perform(put("/jedi/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(putJedi)))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Princess XXXX")))
                .andExpect(jsonPath("$.strength", is(1)))
                .andExpect(jsonPath("$.version", is(2)));

    }

    //TODO: Teste do PUT com uma versao igual da ja existente - deve retornar conflito
    @Test
    @DisplayName("PUT /jedi/1 - Version Mismatch - CONFLICT")
    public void testPutJediVersionMismatch() throws Exception {

        // cenario
        Jedi putJedi = new Jedi("Yoda", 500);
        Jedi mockJedi = new Jedi(1,"Yoda", 500, 2);

        // execucao
        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);
        Mockito.doReturn(true).when(jediService).update(any());

        // assert
        mockMvc.perform(put("/jedi/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH,1)
                        .content(asJsonString(putJedi)))
                .andExpect(status().isConflict());

    }

    //TODO: Teste do PUT com erro - not found
    @Test
    @DisplayName("PUT /jedi/1 - Not Found")
    public void testJediPutNotFound() throws Exception {

        // cenario
        Jedi putJedi = new Jedi("Darth Vader", 400);

        // execucao
        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);

        // assert
        mockMvc.perform(put("/jedi/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(putJedi)))

                .andExpect(status().isNotFound());
    }

    //TODO: Teste do delete com sucesso
    @Test
    @DisplayName("DELETE /jedi/1 - SUCCESS")
    public void testDeleteJediWithSuccess() throws Exception {

        // cenario
        Integer id = 1;
        Jedi jediFound = new Jedi(1, "HanSolo", 100, 1);

        // execucao
        Mockito.doReturn(Optional.of(jediFound)).when(jediService).findById(id);
        Mockito.doReturn(true).when(jediService).delete(id);

        // assert
        mockMvc.perform(delete("/jedi/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.LOCATION, "/jedi/"+id)
                )
                .andExpect(status().isOk());
                //.andExpect(status().isNoContent())

    }

    //TODO: Teste do delete com erro - deletar um id ja deletado
    @Test
    @DisplayName("DELETE /jedi/1 - Not Found")
    public void testJediDeleteNotFound() throws Exception {

        // execucao
        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);
        Mockito.doReturn(false).when(jediService).delete(any());

        // assert
        mockMvc.perform(delete("/jedi/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.LOCATION,"/jedi/1"))
                .andExpect(status().isNotFound());
    }

    //TODO: teste do delete com erro - internal server error
    @Test
    @DisplayName("DELETE /jedi/1 - internal server error")
    public void testJediDeleteInternalServerError() throws Exception {

        Integer id = 1;
        Jedi mockJedi = new Jedi(1, "Luke Skywalker", 100, 1);

        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(id);
        Mockito.doReturn(false).when(jediService).delete(id);

        mockMvc.perform(delete("/jedi/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.LOCATION, "/jedi/1"))
                .andExpect(status().isInternalServerError());

    }
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
