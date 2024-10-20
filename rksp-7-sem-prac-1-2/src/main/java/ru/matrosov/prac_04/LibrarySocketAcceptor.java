package ru.matrosov.prac_04;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class LibrarySocketAcceptor implements RSocket {

    private final List<Book> books = new ArrayList<>();

    // Request-Response: Add a book
    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        String bookInfo = payload.getDataUtf8();
        Book newBook = new Book(bookInfo);
        books.add(newBook);
        return Mono.just(DefaultPayload.create("Book added: " + bookInfo));
    }

    // Request-Stream: Get all books
    @Override
    public Flux<Payload> requestStream(Payload payload) {
        return Flux.fromIterable(books)
                .map(book -> DefaultPayload.create(book.toString()));
    }

    // Fire-and-Forget: Log an action
    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        System.out.println("Action logged: " + payload.getDataUtf8());
        return Mono.empty();
    }

    // Channel: Real-time book availability updates
    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return Flux.from(payloads)
                .flatMap(payload -> {
                    String bookTitle = payload.getDataUtf8();
                    return Flux.interval(Duration.ofSeconds(1))
                            .map(tick -> {
                                boolean isAvailable = Math.random() < 0.5;
                                return DefaultPayload.create(
                                        bookTitle + " is " + (isAvailable ? "available" : "unavailable")
                                );
                            });
                });
    }
}

