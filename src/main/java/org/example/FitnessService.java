package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class FitnessService {

    private final MongoCollection<Document> usersCollection;

    public FitnessService(MongoDatabase database) {
        this.usersCollection = database.getCollection("users");
    }

    public void addActivity(String username, String aktivnost, int trajanje, String intenzitet) {
        Document userDoc = usersCollection.find(eq("username", username)).first();
        String today = LocalDate.now().toString();

        int kalorije = calculateCalories(trajanje, intenzitet);

        if (userDoc != null) {
            List<Document> fitnessList = userDoc.containsKey("fitnessEntries") ?
                    (List<Document>) userDoc.get("fitnessEntries") :
                    new ArrayList<>();

            Document newEntry = new Document("date", today)
                    .append("aktivnost", aktivnost)
                    .append("trajanje", trajanje)
                    .append("intenzitet", intenzitet)
                    .append("kalorije", kalorije);
            fitnessList.add(newEntry);

            int total = computeDailyTotal(fitnessList, today);

            usersCollection.updateOne(eq("username", username),
                    new Document("$set", new Document("fitnessEntries", fitnessList)
                            .append("fitnessCalories", total)));

        } else {
            List<Document> fitnessList = new ArrayList<>();
            fitnessList.add(new Document("date", today)
                    .append("aktivnost", aktivnost)
                    .append("trajanje", trajanje)
                    .append("intenzitet", intenzitet)
                    .append("kalorije", kalorije));

            Document newUser = new Document("username", username)
                    .append("fitnessEntries", fitnessList)
                    .append("fitnessCalories", kalorije);
            usersCollection.insertOne(newUser);
        }
    }

    public List<FitnessEntry> getFitnessData(String username) {
        Document userDoc = usersCollection.find(eq("username", username)).first();
        List<FitnessEntry> result = new ArrayList<>();
        if (userDoc != null && userDoc.containsKey("fitnessEntries")) {
            List<Document> docs = (List<Document>) userDoc.get("fitnessEntries");
            for (Document d : docs) {
                result.add(new FitnessEntry(
                        d.getString("aktivnost"),
                        d.getInteger("trajanje"),
                        d.getString("intenzitet"),
                        d.getInteger("kalorije"),
                        d.getString("date")
                ));
            }
        }
        return result;
    }

    public Integer getDailyCalories(String username) {
        Document userDoc = usersCollection.find(eq("username", username)).first();
        if (userDoc != null && userDoc.containsKey("fitnessCalories")) {
            return userDoc.getInteger("fitnessCalories");
        }
        return 0;
    }

    private int computeDailyTotal(List<Document> fitnessList, String date) {
        int sum = 0;
        for (Document f : fitnessList) {
            if (date.equals(f.getString("date"))) {
                sum += f.getInteger("kalorije");
            }
        }
        return sum;
    }

    private int calculateCalories(int trajanje, String intenzitet) {
        int factor;
        switch (intenzitet.toLowerCase()) {
            case "high": factor = 12; break;
            case "medium": factor = 8; break;
            default: factor = 5; break;
        }
        return trajanje * factor; // jednostavna formula
    }
}

