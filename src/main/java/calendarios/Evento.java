package calendarios;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import calendarios.Usuario;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Evento {

  private String nombre;
  private Ubicacion ubicacion;
  private LocalDateTime horaInicio;
  private LocalDateTime horaFin;
  private List<Usuario> invitados;


  public Evento(String nombre, Ubicacion ubicacion, LocalDateTime horaInicio, LocalDateTime horaFin, List<Usuario> invitados) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.horaInicio = horaInicio;
    this.horaFin = horaFin;
    this.invitados = invitados;
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
    return invitados;
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
}

