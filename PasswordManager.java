package jetbrains;

import com.mongodb.*;
import com.mongodb.DB;

import java.util.Scanner;


class User{


    private String user_name, master_password;

    // Creating user name for the new user
    void createUserName(){
        Scanner s1 = new Scanner(System.in);
        System.out.println("Enter your user name");
        user_name = s1.nextLine();
    }

    // Creating master password for the new user
    void createPassword(){
        String temp;
        Scanner s1 = new Scanner(System.in);
        while(true){
            System.out.println("Choose a master password");
            master_password = s1.nextLine();
            while(master_password.length() < 5){
                System.out.println("password length too small re-enter a longer password");
                master_password = s1.nextLine();
            }
            System.out.println("Confirm your master password");
            temp = s1.nextLine();
            if(temp.equals(master_password)){
                System.out.println("MASTER PASSWORD CREATED!!");
                break;
            }
            else{
                System.out.println("password did not match please try again");
            }
        }
    }


    // getting password
    String getMasterPassword(){
        return master_password;
    }

    //getting username
    String getUserName(){
        return user_name;
    }
}

public class PasswordManager{

    public static void main(String args[]) {

            // Since 2.10.0, uses MongoClient
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
            // Gets the peopledb from the MongoDB instance.
            DB database = mongoClient.getDB("peopleDB3");


            System.out.println("Returning users press 1\nNew User press 2");
            Scanner s = new Scanner(System.in);
            int user_type = s.nextInt();

            // Code for Returning users
            if (user_type == 1) {
                Scanner s3 = new Scanner(System.in);
                String user;
                String master_password;
                System.out.println("Enter your user name ");
                user = s3.nextLine();

                if(!database.collectionExists(user)){
                    System.out.println("No such user exist");
                    return;
                }

                DBCollection collection = database.getCollection(user);


                // fetching the master password from the document stored
                String originalmasterPassword = "empty";

                BasicDBObject query = new BasicDBObject();
                BasicDBObject field = new BasicDBObject();
                field.put("masterPassword", 1);
                DBCursor cursor2 = collection.find(query,field);
                while (cursor2.hasNext()) {
                    BasicDBObject obj = (BasicDBObject) cursor2.next();
                    originalmasterPassword = obj.getString("masterPassword");
                }

                System.out.println("Enter your master password");
                master_password = s3.nextLine();


                if(!originalmasterPassword.equals(master_password)){
                    System.out.println("password didint match\n EXITING");
                    return;
                }

                while (true) {
                    int operation, flag = 0;
                    System.out.println("Enter an operation to perform");
                    System.out.println("1)create entry\t2)view entry\t3)exit");
                    operation = s.nextInt();
                    Scanner s2 = new Scanner(System.in);
                    switch (operation) {

                        case 1:
                            String website, email, password;
                            System.out.println("enter the website");
                            website = s2.nextLine();
                            System.out.println("enter the email");
                            email = s2.nextLine();
                            System.out.println("enter the password for the website");
                            password = s2.nextLine();
                            DBObject entry = new BasicDBObject()
                                    .append("website", website)
                                    .append("email", email)
                                    .append("password", password)
                                    .append("masterPassword", master_password);
                            collection.insert(entry);
                            break;
                        case 2:
                            DBCursor cursor = collection.find();
                            while(cursor.hasNext()) {
                                System.out.println(cursor.next());
                            }
                            cursor.close();
                            break;
                        default:
                            flag++;
                            mongoClient.close();
                            break;
                    }
                    if (flag == 1) {
                        break;
                    }
                }

            }

            // Code for New Users
            else {
                User new_user = new User();
                new_user.createUserName();
                new_user.createPassword();
                System.out.println(new_user.getUserName());
                System.out.println(new_user.getMasterPassword());

                DBCollection collection = database.getCollection(new_user.getUserName());


                while (true) {
                    int operation, flag = 0;
                    System.out.println("Enter an operation to perform");
                    System.out.println("1)create entry\t2)view entry\t3)exit");
                    operation = s.nextInt();
                    Scanner s2 = new Scanner(System.in);
                    switch (operation) {

                        case 1:
                            String website, email, password;
                            System.out.println("enter the website");
                            website = s2.nextLine();
                            System.out.println("enter the email");
                            email = s2.nextLine();
                            System.out.println("enter the password for the website");
                            password = s2.nextLine();
                            DBObject entry = new BasicDBObject()
                                    .append("website", website)
                                    .append("email", email)
                                    .append("password", password)
                                    .append("masterPassword", new_user.getMasterPassword());
                            collection.insert(entry);
                            break;
                        case 2:
                            DBCursor cursor = collection.find();
                            while(cursor.hasNext()) {
                                System.out.println(cursor.next());
                            }
                            cursor.close();
                            break;
                        default:
                            flag++;
                            mongoClient.close();
                            break;
                    }
                    if (flag == 1) {
                        break;
                    }
                }
            }

    }
}
