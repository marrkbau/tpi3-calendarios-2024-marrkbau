package calendarios;

import calendarios.servicios.EnviadorDeMails;

import java.time.Duration;
import java.time.LocalDateTime;

public class Recordatorio {

    private Duration tiempoAntesDelEvento;
    private boolean enviado;

    public Recordatorio(Duration tiempoAntesDelEvento) {
        this.tiempoAntesDelEvento = tiempoAntesDelEvento;
        this.enviado = false;

    }

    public Duration getTiempoAntesDelEvento() {
        return tiempoAntesDelEvento;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void enviar(EnviadorDeMails enviadorMails, String destinatario, String asunto, String cuerpo) {
        enviadorMails.enviarMail(destinatario, asunto, cuerpo);
        this.enviado = true;
    }


    public boolean debeEnviar(LocalDateTime horaActual, LocalDateTime horaInicioEvento) {
        Duration tiempoRestante = Duration.between(horaActual, horaInicioEvento);
        return !enviado && tiempoRestante.compareTo(tiempoAntesDelEvento) <= 0;
    }

}
