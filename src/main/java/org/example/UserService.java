package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class UserService {
    private final MongoCollection<Document> users;

    public UserService() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        users = db.getCollection("users");
    }

    public boolean register(String username, String password) {
        if (users.find(eq("username", username)).first() != null) {
            return false;
        }
        Document user = new Document("username", username)
                .append("password", password);
        users.insertOne(user);
        return true;
    }

    public boolean login(String username, String password) {
        Document user = users.find(eq("username", username)).first();
        return user != null && user.getString("password").equals(password);
    }
}
