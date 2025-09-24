package ai.rodolfomendes;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        // Connect to MongoDB
        String uri = "mongodb://localhost:27017"; // Replace with your MongoDB URI
        MongoClient mongoClient = MongoClients.create(uri);

        // Access database
        MongoDatabase database = mongoClient.getDatabase("testdb");

        // Access collection
        MongoCollection<Document> collection = database.getCollection("users");

        // Create a document
        Document user = new Document("name", "Alice")
                .append("email", "alice@example.com")
                .append("age", 30);

        // Insert the document
        collection.insertOne(user);

        System.out.println("User inserted successfully!");

        // Close the connection
        mongoClient.close();
    }
}
