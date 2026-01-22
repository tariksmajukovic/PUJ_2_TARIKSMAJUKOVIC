package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PrehranaService {

    private final MongoCollection<Document> usersCollection;

    public PrehranaService(MongoDatabase database) {
        this.usersCollection = database.getCollection("users");
    }

    public void addFood(String username, String naziv, int kalorije) {
        Document userDoc = usersCollection.find(eq("username", username)).first();
        String today = LocalDate.now().toString();

        if (userDoc != null) {
            List<Document> foodList = userDoc.containsKey("foodEntries") ?
                    (List<Document>) userDoc.get("foodEntries") :
                    new ArrayList<>();

            Document newEntry = new Document("date", today)
                    .append("naziv", naziv)
                    .append("kalorije", kalorije);
            foodList.add(newEntry);

            int total = computeDailyTotal(foodList, today);

            usersCollection.updateOne(eq("username", username),
                    new Document("$set", new Document("foodEntries", foodList)
                            .append("dailyCalories", total)));

        } else {
            List<Document> foodList = new ArrayList<>();
            foodList.add(new Document("date", today)
                    .append("naziv", naziv)
                    .append("kalorije", kalorije));

            Document newUser = new Document("username", username)
                    .append("foodEntries", foodList)
                    .append("dailyCalories", kalorije);
            usersCollection.insertOne(newUser);
        }
    }

    public List<FoodEntry> getFoodData(String username) {
        Document userDoc = usersCollection.find(eq("username", username)).first();
        List<FoodEntry> result = new ArrayList<>();
        if (userDoc != null && userDoc.containsKey("foodEntries")) {
            List<Document> docs = (List<Document>) userDoc.get("foodEntries");
            for (Document d : docs) {
                result.add(new FoodEntry(
                        d.getString("naziv"),
                        d.getInteger("kalorije"),
                        d.getString("date")
                ));
            }
        }
        return result;
    }

    public Integer getDailyCalories(String username) {
        Document userDoc = usersCollection.find(eq("username", username)).first();
        if (userDoc != null && userDoc.containsKey("dailyCalories")) {
            return userDoc.getInteger("dailyCalories");
        }
        return 0;
    }

    private int computeDailyTotal(List<Document> foodList, String date) {
        int sum = 0;
        for (Document f : foodList) {
            if (date.equals(f.getString("date"))) {
                sum += f.getInteger("kalorije");
            }
        }
        return sum;
    }
}
