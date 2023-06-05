package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.OrderDTO;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.OrderState;
import com.tup.textilapp.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> registerOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(this.orderService.registerOrder(orderDTO, token));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String searchString
    ) {
        if ((pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
            return ResponseEntity.ok(this.orderService.getAll());
        }
        if (!(pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
            return ResponseEntity.ok(this.orderService.getByPageAndSize(pageNum, pageSize));
        }
        return ResponseEntity.ok(this.orderService.getByUsernameAndPageAndSize(pageNum, pageSize, searchString));

    }

    @GetMapping(path = "myOrders")
    public ResponseEntity<?> getMyOrders(
            HttpServletRequest request,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize
    ) {
        String token = request.getHeader("Authorization").substring(7);
        if (pageNum == null || pageSize == null) {
            return ResponseEntity.ok(this.orderService.getAllByToken(token));
        }
        return ResponseEntity.ok(this.orderService.getAllByTokenAndPageAndSize(token, pageNum, pageSize));
    }

    @GetMapping(path = "pending")
    public ResponseEntity<?> getPending(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String searchString
    ) {
        if ((pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
            return ResponseEntity.ok(this.orderService.getPending());
        }
        if (!(pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
            return ResponseEntity.ok(this.orderService.getPendingByPageAndSize(pageNum, pageSize));
        }
        return ResponseEntity.ok(this.orderService.getPendingByUsernameAndPageAndSize(pageNum, pageSize, searchString));
    }

    @GetMapping(path = "{orderId}")
    public ResponseEntity<?> getById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(this.orderService.getById(orderId));
    }

    @GetMapping(path = "highestSellingProducts")
    public ResponseEntity<?> getHighestSellingProducts(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate) {

        return ResponseEntity.ok(this.orderService.getHighestSellingProducts(startDate, endDate));
    }

    @PutMapping(path = "state/{orderId}")
    public ResponseEntity<?> updateState(@PathVariable Integer orderId, @RequestBody OrderState state) {
        this.orderService.changeState(orderId, state);
        return ResponseEntity.ok(new ResponseMessageDTO("Order state updated succesfully"));
    }

    @PutMapping(path = "cancel/{orderId}")
    public ResponseEntity<?> cancel(@PathVariable Integer orderId, HttpServletRequest request) {
        System.out.println("llegue acá");
        String token = request.getHeader("Authorization").substring(7);

        this.orderService.cancelOrder(orderId, token);
        return ResponseEntity.ok(new ResponseMessageDTO("Order cancelled succesfully"));
    }
}
