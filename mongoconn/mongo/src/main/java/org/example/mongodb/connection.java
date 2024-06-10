package org.example.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;

import java.util.ArrayList;
import java.util.List;
public class connection {
    public static void main(String[] args){
         String connectionString = "mongodb+srv://vaidikparashar:Vasu1234@cluster0.z7dnfok.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

        try(MongoClient mongoClient = MongoClients.create(connectionString)){
            System.out.println("Connection Successful " + preFlightChecks(mongoClient));
            System.out.println("Print list of databases");
            List<Document> databases  =mongoClient.listDatabases().into(new ArrayList<>());
            databases.forEach(db -> System.out.println(db.toJson()));
        }


    }
    static boolean preFlightChecks(MongoClient mongoClient){
        Document pingCommand = new Document("ping" , 1);
        Document response = mongoClient.getDatabase("admin").runCommand(pingCommand);
        System.out.println("Print result of the '{ping: 1} command'");
        System.out.println(response.toJson(JsonWriterSettings.builder().indent(true).build()));
        return response.get("ok", Number.class).intValue() == 1;


    }
}
