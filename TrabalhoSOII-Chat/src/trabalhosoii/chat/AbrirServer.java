/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhosoii.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author supor
 */
public class AbrirServer extends Thread {
    
    private ServerSocket server;
    
    @Override
    public void run(){
    // cria um socket que fica escutando a porta 5555.
        //Server.CLIENTES = new Vector();
        try {
            // cria um socket que fica escutando a porta indicada.
            server = new ServerSocket(Constantes.porta);
            //System.out.println("Servidor iniado");
            JOptionPane.showMessageDialog(null, Constantes.aberturaChat, "Importante", JOptionPane.INFORMATION_MESSAGE);
            
            // Loop principal.
            while (true) {
                //Durante toda a axecução do programa o Servidor aguardará um cliente tentar se conectar               
                Socket conexao = server.accept(); //Recebe a conexão
                // cria uma nova thread para tratar essa conexão
                Thread t = new Server(conexao);
                t.start();
                
            }
        } catch (IOException e) {
            // Captura IOExcepetion (Normamente quando o servidor já está aberto e algum cliente tenta abri-lo novamente
            System.out.println("Ops" + Constantes.separador  + e);
            
        }
    }
    
}
