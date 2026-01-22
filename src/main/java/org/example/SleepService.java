package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class SleepService {

    private final MongoCollection<Document> usersCollection;

    public SleepService(MongoDatabase database) {
        this.usersCollection = database.getCollection("users");
    }

    public void addSleep(String username, double hours) {
        Document userDoc = usersCollection.find(eq("username", username)).first();

        if (userDoc != null) {
            List<Number> hoursList = userDoc.containsKey("sleepHours")
                    ? (List<Number>) userDoc.get("sleepHours")
                    : new ArrayList<>();

            hoursList.add(hours);

            double avg = computeAverage(hoursList);
            usersCollection.updateOne(eq("username", username),
                    new Document("$set", new Document("sleepHours", hoursList)
                            .append("sleepAverage", avg)));

        } else {
            List<Number> hoursList = new ArrayList<>();
            hoursList.add(hours);
            Document newUser = new Document("username", username)
                    .append("sleepHours", hoursList)
                    .append("sleepAverage", hours);
            usersCollection.insertOne(newUser);
        }
    }

    public List<Number> getSleepData(String username) {
        Document userDoc = usersCollection.find(eq("username", username)).first();
        if (userDoc != null && userDoc.containsKey("sleepHours")) {
            return (List<Number>) userDoc.get("sleepHours");
        }
        return new ArrayList<>();
    }

    public Double getAverage(String username) {
        Document userDoc = usersCollection.find(eq("username", username)).first();
        if (userDoc != null && userDoc.containsKey("sleepAverage")) {
            Object val = userDoc.get("sleepAverage");
            if (val instanceof Number) {
                return ((Number) val).doubleValue();
            }
            if (val != null) {
                try {
                    return Double.parseDouble(val.toString());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private double computeAverage(List<Number> data) {
        if (data.isEmpty()) return 0;
        double sum = 0;
        for (Number h : data) sum += h.doubleValue();
        return sum / data.size();
    }
}
