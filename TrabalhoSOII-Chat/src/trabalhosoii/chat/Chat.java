/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package trabalhosoii.chat;

import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author 
 * Lucas Eduardo S. Mantovani
 * Leonardo Yudi 
 */
public class Chat {
    private Socket conexao;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
        
            AbrirServer abrir = new AbrirServer();
            abrir.start();
            FrameClient cliente = new FrameClient();
            cliente.run();
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(),"Erro Inesperado",JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
