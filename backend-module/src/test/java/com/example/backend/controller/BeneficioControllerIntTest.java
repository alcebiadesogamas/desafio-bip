package com.example.backend.controller;

import com.example.backend.dto.BeneficioDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BeneficioControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve criar um benefício")
    void shouldCreateBenefitTest() throws Exception {
        var beneficioDTO = new BeneficioDTO(null, "name", "description", BigDecimal.ONE, true);
        var inputJson = objectMapper.writeValueAsString(beneficioDTO);

        MvcResult result = mockMvc.perform(post("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        var responseJson = result.getResponse().getContentAsString();
        var created = objectMapper.readValue(responseJson, BeneficioDTO.class);

        assertThat(created).isNotNull();
        assertThat(created.id()).isNotNull();
        assertThat(created.name()).isEqualTo(beneficioDTO.name());
        assertThat(created.description()).isEqualTo(beneficioDTO.description());
        assertThat(created.value()).isEqualByComparingTo(beneficioDTO.value());
        assertThat(created.active()).isEqualTo(beneficioDTO.active());
    }

    @Test
    @DisplayName("Nao deve criar beneficio para objeto incompleto")
    void shouldNotCreateBenefitTest() throws Exception {
        var beneficioDTO = new BeneficioDTO(null, null, "description", BigDecimal.ONE, true);
        var inputJson = objectMapper.writeValueAsString(beneficioDTO);

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(inputJson))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Deve obter lista de benefícios")
    void shouldListBenefitsTest() throws Exception {
        mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    var json = result.getResponse().getContentAsString();
                    assertThat(json.trim()).startsWith("[");
                });
    }

    @Test
    @DisplayName("Deve obter beneficio por id")
    void shouldGetOneBenefitTest() throws Exception {
        var beneficioDTO = new BeneficioDTO(null, "getOneName", "desc", BigDecimal.TEN, true);
        var inputJson = objectMapper.writeValueAsString(beneficioDTO);

        MvcResult createResult = mockMvc.perform(post("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();

        var created = objectMapper.readValue(createResult.getResponse().getContentAsString(), BeneficioDTO.class);

        mockMvc.perform(get("/api/v1/beneficios/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    var fetched = objectMapper.readValue(result.getResponse().getContentAsString(), BeneficioDTO.class);
                    assertThat(fetched.id()).isEqualTo(created.id());
                    assertThat(fetched.name()).isEqualTo(created.name());
                });
    }

    @Test
    @DisplayName("Deve atualizar um beneficio")
    void shouldUpdateBenefitTest() throws Exception {
        var beneficioDTO = new BeneficioDTO(null, "toUpdate", "desc", BigDecimal.valueOf(2), true);
        var inputJson = objectMapper.writeValueAsString(beneficioDTO);

        MvcResult createResult = mockMvc.perform(post("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();

        var created = objectMapper.readValue(createResult.getResponse().getContentAsString(), BeneficioDTO.class);

        var updatePayload = Map.of(
                "id", created.id(),
                "name", "updatedName",
                "description", "updatedDesc",
                "value", 5,
                "active", false
        );
        var updateJson = objectMapper.writeValueAsString(updatePayload);

        mockMvc.perform(put("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    var updated = objectMapper.readValue(result.getResponse().getContentAsString(), BeneficioDTO.class);
                    assertThat(updated.id()).isEqualTo(created.id());
                    assertThat(updated.name()).isEqualTo("updatedName");
                    assertThat(updated.description()).isEqualTo("updatedDesc");
                    assertThat(updated.value()).isEqualByComparingTo(BigDecimal.valueOf(5));
                    assertThat(updated.active()).isFalse();
                });
    }

    @Test
    @DisplayName("Deve atualizar parcialmente um beneficio")
    void shouldPartialUpdateBenefitTest() throws Exception {
        var beneficioDTO = new BeneficioDTO(null, "toPatch", "desc", BigDecimal.valueOf(3), true);
        var inputJson = objectMapper.writeValueAsString(beneficioDTO);

        MvcResult createResult = mockMvc.perform(post("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();

        var created = objectMapper.readValue(createResult.getResponse().getContentAsString(), BeneficioDTO.class);

        var patchPayload = Map.of(
                "id", created.id(),
                "name", "patchedName"
        );
        var patchJson = objectMapper.writeValueAsString(patchPayload);

        mockMvc.perform(patch("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    var patched = objectMapper.readValue(result.getResponse().getContentAsString(), BeneficioDTO.class);
                    assertThat(patched.id()).isEqualTo(created.id());
                    assertThat(patched.name()).isEqualTo("patchedName");
                    assertThat(patched.description()).isEqualTo(created.description());
                    assertThat(patched.value()).isEqualByComparingTo(created.value());
                });
    }

    @Test
    @DisplayName("Deve excluir um beneficio")
    void shouldDeleteBenefitTest() throws Exception {
        // create
        var beneficioDTO = new BeneficioDTO(null, "toDelete", "desc", BigDecimal.valueOf(4), true);
        var inputJson = objectMapper.writeValueAsString(beneficioDTO);

        MvcResult createResult = mockMvc.perform(post("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();

        var created = objectMapper.readValue(createResult.getResponse().getContentAsString(), BeneficioDTO.class);

        // delete
        mockMvc.perform(delete("/api/v1/beneficios/{id}", created.id()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/beneficios/{id}", created.id()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Deve chamar EJB transfer endpoint e verificar via listar")
    void shouldCallTransferEndpointTest() throws Exception {
        var b1 = new BeneficioDTO(null, "From", "desc", BigDecimal.valueOf(100), true);
        var b2 = new BeneficioDTO(null, "To", "desc", BigDecimal.valueOf(50), true);

        var b1Json = objectMapper.writeValueAsString(b1);
        var b2Json = objectMapper.writeValueAsString(b2);

        MvcResult r1 = mockMvc.perform(post("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(b1Json))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult r2 = mockMvc.perform(post("/api/v1/beneficios")
                        .contentType("application/json")
                        .content(b2Json))
                .andExpect(status().isOk())
                .andReturn();

        var created1 = objectMapper.readValue(r1.getResponse().getContentAsString(), BeneficioDTO.class);
        var created2 = objectMapper.readValue(r2.getResponse().getContentAsString(), BeneficioDTO.class);

        var transferPayload = Map.of(
                "fromId", created1.id(),
                "toId", created2.id(),
                "amount", 10
        );
        var transferJson = objectMapper.writeValueAsString(transferPayload);

        mockMvc.perform(post("/api/v1/beneficios/transfer")
                        .contentType("application/json")
                        .content(transferJson))
                .andExpect(status().isOk());

        MvcResult listResult = mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        var json = listResult.getResponse().getContentAsString();
        var all = objectMapper.readValue(json, BeneficioDTO[].class);

        var afterFrom = Arrays.stream(all).filter(b -> b.id().equals(created1.id())).findFirst().orElseThrow();
        var afterTo = Arrays.stream(all).filter(b -> b.id().equals(created2.id())).findFirst().orElseThrow();

        assertThat(afterFrom.value()).isEqualByComparingTo(created1.value().subtract(BigDecimal.valueOf(10)));
        assertThat(afterTo.value()).isEqualByComparingTo(created2.value().add(BigDecimal.valueOf(10)));
    }
}
