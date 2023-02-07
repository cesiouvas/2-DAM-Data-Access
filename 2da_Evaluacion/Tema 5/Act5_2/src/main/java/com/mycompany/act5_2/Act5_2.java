package com.mycompany.act5_2;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import java.util.Arrays;
import java.util.Date;
import org.bson.Document;
import org.bson.conversions.Bson;

/*
 * @author Sergio Castillo Llorens
 */
public class Act5_2 {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();

        //CONNECTION TO DB
        MongoDatabase db = client.getDatabase("act5_2");
        String colName = "user";
        MongoCollection<Document> users = db.getCollection(colName);

        //DROP COLLECTION
        users.drop();

        //Get documents
        System.out.println("Number of Documents " + users.countDocuments());

        //1. INSERT 2 USERS
        Document newUser = new Document()
                .append("_id", 5L)
                .append("name", "Juan")
                .append("surname", "García Castellano")
                .append("age", 23)
                .append("gender", "male")
                .append("registrationDate", new Date());

        Document newUser2 = new Document()
                .append("_id", 6L)
                .append("name", "Beatriz")
                .append("surname", "Perez Solaz")
                .append("age", 27)
                .append("gender", "female")
                .append("registrationDate", new Date());

        users.insertMany(Arrays.asList(newUser, newUser2));
        System.out.println("Number of documents " + users.countDocuments());

        //2. RETRIEVE ALL DOCUMENTS
        System.out.println("\n1. INSERT 2 USERS");
        mostrar(users);

        //3. INSERT USER WITH GROUPS
        Document newUser3 = new Document()
                .append("_id", 7L)
                .append("name", "Jorge")
                .append("surname", "Lopez Sevilla")
                .append("gender", "male")
                .append("groups", Arrays.asList("basketball", "kitchen", "historical novel"))
                .append("registrationDate", new Date());

        users.insertOne(newUser3);
        System.out.println("\n3. INSERT USER WITH GROUPS");
        mostrar(users);

        //4. DELETE JUAN GARCIA
        Bson filterDoc = eq("_id", 5L);
        users.deleteOne(filterDoc);
        System.out.println("\n4. DELETE JUAN GARCIA");
        mostrar(users);

        //5. UPDATE BEATRIZ GROUP
        filterDoc = eq("_id", 6L);
        Bson updateDoc = set("groups", Arrays.asList("historical novel", "dance"));
        
        users.updateOne(filterDoc, updateDoc);
        System.out.println("\n5. UPDATE BEATRIZ GROUP");
        mostrar(users);

        //6. INSERT GARDENING GARDENIA
        colName = "company";
        MongoCollection<Document> company = db.getCollection(colName);
        company.drop();

        Document newCompany = new Document()
                .append("_id", 10L)
                .append("name", "Gardening Gardenia");

        company.insertOne(newCompany);
        System.out.println("\n6. INSERT GARDENING GARDENIA");
        mostrar(company);

        //7. UPDATE COMPANY
        filterDoc = eq("_id", 10L);
        updateDoc = and(set("address", new Document()
                .append("street", "Palmeras")
                .append("number", 8)
                .append("town", "Torrente")),
        set("sector", "services"), set("web", "http://www.gardeninggardenia.com"));
        
        company.updateOne(filterDoc, updateDoc);
        System.out.println("\n7. ADD ADDRESS TO THE COMPANY");
        mostrar(company);
        
        //8. ADD 5 FOLLOWERS TO THE COMPANY
        filterDoc = eq("name", "Gardening Gardenia");
        updateDoc = set("followers", 5);
        company.updateOne(filterDoc, updateDoc);
        System.out.println("\n8. ADD 5 FOLLOWERS TO THE COMPANY");
        mostrar(company);

        //INCREMENT FOLLOWERS
        filterDoc = eq("name", "Gardening Gardenia");
        updateDoc = inc("followers", 2);
        
        company.updateOne(filterDoc, updateDoc);
        System.out.println("INCREMENT FOLLOWERS");
        mostrar(company);

        //DECREASE FOLLOWERS
        filterDoc = eq("name", "Gardening Gardenia");
        updateDoc = inc("followers", -1);
        
        company.updateOne(filterDoc, updateDoc);
        System.out.println("DECREASE FOLLOWERS");
        mostrar(company);

        //9. UPDATE THE ADDRESS
        filterDoc = eq("name", "Gardening Gardenia");
        updateDoc = set("address.postal_code", 46009);
        
        company.updateOne(filterDoc, updateDoc);
        System.out.println("\n9. ADD POSTAL CODE TO THE COMPANY");
        mostrar(company);
        
        //10. ELIMINATE FIELD SECTOR
        filterDoc = eq("name", "Gardening Gardenia");
        updateDoc = unset("sector");
        
        company.updateOne(filterDoc, updateDoc);
        System.out.println("\n10. ELIMINATE FIELD SECTOR");
        mostrar(company);

        //11. ADD BEATRIZ TO GROUP THEATER
        filterDoc = eq("_id", 6L);
        updateDoc = push("groups", "theater");
        
        users.updateOne(filterDoc, updateDoc);
        System.out.println("\n11. ADD BEATRIZ TO GROUP THEATER");
        mostrar(users);

        //12. REMOVE BEATRIZ FROM GROUP DANCE
        filterDoc = eq("_id", 6L);
        updateDoc = pull("groups", "dance");
        
        users.updateOne(filterDoc, updateDoc);
        System.out.println("\n12. REMOVE BEATRIZ FROM GROUP DANCE");
        mostrar(users);

        //13.1 INSERT A NEW COMMENT
        filterDoc = and(eq("name", "Jorge"), eq("surname", "Lopez Sevilla"));
        updateDoc = and(set("comments", new Document()
                .append("title", "comment 1")
                .append("text", "example of comment")
                .append("group", "historical novel")
                .append("date", new Date())),
        inc("total_comments", 1));
        
        users.updateOne(filterDoc, updateDoc);
        System.out.println("\n13.1. JORGE INSERTS A NEW COMMENT");
        mostrar(users);
        
        //13.2 INSERT ANOTHER COMMENT
        /*
        filterDoc = and(eq("name", "Jorge"), eq("surname", "Lopez Sevilla"));
        updateDoc = and(push("comments", new Document()
                .append("title", "comment 2")
                .append("text", "example of comment 2")
                .append("group", "basketball")
                .append("date", new Date())),
        inc("total_comments", 1));
        
        users.updateOne(filterDoc, updateDoc);
        System.out.println("\n13.2. INSERT A NEW COMMENT");
        mostrar(users);
        */

        //14. SELECTS
        //FILTER
        Document projectDoc = new Document()
                .append("name", 1)
                .append("surname", 1)
                .append("age", 1)
                .append("_id", 1);
        FindIterable<Document> findRes;

        //14.1
        filterDoc = and(gt("age", 25), eq("groups", "historical novel"));
        findRes = users.find(filterDoc).projection(projectDoc);

        System.out.println("\nQUERY 1");
        for (Document doc : findRes) {
            System.out.println(doc.toString());
        }

        //14.2
        projectDoc = new Document()
                .append("name", 1)
                .append("surname", 1)
                .append("age", 1);
        findRes = users.find().sort(projectDoc);

        filterDoc = exists("groups.1", true);

        System.out.println("\nQUERY 2");
        for (Document doc : findRes) {
            System.out.println(doc.toString());
        }

        //14.3
        filterDoc = all("groups", Arrays.asList("historical novel", "theater"));
        
        findRes = users.find(filterDoc).projection(projectDoc);
        System.out.println("\nQUERY 3");
        for (Document doc : findRes) {
            System.out.println(doc.toString());
        }

        //14.4
        filterDoc = exists("comments", true);
        findRes = users.find(filterDoc).projection(projectDoc);
        System.out.println("\nQUERY 4");
        for (Document doc : findRes) {
            System.out.println(doc.toString());
        }

        //14.5
        projectDoc = new Document()
                .append("name", 1);
        filterDoc = and(eq("address.town", "Torrente"), eq("followers", 0));
        
        findRes = company.find(filterDoc).projection(projectDoc);
        System.out.println("\nQUERY 5");
        for (Document doc : findRes) {
            System.out.println(doc.toString());
        }

        //14.6
        filterDoc = and(eq("address.town", "Torrente"), gt("followers", 5));
        
        findRes = company.find(filterDoc).projection(projectDoc);
        System.out.println("\nQUERY 6");
        for (Document doc : findRes) {
            System.out.println(doc.toString());
        }

    }

    public static void mostrar(MongoCollection<Document> col) {
        FindIterable<Document> findRes = col.find();
        for (Document r : findRes) {
            System.out.println(r.toString());
        }
    }
}
