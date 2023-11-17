import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class CoapObserveServer {

    private static Long startTime = System.currentTimeMillis();

    public static void main(String[] args) throws InterruptedException {
        // Criação do servidor CoAP
        CoapServer coapServer = new CoapServer();

        // Adiciona um recurso CoAP ao servidor com suporte a "observe"
        var observableResource = new ObservableResource();
        coapServer.add(observableResource);

        // Inicia o servidor na porta 5683
        coapServer.start();

        while (true) {
            Thread.sleep(5000);
            observableResource.sendNotification();
        }
    }

    // Definição de um recurso CoAP observável
    static class ObservableResource extends CoapResource {

        public ObservableResource() {
            super("scheduled-notify");
            setObservable(true);
            setObserveType(CoAP.Type.NON);
            // setObserveType(CoAP.Type.CON);

            // Configura um executor para notificações periódicas
            // var executorService = Executors.newSingleThreadScheduledExecutor();
            // executorService.scheduleAtFixedRate(this::sendNotification, 0, 5, TimeUnit.SECONDS);
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            // Responde com dados do recurso
            Long responseData = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("GET recebido, respondendo com: " + responseData);
            exchange.respond(String.valueOf(responseData));
        }

        private void sendNotification() {
            // Notifica os clientes que o recurso foi alterado
            changed();
        }
    }
}
