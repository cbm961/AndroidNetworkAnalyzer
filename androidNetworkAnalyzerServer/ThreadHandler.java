package androidNetworkAnalyzer;
import java.io.*;
import java.net.*;
import java.sql.SQLException;

class ThreadHandler extends Thread {
  Socket newsock;
  int n;

  ThreadHandler(Socket s, int v) {
      newsock = s;
      n = v;
  }


  public void run() {
      try {

          PrintWriter outp = new PrintWriter(newsock.getOutputStream(), true);
          BufferedReader inp = new BufferedReader(new InputStreamReader(
                  newsock.getInputStream()));

          outp.println("Hello :: enter QUIT to exit \n");
          boolean more_data = true;
          String line;
          while (more_data) {
              line = inp.readLine();
              System.out.println("Message '" + line + "' echoed back to client.");
              if (line == null) {
                  System.out.println("line = null");
                  more_data = false;
              } else {
                  outp.println("From server: " + line + ". \n");
                  
                  /*write your protocol here */
                  if (line.trim().startsWith("ACT")) {
                	  String perOperatorOrNetwork;
                	  String date1;
                	  String date2;
                	  String user_id;
                	  
                	  String[] Fields = line.split(","); //must pass empty or -1 for fields that are not applicable/optional
              		
	              	   user_id = Fields[1];
	              	   date1 = Fields[2];
	              	   date2 = Fields[3];
	              	   perOperatorOrNetwork = Fields[4];
	              		
	              	  String out = MySQLConnection.averageConnectivityTime(user_id, date1, date2, perOperatorOrNetwork);
	              	  outp.println(out + ". \n");
                  }
          
                  else if (line.trim().startsWith("ASP")) {
                	  String perNetworkTypeOrDevice;
                	  String date1;
                	  String date2;
                	  String user_id;
                	  
                	  String[] Fields = line.split(","); //must pass empty or -1 for fields that are not applicable/optional
              		
	              	   user_id = Fields[1];
	              	   date1 = Fields[2];
	              	   date2 = Fields[3];
	              	   perNetworkTypeOrDevice = Fields[4];
	              		
	              	  String out = MySQLConnection.averageSignalPower(user_id, date1, date2, perNetworkTypeOrDevice);
	              	  outp.println(out + ". \n"); //format 4G,12;3G,9;2G
	                  
                  }
                  
                  else if (line.trim().startsWith("ASS")) {
                	  
                  }
                  
                  else if (line.trim().startsWith("RTD")) {
                	  //insert info
              		String network_operator;
            		String signal_power;
            		String SINR;
            		String SNR;
            		String network_type;
            		String frequency_band;
            		String cell_id;
            		String time_stamp;
            		String sim_id;
            		String user_id;
            		
              		String[] cellInformationFields = line.split(","); //must pass empty or -1 for fields that are not applicable/optional
            		
            		network_operator = cellInformationFields[1];
            		signal_power = cellInformationFields[2];
            		SINR = cellInformationFields[3];
            		SNR = cellInformationFields[4];
            		network_type = cellInformationFields[5];
            		frequency_band = cellInformationFields[6];
            		cell_id = cellInformationFields[7];
            		time_stamp = cellInformationFields[8];
            		sim_id = cellInformationFields[9];
            		user_id = cellInformationFields[10];
            		
            		MySQLConnection.insertCellInformation( network_operator, signal_power, SINR, SNR, network_type, frequency_band, cell_id, time_stamp, sim_id, user_id);
                  }
                  if (line.trim().equals("QUIT"))
                      more_data = false;
              }
          }
          newsock.close();
          System.out.println("Disconnected from client number: " + n);
      } catch (Exception e) {
          System.out.println("IO error " + e);
      }

  }
}