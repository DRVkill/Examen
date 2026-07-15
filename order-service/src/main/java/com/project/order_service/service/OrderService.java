package com.project.order_service.service;

import com.project.order_service.dto.ItemOrderRequest;
import com.project.order_service.dto.OrderRequest;
import com.project.order_service.dto.OrderResponse;
import com.project.order_service.dto.ProductoSimpleResponse;
import com.project.order_service.exception.ResourceNotFoundException;
import com.project.order_service.model.ItemOrder;
import com.project.order_service.model.Order;
import com.project.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.webClient = WebClient.builder().build();
    }

    public List<OrderResponse> listarTodos() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse obtenerPorId(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));

        return mapToResponse(order);
    }

    public OrderResponse crear(OrderRequest request) {
        validarUsuarioExiste(request.userId());

        Order order = new Order();
        order.setUserId(request.userId());
        order.setFechaCreacion(LocalDateTime.now());
        order.setEstado("PENDIENTE");

        List<ItemOrder> items = new ArrayList<>();
        double total = 0;

        for (ItemOrderRequest itemRequest : request.items()) {
            ProductoSimpleResponse producto = obtenerProducto(itemRequest.productoId());

            ItemOrder item = new ItemOrder();
            item.setOrder(order);
            item.setProductoId(itemRequest.productoId());
            item.setProductoTitulo(producto.titulo());
            item.setCantidad(itemRequest.cantidad());
            item.setPrecioUnitario(producto.precio());

            items.add(item);
            total += producto.precio() * itemRequest.cantidad();
        }

        order.setItems(items);
        order.setTotal(total);

        Order guardado = orderRepository.save(order);
        return mapToResponse(guardado);
    }

    public OrderResponse actualizar(Integer id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));

        validarUsuarioExiste(request.userId());

        order.setUserId(request.userId());

        List<ItemOrder> items = new ArrayList<>();
        double total = 0;

        for (ItemOrderRequest itemRequest : request.items()) {
            ProductoSimpleResponse producto = obtenerProducto(itemRequest.productoId());

            ItemOrder item = new ItemOrder();
            item.setOrder(order);
            item.setProductoId(itemRequest.productoId());
            item.setProductoTitulo(producto.titulo());
            item.setCantidad(itemRequest.cantidad());
            item.setPrecioUnitario(producto.precio());

            items.add(item);
            total += producto.precio() * itemRequest.cantidad();
        }

        order.setItems(items);
        order.setTotal(total);

        Order actualizado = orderRepository.save(order);
        return mapToResponse(actualizado);
    }

    public void eliminar(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));

        orderRepository.delete(order);
    }

    private void validarUsuarioExiste(Integer userId) {
        try {
            webClient.get()
                    .uri("http://localhost:8084/api/usuarios/{id}", userId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + userId);
        }
    }

    private ProductoSimpleResponse obtenerProducto(Integer productoId) {
        try {
            ProductoSimpleResponse producto = webClient.get()
                    .uri("http://localhost:8083/api/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(ProductoSimpleResponse.class)
                    .block();

            if (producto == null) {
                throw new ResourceNotFoundException("Producto no encontrado con id: " + productoId);
            }

            return producto;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + productoId);
        }
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderResponse.ItemOrderResponse> items = order.getItems()
                .stream()
                .map(item -> new OrderResponse.ItemOrderResponse(
                        item.getProductoId(),
                        item.getProductoTitulo(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getCantidad() * item.getPrecioUnitario()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotal(),
                order.getEstado(),
                order.getFechaCreacion(),
                items
        );
    }
}