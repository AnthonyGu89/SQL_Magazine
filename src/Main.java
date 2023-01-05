import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/store2", "root", ""); // Выполняем подключение к БД
        try (connection) {
            System.out.println("DB connected"); // Проверяем установилось ли соединение с БД
            byte[] data1 = "Hello World".getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(data1);
            System.out.println(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        updateData();
        buyTovar();
    }
    static void updateData() throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/store2", "root", "");
        Statement statement = connection.createStatement();
        ResultSet resultSet10 = statement.executeQuery("select password, id from Users ");
        int q = 0;
        while(resultSet10.next()){
            Statement statement2 = connection.createStatement();
            String password = resultSet10.getString("password");
            int n = resultSet10.getInt("id");
            System.out.println(password);
            System.out.println(n);
            byte[] data1 = password.getBytes("UTF-8"); // Загоняю свое слово в обработку функцией в формате UTF-8
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256"); // Тут Мы указываем какой именно методом шифрования Мы хотим зашифровать
            byte[] digest = messageDigest.digest(data1); // шифруем наши данные в кодировке UTF-8
            System.out.println(digest);
            statement2.executeUpdate("update Users set password = '"+digest+"' where id = "+n+" ");
            q++;
        }
        resultSet10.close();
        statement.close();
    }
    static void buyTovar() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/store2", "root", "");
        Statement statement = connection.createStatement();
        System.out.println("Добро пожаловать в нашем магазине");
        ResultSet resultSet3 = statement.executeQuery("SELECT * FROM goods");
        int x = 0;
        while (resultSet3.next()) {
            int num1 = resultSet3.getInt(1);
            String num = resultSet3.getString("name");
            String nym = resultSet3.getString(3);
            String nym1 = resultSet3.getString(4);
            System.out.println(num1 + " " + num + " "+nym+ " "+nym1);
            x = x + 1;
        }
        System.out.println("Введите номер товара, который Вам нужен");
        Scanner in = new Scanner(System.in);
        int pub = in.nextInt();
        if (pub > x) {
            System.out.println("Введите верный номер товара, который Вам нужен");
            buyTovar();
        } else {
            ResultSet resultSet1 = statement.executeQuery("SELECT * FROM goods WHERE id = " + pub);
            String got = "";
            String got1 = "";
            String got2 = "";
            while (resultSet1.next()) {
                int num7 = resultSet1.getInt(1);
                String num4 = resultSet1.getString("name");
                String num5 = resultSet1.getString(3);
                String num6 = resultSet1.getString(4);
//                System.out.println(num7 + " " + num4 + " " + num5 + " " + num6);
                got = num7 + " " + num4 + " " + num5 + " " + num6;
                System.out.println(got);
                got1 = num4;
                got2 = num6;
            }
            System.out.println("Хотите оформить покупку? Нажмите Да");
            Scanner in2 = new Scanner(System.in);
            String pub2 = in2.nextLine();
            int num8 = 0;
            String [] fuk = {"select","insert","update","delete","drop"};
            if (pub2.equals("Да")) {   // Тут Мы не прописали, если человек нажмет НЕТ, я добавил else
                System.out.println("Введите Ваше Имя");
                Scanner on = new Scanner(System.in);
                String qwe = on.nextLine();
                int y = 0;
                int person = 0;
                while(y<5){
                    System.out.println(fuk[y]);
                    System.out.println(qwe.contains(fuk[y]));
                    boolean b = qwe.contains(fuk[y]);
                    y = y+1;
                    if(b==(true)){
                        person++;
//                        System.exit(0);
                    }
                }
                System.out.println(person);
                if(person>0){
                    System.out.println("Danger client");
                }
                if (qwe.equals("")) {
                    System.out.println("Введите корректно Ваше Имя");
                    buyTovar(); // а как сделать чтобы возращало не в начало функции, а на момент ввода имени обратно
                } else {
                    System.out.println("Введите Вашу Фамилию");
                    Scanner on1 = new Scanner(System.in);
                    String qwe1 = on1.nextLine();
                    if (qwe1.equals("")) {
                        System.out.println("Введите корректно Вашу Фамилию");
                        buyTovar();
                    } else {
                    ResultSet resultSet7 = statement.executeQuery("select * from billing order by id desc limit 1");
                    resultSet7.next();
                    String num3 = resultSet7.getString("active");
//                    System.out.println(num3);
                    if (num3.equals("yes")) {
                        System.out.println("yes");
                        int myidbasket = resultSet7.getInt("id");
                        int resultSet5 = statement.executeUpdate("INSERT INTO orders (idbasket,tovarname,price) VALUES (" + myidbasket + ",'" + got1 + "'," + got2 + ")");
                        System.out.println("Запись добавлена");
                    } else {
                        System.out.println("no");
                        System.out.println("Вставляем новую запись в billing");
                        System.out.println(qwe);
                        System.out.println(qwe1);
                        int resultSet2 = statement.executeUpdate("INSERT INTO billing (name,surname,cardname,active) VALUES ('"+qwe+"','"+qwe1+"','123','yes')");
                        if (resultSet2 == 1) {
                            ResultSet resultSet4 = statement.executeQuery("select * from billing order by id desc limit 1");
                            resultSet4.next();
                            num8 = resultSet4.getInt(1);
                            System.out.println(num8);
                            int resultSet8 = statement.executeUpdate("INSERT INTO orders (idbasket,tovarname,price) VALUES (" + num8 + ",'" + got1 + "'," + got2 + ")");
                            System.out.println("Запись добавлена");
                        } else {
                            System.out.println("Не получилось добавить");
                            buyTovar();
                        }
                        System.out.println("Спасибо, Вы оформили заказ");
                        System.out.println(got);
                    }
                }
            }
            } else {
                buyTovar();
            }
        }
                System.out.println("Хотите продолжить заказ? 1-да, 2 - нет, хочу закончить заказ, 3 - нет, Я передумал, удалите этот товар");
                Scanner in3 = new Scanner(System.in);
                int nam = in3.nextInt();
                if (nam==1) {
                    buyTovar();
                } else {
                    if(nam ==2) {
                        ResultSet resultSet7 = statement.executeQuery("select * from billing order by id desc limit 1");
                        resultSet7.next();
                        int num3 = resultSet7.getInt(1);
                        System.out.println("update billing set active = 'no' where id = " + num3 + " ");
                        int resultSet6 = statement.executeUpdate("update billing set active = 'no' where id = " + num3 + " ");
                        System.out.println(resultSet6);
                        System.out.println("Спасибо за покупку");
                        System.exit(0);
                    } else {
                        ResultSet resultSet9 = statement.executeQuery("select * from orders order by id desc limit 1");
                        resultSet9.next();
                        int num10 = resultSet9.getInt(1);
                        int resultSet10 = statement.executeUpdate("delete from orders where id = " +num10);
                        buyTovar();
                    }
                }
            }
        }

