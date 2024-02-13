package in.co.codeplanet.BankingApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    public void register()
    {
        scanner.nextLine();

        System.out.println("Full Name");
        String fullName=scanner.nextLine();

        System.out.println("Email:");
        String email=scanner.nextLine();

        System.out.println("Password:");
        String password=scanner.nextLine();

        if(!user_exists(email))
        {
            System.out.println("User already exists for this email");
            return;
        }

        String register_query="insert into user(fullname, email, password) values(?,?,?)";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(register_query);
            preparedStatement.setString(1,fullName);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows=preparedStatement.executeUpdate();
            if(affectedRows>0)
            {
                System.out.println("Registration Successful");
            }
            else{
                System.out.println("Registration Failed");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public String login()
    {
        scanner.nextLine();
        System.out.println("Enter Your Email");
        String email=scanner.nextLine();

        System.out.println("Enter Your Password");
        String password=scanner.nextLine();

        String login_query="select * from user where email=? and password=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return email;
            }
            else{
                return null;
            }

        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
  return null;
    }
  public boolean user_exists(String email)
  {
      String query="select * from user where email=?";
      try{
          PreparedStatement preparedStatement=connection.prepareStatement(query);
          preparedStatement.setString(1,email);
          ResultSet resultSet=preparedStatement.executeQuery();
          if(resultSet.next())
          {
              return false;
          }
          else{
              return true;
          }

      }
      catch(SQLException e)
      {

      }
      return false;
  }
}
