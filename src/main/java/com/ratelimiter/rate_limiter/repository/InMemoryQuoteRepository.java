package com.ratelimiter.rate_limiter.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Repository;

import com.ratelimiter.rate_limiter.domain.Quote;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class InMemoryQuoteRepository implements QuoteRepository {

    private final Map<String,Quote> quotesMap = new ConcurrentHashMap<>();

    private void loadSeedData(){
        quotesMap.put("q-1001", new Quote("q-1001", "Simplicity is the soul of efficiency.", "Austin Freeman", java.time.Instant.now()));
        quotesMap.put("q-1002", new Quote("q-1002", "The best way to predict the future is to invent it.", "Alan Kay", java.time.Instant.now()));
        quotesMap.put("q-1003", new Quote("q-1003", "Any fool can write code that a computer can understand. Good programmers write code that humans can understand.", "Martin Fowler", java.time.Instant.now()));
        quotesMap.put("q-1004", new Quote("q-1004", "First, solve the problem. Then, write the code.", "John Johnson", java.time.Instant.now()));
        quotesMap.put("q-1005", new Quote("q-1005", "Talk is cheap. Show me the code.", "Linus Torvalds", java.time.Instant.now()));
        quotesMap.put("q-1006", new Quote("q-1006", "Premature optimization is the root of all evil.", "Donald Knuth", java.time.Instant.now()));
        quotesMap.put("q-1007", new Quote("q-1007", "Programs must be written for people to read, and only incidentally for machines to execute.", "Harold Abelson", java.time.Instant.now()));
        quotesMap.put("q-1008", new Quote("q-1008", "Truth can only be found in one place: the code.", "Robert C. Martin", java.time.Instant.now()));
        quotesMap.put("q-1009", new Quote("q-1009", "Make it work, make it right, make it fast.", "Kent Beck", java.time.Instant.now()));
        quotesMap.put("q-1010", new Quote("q-1010", "Walking on water and developing software from a specification are easy if both are frozen.", "Edward V. Berard", java.time.Instant.now()));
        quotesMap.put("q-1011", new Quote("q-1011", "The most disastrous thing that you can do is learn your first programming language so well that you think it's the only one.", "Alan Perlis", java.time.Instant.now()));
        quotesMap.put("q-1012", new Quote("q-1012", "Software is a great combination between artistry and engineering.", "Bill Gates", java.time.Instant.now()));
        quotesMap.put("q-1013", new Quote("q-1013", "There are only two hard things in Computer Science: cache invalidation and naming things.", "Phil Karlton", java.time.Instant.now()));
        quotesMap.put("q-1014", new Quote("q-1014", "Good design adds value faster than it adds cost.", "Thomas C. Gale", java.time.Instant.now()));
        quotesMap.put("q-1015", new Quote("q-1015", "Computers are good at following instructions, but not at reading your mind.", "Donald Knuth", java.time.Instant.now()));
        quotesMap.put("q-1016", new Quote("q-1016", "Perfection is achieved, not when there is nothing more to add, but when there is nothing left to take away.", "Antoine de Saint-Exupéry", java.time.Instant.now()));
        quotesMap.put("q-1017", new Quote("q-1017", "Fix the cause, not the symptom.", "Steve Maguire", java.time.Instant.now()));
        quotesMap.put("q-1018", new Quote("q-1018", "Simplicity is prerequisite for reliability.", "Edsger W. Dijkstra", java.time.Instant.now()));
        quotesMap.put("q-1019", new Quote("q-1019", "Measuring programming progress by lines of code is like measuring aircraft building progress by weight.", "Bill Gates", java.time.Instant.now()));
        quotesMap.put("q-1020", new Quote("q-1020", "The system of nature, of which man is a part, tends to be self-balancing, self-adjusting, self-cleansing.", "E. F. Schumacher", java.time.Instant.now()));
    }

    public InMemoryQuoteRepository() {
        loadSeedData();
    }

    @Override
    public Optional<Quote> getRandomQuote() {
        if(quotesMap.isEmpty()){
            return  Optional.empty();
        }
        List<Quote> quotes = new ArrayList<>(quotesMap.values());
        int randomIndex = ThreadLocalRandom.current().nextInt(quotes.size());
        return Optional.of(quotes.get(randomIndex));
    }

    @Override
    public Quote createQuote(Quote quote) {
        quotesMap.put(quote.getId(), quote);
        return quote;
    }
    
}
