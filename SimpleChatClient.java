import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleChatClient{
  JTextArea incoming;
  JTextField outgoing;
  BufferedReader reader;
  PrintWriter writer;
  Socket serverSocket;
  String name = "Anonymous";
  
  public SimpleChatClient(){};
  public SimpleChatClient(String n){name = n;}
  
  
  public void go(){
    JFrame frame = new JFrame("Simple chat client");
    JPanel mainPanel = new JPanel();
    incoming = new JTextArea(15,30);
    incoming.setLineWrap(true);
    incoming.setWrapStyleWord(true);
    incoming.setEditable(false);
    JScrollPane qScroller = new JScrollPane(incoming);
    qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    outgoing = new JTextField(20);
    JButton sendButton = new JButton("Send");
    sendButton.addActionListener(new SendButtonListener());
    mainPanel.add(qScroller);
    mainPanel.add(outgoing);
    mainPanel.add(sendButton);
    setUpNetworking(); 
    
    Thread readerThread = new Thread(new IncomingReader());
    readerThread.start();
    
    frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
    frame.setSize(400, 500);
    frame.setVisible(true);
  }
  
  public void setUpNetworking(){
    try{
      serverSocket = new Socket("127.0.0.1", 5000);
      InputStreamReader iStreamReader = new InputStreamReader(serverSocket.getInputStream());
      reader = new BufferedReader(iStreamReader);
      writer = new PrintWriter(serverSocket.getOutputStream());
      System.out.println("Networking established");
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  public class SendButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent ev){
      try{
        writer.println(name + ":\t" + outgoing.getText());
        writer.flush();
      }catch(Exception ex){
        ex.printStackTrace();
      }
      outgoing.setText("");
      outgoing.requestFocus();
    }
  }
  
  public class IncomingReader implements Runnable{
    public void run(){
      String message;
      try{
        while((message = reader.readLine()) != null){
          System.out.println("read " + message);
          incoming.append(message + "\n");
          
        }
      }catch(Exception ex){
        ex.printStackTrace();
      }
      
    }
    
  }
  
  public static void main(String[] args){
    SimpleChatClient chatClient;
    if(args.length > 0)
       chatClient = new SimpleChatClient(args[0]);
    else
      chatClient = new SimpleChatClient();
    chatClient.go();
  }
}



