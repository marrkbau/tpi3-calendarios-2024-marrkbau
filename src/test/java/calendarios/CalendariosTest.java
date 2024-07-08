package calendarios;

import calendarios.servicios.*;
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
  private ShemailLib shemailLib;

  private CalculadorDeTiempo calculadorDeTiempo;
  private PosicionDeUsuario posicionDeUsuario;
  private EnviadorDeMails enviadorDeMails;

  Ubicacion utnMedrano = new Ubicacion(-34.5984145, -58.4222096);
  Ubicacion utnCampus = new Ubicacion(-34.6591644,-58.4694862);

  @BeforeEach
  void init() {
    positionService = mock(PositionService.class);
    gugleMapas = mock(GugleMapas.class);
    shemailLib = mock(ShemailLib.class);
    posicionDeUsuario = new PosicionDeUsuario(positionService);
    calculadorDeTiempo = new CalculadorDeTiempo(gugleMapas);
    enviadorDeMails = new EnviadorDeMails(shemailLib);
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
    Usuario rene = crearUsuario("rene@gugle.com.ar");
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Usuario pablo = crearUsuario("pablo@hola.com");

    Calendario calendario = crearCalendarioVacio();
    rene.agregarCalendario(calendario);

    Evento unEvento = crearEventoSimple("evento", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), utnCampus, List.of(feli, pablo));

    calendario.agendar(unEvento);

    assertEquals(unEvento.getInvitados().size(), 2);

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





  // 9. Permitir asignarle a un evento varios recordatorios, que se enviarán cuando falte un cierto tiempo


  @Test
  void proximoEvento() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");

    LocalDateTime inicio = LocalDateTime.now().plusDays(60);
    Evento parcialDds = crearEventoSimpleEnMedrano("Parcial DDS", inicio, Duration.of(2, HOURS));

    parcialDds.agregarRecordatorio(new Recordatorio(Duration.ofMinutes(10)));

    // TODO completar
    fail("Pendiente");
  }

  @Test
  void proximoEvento2() {
    EnviadorDeMails enviadorMails = mock(EnviadorDeMails.class);
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = new Calendario();
    feli.agregarCalendario(calendario);

    LocalDateTime ahora = LocalDateTime.now();
    Evento evento = crearEventoSimpleEnMedrano("Evento Importante", ahora.plusHours(1),Duration.ofMinutes(30));
    Recordatorio recordatorio1 = new Recordatorio(Duration.ofMinutes(30));
    Recordatorio recordatorio2 = new Recordatorio(Duration.ofMinutes(10));
    evento.agregarRecordatorio(recordatorio1);
    evento.agregarRecordatorio(recordatorio2);
    calendario.agendar(evento);
    
    feli.enviarRecordatoriosPendientes(enviadorMails);

    verify(enviadorMails, times(1)).enviarMail(eq("test@mail.com"), anyString(), anyString());
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
    return new Evento(nombre, ubicacion, inicio, fin, usuarios, null);
  }

  EventoRecurrente crearEventoRecurrente(String nombre, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion, List<Usuario> usuarios, ChronoUnit unidad, Integer frecuencia) {
    return new EventoRecurrente(nombre, ubicacion, inicio, fin, usuarios, unidad, frecuencia, null);
  }

}