
package com.clinica.recordatorios.service;

import com.clinica.recordatorios.channel.ReminderChannel;
import com.clinica.recordatorios.channel.ReminderChannelFactory;
import com.clinica.recordatorios.domain.CanalNotificacion;
import com.clinica.recordatorios.domain.Cita;
import com.clinica.recordatorios.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaReminderServiceImplTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private ReminderChannelFactory reminderChannelFactory;

    @Mock
    private ReminderChannel reminderChannel;

    private CitaReminderServiceImpl service;

    @BeforeEach
    void setUp() {
        Clock relojFijo = Clock.fixed(
                Instant.parse("2026-08-10T09:00:00Z"),
                ZoneId.of("UTC")
        );

        service = new CitaReminderServiceImpl(
                citaRepository,
                reminderChannelFactory,
                relojFijo
        );
    }

    @Test
    void debeEnviarRecordatoriosYGuardarLasCitas() {
        LocalDateTime fechaCita =
                LocalDateTime.of(2026, 8, 10, 10, 0);

        Cita cita = new Cita(
                1L,
                "Ana Pérez",
                "ana@example.com",
                fechaCita,
                CanalNotificacion.EMAIL,
                false
        );

        when(citaRepository
                .findByFechaHoraCitaBetweenAndRecordatorioEnviadoFalse(
                        LocalDateTime.of(2026, 8, 10, 9, 0),
                        LocalDateTime.of(2026, 8, 11, 9, 0)
                ))
                .thenReturn(List.of(cita));

        when(reminderChannelFactory.obtenerCanal(
                CanalNotificacion.EMAIL
        )).thenReturn(reminderChannel);

        int enviados = service.enviarRecordatoriosPendientes();

        assertEquals(1, enviados);
        verify(reminderChannelFactory)
                .obtenerCanal(CanalNotificacion.EMAIL);
        verify(reminderChannel).enviar(
                cita,
                "Recordatorio: tiene una cita médica programada para "
                        + fechaCita
        );
        verify(citaRepository).save(cita);
        assertEquals(true, cita.isRecordatorioEnviado());
    }

    @Test
    void noDebeEnviarNiGuardarSiNoHayCitasPendientes() {
        when(citaRepository
                .findByFechaHoraCitaBetweenAndRecordatorioEnviadoFalse(
                        LocalDateTime.of(2026, 8, 10, 9, 0),
                        LocalDateTime.of(2026, 8, 11, 9, 0)
                ))
                .thenReturn(List.of());

        int enviados = service.enviarRecordatoriosPendientes();

        assertEquals(0, enviados);
        verify(citaRepository, never()).save(any(Cita.class));
        verifyNoInteractions(reminderChannelFactory, reminderChannel);
    }
}
