package com.addiction.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.timeseries.Granularity;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {

    public static final String COLLECTION_NAME = "cigarette_history";

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initCollections() {
        if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
            CollectionOptions options = CollectionOptions.empty()
                    .timeSeries(
                            CollectionOptions.TimeSeriesOptions.timeSeries("smokeDate")
                                    .metaField("userId")
                                    .granularity(Granularity.HOURS)
                    );
            mongoTemplate.createCollection(COLLECTION_NAME, options);
        }
    }
}
