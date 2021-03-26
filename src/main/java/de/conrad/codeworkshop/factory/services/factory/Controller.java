package de.conrad.codeworkshop.factory.services.factory;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andreas Hartmann
 */
@Validated
@RestController("factoryController")
@RequestMapping("/factory")
public class Controller {

    private final Service factoryService;

    @Autowired
    public Controller(final Service factoryService) {

        this.factoryService = factoryService;
    }

    @RequestMapping(
            value = "enqueue",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public final HttpStatus enqueue(final Order order) {

        HttpStatus response = OK;

        try {
            factoryService.enqueue(order);
        } catch (final Exception exception) {
            response = INTERNAL_SERVER_ERROR;
        }

        return response;
    }
}
