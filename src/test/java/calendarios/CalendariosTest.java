package calendarios;

import calendarios.servicios.CalculadorDeTiempo;
import calendarios.servicios.GugleMapas;
import calendarios.servicios.PosicionDeUsuario;
import calendarios.servicios.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static java.time.temporal.ChronoUnit.*;
import static calendarios.Pending.*;
import static org.junit.jupiter.api.Assertions.*;

/** TODO:
 *   - Partí opcionalmente esta clase entre varias para hacerla más fácil de mantener.
 *   - Modificá los tests si es necesario, pero se recomienda fuertemente mantener las interfaces propuestas
 *   - Agregá más casos de prueba para satisfacer los requerimientos de cobertura
 */
class CalendariosTest {

  private PositionService positionService;
  private GugleMapas gugleMapas;

  private CalculadorDeTiempo calculadorDeTiempo;

  private PosicionDeUsuario posicionDeUsuario;

  Ubicacion utnMedrano = new Ubicacion(-34.5984145, -58.4222096);
  Ubicacion utnCampus = new Ubicacion(-34.6591644,-58.4694862);

  @BeforeEach
  void init() {
    positionService = mock(PositionService.class);
    gugleMapas = mock(GugleMapas.class);
    posicionDeUsuario = new PosicionDeUsuario(positionService);
    calculadorDeTiempo = new CalculadorDeTiempo(gugleMapas);
  }

  // 1. Permitir que une usuarie tenga muchos calendarios

  @Test
  void uneUsuarieTieneMuchosCalendarios() {
    Usuario rene = crearUsuario("rene@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    Calendario calendario2 = crearCalendarioVacio();

    rene.agregarCalendario(calendario);
    rene.agregarCalendario(calendario2);
    assertTrue(rene.tieneCalendario(calendario));
    assertTrue(rene.tieneCalendario(calendario2));

  }

  // 2. Permitir que en cada calendario se agenden múltiples eventos
  // 3. Permitir que los eventos registren nombre, fecha y hora de inicio y fin, ubicación, invitades (otros usuaries)

  @Test
  void unEventoPuedeTenerMultiplesInvitades() {
    // TODO completar
    fail("Pendiente");
  }

  @Test
  void unCalendarioPermiteAgendarUnEvento() {
    Calendario calendario = new Calendario();

    Evento seguimientoDeTP = crearEventoSimpleEnMedrano("Seguimiento de TP", LocalDateTime.of(2021, 10, 1, 15, 30), Duration.of(30, MINUTES));
    calendario.agendar(seguimientoDeTP);

    assertTrue(calendario.estaAgendado(seguimientoDeTP));
  }

  @Test
  void unCalendarioPermiteAgendarDosEvento() {
    Calendario calendario = new Calendario();
    LocalDateTime inicio = LocalDateTime.of(2021, 10, 1, 15, 30);

    Evento seguimientoDeTPA = crearEventoSimpleEnMedrano("Seguimiento de TPA", inicio, Duration.of(30, MINUTES));
    Evento practicaParcial = crearEventoSimpleEnMedrano("Practica para el primer parcial", inicio.plusMinutes(60), Duration.of(90, MINUTES));

    calendario.agendar(seguimientoDeTPA);
    calendario.agendar(practicaParcial);

    assertTrue(calendario.estaAgendado(seguimientoDeTPA));
    assertTrue(calendario.estaAgendado(practicaParcial));
  }




  // 5. Permitir saber cuánto falta para un cierto calendarios.evento (por ejemplo, 15 horas)

  @Test
  void unEventoSabeCuantoFalta() {
    LocalDateTime inicio = LocalDateTime.now().plusDays(60);
    Evento parcialDds = crearEventoSimpleEnMedrano("Parcial DDS", inicio, Duration.of(2,  HOURS));

    assertTrue(parcialDds.cuantoFalta().compareTo(Duration.of(60, ChronoUnit.DAYS)) <= 0);
    assertTrue(parcialDds.cuantoFalta().compareTo(Duration.of(59, ChronoUnit.DAYS)) >= 0);
  }

  // 7. Permitir agendar eventos con repeticiones, con una frecuencia diaria, semanal, mensual o anual

  @Test
  void sePuedenAgendarYListarEventosRecurrrentes() {
    Usuario usuario = crearUsuario("rene@gugle.com.ar");

    // TODO completar
    fail("Agregar uno evento recurrente que se repita los martes a las 19 y dure 45 minutos y " +
        "por tanto deberá aparecer dos veces entre el lunes 14 a las 9 y el lunes 28 a las 21");

    LocalDateTime inicio = LocalDateTime.of(2020, 9, 8, 19, 0);
    Evento claseDds = crearEventoSimpleEnMedrano("clase DDS", inicio, Duration.of(45,  MINUTES));


    List<Evento> eventos = usuario.eventosEntreFechas(
        LocalDateTime.of(2020, 9, 14, 9, 0),
        LocalDateTime.of(2020, 9, 28, 21, 0));

    assertEquals(eventos.size(), 2);
  }

  @Test
  void unEventoRecurrenteSabeCuantoFaltaParaSuProximaRepeticion() {
    // TODO completar
    Evento unRecurrente = fail("crear un evento recurrente que se repita, a partir de hoy, cada 15 días, y arranque una hora antes de la hora actual");

    assertTrue(unRecurrente.cuantoFalta().compareTo(Duration.of(15, ChronoUnit.DAYS)) <= 0);
    assertTrue(unRecurrente.cuantoFalta().compareTo(Duration.of(14, ChronoUnit.DAYS)) >= 0);
  }



  // 9. Permitir asignarle a un evento varios recordatorios, que se enviarán cuando falte un cierto tiempo


  @Test
  void proximoEvento() {
    // TODO completar
    fail("Pendiente");
  }

  // 8. Permitir saber si le usuarie llega al evento más próximo a tiempo, tomando en cuenta la ubicación actual de le usuarie y destino.


  @Test
  void llegaATiempoAlProximoEventoCuandoNoHayEventos() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    assertTrue(feli.llegaATiempoAlProximoEvento());
  }

  @Test
  void llegaATiempoAlProximoEventoCuandoHayUnEventoCercano() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    feli.agregarCalendario(calendario);

    fail("mockear al Position Service para que diga que ya está en medrano y a GugleMaps para que diga que tarda 0 minutos en llegar");

    calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(2, HOURS)));

    assertTrue(feli.llegaATiempoAlProximoEvento());
  }

  @Test
  void noLlegaATiempoAlProximoEventoCuandoHayUnEventoFisicamenteLejano() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    feli.agregarCalendario(calendario);

    fail("mockear al Position Service para que diga que está en Medrano y a GugleMaps para que diga que tarda 0 minutos en llegar");

    calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(2, HOURS)));

    assertFalse(feli.llegaATiempoAlProximoEvento());
  }


  @Test
  void llegaATiempoAlProximoEventoCuandoHayUnEventoCercanoAunqueAlSiguienteNoLlegue() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    feli.agregarCalendario(calendario);

    fail("mockear al Position Service para que diga que está en Medrano y a GugleMaps para que diga que tarda 0 minutos en llegar a Medrano y 1:30 horas en llegar a Campus");

    calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(3, HOURS)));
    calendario.agendar(crearEventoSimpleEnCampus("Final", LocalDateTime.now().plusMinutes(45), Duration.of(1, HOURS)));

    assertTrue(feli.llegaATiempoAlProximoEvento());
  }

  /**
   * @return une usuarie con el mail dado
   */
  Usuario crearUsuario(String email) {
    return new Usuario(email, posicionDeUsuario, calculadorDeTiempo);
  }

  /*
   * @return Un calendario sin ningún evento agendado aún
   */
  Calendario crearCalendarioVacio() {
    return new Calendario();
  }

  Evento crearEventoSimpleEnMedrano(String nombre, LocalDateTime inicio, Duration duracion) {
    return crearEventoSimple("Seguimiento de TPA", inicio, inicio.plus(duracion), utnMedrano, Collections.emptyList());
  }

  Evento crearEventoSimpleEnCampus(String nombre, LocalDateTime inicio, Duration duracion) {
    return crearEventoSimple("Seguimiento de TPA", inicio, inicio.plus(duracion), utnCampus, Collections.emptyList());
  }

  /**
   * @return un evento sin invtades que no se repite, que tenga el nombre, fecha de inicio y fin, ubicación dados
   */
  Evento crearEventoSimple(String nombre, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion, List<Usuario> usuarios) {
    return new Evento(nombre, ubicacion, inicio, fin, usuarios);
  }

  Evento crearEventoRecurrente(String nombre, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion, List<Usuario> usuarios) {
    return new Evento()
  }

}