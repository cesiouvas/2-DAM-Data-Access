package main;

import com.mongodb.client.FindIterable;
import java.util.Arrays;
import javax.xml.crypto.Data;
import pojos.Address;
import pojos.Article;
import pojos.Comment;
import api.DataAPI;
import pojos.User;

/*
 * @author Sergio Castillo Llorens
 */
public class NewMain {

    public static void main(String[] args) {
        DataAPI.init();

        //Insert articles
        Article art1 = new Article("the universe", 22, Arrays.asList("science", "astronomy"));
        Article art2 = new Article("start cooking", 14, Arrays.asList("cook", "hobby"));
        Article art3 = new Article("advanced cooking", 30, Arrays.asList("cook", "job"));
        Article art4 = new Article("easter", 10, Arrays.asList("holiday", "family"));

        DataAPI.insertArticle(art1);
        DataAPI.insertArticle(art2);
        DataAPI.insertArticle(art3);
        DataAPI.insertArticle(art4);

        //Insert users
        User us1 = new User("Juan", "juandmcb@gmail.com", new Address("palomas", 2, "Valencia", "Spain"));
        User us2 = new User("Fernando", "ferfer@gmail.com", new Address("ancha", 24, "Castellon", "Spain"));
        User us3 = new User("Lola", "lolasori@gmail.com", new Address("calle", 6, "Puzol", "Spain"));

        DataAPI.insertUser(us1);
        DataAPI.insertUser(us2);
        DataAPI.insertUser(us3);

        //Return article by ID
        System.out.println("\nReturn article by ID");
        System.out.println(DataAPI.findArticle(art1.getId()));

        //Returns all the atricles of the same category
        System.out.println("\nReturns all the atricles of the same category");
        for (Article art : DataAPI.findArticleByCategory("cook")) {
            System.out.println(art.toString());
        }

        //Returns all the articles that contains str in its name
        System.out.println("\nReturns all the articles that contains str in its name");
        for (Article art : DataAPI.findArticleByName("cooking")) {
            System.out.println(art.toString());
        }

        //Returns the articles whose price is in the rank
        System.out.println("\nReturns the articles whose price is in the rank");
        for (Article art : DataAPI.findArticleInPriceRank(20, 32)) {
            System.out.println(art.toString());
        }

        //FindUser by id
        System.out.println("\nReturns user by ID");
        System.out.println(DataAPI.findUser(us1.getId()));

        //Returns all the users who live in the country specified
        System.out.println("\nReturns all the users who live in the country specified");
        for (User us : DataAPI.findUserByCountry("Spain")) {
            System.out.println(us.toString());
        }

        //Returns articles ordered by price
        System.out.println("\nReturns articles ordered by price");
        FindIterable<Article> art = DataAPI.findArticleInPriceRank(6, 30);
        FindIterable<Article> artSorted = DataAPI.orderByPrice(art, true);
        //Ascendant order
        System.out.println("\nAscendant order");
        for (Article article : artSorted) {
            System.out.println(article.toString());
        }
        //Descendant order
        System.out.println("\nDescendant order");
        artSorted = DataAPI.orderByPrice(art, false);
        for (Article article : artSorted) {
            System.out.println(article.toString());
        }

        //Update address
        Address updateAd = new Address("judias", 2, "Burgos", "Spain");
        DataAPI.updateAddress(us1, updateAd);

        //Update email
        DataAPI.updateEmail(us2, "fernando534@gmail.com");

        //Add comment to an article
        DataAPI.addComment(art4, new Comment(3, us2.getId(), "juan2"));
        DataAPI.addComment(art4, new Comment(2, us2.getId(), "juan"));
        DataAPI.addComment(art4, new Comment(5, us1.getId(), "comentario"));

        //Delete article
        DataAPI.deleteArticle(art3);

        //Delete user
        DataAPI.deleteUser(us1);
    }
}
