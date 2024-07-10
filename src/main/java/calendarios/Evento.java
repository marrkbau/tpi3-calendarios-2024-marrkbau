package calendarios;

import calendarios.servicios.EnviadorDeMails;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Evento {

  private String nombre;
  private Ubicacion ubicacion;
  private LocalDateTime horaInicio;
  private LocalDateTime horaFin;
  private List<Usuario> invitados;
  private List<Recordatorio> recordatorios;


  public Evento(String nombre,
                Ubicacion ubicacion,
                LocalDateTime horaInicio,
                LocalDateTime horaFin,
                List<Usuario> invitados,
                List<Recordatorio> recordatorios
  ) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.horaInicio = horaInicio;
    this.horaFin = horaFin;
    this.invitados = invitados != null ? invitados : new ArrayList<>();
    this.recordatorios = recordatorios != null ? recordatorios : new ArrayList<>();
  }


  public List<Recordatorio> getRecordatorios() {
    return new ArrayList<>(recordatorios);
  }

  public String getNombre() {
    return nombre;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

  public LocalDateTime getHoraInicio() {
    return horaInicio;
  }

  public LocalDateTime getHoraFin() {
    return horaFin;
  }

  public List<Usuario> getInvitados() {

    List<Usuario> invitadosList = new ArrayList<>(invitados);
    return invitadosList;
  }

  public Duration cuantoFalta() {
    return Duration.ofMinutes(LocalDateTime.now().until(horaInicio, ChronoUnit.MINUTES));
  }

  public boolean estaSolapadoCon(Evento otro) {
    return this.horaInicio.isBefore(otro.horaFin) && otro.horaInicio.isBefore(this.horaFin);
  }

  public List<Evento> eventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
    if (horaInicio.isAfter(inicio) && horaInicio.isBefore(fin)) {
      return List.of(this);
    } else {
      return List.of();
    }
  }

  public void agregarRecordatorio(Recordatorio recordatorio) {
    recordatorios.add(recordatorio);
  }

  public void enviarRecordatoriosPendientes(EnviadorDeMails enviadorMails) {
    LocalDateTime ahora = LocalDateTime.now();

    for (Recordatorio recordatorio : recordatorios) {
      if (recordatorio.debeEnviar(ahora, horaInicio)) {
        for (Usuario invitado : invitados) {
          String asunto = "Recordatorio de Evento: " + nombre;
          String cuerpo = "Te recordamos que el evento "
              + nombre + " comienza a las " + horaInicio;
          recordatorio
              .enviar(enviadorMails, invitado.getMail(), asunto, cuerpo);
        }
      }
    }
  }
}

