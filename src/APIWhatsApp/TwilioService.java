package APIWhatsApp;

import java.net.URI;
import java.math.BigDecimal;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {

    private static final String ACCOUNT_SID = "";
    private static final String AUTH_TOKEN = "";
    private static final String TWILIO_NUMBER = "whatsapp:+14155238886";

    public static void enviarMensaje(
            String numeroPaciente,
            String nombrePaciente,
            String nombreMedico,
            String especialidad,
            String fecha,
            String hora
    ) {
        String numeroTwilio = "51924823729";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        
        // Construcción del mensaje
        StringBuilder sb = new StringBuilder();
        sb.append("🏥 *CONFIRMACIÓN DE CITA MÉDICA* 🏥\n\n");
        sb.append("Hola, *").append(nombrePaciente).append("*:\n");
        sb.append("Su cita ha sido registrada con éxito. A continuación los detalles:\n\n");
        sb.append("👨‍⚕️ *Médico:* Dr(a). ").append(nombreMedico).append("\n");
        sb.append("🩺 *Especialidad:* ").append(especialidad).append("\n");
        sb.append("📅 *Fecha:* ").append(fecha).append("\n");
        sb.append("⏰ *Hora:* ").append(hora).append("\n\n");
        sb.append("⚠️ *Recomendaciones:*\n");
        sb.append("• Por favor, llegue 15 minutos antes de su cita.\n");
        sb.append("• Para cancelaciones o cambios, avisar con anticipación.\n\n");
        sb.append("¡Gracias por su confianza!\n");
        sb.append("_Mensaje automático, favor de no responder._");
        
        Message.creator(
                new PhoneNumber("whatsapp:+" + numeroTwilio),
                new PhoneNumber(TWILIO_NUMBER),
                sb.toString()
        ).create();
    }
}
