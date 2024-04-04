package javaapplication139;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import java.util.Random;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginScreen {

    private JFrame frame;
    private JButton[][] buttons;
    private int numRows, numCols;
    private int numRedButtons = 0;
    private String username;
    private String password;
    public int Butce = 0;
    private int selectedButtonX = -1;  // Store the selected button's X-coordinate
    private int selectedButtonY = -1;  // Store the selected button's Y-coordinate
    private Object pstmt;
    private Connection conn;
    static Object rs;
    private String kullaniciadi;
    private int baslangicparamiktari;
    JLabel budgetLabel = new JLabel();

    public LoginScreen(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        buttons = new JButton[numRows][numCols];

    }
   public int getUpdatedBudget() {
        String url = "jdbc:postgresql://localhost:5432/postgres"; // Veritabanı bağlantı URL'si
        String kullaniciAdi = "postgres"; // Veritabanı kullanıcı adı
        String sifre = "1234"; // Veritabanı şifresi

        int updatedBudget = 0;

        try (Connection conn = DriverManager.getConnection(url, kullaniciAdi, sifre)) {
            String sql = "SELECT baslangicparamiktari FROM kullanicilar WHERE kullaniciadi = 'furkanaras'"; // Güncellenmiş bütçeyi almak için SQL sorgusu

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    updatedBudget = rs.getInt("baslangicparamiktari");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return updatedBudget;
    }


    void createGUI(int a, int b) {
        frame = new JFrame("OYUNA HOSGELDİNİZ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(numRows, numCols));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Butonları temizle ve yeni boyuta uygun butonları ekle
        panel.removeAll(); // Butonları temizle
        buttons = new JButton[numRows][numCols];

        int[][] dizi = new int[numRows][numCols];
        Random random = new Random();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int randomNumber = random.nextInt(2001) + 1000;
                dizi[i][j] = randomNumber;

                JButton button = new JButton();
                button.setBackground(Color.GREEN);
                buttons[i][j] = button;
                panel.add(button);

                button.setIcon(new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Masaüstü\\resimler\\cimen.jpg"));
                button.setText("Bos Arsa Fiyati = " + dizi[i][j]);
                button.setHorizontalTextPosition(JButton.CENTER);
                button.setVerticalTextPosition(JButton.CENTER);

                button.addActionListener(new ButtonListener(buttons, Butce, panel, dizi, i, j));  // Pass the coordinates to ButtonListener
            }
        }

        // Update button colors based on the database data
        updateButtonColors();

        // Yeni sayfa butonu oluştur ve panele ekle
        // Bütçe göstermek için JLabel oluştur ve panele ekle
        budgetLabel.setText("Bütçe = " + Butce);
        panel.add(budgetLabel);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }

private void updateButtonColors() {
    try {
            // JDBC sürücüsünü yükle
            Class.forName("org.postgresql.Driver");

            // Veritabanı bağlantısı ve diğer gerekli bilgileri ayarla
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "1234";
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

// Veritabanından basılı butonları al
            String query = "SELECT * FROM basilibutonlar WHERE basildimi = true;";
            ResultSet resultSet = statement.executeQuery(query);

          while (resultSet.next()) {
                int xKoordinati = resultSet.getInt("xkoordinati");
                int yKoordinati = resultSet.getInt("ykoordinati");

                if (xKoordinati >= 0 && xKoordinati < buttons.length
                        && yKoordinati >= 0 && yKoordinati < buttons[xKoordinati].length) {
                    JButton button = buttons[xKoordinati][yKoordinati];
                    button.setIcon(new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Masaüstü\\resimler\\satildi.jpg"));

                }
            }
           /*  while (resultSet.next()) {
            int xKoordinati = resultSet.getInt("xkoordinati");
            int yKoordinati = resultSet.getInt("ykoordinati");
            String isletmeTuru = resultSet.getString("isletmeturu");

            if (xKoordinati >= 0 && xKoordinati < buttons.length
                    && yKoordinati >= 0 && yKoordinati < buttons[xKoordinati].length) {
                JButton button = buttons[xKoordinati][yKoordinati];
                
                // Resim yolunu belirleyin ve resmi butona yerleştirin
                String imagePath = "C:\\Users\\ASUS\\OneDrive\\Masaüstü\\resimler\\" + isletmeTuru + ".jpg";
                button.setIcon(new ImageIcon(imagePath));
            }
        }
*/
// Veritabanı ve diğer kaynakları kapat
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


private boolean showLoginDialog() {
    JPanel loginPanel = new JPanel();
    JTextField usernameField = new JTextField(10);
    JPasswordField passwordField = new JPasswordField(10);
    loginPanel.add(new JLabel("Kullanıcı Adı:"));
    loginPanel.add(usernameField);
    loginPanel.add(Box.createHorizontalStrut(15));
    loginPanel.add(new JLabel("Şifre:"));
    loginPanel.add(passwordField);

    int result = JOptionPane.showConfirmDialog(frame, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = new String(passwordField.getPassword());

        if (enteredUsername.equals("furkan aras") && enteredPassword.equals("2022")) {
            JOptionPane.showMessageDialog(frame, "Oyuncu kısmına giriş yapamazsınız. Lütfen yönetici kısmına giriş yapınız!", "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            // JDBC sürücüsünü yükle
            Class.forName("org.postgresql.Driver");

            // Veritabanı URL'si
            String url = "jdbc:postgresql://localhost:5432/postgres";
            // Veritabanı kullanıcı adı
            String user = "postgres";
            // Veritabanı şifresi
            String password = "1234";

            // Veritabanına bağlan
            conn = DriverManager.getConnection(url, user, password);

            // Kullanıcı adı ve şifreyi doğrulamak için sorgu oluştur
            String selectSql = "SELECT * FROM public.kullanicilar WHERE kullaniciadi = ? AND kullanicisifresi = ?";
            PreparedStatement pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, enteredUsername);
            pstmt.setString(2, enteredPassword);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String selectButonSql = "SELECT COUNT(*) FROM public.basilibutonlar";
                Statement butonStatement = conn.createStatement();
                ResultSet butonResultSet = butonStatement.executeQuery(selectButonSql);

                if (butonResultSet.next()) {
                    int buttonCount = butonResultSet.getInt(1);

                    if (buttonCount > 0) {
                        String selectGuncelButceSql = "SELECT guncelbutce FROM public.guncelveriler";
                        Statement guncelButceStatement = conn.createStatement();
                        ResultSet guncelButceResultSet = guncelButceStatement.executeQuery(selectGuncelButceSql);

                        if (guncelButceResultSet.next()) {
                            Butce = guncelButceResultSet.getInt("guncelbutce");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Güncel veriler bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    } else {
                        Butce = rs.getInt("baslangicparamiktari");
                    }
                }
                
                kullaniciadi = enteredUsername;
                return true;
            } else {
                JOptionPane.showMessageDialog(frame, "Geçersiz kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Veritabanına bağlanırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } else {
        return false; // Kullanıcı dialogu iptal etti
    }
}



    private void checkRedButtons() {
        if (numRedButtons > 2) {
            numRedButtons--;
        }
    }

    public class ButtonListener implements ActionListener {

        private JButton[][] buttons;
        private int[][] dizi;
        private int totalSpent;
        private int numRedButtons;
        private JPanel panel;
        private int buttonX;  // Store the button's X-coordinate
        private int buttonY;  // Store the button's Y-coordinate

        public ButtonListener(JButton[][] buttons, int Butce, JPanel panel, int[][] dizi, int x, int y) {
            this.buttons = buttons;
            this.dizi = dizi;
            this.totalSpent = 0;
            this.numRedButtons = 0;
            this.panel = panel;
            this.buttonX = x;
            this.buttonY = y;
        }

        public void butceGuncelle(int Butce, JLabel budgetLabel) {
        try {
            // PostgreSQL veritabanına bağlantı bilgilerini girin
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String username = "postgres";
            String password = "1234";

            // Veritabanı bağlantısı oluştur
            Connection connection = DriverManager.getConnection(url, username, password);

            // Güncel bütçe değerini veritabanında güncelle
            int yeniButce = Butce; // Güncel bütçe değeri
            String updateQuery = "UPDATE guncelveriler SET guncelbutce = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, yeniButce);
            preparedStatement.executeUpdate();

            // budgetLabel'ın metnini güncelle
            budgetLabel.setText("Yeni Butce = " + yeniButce);

            // Kaynakları serbest bırak
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Hata durumunda ilgili işlemleri gerçekleştir
        }
    }
       @Override
        public void actionPerformed(ActionEvent e) {
            JButton yeniSayfaButton = new JButton("İŞLETMELER");
            yeniSayfaButton.setEnabled(true);
            
            yeniSayfaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   
                    if (selectedButtonX == buttonX && selectedButtonY == buttonY) {  // Check if the current button is the selected button
                        MyDialog dialog = new MyDialog(frame, "İŞLETMELER",Butce);
                        dialog.isletmeDik(Butce,dialog, selectedButtonX, selectedButtonY, buttons, buttonX, buttonY, dizi, frame, panel);
                         
               // İşletme türünü belirle
                String isletmeturu = "";
                if (e.getSource() == dialog.getEmlakButton()) {
                    isletmeturu = "Emlak";
                    dialog.performButtonAction("emlak", selectedButtonX, selectedButtonY);
                     Butce=Butce-1800;
                     butceGuncelle(Butce, budgetLabel);
                      
                    
                } else if (e.getSource() == dialog.getMarketButton()) {
                    isletmeturu = "Market";
                    dialog.performButtonAction("market", selectedButtonX, selectedButtonY);
                    Butce=Butce-1700; 
                    butceGuncelle(Butce, budgetLabel);
                   
                } else if (e.getSource() == dialog.getMagazaButton()) {
                    isletmeturu = "Mağaza";
                    dialog.performButtonAction("mağaza", selectedButtonX, selectedButtonY);
                    Butce=Butce-2000;
                     butceGuncelle(Butce, budgetLabel);
                      
                }
                       
                        dialog.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(frame, "Bu arsayı satın almadınız!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
           panel.add(yeniSayfaButton);

            JButton button = (JButton) e.getSource();
            JLabel budgetLabel = new JLabel("Bütçe: " + Butce);
            panel.add(budgetLabel);

            frame.add(panel);
            frame.pack();
            frame.setVisible(true);
frame.revalidate(); // Paneli yeniden düzenle
frame.repaint();
            int i, j;
            for (i = 0; i < numRows; i++) {
                for (j = 0; j < numCols; j++) {
                    if (buttons[i][j] == button && button.getBackground() == Color.GREEN) {
                        if (numRedButtons < 2) {
                            if (dizi[i][j] < Butce) {
                                System.out.println("Satin Alindi! Kalan Butce = " + (Butce - (dizi[i][j])));
                                button.setIcon(new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Masaüstü\\resimler\\satildi.jpg"));
                                Butce -= dizi[i][j]; // Güncelleme
                                numRedButtons++;
                                butceGuncelle(Butce, budgetLabel);

                                // Update selected button coordinates
                                selectedButtonX = buttonX;
                                selectedButtonY = buttonY;

                                // Veritabanına kayıt eklemek için gerekli işlemler
                                try {
                                    // JDBC sürücüsünü yükle
                                    Class.forName("org.postgresql.Driver");

                                    // Veritabanı bağlantısı ve diğer gerekli bilgileri ayarla
                                    String url = "jdbc:postgresql://localhost:5432/postgres";
                                    String user = "postgres";
                                    String password = "1234";

                                    // Veritabanına bağlan
                                    Connection conn = DriverManager.getConnection(url, user, password);

                                    // Veritabanına kayıt işlemini gerçekleştir
                                    String insertSql = "INSERT INTO basilibutonlar (xkoordinati, ykoordinati, basildimi) VALUES (?, ?, ?)";
                                    PreparedStatement pstmt = conn.prepareStatement(insertSql);
                                    pstmt.setInt(1, i);
                                    pstmt.setInt(2, j);
                                    pstmt.setBoolean(3, true);
                                    pstmt.executeUpdate();

                                    // Kayıt işlemi başarılı mesajı göster
                                    System.out.println("Buton veritabanına kaydedildi!");

                                } catch (ClassNotFoundException | SQLException ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(frame, "Veritabanına kayıt yaparken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                                }

                            } else {
                                System.out.println("Satin Alma Basarisiz! Butceniz = " + Butce);
                                JOptionPane.showMessageDialog(frame, "Butce Yetersiz", "Hata", JOptionPane.ERROR_MESSAGE);
                                butceGuncelle(Butce, budgetLabel);
                            }
                        } else {
                            checkRedButtons();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        int a, b;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Satir sayisini giriniz");
        a = scanner.nextInt();
        System.out.println("Sutun sayisini giriniz");
        b = scanner.nextInt();
        JFrame frame = new JFrame("OYUNA HOSGELDİNİZ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null); // Ekranın ortasında görüntülenmesi için

        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 240)); // Arka plan rengi ayarla

        JButton yonetici = new JButton("YONETICI");
        yonetici.setBackground(new Color(55, 95, 255)); // Butonun arka plan rengini ayarla
        yonetici.setForeground(Color.WHITE); // Butonun metin rengini ayarla
        yonetici.setPreferredSize(new Dimension(150, 50)); // Butonun boyutunu ayarla

        JButton oyuncu = new JButton("OYUNCU");
        oyuncu.setBackground(new Color(255, 155, 55)); // Butonun arka plan rengini ayarla
        oyuncu.setForeground(Color.WHITE); // Butonun metin rengini ayarla
        oyuncu.setPreferredSize(new Dimension(150, 50)); // Butonun boyutunu ayarla

        panel.add(yonetici);
        panel.add(oyuncu);

        frame.add(panel);
        frame.setVisible(true);

        yonetici.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel loginPanel = new JPanel();
                JTextField usernameField = new JTextField(10);
                JPasswordField passwordField = new JPasswordField(10);
                loginPanel.add(new JLabel("Kullanıcı Adı:"));
                loginPanel.add(usernameField);
                loginPanel.add(Box.createHorizontalStrut(15));
                loginPanel.add(new JLabel("Şifre:"));
                loginPanel.add(passwordField);

                int result = JOptionPane.showConfirmDialog(frame, loginPanel, "Yönetici Girişi", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String enteredUsername = usernameField.getText();
                    String enteredPassword = new String(passwordField.getPassword());

                    // Yönetici kullanıcı adı ve şifresini kontrol et
                    if (enteredUsername.equals("furkan aras") && enteredPassword.equals("2022")) {
                        // Yönetici girişi başarılı, devam et

                        yoneticiClass yoneticiClas = new yoneticiClass(enteredUsername, a, b);
                        yoneticiClas.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(frame, "Geçersiz kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
        oyuncu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginScreen loginScreen = new LoginScreen(a + 1, b - 1);
                boolean loginSuccessful = false;

                while (!loginSuccessful) {
                    loginSuccessful = loginScreen.showLoginDialog();

                    if (!loginSuccessful) {
                        int option = JOptionPane.showConfirmDialog(null, " Çıkmak istiyor musunuz?", "Hata", JOptionPane.YES_NO_OPTION);

                        if (option == JOptionPane.YES_OPTION) {

                            System.exit(0);
                        }
                    }
                }

                loginScreen.createGUI(a, b);
            }

        });

    }

}
