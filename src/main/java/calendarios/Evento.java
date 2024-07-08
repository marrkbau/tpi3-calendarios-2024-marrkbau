package calendarios;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import calendarios.Usuario;

public class Evento {

  // TODO implementar estado, comportamiento y/o polimorfismo según sea neceario

  private String nombre;
  private Ubicacion ubicacion;
  private LocalDateTime horaInicio;
  private LocalDateTime horaFin;
  private List<Usuario> invitados;

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

  public Evento(String nombre, Ubicacion ubicacion, LocalDateTime horaInicio, LocalDateTime horaFin, List<Usuario> invitados) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.horaInicio = horaInicio;
    this.horaFin = horaFin;
    this.invitados = invitados;
  }

  private LocalDateTime getInicio() {
    return horaInicio;
  }
  public Duration cuantoFalta() {
    // Este es un ejemplo de cómo se puede obtener una duración
    // Modificar en caso de que sea necesario
    return Duration.ofHours(LocalDateTime.now().until(getInicio(), ChronoUnit.HOURS));
  }

  public boolean estaSolapadoCon(Evento otro) {
    return this.horaInicio.isBefore(otro.horaFin) && otro.horaInicio.isBefore(this.horaFin);
  }
}
