package com.knoldus.jdbcexample;

import java.sql.*;


public class JDBCExample {
    static Connection con ;
    PreparedStatement stmt ;
    ResultSet rs;
    public static void main(String[] args) throws SQLException {

        try {
           String dbUrl = "jdbc:mysql://127.0.0.1:3306/test?useSSL=no&user=akshit&password=Akshit@1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con  = DriverManager.getConnection(dbUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBCExample obj=new JDBCExample();
        obj. showProductsTable();
        obj.TotalAmount();
        obj.GrandTotal();
        obj.notSold();
        obj.mostSold();
    }


    // Show product table
    public void  showProductsTable(){
        try {
            String query=("select * from product");
            stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            System.out.println(" ************* Product Table ***********");
            System.out.print("Product ID\t\tPrice\t \t\t Product Name\n");
            while (rs.next()) {
                System.out.println("\t" + rs.getString(1) + "\t\t " + rs.getString(3) + "\t\t\t\t\t\t" +
                        rs.getString(2));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
 // total amount = Quantity * price
    public void TotalAmount() {
        try {
            String query=("select product.pid, product.pname,cart.Qty,cart.Qty*product.price as Total from product,cart " +
                    "where product.pid= cart.pid ");
            stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            System.out.println("****************** Cart Items *****************");
            System.out.print("Product ID\t\tQuantity\t\tTotalPrice\t\t Product Name\n");
            while (rs.next()) {
                System.out.println("\t" + rs.getString(1) + "\t\t\t" + rs.getString(3) + "\t\t\t\t\t\t" +
                        rs.getString(4) + "\t\t\t" + rs.getString(2));

            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // Grand total at the time of checkout
public void  GrandTotal() throws SQLException {
String query=("select sum(GrandTotal) from(select cart.Qty*product.price as grandTotal from product,cart where product.pid=cart.pid) as total");
    stmt = con.prepareStatement(query);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
        System.out.println("\t" + "                  Grand total = " + rs.getString(1));
    }
    System.out.println();

    }

    // which product not sold
    public void notSold(){
        try {
            String query=("select * from product where pid not in " +
                    "(select pid from cart);");
            stmt = con.prepareStatement(query);
            rs= stmt.executeQuery();
            System.out.println("***************Not sold Products **************");
            System.out.print("Product ID\t\tPrice\t\tProduct Name\n");
            while(rs.next()){
                System.out.println("\t"+rs.getString(1)+"\t\t\t"+rs.getString(3)+"\t\t" +
                        rs.getString(2));
            }
            System.out.println();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // for most sold product
    public void mostSold(){
        try{
            String query=("select product.pid, product.pname, cart.Qty " +
                    "from product,cart where product.pid= cart.pid order by Qty desc limit 3;");
            stmt = con.prepareStatement(query);
            rs=stmt.executeQuery();
            System.out.println("*************** Most Sold Product ************");
            System.out.print("Product ID\t\tQuantity\t\tProduct Name\n");
            while(rs.next()){
                System.out.println("\t"+rs.getString(1)+"\t\t\t\t"+rs.getString(3)+"\t\t\t" +
                        rs.getString(2)+"\n");
                break;
            }
        }catch(SQLException e){ System.out.println(e);}
    }

}
