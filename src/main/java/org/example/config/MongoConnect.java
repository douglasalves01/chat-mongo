package org.example.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnect {
    public static MongoCollection<Document> connectionDB() {
        // Inclui as credenciais de autenticação (usuário e senha)
        MongoClient client = MongoClients.create("mongodb://admin:123@localhost:27017");
        MongoDatabase database = client.getDatabase("chatMongo");
        MongoCollection<Document> collection = database.getCollection("messages");
        return collection;
    }


}