package ai.rodolfomendes.travel.chat.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDbConfiguration {
    @Bean
    public MongoClient getMongoClient() {
        return MongoClients.create("mongodb://localhost:27017/");
    }
}
