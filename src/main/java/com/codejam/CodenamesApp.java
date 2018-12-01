package com.codejam;

import com.codejam.discovery.DiscoveryThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

@SpringBootApplication
public class CodenamesApp {
    public static void main(String[] args){
        SpringApplication.run(CodenamesApp.class, args);

        Thread discoveryThread = new Thread(DiscoveryThread.getInstance());
        discoveryThread.start();

        try (final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            System.out.println(socket.getLocalAddress().getHostAddress());
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
