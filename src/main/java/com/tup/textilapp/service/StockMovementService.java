package com.tup.textilapp.service;

import com.tup.textilapp.model.dto.StockMovementDTO;
import com.tup.textilapp.model.dto.StockMovementProdDTO;
import com.tup.textilapp.model.entity.OrderDetail;
import com.tup.textilapp.model.entity.Product;
import com.tup.textilapp.model.entity.StockMovement;
import com.tup.textilapp.repository.OrderDetailRepository;
import com.tup.textilapp.repository.ProductRepository;
import com.tup.textilapp.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public StockMovementService(StockMovementRepository stockMovementRepository, ProductRepository productRepository,
                                OrderDetailRepository orderDetailRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Transactional
    public void register(StockMovement stockMovement) {
        Product product = this.productRepository.findById(stockMovement.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product with id: '" +
                        stockMovement.getProduct().getId() + "' doesn't exist"));
        Integer newStockValue = product.getStock() + stockMovement.getQuantity();
        if(newStockValue < 0) {
            throw new IllegalStateException("Final stock cannot be a negative value");
        }
        stockMovement.setProduct(product);
        stockMovement.setDate(new Date());
        stockMovement.setPriorStock(product.getStock());
        product.setStock(newStockValue);

        this.stockMovementRepository.save(stockMovement);
    }
    public Integer getStockByProduct(Integer productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with id: '" +
                        productId + "' doesn't exist"));
        List<StockMovement> mvLst = this.stockMovementRepository.findAllByProduct(product);
        Integer result = 0;
        for (StockMovement s : mvLst ) {
            result += s.getQuantity();
        }
        List<OrderDetail> dtLst = this.orderDetailRepository.findAllByProduct(product);
        for (OrderDetail d : dtLst) {
            if (!d.getOrder().getState().getName().equals("Cancelado")) {
                result -= d.getQuantity();
            }
        }
        return result;
    }

    public List<StockMovementDTO> getMovementsByProduct(Integer productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with id: '" +
                        productId + "' doesn't exist"));
        return this.stockMovementRepository.findAllByProduct(product).stream().map(
                (StockMovement s) -> new StockMovementDTO(s.getId(),s.getQuantity(),s.getPriorStock(),s.getDate(),s.getObservations())
        ).collect(Collectors.toList());
    }
    public List<StockMovementDTO> getMovementsByProductAndPeriod(Integer productId, Date startDate, Date endDate) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with id: '" +
                        productId + "' doesn't exist"));
        if (startDate.compareTo(endDate) > 0) {
            throw new IllegalStateException("Start date cannot be more recent than end date");
        }
        if (startDate.compareTo(endDate) == 0) {
            throw new IllegalStateException("Start date cannot be the exact same as end date");
        }
        System.out.println(startDate.compareTo(endDate));
        return this.stockMovementRepository.findByDateBetweenAndProduct(startDate, endDate, product).stream().map(
                (StockMovement s) -> new StockMovementDTO(s.getId(),s.getQuantity(),s.getPriorStock(),s.getDate(),s.getObservations())
        ).collect(Collectors.toList());
    }
    public List<StockMovementProdDTO> getAllMovements() {
        return this.stockMovementRepository.findAll().stream().map(
                (StockMovement s) -> new StockMovementProdDTO(s.getId(),s.getQuantity(),s.getPriorStock(),s.getDate(),
                        s.getObservations(), s.getProduct().getName(), s.getProduct().getUnit().getName())
        ).collect(Collectors.toList());
    }
}
