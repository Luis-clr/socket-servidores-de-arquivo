import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        try {
            InetAddress servidorPrincipalAddr = InetAddress.getByName("127.0.0.1");
            clientSocket = new DatagramSocket();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite o nome do arquivo desejado: ");
            String arquivoSolicitado = scanner.nextLine();

            byte[] sendData = arquivoSolicitado.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, servidorPrincipalAddr, 5007);

            clientSocket.send(sendPacket);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String servidoresRespondendo = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] servidoresArray = servidoresRespondendo.split(",");

            if (servidoresArray.length == 0 || servidoresArray[0].equals("")) {
                System.out.println("Nenhum servidor de arquivos possui o arquivo solicitado.");
                return;
            }

            System.out.println("Servidores de arquivos que possuem o arquivo:");
            for (String servidorIp : servidoresArray) {
                System.out.println(servidorIp);
            }

            System.out.print("Digite o IP do servidor escolhido: ");
            String servidorEscolhido = scanner.nextLine();

            try (Socket downloadSocket = new Socket(servidorEscolhido, 5000);
                 InputStream inputStream = downloadSocket.getInputStream();
                 FileOutputStream fileOutputStream = new FileOutputStream(
                         "C:/Users/logxp/Documents/socket-servidores-de-arquivo/baixados/" + arquivoSolicitado)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("Arquivo '" + arquivoSolicitado + "' baixado com sucesso.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        }
    }
}
