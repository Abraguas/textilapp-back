package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.StockMovement;
import com.tup.textilapp.service.StockMovementService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/stockMovement")
public class StockMovementController {
    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody StockMovement stockMovement) {
        this.stockMovementService.register(stockMovement);
        return ResponseEntity.ok(new ResponseMessageDTO("Stock movement registered succesfully"));
    }

    @PostMapping(path = "all")
    public ResponseEntity<?> registerMultiple(@RequestBody List<StockMovement> stockMovements) {
        this.stockMovementService.registerAll(stockMovements);
        return ResponseEntity.ok(new ResponseMessageDTO("Stock movements registered succesfully"));
    }

    @GetMapping(path = "product/{productId}")
    public ResponseEntity<?> getStockByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(this.stockMovementService.getStockByProduct(productId));
    }

    @GetMapping(path = "/{productId}")
    public ResponseEntity<?> getMovementsByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(this.stockMovementService.getMovementsByProduct(productId));
    }

    @GetMapping(path = "/report")
    public ResponseEntity<?> getMovementsByProductIdAndDatePeriod(
            @RequestParam Integer productId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate) {
        return ResponseEntity.ok(this.stockMovementService.getMovementsByProductAndPeriod(productId, startDate, endDate));
    }

    @GetMapping
    public ResponseEntity<?> getAllMovements() {
        return ResponseEntity.ok(this.stockMovementService.getAllMovements());
    }
}
