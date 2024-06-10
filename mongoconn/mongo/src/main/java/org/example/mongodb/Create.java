package org.example.mongodb;

import org.example.mongodb.connection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Random;
import java.util.List;

public class Create {
    public static void main(String[] args) {
        String connectionString = "mongodb+srv://vaidikparashar:Vasu1234@cluster0.z7dnfok.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");

            Random rand = new Random();
            Document student = new Document("_id", new ObjectId());
            student.append("student_id", 10000d)
                    .append("class_id", 1d)
                    .append("scores", List.of(
                            new Document("type", "exam").append("score", rand.nextDouble() * 100),
                            new Document("type", "quiz").append("score", rand.nextDouble() * 100),
                            new Document("type", "homework").append("score", rand.nextDouble() * 100),
                            new Document("type", "homework").append("score", rand.nextDouble() * 100)
                    ));

            gradesCollection.insertOne(student);

            System.out.println("Document inserted successfully!");
            //read command
            System.out.println(student);

            // Update the inserted document
            Document filter = new Document("student_id", 10000);
            Document update = new Document("$set", new Document("class_id", 2));
            gradesCollection.updateOne(filter, update);

            // Print the updated document
            Document updatedDocument = gradesCollection.find(filter).first();
            System.out.println("Updated Document: " + updatedDocument.toJson());

            //to delete the filter use deleteOne
            gradesCollection.deleteOne(filter);
        }
    }
}



