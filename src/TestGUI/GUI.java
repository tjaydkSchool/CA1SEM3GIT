package TestGUI;

import Client.Client;
import java.util.Observable;
import java.util.Observer;
import static Server.MSNServer.props;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

public class GUI extends javax.swing.JFrame implements Observer {

    private Client client = new Client();
    private List<String> chm = new ArrayList();
    private DefaultListModel model;

    public GUI() {
        initComponents();
        model = new DefaultListModel();
        userList.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        msgField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatField = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList();
        actionBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        chatField.setColumns(20);
        chatField.setRows(5);
        jScrollPane1.setViewportView(chatField);

        jScrollPane2.setViewportView(userList);

        actionBtn.setText("Connect");
        actionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .addComponent(msgField))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                    .addComponent(actionBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(msgField)
                    .addComponent(actionBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void actionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionBtnActionPerformed
        try {
            if (actionBtn.getText().equals("Connect")) {
                client.connect(props.getProperty("serverIp"), Integer.parseInt(props.getProperty("port")));
                client.addObserver(this);
                actionBtn.setText("Choose username");
            } else if (actionBtn.getText().equals("Choose username")) {
                client.send("USER#" + msgField.getText());
                actionBtn.setText("Send");
                msgField.setText("");
            } else if (actionBtn.getText().equals("Send")) {
                String reciepients = userList.getSelectionModel().toString(); // DETTE SKAL VIRKE MED EN LISTE DA DET DER KOMMER UD ER HELE LISTENS NAVN PLUS INDEX NUMMER
                if (reciepients.equals(null) || reciepients.equals("") || reciepients.equals("-1")) {
                    reciepients = "*";
                }
                client.send("MSG#*#" + msgField.getText());
                String message = "MSG#*#" + msgField.getText();
                msgField.setText("");
                System.out.println("Message from GUI: " + message);
            }
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_actionBtnActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (!actionBtn.getText().equals("Connect")) {
            client.send("STOP#");
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actionBtn;
    private javax.swing.JTextArea chatField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField msgField;
    private javax.swing.JList userList;
    // End of variables declaration//GEN-END:variables

    @Override
    public synchronized void update(Observable o, Object arg) {
        Scanner scanArg = new Scanner(arg.toString()).useDelimiter("#");
        System.out.println("Message from Server to GUI: " + arg.toString());

        String action = scanArg.next();
        

        switch (action) {
            case "USERLIST":
                model.removeAllElements();
                String users = scanArg.next();
                Scanner scanUser = new Scanner(users).useDelimiter(",");
                while (scanUser.hasNext()) {
                    model.addElement(scanUser.next());
                }
                break;
            case "MSG":
                String sendFrom = scanArg.next();
                String message = scanArg.next();
                chatField.append(sendFrom + ": " + message + "\n");
                break;
        }

    }
}
