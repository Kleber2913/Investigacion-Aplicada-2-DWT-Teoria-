package sv.edu.udb.scheduler.repository;

import sv.edu.udb.scheduler.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Long> {

    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.expiraEn < :ahora AND s.activa = false")
    int eliminarSesionesExpiradas(@Param("ahora") LocalDateTime ahora);

    @Modifying
    @Query("UPDATE UserSession s SET s.activa = false WHERE s.expiraEn < :ahora AND s.activa = true")
    int desactivarSesionesExpiradas(@Param("ahora") LocalDateTime ahora);
}
