package ru.matrosov.prac_04;

import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.TcpServerTransport;

public class LibraryServer {
    public void start() {
        RSocketServer.create(SocketAcceptor.with(new LibrarySocketAcceptor()))
                .bindNow(TcpServerTransport.create("localhost", 7000))
                .onClose()
                .block();
        System.out.println("Server started on port 7000");
    }

    public static void main(String[] args) {
        new LibraryServer().start();
    }
}
