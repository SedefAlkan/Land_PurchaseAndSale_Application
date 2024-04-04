package javaapplication139;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyDialog extends JDialog {

    private static final int BUTTON_SIZE = 100;

    private int marketFiyat;
    private int magazaFiyat;
    private int emlakFiyat;
    private int Butce;
    public String isletmeturu;
    private ImageIcon emlakIcon = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Masaüstü\\resimler\\emlak.jpg");
    private ImageIcon marketIcon = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Masaüstü\\resimler\\market.jpg");
    private ImageIcon magazaIcon = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Masaüstü\\resimler\\mağaza (3).jpg");

    public JButton emlakButton = new JButton(emlakIcon);
    public JButton marketButton = new JButton(marketIcon);
    public JButton magazaButton = new JButton(magazaIcon);
    private Component frame;
  public JButton getEmlakButton() {
        return emlakButton;
    }
    public JButton getMarketButton() {
        return marketButton;
    }
      public JButton getMagazaButton() {
        return magazaButton;
    }
    public MyDialog(JFrame frame, String title,int butce) {
        super(frame, title, true);
        setSize(400, 400);
        setLocationRelativeTo(frame);
        JPanel panel = new JPanel();

        // Düğmelerin boyutlarını ayarlayın
        Dimension buttonSize = new Dimension(BUTTON_SIZE, BUTTON_SIZE);
        emlakButton.setPreferredSize(buttonSize);
        marketButton.setPreferredSize(buttonSize);
        magazaButton.setPreferredSize(buttonSize);

        // Resimlerin boyutlarını butonların boyutuna ayarlayın
        emlakButton.setIcon(new ImageIcon(emlakIcon.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH)));
        marketButton.setIcon(new ImageIcon(marketIcon.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH)));
        magazaButton.setIcon(new ImageIcon(magazaIcon.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH)));

        // Düğmelerin hizalamasını ayarlayın
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = 0;
        panel.add(emlakButton, c);

        c.gridx = 1;
        c.gridy = 0;
        panel.add(marketButton, c);

        c.gridx = 2;
        c.gridy = 0;
        panel.add(magazaButton, c);

        emlakFiyat = getFiyatFromDatabase("emlak");
        // Fiyatları göstermek için JLabel'ler oluşturun
        JLabel label1 = new JLabel("Emlak Fiyat: " + emlakFiyat);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(label1, c);

       marketFiyat = getFiyatFromDatabase("market");
        JLabel label2 = new JLabel("Market Fiyat: " + marketFiyat);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(label2, c);

        magazaFiyat = getFiyatFromDatabase("mağaza");
        JLabel label3 = new JLabel("Mağaza Fiyat: " + magazaFiyat);
        c.gridx = 2;
        c.gridy = 1;
        panel.add(label3, c);

        // Butonlara dinleyiciler ekleyin
        emlakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedButtonX = 0;
                int selectedButtonY = 0;
                performButtonAction("emlak", selectedButtonX, selectedButtonY);
                dispose();
            }
        });

        marketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedButtonX = 1;
                int selectedButtonY = 0;
                performButtonAction("market", selectedButtonX, selectedButtonY);
                dispose();
            }
        });

        magazaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedButtonX = 2;
                int selectedButtonY = 0;
                performButtonAction("mağaza", selectedButtonX, selectedButtonY);
                dispose();
            }
        });

        add(panel);
    }

public int performButtonAction(String isletmeturu, int selectedButtonX, int selectedButtonY) {
    int fiyat = getFiyatFromDatabase(isletmeturu); // İlgili işletmenin fiyatını veritabanından al

    boolean basildimi = true; // Butonun basıldığını göstermek için basildimi değişkenini true olarak ayarlayın

    String url = "jdbc:postgresql://localhost:5432/postgres";
    String username = "postgres";
    String password = "1234";

    try (Connection connection = DriverManager.getConnection(url, username, password)) {
        // Önceki sorguyu güncelleyerek, basildimi değerini de veritabanına ekleyin
        String query = "INSERT INTO basilibutonlar (xkoordinati, ykoordinati, isletmeturu, basildimi) VALUES (" + selectedButtonX + ", " + selectedButtonY + ", '" + isletmeturu + "', " + basildimi + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

        // Eklenen kaydın ID'sini alın
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int insertedId = generatedKeys.getInt(1);

            // Buton basıldığında Butce değişkeninden fiyatı çıkartın
            Butce -= fiyat;
            // Butce'yi güncelleyin
           // JLabel budgetLabel = new JLabel("Butce: " + Butce);
            //budgetLabel.setText("Butce: " + Butce);
        }

        statement.close();
        generatedKeys.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return Butce;
}





    public void isletmeDik(int Butce, MyDialog dialog, int selectedButtonX, int selectedButtonY, JButton[][] buttons, int buttonX, int buttonY, int[][] dizi, JFrame frame, JPanel panel) {
        emlakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  String isletmeturu = "emlak";
               performButtonAction(isletmeturu,selectedButtonX,selectedButtonY);
                
                Image scaledImage = emlakIcon.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                buttons[selectedButtonX][selectedButtonY].setText(" ");
                buttons[selectedButtonX][selectedButtonY].setIcon(scaledIcon);
                // Emlak fiyatını güncelle
              /*   emlakFiyat = getFiyatFromDatabase("emlak"); // Veritabanından emlak fiyatını al
               JLabel label1 = new JLabel("Emlak Fiyat: " + emlakFiyat);
                panel.add(label1, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
*/
               // panel.revalidate();
            }
        });

        magazaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  String isletmeturu = "mağaza";
               performButtonAction(isletmeturu,selectedButtonX,selectedButtonY);
                Image scaledImage = magazaIcon.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                buttons[selectedButtonX][selectedButtonY].setText(" ");
                buttons[selectedButtonX][selectedButtonY].setIcon(scaledIcon);
               
                // Mağaza fiyatını güncelle
               /* magazaFiyat = getFiyatFromDatabase("mağaza"); // Veritabanından mağaza fiyatını al
                JLabel label3 = new JLabel("Mağaza Fiyat: " + magazaFiyat);
                panel.add(label3, new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                */
               // panel.revalidate();
            }
        });

        marketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 String isletmeturu = "market";
               performButtonAction(isletmeturu,selectedButtonX,selectedButtonY);
                Image scaledImage = marketIcon.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                buttons[selectedButtonX][selectedButtonY].setText(" ");
                buttons[selectedButtonX][selectedButtonY].setIcon(scaledIcon);
               /* // Market fiyatını güncelle
                marketFiyat = getFiyatFromDatabase("market"); // Veritabanından market fiyatını al
                JLabel label2 = new JLabel("Market Fiyat: " + marketFiyat);
                panel.add(label2, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
*/
               //  panel.revalidate();
            }
        });
    }

  /*  public int butceGuncelle1(int Butce, MyDialog dialog) {
        if(Butce>=emlakFiyat)
        {
        Butce = Butce - emlakFiyat;
        }
        else
        {
             JOptionPane.showMessageDialog(frame, "Butce Yetersiz", "Hata", JOptionPane.ERROR_MESSAGE);
        }
        return Butce;
    }*/
private int getFiyatFromDatabase(String isletmeturu) {
        // Veritabanından ilgili işletmenin fiyatını alın
        // Bu kısmı kendi veritabanı yapınıza göre uyarlamalısınız
        int fiyat = 0;

        // Örnek olarak, belirli işletme türleri için varsayılan fiyatlar döndürüyoruz
        if (isletmeturu.equals("emlak")) {
            fiyat = 1800;
        } else if (isletmeturu.equals("market")) {
            fiyat = 1700;
        } else if (isletmeturu.equals("mağaza")) {
            fiyat = 2000;
        }

        return fiyat;
    }


    
    int butceGuncelle(int Butce, JLabel budgetLabel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
 
}

