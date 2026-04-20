package sv.edu.udb.scheduler.scheduler;

import sv.edu.udb.scheduler.enums.OrderStatus;
import sv.edu.udb.scheduler.repository.OrderRepository;
import sv.edu.udb.scheduler.repository.SessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CleanerScheduler {

    private final SessionRepository sessionRepository;
    private final OrderRepository   orderRepository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public CleanerScheduler(SessionRepository sessionRepository, OrderRepository orderRepository) {
        this.sessionRepository = sessionRepository;
        this.orderRepository   = orderRepository;
    }

    // -------------------------------------------------------
    // TAREA 1: Desactiva sesiones expiradas cada 2 minutos
    //          (en producción sería cada 30 minutos)
    //          cron = "0 */30 * * * *"
    // -------------------------------------------------------
    @Scheduled(cron = "${scheduler.sesiones.cron}")
    @Transactional
    public void limpiarSesionesExpiradas() {
        LocalDateTime ahora = LocalDateTime.now();
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║  [SCHEDULER] Limpieza de sesiones expiradas      ║");
        System.out.println("║  Hilo: " + Thread.currentThread().getName());
        System.out.println("║  Hora: " + ahora.format(FMT));
        System.out.println("╚══════════════════════════════════════════════════╝");

        int desactivadas = sessionRepository.desactivarSesionesExpiradas(ahora);
        int eliminadas   = sessionRepository.eliminarSesionesExpiradas(ahora);

        System.out.println("  ✔ Sesiones desactivadas : " + desactivadas);
        System.out.println("  ✔ Sesiones eliminadas   : " + eliminadas);
        System.out.println("  ─────────────────────────────────────────────────\n");
    }

    // -------------------------------------------------------
    // TAREA 2: Cancela órdenes PENDIENTE con más de 24 horas
    //          de inactividad — se ejecuta cada 1 minuto
    //          (en producción sería cada hora: "0 0 * * * *")
    // -------------------------------------------------------
    @Scheduled(cron = "${scheduler.ordenes.cron}")
    @Transactional
    public void cancelarOrdenesPendientes() {
        LocalDateTime ahora  = LocalDateTime.now();
        LocalDateTime limite = ahora.minusMinutes(2); // demo: 2 min = equivale a 24h en producción

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║  [SCHEDULER] Cancelación de órdenes inactivas    ║");
        System.out.println("║  Hilo: " + Thread.currentThread().getName());
        System.out.println("║  Hora: " + ahora.format(FMT));
        System.out.println("╚══════════════════════════════════════════════════╝");

        int canceladas = orderRepository.cancelarOrdenesInactivas(
                OrderStatus.CANCELADA,
                ahora,
                OrderStatus.PENDIENTE,
                limite
        );

        System.out.println("  ✔ Órdenes canceladas    : " + canceladas);
        System.out.println("  ─────────────────────────────────────────────────\n");
    }
}
