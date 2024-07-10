package calendarios.servicios;

public class EnviadorDeMails implements AdapterMails {

  ShemailLib shemailLib;

  public EnviadorDeMails(ShemailLib shemailLib) {
    this.shemailLib = shemailLib;
  }

  @Override
  public void enviarMail(String direccion, String asunto, String contenido) {
    shemailLib.enviarMailA(direccion, asunto, contenido);
  }
}
