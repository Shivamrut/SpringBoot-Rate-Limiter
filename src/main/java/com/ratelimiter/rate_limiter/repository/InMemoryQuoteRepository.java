package com.ratelimiter.rate_limiter.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Repository;

import com.ratelimiter.rate_limiter.domain.BatchQuotes;
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
        quotesMap.put("q-1021", new Quote("q-1021", "Code is like humor. When you have to explain it, it's bad.", "Cory House", java.time.Instant.now()));
        quotesMap.put("q-1022", new Quote("q-1022", "Experience is the name everyone gives to their mistakes.", "Oscar Wilde", java.time.Instant.now()));
        quotesMap.put("q-1023", new Quote("q-1023", "It's not a bug, it's an undocumented feature.", "Anonymous", java.time.Instant.now()));
        quotesMap.put("q-1024", new Quote("q-1024", "The best error message is the one that never shows up.", "Thomas Fuchs", java.time.Instant.now()));
        quotesMap.put("q-1025", new Quote("q-1025", "Deleted code is debugged code.", "Anonymous", java.time.Instant.now()));
        quotesMap.put("q-1026", new Quote("q-1026", "A good programmer is someone who always looks both ways before crossing a one-way street.", "Doug Linder", java.time.Instant.now()));
        quotesMap.put("q-1027", new Quote("q-1027", "Programming isn't about what you know; it's about what you can figure out.", "Chris Pine", java.time.Instant.now()));
        quotesMap.put("q-1028", new Quote("q-1028", "The most effective debugging tool is still careful thought, coupled with a judiciously placed print statement.", "Brian Kernighan", java.time.Instant.now()));
        quotesMap.put("q-1029", new Quote("q-1029", "Controlling complexity is the essence of computer programming.", "Brian Kernighan", java.time.Instant.now()));
        quotesMap.put("q-1030", new Quote("q-1030", "Debugging is twice as hard as writing the code in the first place.", "Brian Kernighan", java.time.Instant.now()));
        quotesMap.put("q-1031", new Quote("q-1031", "If builders built buildings the way programmers wrote programs, the first woodpecker would destroy civilization.", "Gerald Weinberg", java.time.Instant.now()));
        quotesMap.put("q-1032", new Quote("q-1032", "The cheapest, fastest, and most reliable components are those that aren't there.", "Gordon Bell", java.time.Instant.now()));
        quotesMap.put("q-1033", new Quote("q-1033", "Before software can be reusable it first has to be usable.", "Ralph Johnson", java.time.Instant.now()));
        quotesMap.put("q-1034", new Quote("q-1034", "The best way to get a project done faster is to start sooner.", "Fred Brooks", java.time.Instant.now()));
        quotesMap.put("q-1035", new Quote("q-1035", "Nine people can't make a baby in a month.", "Fred Brooks", java.time.Instant.now()));
        quotesMap.put("q-1036", new Quote("q-1036", "Good code is its own best documentation.", "Steve McConnell", java.time.Instant.now()));
        quotesMap.put("q-1037", new Quote("q-1037", "The function of good software is to make the complex appear to be simple.", "Grady Booch", java.time.Instant.now()));
        quotesMap.put("q-1038", new Quote("q-1038", "Simplicity carried to the extreme becomes elegance.", "Jon Franklin", java.time.Instant.now()));
        quotesMap.put("q-1039", new Quote("q-1039", "Weeks of coding can save you hours of planning.", "Anonymous", java.time.Instant.now()));
        quotesMap.put("q-1040", new Quote("q-1040", "One man's crappy software is another man's full-time job.", "Jessica Gaston", java.time.Instant.now()));
        quotesMap.put("q-1041", new Quote("q-1041", "Always code as if the person who ends up maintaining your code is a violent psychopath who knows where you live.", "Martin Golding", java.time.Instant.now()));
        quotesMap.put("q-1042", new Quote("q-1042", "Simplicity is the soul of efficiency.", "Austin Freeman", java.time.Instant.now()));
        quotesMap.put("q-1043", new Quote("q-1043", "Programs must be written for people to read, and only incidentally for machines to execute.", "Harold Abelson", java.time.Instant.now()));
        quotesMap.put("q-1044", new Quote("q-1044", "When in doubt, use brute force.", "Ken Thompson", java.time.Instant.now()));
        quotesMap.put("q-1045", new Quote("q-1045", "The only way to learn a new programming language is by writing programs in it.", "Dennis Ritchie", java.time.Instant.now()));
        quotesMap.put("q-1046", new Quote("q-1046", "Small minds are concerned with the extraordinary, great minds with the ordinary.", "Blaise Pascal", java.time.Instant.now()));
        quotesMap.put("q-1047", new Quote("q-1047", "The best thing about a boolean is even if you are wrong, you are only off by a bit.", "Anonymous", java.time.Instant.now()));
        quotesMap.put("q-1048", new Quote("q-1048", "Without requirements or design, programming is the art of adding bugs to an empty text file.", "Louis Srygley", java.time.Instant.now()));
        quotesMap.put("q-1049", new Quote("q-1049", "Walking on water and developing software from a specification are easy if both are frozen.", "Edward V. Berard", java.time.Instant.now()));
        quotesMap.put("q-1050", new Quote("q-1050", "Rate limiting protects shared systems from unfair usage.", "PulseAPI", java.time.Instant.now()));
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
    
    @Override
    public Optional<Quote> getQuoteById(String id) {
        return Optional.ofNullable(quotesMap.get(id));
    }

    @Override
    public BatchQuotes fetchQuotes(List<String> ids) {
        BatchQuotes quotes = new BatchQuotes();
        List<Quote> quotesFound = new ArrayList<>();
        List<String> notFound = new ArrayList<>();
        for(String id : ids){
            Quote quote = quotesMap.get(id);
            if(quote!=null){
                quotesFound.add(quote);
            }
            else {
                notFound.add(id);
            }
        }
        quotes.setQuotes(quotesFound);
        quotes.setFound(quotesFound.size());
        quotes.setNotFound(notFound);
        return quotes;
    }

}
