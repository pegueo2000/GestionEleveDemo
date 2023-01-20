package gestion;



import gestion.constantes.InterfaceGui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

public class EnregistrementEtudiant extends JFrame {
    ConnectionGestion con = new ConnectionGestion();
    String path = null;
    byte[] etudiantImage = null;
    Statement pst;
    ResultSet rs;
    JLabel lbltire, lblcode, lblnom, lblclasse, lblsexe,image1;
    JTextField txtcode, txtnom;
    JComboBox combosexe, comboclasse;
    JButton btnEnregistrer, btnSupprimer, btnTelecharger;
    JTable table, table1;
    JScrollPane scroll, scrolll;


    public EnregistrementEtudiant() throws HeadlessException {
        super.setTitle(InterfaceGui.TITRE_PRINCIPAL_FRAME);
        super.setSize(800,450);
        super.setLocationRelativeTo(null);
        super.setResizable(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pn =new JPanel();
        JScrollPane sc = new JScrollPane(pn);
        pn.setLayout(null);
        add(pn);
        pn.setBackground(new Color(220,210,220));

        lbltire = new JLabel(InterfaceGui.TITRE_LABEL);
        lbltire.setBounds(250,10,800,30);
        lbltire.setFont(new Font("Arial",Font.BOLD,24));
        lbltire.setForeground(new Color(0,0,205));
        pn.add(lbltire);
        //code etudiant
        lblcode = new JLabel(InterfaceGui.MATRICULE_LABEL);
        lblcode.setBounds(60,60,800,30);
        lblcode.setFont(new Font("Arial",Font.BOLD,16));
        lblcode.setForeground(new Color(0,0,0));
        pn.add(lblcode);

        txtcode = new JTextField();
        txtcode.setBounds(220,60,150,30);
        txtcode.setFont(new Font("Arial",Font.BOLD,14));
        pn.add(txtcode);

        // nom de l'etudiant
        lblnom = new JLabel(InterfaceGui.NOM_LABEL);
        lblnom.setBounds(60,100,800,30);
        lblnom.setFont(new Font("Arial",Font.BOLD,16));
        lblnom.setForeground(new Color(0,0,0));
        pn.add(lblnom);

        txtnom = new JTextField();
        txtnom.setBounds(220,100,310,30);
        txtnom.setFont(new Font("Arial",Font.BOLD,14));
        pn.add(txtnom);

        // sexe etudiant
        lblsexe = new JLabel(InterfaceGui.GENRE_LABEL);
        lblsexe.setBounds(60,140,100,30);
        lblsexe.setFont(new Font("Arial",Font.BOLD,16));
        lblsexe.setForeground(new Color(0,0,0));
        pn.add(lblsexe);

        combosexe = new JComboBox();
        combosexe.setBounds(215,140,150,30);
        combosexe.setFont(new Font("Arial",Font.BOLD,14));
        combosexe.addItem("");
        combosexe.addItem("Masculin");
        combosexe.addItem("Feminin");
        pn.add(combosexe);

        //classe
        lblclasse = new JLabel(InterfaceGui.CLASSE_LABEL);
        lblclasse.setBounds(60,180,800,30);
        lblclasse.setFont(new Font("Arial",Font.BOLD,16));
        lblclasse.setForeground(new Color(0,0,0));
        pn.add(lblclasse);

        comboclasse = new JComboBox();
        comboclasse.setBounds(215,180,150,30);
        comboclasse.setFont(new Font("Arial",Font.BOLD,14));
        comboclasse.addItem("");
        comboclasse.addItem("1ere année");
        comboclasse.addItem("2eme année");
        comboclasse.addItem("3eme année");
        pn.add(comboclasse);

        // bouton d'enregistrement
        //btnEnregistrer = new JButton("Enregistrer");
        //btnEnregistrer.setBounds(215,230,150,30);
        //photo
        image1 = new JLabel();
        image1.setBounds(540,60,150,150);
        image1.setFont(new Font("Arial",Font.BOLD,16));
        image1.setBackground(new Color(255,0,0));
        image1.setHorizontalAlignment(SwingConstants.CENTER);
        image1.setBorder(BorderFactory.createEtchedBorder());
        image1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                image1MouseClicked(mouseEvent);
            }
            private void image1MouseClicked(MouseEvent event){
                JFileChooser pic = new JFileChooser();
                pic.showOpenDialog(null);
                File picture = pic.getSelectedFile();
                path = picture.getAbsolutePath();
                BufferedImage img;
                try{
                    img = ImageIO.read(picture);
                    ImageIcon imageic = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(150,150,Image.SCALE_DEFAULT));
                    image1.setIcon(imageic);
                    File image = new File(path);
                    FileInputStream fis = new FileInputStream(image);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buff = new byte[1024];
                    for (int i = 0;(i=fis.read(buff)) !=-1 ; ) {
                        bos.write(buff,0,i);
                    }
                    etudiantImage = bos.toByteArray();
                }catch (Exception e){

                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        pn.add(image1);

        // bouton enregistrement
        btnEnregistrer = new JButton(InterfaceGui.ENREGISTRER_BUTTON);
        btnEnregistrer.setBounds(215,230,150,30);
        btnEnregistrer.setFont(new Font("Arial",Font.BOLD,16));
        btnEnregistrer.setForeground(Color.black);
        btnEnregistrer.setBackground(new Color(173,216,230));
        btnEnregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String num, nom, sexe, classe;
                num = txtcode.getText();
                nom= txtnom.getText();
                sexe= combosexe.getSelectedItem().toString();
                classe = comboclasse.getSelectedItem().toString();
                String rq = "insert into etudiant(code,nom,sexe,classe,photo) values(?,?,?,?,?)";
                try{
                    PreparedStatement ps = con.maconnection().prepareStatement(rq);
                    ps.setString(1,num);
                    ps.setString(2,nom);
                    ps.setString(3,sexe);
                    ps.setString(4,classe);
                    ps.setBytes(5, etudiantImage);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Etudiant enregistrer!!",null,JOptionPane.INFORMATION_MESSAGE);
                    con.maconnection().close();
                }catch (Exception e){
                    JOptionPane.showMessageDialog(null,"erreur!!",null,JOptionPane.ERROR_MESSAGE);
                    System.out.println(e.getMessage());
                }
                dispose();
               EnregistrementEtudiant etu = new EnregistrementEtudiant();
               etu.setVisible(true);
            }
        });
        pn.add(btnEnregistrer);

        // Button supprimer
        btnSupprimer = new JButton(InterfaceGui.SUPPRIMER_BUTTON);
        btnSupprimer.setBounds(370,230,150,30);
        btnSupprimer.setFont(new Font("Arial",Font.BOLD,15));
        btnSupprimer.setForeground(Color.black);
        btnSupprimer.setBackground(new Color(173,216,230));
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String num;
                num = txtcode.getText();
                String rq = "delete from etudiant where code='"+ num + "'";
                try{
                    pst =con.maconnection().createStatement();
                    pst.executeUpdate(rq);

                    JOptionPane.showMessageDialog(null,"Etudiant supprimer!!",null,JOptionPane.INFORMATION_MESSAGE);
                    con.maconnection().close();
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(null,"erreur!!",null,JOptionPane.ERROR_MESSAGE);
                    System.out.println(e.getMessage());
                }
                dispose();
                EnregistrementEtudiant etu = new EnregistrementEtudiant();
                etu.setVisible(true);
            }
        });
        pn.add(btnSupprimer);

        // liste des etudiants

        DefaultTableModel model = new DefaultTableModel();
        init();
        pn.add(scrolll);
        model.addColumn("Matricule");
        model.addColumn("Nom et prenom");
        model.addColumn("Genre");
        model.addColumn("Classe");

        table1.setModel(model);
        String sql = "select * from etudiant order by code desc";
        try{
            pst = con.maconnection().createStatement();
            rs = pst.executeQuery(sql);
            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getString("code"),
                        rs.getString("nom"),
                        rs.getString("sexe"),
                        rs.getString("classe")
                });
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"erreur",null, JOptionPane.ERROR_MESSAGE);
        }
        table1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                table1MouseReleased(mouseEvent);
            }
            private void table1MouseReleased(MouseEvent event){
                int selectionner = table1.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                txtcode.setText(model.getValueAt(selectionner,0).toString());
                txtnom.setText(model.getValueAt(selectionner,1).toString());
                combosexe.setSelectedItem(model.getValueAt(selectionner,2).toString());
                comboclasse.setSelectedItem(model.getValueAt(selectionner,3).toString());

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        //bouton recherche
        btnTelecharger = new JButton(InterfaceGui.RECHERCHER_BUTTON);
        btnTelecharger.setBounds(375,60,150,30);
        btnTelecharger.setFont(new Font("Arial",Font.BOLD,15));
        btnTelecharger.setForeground(Color.black);
        btnTelecharger.setBackground(new Color(173,216,230));
        btnTelecharger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                btnTelechargerActionPerformed(actionEvent);
            }
        });
        pn.add(btnTelecharger);

    }


    private void btnTelechargerActionPerformed(ActionEvent actionEvent) {
        String num;
        num = txtcode.getText();
        try{
            String rq = "select * from etudiant where code = ?";
            PreparedStatement ps = con.maconnection().prepareStatement(rq);
            ps.setString(1, num);
            rs = ps.executeQuery();
            if(rs.next() ==false){
                JOptionPane.showMessageDialog(null,"matricule n'existe pas!!",null,JOptionPane.ERROR_MESSAGE);
                txtcode.setText("");
            }
            else {
                txtnom.setText(rs.getString(2).trim());
                combosexe.setSelectedItem(rs.getString(3).trim());
                comboclasse.setSelectedItem(rs.getString(4).trim());
                try{
                    Blob blob = rs.getBlob("photo");
                    byte[] imagebyte = blob.getBytes(1, (int) blob.length());
                    ImageIcon imag = new ImageIcon(new ImageIcon(imagebyte).getImage().getScaledInstance(150,150,Image.SCALE_DEFAULT));
                    image1.setIcon(imag);

                }catch (Exception e){
                    JOptionPane.showMessageDialog(null,"Erreur",null,JOptionPane.ERROR_MESSAGE);
                }
            }

        }catch (Exception e)
        {

        }
    }

    public void init(){
        table1 = new JTable();
        scrolll = new JScrollPane();
        scrolll.setBounds(10,280,770,130);
        scrolll.setViewportView(table1);
    }
}
