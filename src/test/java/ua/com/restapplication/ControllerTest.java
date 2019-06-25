package ua.com.restapplication;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Main.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void A_getAllClientsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(4)))
                .andDo(print());
    }

    @Test
    public void C_getClientByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("aaa"))
                .andExpect(jsonPath("$.email").value("aaa@ukr.net"))
                .andDo(print());
    }

    @Test()
    public void D_getClientByIdExceptionNotFoundTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/1111111").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test()
    public void E_getClientByIdExceptionIllegalArgumentTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/fff").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test()
    public void F_createClientTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "        \"name\": \"Tommy\",\n" +
                        "        \"email\": \"leeTommy@ukr.net\"\n" +
                        "    }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tommy"))
                .andExpect(jsonPath("$.email").value("leeTommy@ukr.net"))
                .andDo(print());
    }

    @Test()
    public void F_createClientExceptionEmailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "        \"name\": \"Tommy\",\n" +
                        "        \"email\": \"aaa@ukr.net\"\n" +
                        "    }"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test()
    public void H_createClientExceptionEmailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "        \"name\": \"Tommy\"\n" +
                        "    }"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test()
    public void G_createClientExceptionNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "        \"email\": \"leeTommy@ukr.net\"\n" +
                        "    }"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void K_updateClientTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "        \"id\": 5,\n" +
                        "        \"name\": \"Benny\",\n" +
                        "        \"email\": \"leeTommy@ukr.net\"\n" +
                        "    }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Benny"))
                .andExpect(jsonPath("$.email").value("leeTommy@ukr.net"))
                .andDo(print());
    }

    @Test
    public void L_updateClientNameTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "        \"id\": 5,\n" +
                        "        \"name\": \"Lenny\"\n" +
                        "    }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lenny"))
                .andExpect(jsonPath("$.email").value("leeTommy@ukr.net"))
                .andDo(print());
    }

    @Test
    public void M_updateClientEmailTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "        \"id\": 5,\n" +
                        "        \"email\": \"benny@ukr.net\"\n" +
                        "    }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lenny"))
                .andExpect(jsonPath("$.email").value("benny@ukr.net"))
                .andDo(print());
    }

    @Test
    public void N_updateClientExceptionTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "        \"name\": \"Benny\",\n" +
                        "        \"email\": \"leeTommy@ukr.net\"\n" +
                        "    }"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void O_updateClientExceptionTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "        \"id\": 55555,\n" +
                        "        \"name\": \"Benny\",\n" +
                        "        \"email\": \"leeTommy@ukr.net\"\n" +
                        "    }"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void P_updateClientExceptionTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "        \"id\": 5,\n" +
                        "        \"name\": \"Benny\",\n" +
                        "        \"email\": \"aaa@ukr.net\"\n" +
                        "    }"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test()
    public void Q_deleteClientTest() throws Exception {
        mockMvc.perform(delete("/clients/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test()
    public void R_deleteClientExceptionTest() throws Exception {
        mockMvc.perform(delete("/clients/55555").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test()
    public void S_deleteClientExceptionTest() throws Exception {
        mockMvc.perform(delete("/clients/fff").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}