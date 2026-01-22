package org.example.finance;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.MongoDBConnection; // ➤ import glavne konekcije

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class TransactionManager {
    private final MongoCollection<Document> collection;
    private final String username; // ➤ korisnik

    public TransactionManager(String username) {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("transactions");
        this.username = username;
    }

    // ➤ Dodaj transakciju
    public void addTransaction(Transaction t) {
        Document doc = t.toDocument();
        doc.append("username", username); // vežemo uz korisnika
        collection.insertOne(doc);
    }

    // ➤ Dohvati sve transakcije za korisnika
    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> list = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find(eq("username", username)).iterator();

        while (cursor.hasNext()) {
            Document d = cursor.next();
            list.add(new Transaction(
                    d.getString("type"),
                    d.getDouble("amount"),
                    d.getString("description"),
                    d.getString("category")
            ));
        }
        return list;
    }

    // ➤ Ukupni prihodi
    public double getTotalIncome() {
        double total = 0;
        for (Transaction t : getAllTransactions()) {
            if ("Prihod".equals(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // ➤ Ukupni rashodi
    public double getTotalExpense() {
        double total = 0;
        for (Transaction t : getAllTransactions()) {
            if ("Rashod".equals(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // ➤ Rashodi po kategorijama
    public Map<String, Double> getTotalExpenseByCategory() {
        Map<String, Double> categoryExpenses = new HashMap<>();
        categoryExpenses.put("Hrana", 0.0);
        categoryExpenses.put("Racuni", 0.0);
        categoryExpenses.put("Zabava", 0.0);
        categoryExpenses.put("Prevoz", 0.0);
        categoryExpenses.put("Ostalo", 0.0);

        for (Transaction t : getAllTransactions()) {
            if ("Rashod".equals(t.getType())) {
                String category = t.getCategory();
                if (categoryExpenses.containsKey(category)) {
                    categoryExpenses.put(category, categoryExpenses.get(category) + t.getAmount());
                }
            }
        }
        return categoryExpenses;
    }

    // ➤ Brisanje svih transakcija korisnika
    public void clearAllTransactions() {
        collection.deleteMany(eq("username", username));
    }

    // ➤ Brisanje jedne transakcije po indexu
    public void deleteTransaction(int index) {
        ArrayList<Document> docs = collection.find(eq("username", username)).into(new ArrayList<>());
        if (index >= 0 && index < docs.size()) {
            Document toDelete = docs.get(index);
            collection.deleteOne(toDelete);
        }
    }

    // ➤ Ažuriranje transakcije po indexu
    public void updateTransaction(Transaction t, int index) {
        ArrayList<Document> docs = collection.find(eq("username", username)).into(new ArrayList<>());
        if (index >= 0 && index < docs.size()) {
            Document oldDoc = docs.get(index);
            Document newDoc = t.toDocument().append("username", username);
            collection.replaceOne(oldDoc, newDoc);
        }
    }
}
