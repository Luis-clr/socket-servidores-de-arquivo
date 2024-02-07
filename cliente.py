import socket

def cliente(arquivo):
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.connect(('localhost', 9999))

    # Enviar o nome do arquivo ao servidor principal
    server.send(arquivo.encode())

    # Receber a lista de servidores que possuem o arquivo
    servidores = eval(server.recv(1024).decode())
    print("Servidores que possuem o arquivo:", servidores)

    # Escolher um servidor para baixar o arquivo
    if servidores:
        servidor_escolhido = servidores[0]  # Pode implementar lógica para escolher

        # Conectar ao servidor de arquivos escolhido
        servidor_arquivos = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        servidor_arquivos.connect((servidor_escolhido, 8888))

        # Solicitar o arquivo
        servidor_arquivos.send(arquivo.encode())

        # Receber o arquivo
        # ...

        servidor_arquivos.close()
    else:
        print("Nenhum servidor encontrado com o arquivo solicitado.")

# Uso do cliente
cliente("exemplo.txt")
