package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.GetOrderDTO;
import com.tup.textilapp.model.dto.OrderDTO;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.OrderState;
import com.tup.textilapp.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
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
        try {

            return ResponseEntity.ok(this.orderService.registerOrder(orderDTO, token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().body(new ResponseMessageDTO(e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String searchString
    ) {
        try {
            if ((pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
               return ResponseEntity.ok(this.orderService.getAll());
            }
            if (!(pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
                return ResponseEntity.ok(this.orderService.getByPageAndSize(pageNum,pageSize));
            }
            return ResponseEntity.ok(this.orderService.getByUsernameAndPageAndSize(pageNum,pageSize,searchString));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }

    @GetMapping(path = "myOrders")
    public ResponseEntity<?> getMyOrders(
            HttpServletRequest request,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize
    ) {
        String token = request.getHeader("Authorization").substring(7);
        try {
            if (pageNum == null || pageSize == null) {
                return ResponseEntity.ok(this.orderService.getAllByToken(token));
            }
            return ResponseEntity.ok(this.orderService.getAllByTokenAndPageAndSize(token, pageNum, pageSize));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }

    @GetMapping(path = "pending")
    public ResponseEntity<?> getPending(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String searchString
    ) {
        try {
            if ((pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
                return ResponseEntity.ok(this.orderService.getPending());
            }
            if (!(pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
                return ResponseEntity.ok(this.orderService.getPendingByPageAndSize(pageNum,pageSize));
            }
            return ResponseEntity.ok(this.orderService.getPendingByUsernameAndPageAndSize(pageNum,pageSize,searchString));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }

    @GetMapping(path = "{orderId}")
    public ResponseEntity<?> getById(@PathVariable Integer orderId) {
        try {
            GetOrderDTO order = this.orderService.getById(orderId);
            return ResponseEntity.ok(order);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }

    @GetMapping(path = "highestSellingProducts")
    public ResponseEntity<?> getHighestSellingProducts(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate) {
        try {
            return ResponseEntity.ok(this.orderService.getHighestSellingProducts(startDate, endDate));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }

    @PutMapping(path = "state/{orderId}")
    public ResponseEntity<?> updateState(@PathVariable Integer orderId, @RequestBody OrderState state) {
        try {
            this.orderService.changeState(orderId, state);
            return ResponseEntity.ok(new ResponseMessageDTO("Order state updated succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().body(new ResponseMessageDTO(e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }

    @PutMapping(path = "cancel/{orderId}")
    public ResponseEntity<?> cancel(@PathVariable Integer orderId, HttpServletRequest request) {
        System.out.println("llegue acá");
        String token = request.getHeader("Authorization").substring(7);
        try {
            this.orderService.cancelOrder(orderId, token);
            return ResponseEntity.ok(new ResponseMessageDTO("Order cancelled succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().body(new ResponseMessageDTO(e.getMessage()));

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(new ResponseMessageDTO(e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
}
