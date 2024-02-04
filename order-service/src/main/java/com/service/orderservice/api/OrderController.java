package com.service.orderservice.api;

import com.service.basedomains.dto.Order;
import com.service.basedomains.dto.OrderEvent;
import com.service.orderservice.kafka.OrderProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/order-service")
public class OrderController {

    private final OrderProducer orderProducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> placeOrder(@RequestBody Order order){
        OrderEvent orderEvent = new OrderEvent();

        order.setOrderId(String.valueOf(UUID.randomUUID()));
        orderEvent.setStatus("PENDING");
        orderEvent.setMessage("order status is pending");
        orderEvent.setOrder(order);

        orderProducer.sendMessage(orderEvent);
        LOGGER.info("Order placed successfully...");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
