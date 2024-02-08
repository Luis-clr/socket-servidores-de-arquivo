import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class SevidorPrincipal {
    private static final int MULTICAST_PORT = 5007;

    public static void main(String[] args) {
        try (MulticastSocket multicastSocket = new MulticastSocket(MULTICAST_PORT)) {
            InetAddress multicastGroup = InetAddress.getByName("224.0.0.1");
            multicastSocket.joinGroup(multicastGroup);

            System.out.println("[Servidor Principal] Aguardando solicitações...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);

                String arquivoSolicitado = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Recebida solicitação para o arquivo '" + arquivoSolicitado + "' de " + packet.getAddress());

                List<String> servidoresRespondendo = buscaServidoresDeArquivos(arquivoSolicitado);

                String resposta = String.join(",", servidoresRespondendo);
                DatagramPacket responsePacket = new DatagramPacket(resposta.getBytes(), resposta.length(), packet.getAddress(), packet.getPort());
                multicastSocket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> buscaServidoresDeArquivos(String arquivoSolicitado) {
        List<String> servidoresRespondendo = new ArrayList<>();

        // Substitua os IPs pelos endereços reais dos seus servidores de arquivos
        String[] servidoresIps = {"192.168.1.2", "127.0.0.1"};

        for (String servidorIp : servidoresIps) {
            if (arquivoExisteNoServidor(servidorIp, arquivoSolicitado)) {
                servidoresRespondendo.add(servidorIp);
            }
        }

        return servidoresRespondendo;
    }

    private static boolean arquivoExisteNoServidor(String servidorIp, String arquivoSolicitado) {
        // Lógica para verificar se o arquivo existe no servidor com o IP especificado
        // Implemente de acordo com a estrutura do seu sistema
        return true;
    }
}
