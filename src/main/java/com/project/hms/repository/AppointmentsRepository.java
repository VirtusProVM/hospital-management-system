package com.project.hms.repository;

import com.project.hms.entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@EnableJpaRepositories
public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
    @Query("SELECT a FROM Appointments a WHERE a.patient.id = :patientId")
    public List<Appointments> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT a FROM Appointments a WHERE a.appointmentDate BETWEEN :start AND :end")
    List<Appointments> findAppointmentsForToday(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    //List<Appointments> findByAppointmentDateTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    //List<Appointments> findByDoctorIdAndAppointmentDateTimeBetweenOrderByAppointmentDateTimeAsc(Long doctorId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    //List<Appointments> findByDoctorIdAndAppointmentDateTimeBetween(Long doctorId, LocalDateTime desiredStart, LocalDateTime desiredEnd);
}
