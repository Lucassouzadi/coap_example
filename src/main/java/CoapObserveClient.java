import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;

public class CoapObserveClient {

    public static void main(String[] args) throws InterruptedException, ConnectorException, IOException {
        // URL do recurso CoAP no servidor com suporte a "observe"
        String serverUri = "coap://localhost:5683/scheduled-notify";

        // Criação do cliente CoAP
        CoapClient coapClient = new CoapClient(serverUri);

        // Exemplo de GET feito para o servidor
        // System.out.println("Resposta a partir de um GET simples: " + coapClient.get().getResponseText());

        // Registra um observador para o recurso
        CoapObserveRelation relation = coapClient.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                System.out.println("Notificação recebida do recurso observável: " + response.getResponseText());
            }

            @Override
            public void onError() {
                System.err.println("Erro na observação do recurso");
            }
        });

        // Aguarda um tempo arbitrário para permitir observações contínuas
        Thread.sleep(60000);

        // Cancela a relação de observação (interrompe a observação)
        relation.proactiveCancel();

        // Fecha o cliente CoAP
        coapClient.shutdown();
    }
}
