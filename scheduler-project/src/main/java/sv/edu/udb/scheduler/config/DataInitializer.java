package sv.edu.udb.scheduler.config;

import sv.edu.udb.scheduler.entity.Order;
import sv.edu.udb.scheduler.entity.UserSession;
import sv.edu.udb.scheduler.enums.OrderStatus;
import sv.edu.udb.scheduler.repository.OrderRepository;
import sv.edu.udb.scheduler.repository.SessionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final OrderRepository   orderRepository;
    private final SessionRepository sessionRepository;

    public DataInitializer(OrderRepository orderRepository, SessionRepository sessionRepository) {
        this.orderRepository   = orderRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void run(String... args) {
        LocalDateTime ahora = LocalDateTime.now();

        // ── Órdenes ─────────────────────────────────────────────────────────
        // Orden reciente (NO debe cancelarse)
        Order o1 = Order.builder()
                .clienteNombre("Carlos Martínez")
                .producto("Laptop Dell XPS")
                .total(1200.00)
                .status(OrderStatus.PENDIENTE)
                .creadoEn(ahora)
                .ultimaActualizacion(ahora)
                .build();

        // Orden con 3 minutos de inactividad (SÍ debe cancelarse en demo)
        Order o2 = Order.builder()
                .clienteNombre("Ana López")
                .producto("Mouse Logitech")
                .total(45.00)
                .status(OrderStatus.PENDIENTE)
                .creadoEn(ahora.minusMinutes(3))
                .ultimaActualizacion(ahora.minusMinutes(3))
                .build();

        // Orden con 5 minutos de inactividad (SÍ debe cancelarse en demo)
        Order o3 = Order.builder()
                .clienteNombre("Pedro Ramírez")
                .producto("Teclado Mecánico")
                .total(89.99)
                .status(OrderStatus.PENDIENTE)
                .creadoEn(ahora.minusMinutes(5))
                .ultimaActualizacion(ahora.minusMinutes(5))
                .build();

        // Orden ya completada (NO debe modificarse)
        Order o4 = Order.builder()
                .clienteNombre("María González")
                .producto("Monitor 4K")
                .total(350.00)
                .status(OrderStatus.COMPLETADA)
                .creadoEn(ahora.minusHours(2))
                .ultimaActualizacion(ahora.minusHours(1))
                .build();

        orderRepository.saveAll(List.of(o1, o2, o3, o4));

        // ── Sesiones ─────────────────────────────────────────────────────────
        // Sesión activa válida (NO debe eliminarse)
        UserSession s1 = UserSession.builder()
                .username("admin")
                .token(UUID.randomUUID().toString())
                .creadoEn(ahora)
                .expiraEn(ahora.plusHours(2))
                .activa(true)
                .build();

        // Sesión ya expirada y desactivada (SÍ debe eliminarse)
        UserSession s2 = UserSession.builder()
                .username("usuario1")
                .token(UUID.randomUUID().toString())
                .creadoEn(ahora.minusHours(3))
                .expiraEn(ahora.minusMinutes(30))
                .activa(false)
                .build();

        // Sesión expirada pero aún activa (debe desactivarse primero)
        UserSession s3 = UserSession.builder()
                .username("usuario2")
                .token(UUID.randomUUID().toString())
                .creadoEn(ahora.minusHours(2))
                .expiraEn(ahora.minusMinutes(10))
                .activa(true)
                .build();

        sessionRepository.saveAll(List.of(s1, s2, s3));

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         DATOS DE DEMO CARGADOS EXITOSAMENTE      ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Órdenes insertadas  : 4                         ║");
        System.out.println("║  Sesiones insertadas : 3                         ║");
        System.out.println("║  H2 Console          : http://localhost:8080/    ║");
        System.out.println("║                        h2-console                ║");
        System.out.println("║  JDBC URL            : jdbc:h2:mem:schedulerdb   ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");
    }
}
