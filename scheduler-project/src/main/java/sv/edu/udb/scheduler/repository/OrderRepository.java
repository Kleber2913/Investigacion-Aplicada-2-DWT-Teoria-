package sv.edu.udb.scheduler.repository;

import sv.edu.udb.scheduler.entity.Order;
import sv.edu.udb.scheduler.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);

    @Modifying
    @Query("UPDATE Order o SET o.status = :nuevoStatus, o.ultimaActualizacion = :ahora " +
           "WHERE o.status = :statusActual AND o.ultimaActualizacion < :limite")
    int cancelarOrdenesInactivas(
            @Param("nuevoStatus")    OrderStatus nuevoStatus,
            @Param("ahora")          LocalDateTime ahora,
            @Param("statusActual")   OrderStatus statusActual,
            @Param("limite")         LocalDateTime limite
    );
}
