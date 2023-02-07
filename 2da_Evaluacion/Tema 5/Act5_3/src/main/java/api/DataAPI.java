package api;

/*
 * @author Sergio Castillo Llorens
 */
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Indexes.*;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Updates.*;
import java.lang.reflect.Array;
import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pojos.Address;
import pojos.Article;
import pojos.Comment;
import pojos.User;

public class DataAPI {

    private static MongoClient client;
    private static MongoDatabase db;

    public static void insertArticle(Article art) {
        MongoCollection<Article> articles = db.getCollection("article", Article.class);
        articles.insertOne(art);
    }

    public static void insertUser(User us) {
        MongoCollection<User> users = db.getCollection("user", User.class);
        users.insertOne(us);
    }

    //Return article by ID
    public static Article findArticle(ObjectId id) {
        MongoCollection<Article> articles = db.getCollection("article", Article.class);
        FindIterable<Article> findResult = articles.find(eq("_id", id));
        Article art = findResult.first();
        return art;
    }

    //Returns all the atricles of the same category
    public static FindIterable<Article> findArticleByCategory(String cat) {
        MongoCollection<Article> articles = db.getCollection("article", Article.class);
        FindIterable<Article> findResult = articles.find(all("categories", cat));
        return findResult;
    }

    //Return all the articles that contains str in its name
    public static FindIterable<Article> findArticleByName(String str) {
        MongoCollection<Article> articles = db.getCollection("article", Article.class);
        FindIterable<Article> findResult = articles.find(regex("name", str));
        return findResult;
    }

    //Returns the articles whose is in the rank
    public static FindIterable<Article> findArticleInPriceRank(double low, double high) {
        MongoCollection<Article> articles = db.getCollection("article", Article.class);
        FindIterable<Article> findResult = articles.find(and(gte("price", low), lte("price", high)));
        return findResult;
    }

    //FindUser
    public static User findUser(ObjectId id) {
        MongoCollection<User> users = db.getCollection("user", User.class);
        FindIterable<User> findResult = users.find(eq("_id", id));
        User us = findResult.first();
        return us;
    }

    //Returns all the users who live in the country specified
    public static FindIterable<User> findUserByCountry(String country) {
        MongoCollection<User> users = db.getCollection("user", User.class);
        FindIterable<User> findResult = users.find(eq("country", country));
        return findResult;
    }

    //Order articles ascendant or descendant
    public static FindIterable<Article> orderByPrice(FindIterable<Article> arts, boolean asc) {
        MongoCollection<Article> articles = db.getCollection("article", Article.class);
        if (asc) {
            arts = articles.find().sort(ascending("price"));
        } else {
            arts = articles.find().sort(descending("price"));
        }
        return arts;
    }

    //Update address of the user
    public static void updateAddress(User us, Address ad) {
        MongoCollection<User> users = db.getCollection("user", User.class);
        Bson filterDoc = eq("_id", us.getId());
        Bson updateDoc = set("address", ad);
        users.updateOne(filterDoc, updateDoc);
    }

    //Update the emails of the user
    public static void updateEmail(User us, String email) {
        MongoCollection<User> users = db.getCollection("user", User.class);
        Bson filterDoc = eq("_id", us.getId());
        Bson updateDoc = set("email", email);
        users.updateOne(filterDoc, updateDoc);
    }

    //Add comment to an article
    public static void addComment(Article art, Comment newCom) {
        if (newCom.getScore() > 5 || newCom.getScore() < 0) {
            System.out.println("Score not valid");
        } else {
            if (newCom.getUser_id() == null) {
                System.out.println("User does not exists");
            } else {
                MongoCollection<Article> articles = db.getCollection("article", Article.class);
                Bson filterDoc = eq("_id", art.getId());
                Bson updateDoc = push("comments", newCom);
                articles.updateOne(filterDoc, updateDoc);
            }
        }
    }

    //Delete article
    public static void deleteArticle(Article art) {
        MongoCollection<Article> articles = db.getCollection("article", Article.class);
        articles.deleteOne(eq("_id", art.getId()));
    }

    //Delete user and the comments from the user
    public static void deleteUser(User us) {
        MongoCollection<User> users = db.getCollection("user", User.class);
        MongoCollection<Article> articles = db.getCollection("article", Article.class);
        users.deleteOne(eq("_id", us.getId()));
        
        articles.updateMany(eq("comments.user_id", us.getId()), pull("comments", eq("user_id", us.getId())));
    }

    public static void init() {
        client = new MongoClient();

        CodecRegistry pojoCodecRegistry
                = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        db = client.getDatabase("act5_3");
        db = db.withCodecRegistry(pojoCodecRegistry);

        MongoCollection<Document> col = db.getCollection("article");
        col.drop();
        col = db.getCollection("user");
        col.drop();
    }

    public static void close() {
        client.close();
    }

}
