package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.MovementReportRequestDTO;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.StockMovement;
import com.tup.textilapp.service.StockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/stockMovement")
public class StockMovementController {
    private final StockMovementService stockMovementService;

    @Autowired
    public StockMovementController (StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }
    @PostMapping
    public ResponseEntity<?> register(@RequestBody StockMovement stockMovement) {
        try {
            this.stockMovementService.register(stockMovement);
            return ResponseEntity.ok(new ResponseMessageDTO("Stock movement registered succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().body(new ResponseMessageDTO(e.getMessage()));
        }  catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @PostMapping(path = "all")
    public ResponseEntity<?> registerMultiple(@RequestBody List<StockMovement> stockMovements) {
        try {
            this.stockMovementService.registerAll(stockMovements);
            return ResponseEntity.ok(new ResponseMessageDTO("Stock movements registered succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().body(new ResponseMessageDTO(e.getMessage()));
        }  catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @GetMapping(path = "product/{productId}")
    public ResponseEntity<?> getStockByProductId(@PathVariable Integer productId) {
        try {
            return ResponseEntity.ok(this.stockMovementService.getStockByProduct(productId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @GetMapping(path = "/{productId}")
    public ResponseEntity<?> getMovementsByProductId(@PathVariable Integer productId) {
        try {
            return ResponseEntity.ok(this.stockMovementService.getMovementsByProduct(productId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @GetMapping(path = "/report")
    public ResponseEntity<?> getMovementsByProductIdAndDatePeriod(@RequestBody MovementReportRequestDTO body) {
        try {
            System.out.println(body.toString());
            return ResponseEntity.ok(this.stockMovementService.getMovementsByProductAndPeriod(body.getProductId(), body.getStartDate(), body.getEndDate()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new ResponseMessageDTO(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllMovements() {
        try {
            return ResponseEntity.ok(this.stockMovementService.getAllMovements());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
}
