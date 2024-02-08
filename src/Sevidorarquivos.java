import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Sevidorarquivos {
    private static final int SERVER_PORT = 5000;
    private static final String DIRETORIO_PADRAO = "C:/Users/logxp/Desktop/atividadejavasss/atividade/baixados";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("[Servidor de Arquivos] Aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String arquivoSolicitado = reader.readLine();
            System.out.println("Recebida solicitação para o arquivo '" + arquivoSolicitado + "' de " + clientSocket.getInetAddress());

            if (arquivoExiste(arquivoSolicitado)) {
                informarServidorPrincipal(writer, arquivoSolicitado);
                enviarArquivo(clientSocket, arquivoSolicitado);
            } else {
                System.out.println("Arquivo '" + arquivoSolicitado + "' não encontrado no servidor " + clientSocket.getInetAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean arquivoExiste(String nomeArquivo) {
        String caminhoArquivo = DIRETORIO_PADRAO + File.separator + nomeArquivo;
        return new File(caminhoArquivo).exists();
    }

    private static void informarServidorPrincipal(PrintWriter writer, String arquivo) {
        writer.println("Servidor de arquivos possui o arquivo: " + arquivo);
    }

    private static void enviarArquivo(Socket clientSocket, String arquivo) {
        try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(DIRETORIO_PADRAO + File.separator + arquivo));
             BufferedOutputStream outputStream = new BufferedOutputStream(clientSocket.getOutputStream())) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            System.out.println("Arquivo '" + arquivo + "' enviado para " + clientSocket.getInetAddress());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
