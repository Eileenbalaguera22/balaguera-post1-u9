
package com.clinica.recordatorios.service;

import com.clinica.recordatorios.channel.ReminderChannel;
import com.clinica.recordatorios.channel.ReminderChannelFactory;
import com.clinica.recordatorios.domain.Cita;
import com.clinica.recordatorios.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaReminderServiceImpl implements CitaReminderService {

    private final CitaRepository citaRepository;
    private final ReminderChannelFactory reminderChannelFactory;
    private final Clock clock;

    public CitaReminderServiceImpl(
            CitaRepository citaRepository,
            ReminderChannelFactory reminderChannelFactory,
            Clock clock) {
        this.citaRepository = citaRepository;
        this.reminderChannelFactory = reminderChannelFactory;
        this.clock = clock;
    }

    @Override
    public int enviarRecordatoriosPendientes() {
        LocalDateTime desde = LocalDateTime.now(clock);
        LocalDateTime hasta = desde.plusHours(24);

        List<Cita> citas =
                citaRepository
                        .findByFechaHoraCitaBetweenAndRecordatorioEnviadoFalse(
                                desde, hasta
                        );

        int enviados = 0;

        for (Cita cita : citas) {
            ReminderChannel canal =
                    reminderChannelFactory.obtenerCanal(
                            cita.getCanalPreferido()
                    );

            String mensaje = "Recordatorio: tiene una cita médica programada para "
                    + cita.getFechaHoraCita();

            canal.enviar(cita, mensaje);

            cita.setRecordatorioEnviado(true);
            citaRepository.save(cita);

            enviados++;
        }

        return enviados;
    }
}
