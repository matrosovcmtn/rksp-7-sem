package ru.matrosov.prac_04;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;

public class LibraryClient {
    private final RSocket socket;

    public LibraryClient() {
        this.socket = RSocketConnector.create()
                .connect(TcpClientTransport.create("localhost", 7000))
                .block();
    }

    // Request-Response: Add a book
    public void addBook(String bookInfo) {
        socket.requestResponse(DefaultPayload.create(bookInfo))
                .map(Payload::getDataUtf8)
                .doOnNext(response -> System.out.println("Server response: " + response))
                .block();
    }

    // Request-Stream: Get all books
    public void getAllBooks() {
        socket.requestStream(DefaultPayload.create(""))
                .map(Payload::getDataUtf8)
                .doOnNext(System.out::println)
                .blockLast();
    }

    // Fire-and-Forget: Log an action
    public void logAction(String action) {
        socket.fireAndForget(DefaultPayload.create(action))
                .block();
    }

    // Channel: Real-time book availability updates
    public void monitorBookAvailability(String bookTitle) {
        Flux<Payload> requests = Flux.just(DefaultPayload.create(bookTitle));
        socket.requestChannel(requests)
                .map(Payload::getDataUtf8)
                .doOnNext(System.out::println)
                .take(10) // Limit to 10 updates
                .blockLast();
    }

    public static void main(String[] args) {
        LibraryClient client = new LibraryClient();

        client.addBook("1984 by George Orwell");
        client.addBook("Aibolit by Chaikovskiy");

        client.getAllBooks();

        client.logAction("User viewed book list");

        client.monitorBookAvailability("1984 by George Orwell");
        client.monitorBookAvailability("Aibolit by Chaikovskiy");
    }
}
