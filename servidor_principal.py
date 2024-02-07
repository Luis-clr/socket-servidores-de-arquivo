import socket
import threading
import struct
import time

def servidor_principal():
    multicast_group = '224.0.0.1'
    multicast_port = 5007

    server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server.bind(('', multicast_port))
    mreq = struct.pack('4sl', socket.inet_aton(multicast_group), socket.INADDR_ANY)
    server.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

    print("[Servidor Principal] Aguardando solicitações...")

    while True:
        data, address = server.recvfrom(1024)
        arquivo_solicitado = data.decode()

        print(f"Recebida solicitação para o arquivo '{arquivo_solicitado}' de {address}")

        # Simular resposta dos servidores de arquivos
        servidores_respondendo = ["Servidor1", "Servidor2"]
        resposta = ",".join(servidores_respondendo)
        server.sendto(resposta.encode(), address)

# Iniciar o servidor principal
thread_servidor_principal = threading.Thread(target=servidor_principal)
thread_servidor_principal.start()

# Cliente para simular uma solicitação
cliente = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
cliente.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)
cliente.sendto("arquivo.txt".encode(), ('224.0.0.1', 5007))

# Esperar para que o servidor principal responda
time.sleep(15)
