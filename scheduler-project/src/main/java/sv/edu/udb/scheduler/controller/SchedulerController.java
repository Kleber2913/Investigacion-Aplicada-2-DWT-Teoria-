package sv.edu.udb.scheduler.controller;

import sv.edu.udb.scheduler.entity.Order;
import sv.edu.udb.scheduler.entity.UserSession;
import sv.edu.udb.scheduler.enums.OrderStatus;
import sv.edu.udb.scheduler.repository.OrderRepository;
import sv.edu.udb.scheduler.repository.SessionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SchedulerController {

    private final OrderRepository   orderRepository;
    private final SessionRepository sessionRepository;

    public SchedulerController(OrderRepository orderRepository, SessionRepository sessionRepository) {
        this.orderRepository   = orderRepository;
        this.sessionRepository = sessionRepository;
    }

    // ── Órdenes ──────────────────────────────────────────────────────────────

    @GetMapping("/ordenes")
    public ResponseEntity<List<Order>> obtenerOrdenes() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/ordenes/pendientes")
    public ResponseEntity<List<Order>> obtenerPendientes() {
        return ResponseEntity.ok(orderRepository.findByStatus(OrderStatus.PENDIENTE));
    }

    @GetMapping("/ordenes/canceladas")
    public ResponseEntity<List<Order>> obtenerCanceladas() {
        return ResponseEntity.ok(orderRepository.findByStatus(OrderStatus.CANCELADA));
    }

    // ── Sesiones ─────────────────────────────────────────────────────────────

    @GetMapping("/sesiones")
    public ResponseEntity<List<UserSession>> obtenerSesiones() {
        return ResponseEntity.ok(sessionRepository.findAll());
    }

    // ── Estado general ────────────────────────────────────────────────────────

    @GetMapping("/estado")
    public ResponseEntity<Map<String, Object>> estado() {
        long totalOrdenes    = orderRepository.count();
        long pendientes      = orderRepository.findByStatus(OrderStatus.PENDIENTE).size();
        long canceladas      = orderRepository.findByStatus(OrderStatus.CANCELADA).size();
        long completadas     = orderRepository.findByStatus(OrderStatus.COMPLETADA).size();
        long totalSesiones   = sessionRepository.count();

        return ResponseEntity.ok(Map.of(
                "ordenes_total",     totalOrdenes,
                "ordenes_pendiente", pendientes,
                "ordenes_cancelada", canceladas,
                "ordenes_completada",completadas,
                "sesiones_total",    totalSesiones
        ));
    }
}
