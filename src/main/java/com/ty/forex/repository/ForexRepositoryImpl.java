package com.ty.forex.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ForexRepositoryImpl implements ForexRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Map<String, String>> findUsdTwdRates(LocalDate start, LocalDate end) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("forex_rates");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Map<String, String>> result = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find(
                Filters.and(
                        Filters.eq("currency", "usd"),
                        Filters.gte("date", start),
                        Filters.lte("date", end)
                )
        ).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Map<String, String> item = new HashMap<>();
                LocalDate date = doc.get("date", LocalDate.class);
                item.put("date", date.format(formatter));
                item.put("usd", doc.get("rate").toString());
                result.add(item);
            }
        }
        return result;
    }
} 