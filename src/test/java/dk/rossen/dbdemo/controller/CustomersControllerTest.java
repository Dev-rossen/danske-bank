package dk.rossen.dbdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.rossen.dbdemo.model.Customer;
import dk.rossen.dbdemo.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CustomersController.class})
class CustomersControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createCustomer() throws Exception {

    }

    @Test
    void getCustomerById() throws Exception {
        Customer customer = new Customer("1234", "Anders", "And", Collections.emptyList());

        when(customerService.getCustomer(anyString())).thenReturn(Optional.of(customer));

        mockMvc.perform(
                        get("/v1/customers/{customer-id}", customer.customerId()))
                .andExpect(status().isOk()
                ).andExpect(content().json(new ObjectMapper().writeValueAsString(customer)));

        verify(customerService).getCustomer(anyString());
    }
}