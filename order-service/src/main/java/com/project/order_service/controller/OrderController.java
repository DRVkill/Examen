package com.project.order_service.controller;

import com.project.order_service.dto.ErrorResponse;
import com.project.order_service.dto.OrderRequest;
import com.project.order_service.dto.OrderResponse;
import com.project.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listarTodos() {
        return ResponseEntity.ok(orderService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> crear(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> actualizar(@PathVariable Integer id,
                                                    @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        orderService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}