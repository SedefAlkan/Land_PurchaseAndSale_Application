package javaapplication139;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import java.sql.*;

public class yoneticiClass extends JFrame {

    public yoneticiClass(String enteredUsername, int a, int b) {
        setTitle("Hoşgeldiniz Ekranı");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 3));

        JLabel label = new JLabel("Hoşgeldiniz: " + enteredUsername);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label);

        JButton button1 = new JButton("Arsayı Görmek İçin Tıklayınız");
        JButton button2 = new JButton("Oyuncuların Bilgilerini Görmek İçin Tıklayınız");
        JButton button3 = new JButton("");

        // Button 1 için ActionListener tanımlama
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginScreen loginScreen = new LoginScreen(a + 1, b - 1);
                loginScreen.createGUI(a, b);
               // updateDailyValues() metodunun çağrıldığı yer
         updateDailyValues();
         int updatedBudget =loginScreen.getUpdatedBudget();
          System.out.println("Güncellenmiş Bütçe: " + updatedBudget);

            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "jdbc:postgresql://localhost:5432/postgres"; // Veritabanı bağlantı URL'si
                String kullaniciAdi = "postgres"; // Veritabanı kullanıcı adı
                String sifre = "1234"; // Veritabanı şifresi

                try (Connection conn = DriverManager.getConnection(url, kullaniciAdi, sifre)) {
                    String sql = "SELECT kullaniciadi,kullanicisoyadi, baslangicparamiktari, baslangıcyemekmiktarı, baslangıcesyamiktarı FROM kullanicilar"; // Veritabanından veri almak için SQL sorgusu

                   try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                 ResultSet rs = stmt.executeQuery(sql)) {
                        int rowCount = 0;
                        while (rs.next()) {
                            rowCount++;
                        }

                        String[][] userData = new String[rowCount][5];
                        rs.beforeFirst();
                        int i = 0;
                        while (rs.next()) {
                            String ad = rs.getString("kullaniciadi");
                            String soyad = rs.getString("kullanicisoyadi");
                            int butce = rs.getInt("baslangicparamiktari");
                            int yemekMiktari = rs.getInt("baslangıcyemekmiktarı");
                            int esyaMiktari = rs.getInt("baslangıcesyamiktarı");

                            userData[i][0] = ad;
                            userData[i][1] = soyad;
                            userData[i][2] = Integer.toString(butce);
                            userData[i][3] = Integer.toString(yemekMiktari);
                            userData[i][4] = Integer.toString(esyaMiktari);

                            i++;
                        }

                        String[] columnNames = {"Adı", "Soyadı", "Bütçe", "Yemek Miktarı", "Eşya Miktarı"};

                        JTable table = new JTable(userData, columnNames);

                        // Hücre boyutunu belirlemek için DefaultTableCellRenderer kullanma
                        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
                        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
                        table.setDefaultRenderer(Object.class, cellRenderer);

                        JScrollPane scrollPane = new JScrollPane(table);

                        JFrame tableFrame = new JFrame("Kullanıcı Bilgileri");
                        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        tableFrame.getContentPane().add(scrollPane);
                        tableFrame.pack();
                        tableFrame.setLocationRelativeTo(null);
                        tableFrame.setVisible(true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.add(button1);
        panel.add(button2);
        panel.add(button3);

        add(panel);
    }
    
public void updateDailyValues() {
    String url = "jdbc:postgresql://localhost:5432/postgres"; // Veritabanı bağlantı URL'si
    String kullaniciAdi = "postgres"; // Veritabanı kullanıcı adı
    String sifre = "1234"; // Veritabanı şifresi

    try (Connection conn = DriverManager.getConnection(url, kullaniciAdi, sifre)) {
        String sql = "UPDATE kullanicilar SET baslangıcesyamiktarı = baslangıcesyamiktarı - 10, baslangıcyemekmiktarı = baslangıcyemekmiktarı - 10"; // Günlük değerleri güncellemek için SQL sorgusu

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Günlük değerler güncellendi.");

            // Güncelleme işlemi tamamlandıktan sonra, güncellenen yemek miktarlarını guncelveriler tablosuna ekleyelim.
            sql = "INSERT INTO guncelveriler (guncelesya, guncelyiyecek, guncelbutce, kullaniciadi) SELECT baslangıcesyamiktarı, baslangıcyemekmiktarı, baslangicparamiktari, kullaniciadi FROM kullanicilar"; // Güncellenen yemek miktarlarını güncelveriler tablosuna ekleme için SQL sorgusu

            stmt.executeUpdate(sql);
            System.out.println("Güncellenen yemek miktarları guncelveriler tablosuna eklendi.");

            // Esya miktarlarını sıfırlayalım
            sql = "UPDATE kullanicilar SET baslangıcesyamiktarı = 0 WHERE baslangıcesyamiktarı < 0";
            stmt.executeUpdate(sql);
            System.out.println("Esya miktarları sıfırlandı.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
    public static void main(String[] args) {
        yoneticiClass yoneticiClass = new yoneticiClass(" ", 60, 30);
        yoneticiClass.setVisible(true);
    }
}
