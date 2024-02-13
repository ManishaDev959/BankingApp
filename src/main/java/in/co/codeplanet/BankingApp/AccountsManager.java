package in.co.codeplanet.BankingApp;

import java.sql.*;
import java.util.Scanner;

public class AccountsManager {
    private Connection connection;
    private Scanner scanner;

    public AccountsManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    public void debit_money(long account_number) throws SQLException
    {
        scanner.nextLine();
        System.out.println("Enter the amount you want to debit");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter your security pin");
        String security_pin=scanner.nextLine();
        try{
            connection.setAutoCommit(false);
           if(account_number!=0)
           {
               PreparedStatement preparedStatement=connection.prepareStatement("select * from Accounts where account_number=? and security_pin=?");
               preparedStatement.setLong(1,account_number);
               preparedStatement.setString(2,security_pin);
               ResultSet resultSet= preparedStatement.executeQuery();

               if(resultSet.next())
               {
                   double current_balance=resultSet.getDouble("balance");
                   if(amount<=current_balance)
                   {
                       String debit_query="update Accounts set balance = balance -? where account_number=?";
                       PreparedStatement preparedStatement1=connection.prepareStatement(debit_query);
                       preparedStatement1.setDouble(1,amount);
                       preparedStatement1.setLong(2,account_number);
                      int affectedRows= preparedStatement1.executeUpdate();
                      if(affectedRows>0)
                      {
                          System.out.println("Rs."+amount+" debited successfully" );
                          connection.commit();
                          connection.setAutoCommit(true);
                          return;
                      }
                      else{
                          System.out.println("Transaction failed");
                          connection.rollback();
                          connection.setAutoCommit(true);
                      }
                   }
                   else{
                       System.out.println("insufficient balance");
                   }
               }
               else{
                   System.out.println("invalid pin");
               }

           }

        }catch(SQLException e)
        {
            e.printStackTrace();

        }
        connection.setAutoCommit(true);
    }
    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter the amount you want to  credit");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter your security pin");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from Accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                        String credit_query = "update Accounts set balance = balance +? where account_number=?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);
                        int affectedRows = preparedStatement1.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Rs." + amount + " credited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                    System.out.println("invalid Security Pin");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        connection.setAutoCommit(true);
    }
    public void getBalance(long account_number)
    {
        scanner.nextLine();
        System.out.println("Enter your security pin");
        String security_pin=scanner.nextLine();

        try{
            String query="select balance from Accounts where account_number=? and security_pin=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet=  preparedStatement.executeQuery();
            if(resultSet.next())
            {
                 double balance=resultSet.getDouble("balance");
                System.out.println("Your Balance is: " +balance);
            }
            else{
                System.out.println("invalid pin");
            }

        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public void transferMoney(long sender_account_number) throws SQLException
    {
        scanner.nextLine();
        System.out.println("Enter Receiver account number");
        long receiver_account_number=scanner.nextLong();
        System.out.println("Enter Amount that you want to transfer");
        double amount=scanner.nextDouble();
       scanner.nextLine();
        System.out.println("Enter Your Security_pin");
        String security_pin=scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0)
            {
                PreparedStatement preparedStatement=connection.prepareStatement("select * from Accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet= preparedStatement.executeQuery();
                if(resultSet.next())
                {
                    double current_balance=resultSet.getDouble("balance");
                    if(amount<=current_balance)
                    {
                        String debit_query="update Accounts set balance= balance-? where account_number=?";
                        String credit_query="update Accounts set balance= balance+? where account_number=?";
                        PreparedStatement debitPrepareStatement=connection.prepareStatement(debit_query);
                        PreparedStatement creditPreParedStatement=connection.prepareStatement(credit_query);
                        debitPrepareStatement.setDouble(1,amount);
                        debitPrepareStatement.setLong(2,sender_account_number);
                        creditPreParedStatement.setDouble(1,amount);
                        creditPreParedStatement.setLong(2,receiver_account_number);
                        int affectedRows1=debitPrepareStatement.executeUpdate();
                        int affectedRows2=creditPreParedStatement.executeUpdate();
                        if(affectedRows1>0 && affectedRows2>0)
                        {
                            System.out.println("Rs." + amount +"Transaction successful");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }
                        else{
                            System.out.println("Transaction Incomplete");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance");
                    }
                }
                else{
                    System.out.println("Invalid security pin");
                }

            }
            else{
                System.out.println("Invalid account number");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
}



















