package com.scotch.OARKit.java.helpers;

import com.scotch.OARKit.java.Command.Commands;
import com.scotch.OARKit.java.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnect {
    //FOR DEFAULT CONNECTION
    private Socket socket;
    PrintWriter out;
    BufferedReader in;
    BufferedReader stdIn;
    public static boolean connected = false;
    @Deprecated
    public ServerConnect(){
        if(Main.properties.getProperty("insideDev").equals("true")){
            try {
                socket = new Socket("127.0.0.1",5006);
                Logger.info("Starting Server on Local Host");
                setUp();
            } catch (IOException e) {
                Logger.error("Problem Connecting to Local Host - Is the server up?");
                Logger.error("Please Specify Your Own IP");
            }
        }else{
            try {
                socket = new Socket("192.168.100.1",5006);
                Logger.info("Starting Server on Default Remote");
                setUp();
            } catch (IOException e) {
                Logger.error("Problem Connecting to Default Remote Host - Is the server up?");
                Logger.error("Please Specify Your Own IP");
            }
        }
        Logger.info("This method going to be removed, please use ServerConnect(String ip, int port)");
    }
    //FOR DIFFERING IP AND PORT
    public ServerConnect(String ip, String port){
        try {
            socket = new Socket(ip, Integer.parseInt(port));
            Logger.info("Connecting to Server...");
            setUp();
        } catch (IOException e) {
            Logger.error("Problem Connecting to Specified Host - Is the server up or is it configured correctly?");
        }
    }
    //FOR DIFFERING IP AND SAME PORT - 5006
    @Deprecated
    public ServerConnect(String ip){

    }
    private void setUp() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        connected = true;
    }

    public void socketClose(){
        try {
            socket.close();
            out.close();
            in.close();
            stdIn.close();
            connected = false;
        } catch (Exception e) {
            Logger.error("Problem Closing Socket - Is the server still up?");
        }
    }

    public boolean sendData(String data){
        if(connected){
            out.print(data);
            out.flush();
            return true;
        } else {
            Logger.error("Socket is not connected");
            return false;
        }
    }
}
