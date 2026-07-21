package com.ratelimiter.rate_limiter.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ratelimiter.rate_limiter.domain.BatchQuotes;
import com.ratelimiter.rate_limiter.domain.Quote;
import com.ratelimiter.rate_limiter.domain.enums.QuoteStatus;
import com.ratelimiter.rate_limiter.dto.response.BatchQuotesResponse;
import com.ratelimiter.rate_limiter.dto.response.CreateQuoteResponse;
import com.ratelimiter.rate_limiter.dto.response.QuoteResponse;

@Component
public class QuoteMapper {
    public QuoteResponse toQuoteResponse(Quote quote) {
        
        return QuoteResponse.builder()
                            .author(quote.getAuthor())
                            .text(quote.getText())
                            .id(quote.getId())
                            .build();
        
    }
    public CreateQuoteResponse toCreateQuoteResponse(Quote quote) {
        
        return CreateQuoteResponse.builder()
                            .author(quote.getAuthor())
                            .text(quote.getText())
                            .id(quote.getId())
                            .status(QuoteStatus.ACCEPTED)
                            .build();
    }

    public BatchQuotesResponse toBatchQuotesResponse(BatchQuotes quotes){
        List<QuoteResponse> quoteList = new ArrayList<>(quotes.getQuotes().stream()
                                            .map(this::toQuoteResponse).toList());
        for(String id: quotes.getNotFound()){
            quoteList.add(toNotFoundResponse(id));
        }
        return BatchQuotesResponse.builder()
                                .results(quoteList)
                                .found(quotes.getFound())
                                .notFound(quotes.getNotFound().size())
                                .build();
                    
    }

    private QuoteResponse toNotFoundResponse(String id) {
        return QuoteResponse.builder()
                        .id(id)
                        .error("not_found")
                        .build();   
    }
}
