/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package simulate;

import DB.DB;
import static DB.ProcessDao.checkReadyStatus;
import static DB.ProcessDao.copyToTempTable;
import static DB.ProcessDao.displayProcesses;
import static DB.ProcessDao.getFirstArrivalTime;
import static DB.ProcessDao.getHighestProcessTime;
import static DB.ProcessDao.ready;
import static DB.ProcessDao.sortAndReplaceTable;
import static DB.ProcessDao.updateBurstAndReadyAutomatically;
import static DB.ProcessDao.updateBurstTime;
import static DB.ProcessDao.updateTurnaroundAndWaiting;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class simulation extends javax.swing.JFrame {

    /**
     * Creates new form simulation
     */
        int elapsed;
               int process;
        public void displayTable(){
                // TODO add your handling code here:
     
        Connection conn = DB.getConnection();
     
           try {
      PreparedStatement resultPs=conn.prepareStatement("SELECT * FROM temprr" );
          
        // Set the value for the placeholder in the query

        // Execute the query and retrieve the result set
        ResultSet resultSet = resultPs.executeQuery();
     
        // Create a DefaultTableModel to hold the data
         DefaultTableModel model = new DefaultTableModel();
     
         model.addColumn("Process Name");
         model.addColumn("Arrival Time");
         model.addColumn("Burst Time");
         model.addColumn("Turn Around Time");
         model.addColumn("Waiting Time");

         
            while (resultSet.next()) {
          Vector<Object> row = new Vector<>();
        row.add(resultSet.getString("process_name"));
        row.add(resultSet.getString("arrival"));
        row.add(resultSet.getString("burst"));
  row.add(resultSet.getString("turnaround"));
    row.add(resultSet.getString("waiting"));

        model.addRow(row);
        }
       //adds the model to the table
        table1.setModel(model);
        // Close the PreparedStatement
        resultPs.close();

        // Close the database connection
        conn.close();
       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
    
    }
        
        
        public void displayTable1(){
                // TODO add your handling code here:
     
        Connection conn = DB.getConnection();
     
           try {
      PreparedStatement resultPs=conn.prepareStatement("SELECT * FROM gantt" );
          
        // Set the value for the placeholder in the query

        // Execute the query and retrieve the result set
        ResultSet resultSet = resultPs.executeQuery();
     
        // Create a DefaultTableModel to hold the data
         DefaultTableModel model = new DefaultTableModel();
     
         model.addColumn("Process Name");
         model.addColumn("Consumed");
         model.addColumn("Process Time");


         
            while (resultSet.next()) {
          Vector<Object> row = new Vector<>();
        row.add(resultSet.getString("name"));
        row.add(resultSet.getString("consumed"));
        row.add(resultSet.getString("processTime"));


        model.addRow(row);
        }
       //adds the model to the table
        table2.setModel(model);
        // Close the PreparedStatement
        resultPs.close();

        // Close the database connection
        conn.close();
       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
    
    }
        
                public void displayTable3(){
                // TODO add your handling code here:
     
        Connection conn = DB.getConnection();
     
           try {
PreparedStatement resultPs = conn.prepareStatement("SELECT * FROM ready ORDER BY Time ASC, Burst ASC");



          
        // Set the value for the placeholder in the query

        // Execute the query and retrieve the result set
        ResultSet resultSet = resultPs.executeQuery();
     
        // Create a DefaultTableModel to hold the data
         DefaultTableModel model = new DefaultTableModel();
     
         model.addColumn("Process Name");
         model.addColumn("Remaining Burst");
         model.addColumn("Elapsed Time");


         
            while (resultSet.next()) {
          Vector<Object> row = new Vector<>();
        row.add(resultSet.getString("ProcessName"));
        row.add(resultSet.getString("Burst"));
        row.add(resultSet.getString("Time"));


        model.addRow(row);
        }
       //adds the model to the table
        table3.setModel(model);
        // Close the PreparedStatement
        resultPs.close();

        // Close the database connection
        conn.close();
       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
    
    }
    public simulation(int timeQuantum) {
        initComponents();
          sortAndReplaceTable();
copyToTempTable();
    elapsed= getFirstArrivalTime();
    System.out.println("Starting Time" + elapsed);

  // int burst=  updateBurstTime("1",5,3);


int status= checkReadyStatus(); 
int ready;


do{
process=displayProcesses();
    updateBurstAndReadyAutomatically(elapsed);
    status= checkReadyStatus(); 
  
if(status==1){

    Connection conn = null;
    int rowCount = 0; // Initialize a variable to track the number of rows

    try {
        conn = DB.getConnection();

        // Retrieve processes sorted by arrival time and then burst time
        PreparedStatement retrievePs = conn.prepareStatement("SELECT * FROM rr ");
        ResultSet retrieveRs = retrievePs.executeQuery();

        // Display the sorted processes
        while (retrieveRs.next()) {
          
            String processName = retrieveRs.getString("process_name");
                

            System.out.println("Process Name: " + processName);
                elapsed+=updateBurstTime(processName,timeQuantum,elapsed);
                  ready=ready(elapsed);
        }
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    } finally {
        // Close the database connection in the finally block
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // Handle the exception, or log it if necessary
            e.printStackTrace();
        }
    }

    // Return 0 if no records were retrieved
   

}
else{
sortAndReplaceTable();
elapsed=getFirstArrivalTime();
break;

}
}while(process!=0);




    Connection conn = null;
    int rowCount = 0; // Initialize a variable to track the number of rows

    try {
        conn = DB.getConnection();

        // Retrieve processes sorted by arrival time and then burst time
        PreparedStatement retrievePs = conn.prepareStatement("SELECT * FROM temprr ");
        ResultSet retrieveRs = retrievePs.executeQuery();

        // Display the sorted processes
        while (retrieveRs.next()) {
          
            String processName = retrieveRs.getString("process_name");
            int TAT=    getHighestProcessTime(processName);

            System.out.println("Process Name TAT: " + TAT);
updateTurnaroundAndWaiting(processName,TAT);
        }
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    } finally {
        // Close the database connection in the finally block
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // Handle the exception, or log it if necessary
            e.printStackTrace();
        }
    }

displayTable();
displayTable1();
displayTable3();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new javax.swing.JSpinner();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        table3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(table2);

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(table1);

        table3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(table3);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("READY QUEUE");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText(" GANNT CHART");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("OVERVIEW");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(51, 51, 51)
                    .addComponent(jLabel2)
                    .addContainerGap(778, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(574, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(284, 284, 284)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(simulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(simulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(simulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(simulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new simulation(1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable table1;
    private javax.swing.JTable table2;
    private javax.swing.JTable table3;
    // End of variables declaration//GEN-END:variables
}
