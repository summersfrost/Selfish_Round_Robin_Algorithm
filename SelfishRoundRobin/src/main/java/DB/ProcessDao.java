/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class ProcessDao {

public static int displayProcesses() {
    Connection conn = null;
    int rowCount = 0; // Initialize a variable to track the number of rows

    try {
        conn = DB.getConnection();

        // Retrieve processes sorted by arrival time and then burst time
        PreparedStatement retrievePs = conn.prepareStatement("SELECT * FROM rr ");
        ResultSet retrieveRs = retrievePs.executeQuery();

        // Display the sorted processes
        while (retrieveRs.next()) {
            rowCount++; // Increment the row count
            String processName = retrieveRs.getString("process_name");
            int arrival = retrieveRs.getInt("arrival");
            int burst = retrieveRs.getInt("burst");

            System.out.println("Process Name: " + processName);
            System.out.println("Arrival Time: " + arrival);
            System.out.println("Burst Time: " + burst);
            System.out.println("---------------");
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
    return rowCount;
}
public static int save(String processName, int arrival, int burst) {
    int status = 0;
    Connection conn = null;
    try {
        conn = DB.getConnection();

        // Check if process with the same processName already exists
        PreparedStatement checkNamePs = conn.prepareStatement("SELECT COUNT(*) FROM rr WHERE process_name = ?");
        checkNamePs.setString(1, processName);
        ResultSet checkNameRs = checkNamePs.executeQuery();
        checkNameRs.next();
        int existingProcessNameCount = checkNameRs.getInt(1);

        if (existingProcessNameCount > 0) {
            JOptionPane.showMessageDialog(null, "Process with the same name already exists.");
            return status;
        }

        // Insert the new process record
        try (PreparedStatement insertPs = conn.prepareStatement(
                "INSERT INTO rr(process_name, arrival, burst) VALUES (?, ?, ?)")) {

            insertPs.setString(1, processName);
            insertPs.setInt(2, arrival);
            insertPs.setInt(3, burst);

            status = insertPs.executeUpdate();
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
    return status;
}

public static int deleteAll() {
    int status = 0;
    try (Connection conn = DB.getConnection()) {
        PreparedStatement deletePs = conn.prepareStatement("DELETE FROM rr");
        status = deletePs.executeUpdate();
        deletePs=conn.prepareStatement("DELETE FROM gantt");
        status=deletePs.executeUpdate();
                deletePs=conn.prepareStatement("DELETE FROM temprr");
        status=deletePs.executeUpdate();
                deletePs=conn.prepareStatement("DELETE FROM ready");
        status=deletePs.executeUpdate();
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    }
    return status;
}public static void sortAndReplaceTable() {
    Connection conn = null;

    try {
        conn = DB.getConnection();

        // Create a new temporary table
        PreparedStatement createTempTablePs = conn.prepareStatement(
                "CREATE TABLE temp_rr AS SELECT * FROM rr ORDER BY arrival, burst"
        );
        createTempTablePs.executeUpdate();

        // Get the arrival time of the first row in the sorted table
        PreparedStatement getFirstArrivalPs = conn.prepareStatement(
                "SELECT arrival FROM temp_rr LIMIT 1"
        );
        ResultSet firstArrivalResultSet = getFirstArrivalPs.executeQuery();
        int firstArrival = 0; // Initialize to a default value
        if (firstArrivalResultSet.next()) {
            firstArrival = firstArrivalResultSet.getInt("arrival");
        }

        // Update all arrival times in the temporary table by subtracting the first arrival time
        PreparedStatement updateArrivalPs = conn.prepareStatement(
                "UPDATE temp_rr SET arrival = arrival - ?"
        );
        updateArrivalPs.setInt(1, firstArrival);
        updateArrivalPs.executeUpdate();

        // Drop the original table
        PreparedStatement dropOriginalTablePs = conn.prepareStatement("DROP TABLE rr");
        dropOriginalTablePs.executeUpdate();

        // Rename the temporary table to the original table name
        PreparedStatement renameTempTablePs = conn.prepareStatement("ALTER TABLE temp_rr RENAME TO rr");
        renameTempTablePs.executeUpdate();

        System.out.println("Table 'rr' sorted and arrival times adjusted successfully.");

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
}

public static int rearrangeProcesses() {
    Connection conn = null;
    int rowCount = 0; // Initialize a variable to track the number of rows

    try {
        conn = DB.getConnection();

        // Update processes to rearrange them based on arrival time and burst time
        String updateQuery = "UPDATE rr SET arrival = ?, burst = ? WHERE process_name = ?";
        PreparedStatement updatePs = conn.prepareStatement(updateQuery);

        // Retrieve processes sorted by arrival time and then burst time
        PreparedStatement retrievePs = conn.prepareStatement("SELECT * FROM rr ORDER BY arrival, burst");
        ResultSet retrieveRs = retrievePs.executeQuery();

        // Update the order of processes
        while (retrieveRs.next()) {
            rowCount++; // Increment the row count
            String processName = retrieveRs.getString("process_name");
            int arrival = retrieveRs.getInt("arrival");
            int burst = retrieveRs.getInt("burst");

            // Update the record with the new order
            updatePs.setInt(1, arrival);
            updatePs.setInt(2, burst);
            updatePs.setString(3, processName);
            updatePs.executeUpdate();
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

    // Return the number of rows processed
    return rowCount;
}



// Example usage:
// Call this function after saving processes using the save method
// displayProcesses();
public static int updateBurstTime(String processName, int deductionValue, int elapsed) {
    Connection conn = DB.getConnection();
    int updatedDeductionValue = deductionValue;

    try {
        // Assuming your table has a primary key named "id"
        PreparedStatement selectPs = conn.prepareStatement("SELECT burst , ready FROM rr WHERE process_name = ? " );
        selectPs.setString(1, processName);

        // Execute the select query to get the current burst value
        ResultSet resultSet = selectPs.executeQuery();

        if (resultSet.next()) {
            int currentBurst = resultSet.getInt("burst");
            int readyvalue=resultSet.getInt("ready");
            // Check if the deduction value is greater than the burst
            if (deductionValue > currentBurst) {
                updatedDeductionValue = currentBurst;
            }

            // Update the burst time in the database
            PreparedStatement updatePs = conn.prepareStatement("UPDATE rr SET burst = burst - ? WHERE process_name = ?");
            updatePs.setInt(1, updatedDeductionValue);
            updatePs.setString(2, processName);
            updatePs.executeUpdate();

            // Insert into Gantt table
            PreparedStatement insertPs = conn.prepareStatement("INSERT INTO gantt (name, consumed, processTime) VALUES (?, ?, ?)");
            insertPs.setString(1, processName);
            insertPs.setInt(2, updatedDeductionValue);
            insertPs.setInt(3, elapsed + updatedDeductionValue);
            insertPs.executeUpdate();

            // Display the number of rows updated and the updated deduction value
            System.out.println("Updated burst time for process " + processName + ". Deduction value: " + updatedDeductionValue);
        }

        // Close the PreparedStatements
        selectPs.close();

        // Close the database connection
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    return updatedDeductionValue + 1;
}


public static int getFirstArrivalTime() {
      Connection conn = null;

        try {
            conn = DB.getConnection();

            // Get the arrival time of the first row in the sorted table
            String sql = "SELECT arrival FROM rr ORDER BY arrival LIMIT 1";
            PreparedStatement getFirstArrivalPs = conn.prepareStatement(sql);
            ResultSet firstArrivalResultSet = getFirstArrivalPs.executeQuery();

            if (firstArrivalResultSet.next()) {
                return firstArrivalResultSet.getInt("arrival");
            }

        } catch (SQLException e) {
            String error = e.getMessage();
            // Log the error instead of showing a dialog
            System.err.println(error);
        } finally {
            // Close the database connection in the finally block
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Handle the exception, or log it if necessary
                e.printStackTrace();
            }
        }

        // Return a default value or throw an exception if needed
        return 0;

}
public static void updateBurstAndReadyAutomatically(int elapsed) {
    Connection conn = DB.getConnection();

    try {
        // Assuming your table has a primary key named "id"
        PreparedStatement selectAllPs = conn.prepareStatement("SELECT process_name, burst, arrival FROM rr");
        
        // Execute the select query to get all processes
        ResultSet resultSet = selectAllPs.executeQuery();

        while (resultSet.next()) {
            String currentProcessName = resultSet.getString("process_name");
            int currentBurst = resultSet.getInt("burst");
            int arrival = resultSet.getInt("arrival");

            // Check if the burst time is equal to 0, delete the row
            if (currentBurst == 0) {
                PreparedStatement deletePs = conn.prepareStatement("DELETE FROM rr WHERE process_name = ?");
                deletePs.setString(1, currentProcessName);
                deletePs.executeUpdate();
                System.out.println("Deleted row for process " + currentProcessName);
            } else {
                // Check if elapsed time is greater than arrival
                if (elapsed < arrival) {
                    // Update the ready to 0
                    PreparedStatement updateReadyPs = conn.prepareStatement("UPDATE rr SET ready = 0 WHERE process_name = ?");
                    updateReadyPs.setString(1, currentProcessName);
                    updateReadyPs.executeUpdate();
                    System.out.println("Updated ready to 0 for process " + currentProcessName);
                } else {
                    // Update the ready to 1
                    PreparedStatement updateReadyPs = conn.prepareStatement("UPDATE rr SET ready = 1 WHERE process_name = ?");
                    updateReadyPs.setString(1, currentProcessName);
                    updateReadyPs.executeUpdate();
                    System.out.println("Updated ready to 1 for process " + currentProcessName);
                }
            }
        }

        // Close the PreparedStatements
        selectAllPs.close();

        // Close the database connection
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}public static int checkReadyStatus() {
    Connection conn = DB.getConnection();
    PreparedStatement selectReadyPs = null;

    try {
        selectReadyPs = conn.prepareStatement("SELECT ready FROM rr");
        ResultSet resultSet = selectReadyPs.executeQuery();

        while (resultSet.next()) {
            int readyStatus = resultSet.getInt("ready");
            if (readyStatus == 1) {
                // If any row has ready = 1, return 1
                return 1;
            }
        }
        
        

        // If no row has ready = 1, return 0
        return 0;
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return -1; // Handle the exception appropriately
    } finally {
        try {
            // Close the PreparedStatements and the database connection in the finally block
            if (selectReadyPs != null) {
                selectReadyPs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
public static int ready(int elapsed) {
    Connection conn = DB.getConnection();
    PreparedStatement selectReadyPs = null;

    try {
      selectReadyPs = conn.prepareStatement("SELECT process_name, burst,arrival FROM rr WHERE ready = 1 && burst!=0");
    ResultSet resultSet = selectReadyPs.executeQuery();


while (resultSet.next()) {
    String processName = resultSet.getString("process_name");
    int burst = resultSet.getInt("burst");
    int arrival = resultSet.getInt("arrival");

    // Check if the process already exists in the readytable
    PreparedStatement checkExistencePs = conn.prepareStatement("SELECT COUNT(*) FROM ready WHERE ProcessName = ?");
    checkExistencePs.setString(1, processName);
    ResultSet existenceResultSet = checkExistencePs.executeQuery();
    existenceResultSet.next();
    int count = existenceResultSet.getInt(1);

    if (count == 0) {
        // Process doesn't exist in readytable, insert with arrival
        PreparedStatement insertReadyPs = conn.prepareStatement("INSERT INTO ready (ProcessName, Burst, Time) VALUES (?, ?, ?)");
        insertReadyPs.setString(1, processName);
        insertReadyPs.setInt(2, burst);
        insertReadyPs.setInt(3, arrival);
        insertReadyPs.executeUpdate();
    } else {
        PreparedStatement insertReadyPs = conn.prepareStatement("INSERT INTO ready (ProcessName, Burst, Time) VALUES (?, ?, ?)");
        insertReadyPs.setString(1, processName);
        insertReadyPs.setInt(2, burst);
        insertReadyPs.setInt(3, elapsed);
        insertReadyPs.executeUpdate();
    }

    // If any row has ready = 1, return 1

}

        

        // If no row has ready = 1, return 0
      return 1;
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return -1; // Handle the exception appropriately
    } finally {
        try {
            // Close the PreparedStatements and the database connection in the finally block
            if (selectReadyPs != null) {
                selectReadyPs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}




    public static int getHighestProcessTime(String name) {
        Connection conn = null;
        int highestProcessTime = -1; // Default value in case no records are found

        try {
            conn = DB.getConnection();

            // Query to find the highest process time for a given name
            String query = "SELECT MAX(processTime) FROM gantt WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            // Check if there are any results
            if (rs.next()) {
                highestProcessTime = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception, or log it if necessary
        } finally {
            // Close the database connection in the finally block
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception, or log it if necessary
            }
        }

        return highestProcessTime;
    }



    public static void updateTurnaroundAndWaiting(String name, int tat) {
        Connection conn = null;

        try {
            conn = DB.getConnection();

            // Update the turnaround and waiting columns in the temprr table
            String query = "UPDATE temprr SET turnaround = ?, waiting = ? WHERE process_name = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            
            // Calculate waiting time (waiting = turnaround - arrival)
            int waitingTime = tat - getArrivalTime(conn, name);

            ps.setInt(1, tat);           // Set the turnaround time
            ps.setInt(2, waitingTime);   // Set the waiting time
            ps.setString(3, name);       // Set the process name for the WHERE clause

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Turnaround and waiting time updated successfully for " + name);
            } else {
                System.out.println("No records found for " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception, or log it if necessary
        } finally {
            // Close the database connection in the finally block
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception, or log it if necessary
            }
        }
    }
    
     private static int getArrivalTime(Connection conn, String name) throws SQLException {
        String query = "SELECT arrival FROM temprr WHERE process_name = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, name);

        int arrivalTime = 0; // Default value if no records found

        try (var resultSet = ps.executeQuery()) {
            if (resultSet.next()) {
                arrivalTime = resultSet.getInt("arrival");
            }
        }

        return arrivalTime;
    }
public static void copyToTempTable() {
    Connection conn = null;

    try {
        conn = DB.getConnection();

        // Delete existing data from the temporary table if it exists
        PreparedStatement deleteTempTableDataPs = conn.prepareStatement("DELETE FROM temprr");
        deleteTempTableDataPs.executeUpdate();

        // Insert data directly from 'rr' to 'temprr'
        PreparedStatement copyDataPs = conn.prepareStatement(
                "INSERT INTO temprr SELECT * FROM rr"
        );
        copyDataPs.executeUpdate();

        System.out.println("Data from 'rr' copied to 'temprr' successfully.");

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
}
 
}
