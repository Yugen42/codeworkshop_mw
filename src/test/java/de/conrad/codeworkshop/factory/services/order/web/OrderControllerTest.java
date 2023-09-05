package de.conrad.codeworkshop.factory.services.order.web;

import de.conrad.codeworkshop.factory.services.order.business.OrderService;
import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderConfirmation;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderNumber;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderStatus;
import de.conrad.codeworkshop.factory.services.order.web.mapping.DtoToDomainMapper;
import de.conrad.codeworkshop.factory.services.order.web.validation.OrderDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockBean
    private OrderService orderService;
    @MockBean
    private DtoToDomainMapper dtoToDomainMapper;
    private OrderDtoValidator validator = new OrderDtoValidator();
    @Autowired
    private MockMvc mockMvc;
    private static final String VALID_ORDER_DTO_AS_JSON = "{\n" +
        "    \"positions\": [\n" +
        "        {\n" +
        "            \"productId\": \"100000\",\n" +
        "            \"quantity\": \"42.42\"\n" +
        "        }\n" +
        "    ]\n" +
        "}";

    public static final String INVALID_ORDER_DTO_WITH_VIOLATIONS_AS_JSON = "{\n" +
        "    \"positions\": [\n" +
        "        {\n" +
        "            \"productId\": \"0\",\n" +
        "            \"quantity\": \"0\"\n" +
        "        }\n" +
        "    ]\n" +
        "}";

    // Verify HTTP request mapping and deserialization
    @Test
    void createOrderShouldAcceptValidRequest() throws Exception {
        when(orderService.createOrder(any())).thenReturn(new OrderConfirmation(OrderNumber.generate(), OrderStatus.ACCEPTED));
        when(dtoToDomainMapper.orderDtoToDomain(any())).thenReturn(new Order());

        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_ORDER_DTO_AS_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void createOrderShouldResponseWithBadRequestWhenRequestPayloadIsEmpty() throws Exception {
        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createOrderShouldResponseWithBadRequestWhenRequestMediaTypeIsPlainText() throws Exception {
        mockMvc.perform(post("/order/create")
                .contentType(MediaType.TEXT_PLAIN)
                .content("notJson"))
            .andExpect(status().isBadRequest());
    }

    // Verify result serialization and validation
    @Test
    void createOrderShouldShouldDeclineIfOrderDtoNotValid() throws Exception {
        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVALID_ORDER_DTO_WITH_VIOLATIONS_AS_JSON))
            .andExpect(jsonPath("$.status").value("DECLINED"));
    }

    @Test
    void createOrderShouldAcceptIfOrderDtoIsValid() throws Exception {
        when(orderService.createOrder(any())).thenReturn(new OrderConfirmation(OrderNumber.generate(), OrderStatus.ACCEPTED));
        when(dtoToDomainMapper.orderDtoToDomain(any())).thenReturn(new Order());

        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_ORDER_DTO_AS_JSON))
            .andExpect(jsonPath("$.status").value("ACCEPTED"))
            .andExpect(jsonPath("$.orderNumber.orderNumberPlain").isNumber());
    }
}
