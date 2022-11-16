/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhosoii.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author supor
 */
public class Server extends Thread {
    // Parte que controla as conexões por meio de threads.
    //private static Vector CLIENTES;
    private static Vector CLIENTES;
    // socket deste cliente
    private Socket conexao;
    private static ServerSocket server;
    // nome deste cliente
    private String nomeCliente;
    // lista que armazena nome de CLIENTES
    private static List LISTA_DE_NOMES = new ArrayList();
    //private static List LISTA_DE_NOMES = new ArrayList();
    // construtor que recebe o socket deste cliente
    public Server(Socket socket) {
        this.conexao = socket;
        
    }
    //testa se nomes são iguais, se for retorna true
    public boolean armazena(String newName){
    //   System.out.println(LISTA_DE_NOMES);
       for (int i=0; i < LISTA_DE_NOMES.size(); i++){
         if(LISTA_DE_NOMES.get(i).equals(newName))
           return true;
       }
       //adiciona na lista apenas se não existir
       LISTA_DE_NOMES.add(newName);
       return false;
    }
    //remove da lista os CLIENTES que já deixaram o chat
    public void remove(String oldName) {
       for (int i=0; i< LISTA_DE_NOMES.size(); i++){
         if(LISTA_DE_NOMES.get(i).equals(oldName))
           LISTA_DE_NOMES.remove(oldName);
       }
    }
    public static int novoServer() {
        // instancia o vetor de CLIENTES conectados
        //CLIENTES = new Vector();
        Thread abrirServer = new AbrirServer();
        abrirServer.run();
        return 1;
    }
    
    // execução da thread
    public void run()
    {
        if(CLIENTES == null)
            CLIENTES = new Vector();
        try {
            // objetos que permitem controlar fluxo de comunicação que vem do cliente
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            
            PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            // recebe o nome do cliente
            this.nomeCliente = entrada.readLine();
            //chamada ao metodo que testa nomes iguais
            if (armazena(this.nomeCliente)){
              saida.println("Este nome ja existe! Conecte novamente com outro Nome.");
              CLIENTES.add(saida);
              //fecha a conexao com este cliente
              this.conexao.close();
              return;
            } else {
               //mostra o nome do cliente conectado ao servidor
               System.out.println(this.nomeCliente + " : Conectado ao Servidor!");
               saida.println(this.nomeCliente + Constantes.separador+Constantes.bemVindo); //Apenas para o Cliente
               sendToAll(saida, Constantes.entrouChat); //Para todos
            }
            //igual a null encerra a execução
            if (this.nomeCliente == null) {
                return;
            }
            //adiciona os dados de saida do cliente no objeto CLIENTES
            CLIENTES.add(saida);
            //recebe a mensagem do cliente
            String msg = entrada.readLine();
            // Verificar se linha é null (conexão encerrada)
            // Se não for nula, mostra a troca de mensagens entre os CLIENTES
            //while (msg != null && !(msg.trim().equals("")))
            while (!(msg.trim().equals("/exitChat")))
            {
                // reenvia a linha para todos os CLIENTES conectados
                sendToAll(saida, Constantes.separador+ msg);
                // espera por uma nova linha.
                msg = entrada.readLine();
            }
            //se cliente enviar linha em branco, mostra a saida no servidor
            System.out.println(this.nomeCliente + Constantes.separador + Constantes.saiuChat);
            //se cliente enviar linha em branco, servidor envia 
			// mensagem de saida do chat aos CLIENTES conectados
            sendToAll(saida,  Constantes.separador +Constantes.saiuChat);
            //remove nome da lista
            remove(this.nomeCliente);
            //exclui atributos setados ao cliente
            CLIENTES.remove(saida);
            //fecha a conexao com este cliente
            this.conexao.close();
        } catch (IOException e) {
            // Caso ocorra alguma excessão de E/S, mostre qual foi.
            System.out.println(e);
        }
    }
    // enviar uma mensagem para todos, menos para o próprio

    /**
     *
     * @param saida
     * @param acao
     * @param msg
     * @throws IOException
     */
    public void sendToAll(PrintStream saida, String msg) throws IOException {
        var e = CLIENTES.elements();
        while (e.hasMoreElements()) {
            // obtém o fluxo de saída de um dos CLIENTES
            var chat = (PrintStream) e.nextElement();
            // envia para todos, menos para o próprio usuário
            if (chat != saida) {
                chat.println(this.nomeCliente +  msg);
            }
        }
    }
}
