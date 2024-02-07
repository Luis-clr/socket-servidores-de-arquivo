import socket
import threading
import os

lock = threading.Lock()
servidores_arquivos = []
diretorio_padrao = 'C:\\Users\\LuisRibeiro\\Desktop\\atividade sd'  # Substitua pelo seu caminho

def servidor_de_arquivos():
    global lock, servidores_arquivos
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind(('0.0.0.0', 8888))
    server.listen(5)

    print("[Servidor de Arquivos] Aguardando conexões...")

    while True:
        client_socket, addr = server.accept()
        arquivo_solicitado = client_socket.recv(1024).decode()

        if arquivo_existe(arquivo_solicitado):
            with lock:
                servidores_arquivos.append(addr[0])
            informar_servidor_principal(client_socket, arquivo_solicitado)
            listar_arquivos_disponiveis()  # Adicionado print
        else:
            print(f"Arquivo '{arquivo_solicitado}' não encontrado no servidor.")

        client_socket.close()

def arquivo_existe(nome_arquivo):
    caminho_arquivo = os.path.join(diretorio_padrao, nome_arquivo)
    return os.path.exists(caminho_arquivo)

def informar_servidor_principal(client_socket, arquivo):
    with lock:
        mensagem = f"Servidor de arquivos possui o arquivo: {arquivo}"
        client_socket.send(mensagem.encode())

def listar_arquivos_disponiveis():
    with lock:
        lista_arquivos = os.listdir(diretorio_padrao)
        print("Arquivos disponíveis no diretório:", lista_arquivos)

# Iniciar o servidor de arquivos
thread_servidor_arquivos = threading.Thread(target=servidor_de_arquivos)
thread_servidor_arquivos.start()

listar_arquivos_disponiveis()
