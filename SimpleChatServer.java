import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleChatServer{
  ArrayList subscribedClientsOutputStreams;
  
  public class ClientHandler implements Runnable{
    BufferedReader reader;
    Socket clientSocket;
    
    public ClientHandler(Socket cSocket){
      try{
        clientSocket = cSocket;
        InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
        reader = new BufferedReader(isReader);
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
    
    
    public void run(){
      String message;
      try{
        while((message = reader.readLine()) != null){
          System.out.println("read " + message);
          broadcast(message);
        }
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
    
    
  }
  
  public void broadcast(String message){
    /*
     * Sends message to all clients connected to the server
     */
    Iterator it = subscribedClientsOutputStreams.iterator();    
    while(it.hasNext()){
      try{
        PrintWriter writer = (PrintWriter) it.next();
        writer.println(message);
        writer.flush();
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
  }
  
  public void go(){
    subscribedClientsOutputStreams = new ArrayList();
    try{
      ServerSocket serverSocket = new ServerSocket(5000);
      
      while(true){
        Socket acceptedClientSocket = serverSocket.accept();
        PrintWriter writer = new PrintWriter(acceptedClientSocket.getOutputStream());
        subscribedClientsOutputStreams.add(writer);
        
        Thread t = new Thread(new ClientHandler(acceptedClientSocket));
        t.start();
        System.out.println("got a connection");
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
  }
  
  
  public static void main(String[] args){
    new SimpleChatServer().go();
  }
  
  
  
  
}