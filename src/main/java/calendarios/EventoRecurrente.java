package calendarios;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EventoRecurrente extends Evento {

  private ChronoUnit unidadRecurrencia;
  private int frecuenciaRecurrencia;

  public EventoRecurrente(String nombre,
                          Ubicacion ubicacion,
                          LocalDateTime horaInicio,
                          LocalDateTime horaFin,
                          List<Usuario> invitados,
                          ChronoUnit unidadRecurrencia,
                          int frecuenciaRecurrencia,
                          List<Recordatorio> recordatorios
  ) {
    super(nombre, ubicacion, horaInicio, horaFin, invitados,
        recordatorios != null ? recordatorios : new ArrayList<>());
    this.unidadRecurrencia = unidadRecurrencia != null ? unidadRecurrencia : ChronoUnit.DAYS;
    this.frecuenciaRecurrencia = frecuenciaRecurrencia;
  }


  @Override
  public List<Evento> eventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
    List<Evento> eventos = new ArrayList<>();
    LocalDateTime fechaInicioEvento = getHoraInicio();
    LocalDateTime fechaFinEvento = getHoraFin();

    while (!fechaInicioEvento.isAfter(fin)) {
      if (!fechaInicioEvento.isBefore(inicio) && !fechaInicioEvento.isAfter(fin)) {
        eventos
            .add(new Evento(getNombre(),
                getUbicacion(),
                fechaInicioEvento,
                fechaFinEvento,
                getInvitados(),
                getRecordatorios()));
      }
      fechaInicioEvento = fechaInicioEvento
          .plus(frecuenciaRecurrencia, unidadRecurrencia);
      fechaFinEvento = fechaFinEvento
          .plus(frecuenciaRecurrencia, unidadRecurrencia);
    }

    return eventos;
  }

  @Override
  public Duration cuantoFalta() {
    LocalDateTime ahora = LocalDateTime.now();
    LocalDateTime proximaRepeticion = getHoraInicio();

    while (proximaRepeticion.isBefore(ahora)) {
      proximaRepeticion = proximaRepeticion
          .plus(frecuenciaRecurrencia, unidadRecurrencia);
    }

    return Duration.between(ahora, proximaRepeticion);
  }
}


