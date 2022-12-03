package androidNetworkAnalyzer;

import java.sql.*;

public class MySQLConnection {
	static final String dbURL = "jdbc:mysql://localhost:3306/androidnetworkanalyzer";
	
	public static Connection establishConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbURL, "root", "Goldelino19");	
			
		}
		catch (SQLException e) {
					
					e.printStackTrace();
					System.out.println("cant connect to database server");
		}
		return conn;	
	}
	
	public static void insertCellInformation(String network_operator, String signal_power, String SINR, String SNR, String network_type, String frequency_band, String cell_id, String time_stamp, String sim_id, String user_id) throws SQLException {

		
		Connection conn = establishConnection();
		String query = "INSERT INTO cell_information (network_operator, signal_power, SINR, SNR, network_type, frequency_band, cell_id, time_stamp, sim_id, user_id) VALUES (?,?,?,?,?,?,?,?,?,?)"; //inserts document in documents 
		System.out.println("query = " + query);
		
		PreparedStatement preparedStmt = conn.prepareStatement(query); 
		
		preparedStmt.setString (1, network_operator);
		preparedStmt.setString (2, signal_power);
		preparedStmt.setString (3, SINR);
		preparedStmt.setString (4, SNR);
		preparedStmt.setString (5, network_type);
		preparedStmt.setString (6, frequency_band);
		preparedStmt.setString (7, cell_id);
		preparedStmt.setString (8, time_stamp);
		preparedStmt.setString (9, sim_id);
		preparedStmt.setString (10, user_id);

        // execute the prepared statement
        preparedStmt.execute();
	    conn.close();


	   
	}

	public static String averageConnectivityTime(String user_id, String date1, String date2, String perOperatorOrNetwork) throws SQLException {
		String out = "";
	
		String totalNumber;
		String query;
		Double iTotalNumber=null;
		Double countPerOp;
		PreparedStatement ps;
		ResultSet rs;
		
		Connection conn = establishConnection();

		if(perOperatorOrNetwork.equals("network_operator")) {
			totalNumber = "select count(network_operator) as count from cell_information where user_id=? and time_stamp >= ? and time_stamp <= ?";
			query = "select network_operator, count(network_operator) from cell_information where user_id=? and time_stamp >= ? and time_stamp <= ? group by network_operator";
		}
		else {
			totalNumber = "select count(network_type) as count from cell_information where user_id=? and time_stamp >= ? and time_stamp <= ?";
			query = "select network_type, count(network_type) from cell_information where user_id=? and time_stamp >= ? and time_stamp <= ? group by network_type";
		}
		ps = conn.prepareStatement(totalNumber);
		ps.setString(1,user_id);
		ps.setString(2,date1);
		ps.setString(3,date2);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			System.out.println(ps);
			System.out.println(rs.getString(1));
			iTotalNumber = Double.parseDouble(rs.getString(1));
		}
		
		ps = conn.prepareStatement(query);
		ps.setString(1,user_id);
		ps.setString(2,date1);
		ps.setString(3,date2);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			countPerOp = Double.parseDouble(rs.getString(2));
			System.out.println(rs.getString(1)+","+countPerOp/iTotalNumber);
			out.concat(rs.getString(1)+","+countPerOp/iTotalNumber+";");
		}
		conn.close();
        return out;
		
	}
	
	public static String averageSignalPower(String user_id,String date1, String date2, String perNetworkTypeOrDevice) throws SQLException {
		String out="";
		String query;
		PreparedStatement ps;
		ResultSet rs;
		
		Connection conn = establishConnection();

		if(perNetworkTypeOrDevice.equals("network_type")) {
			query = "select network_type, avg(signal_power) from cell_information where user_id=? and time_stamp >= ? and time_stamp <= ? group by network_type";
		}
		else {
			query = "select sim_id, avg(signal_power) from cell_information where user_id=? and time_stamp >= ? and time_stamp <= ?  group by sim_id";	
		}
		ps = conn.prepareStatement(query);
		ps.setString(1,user_id);
		ps.setString(2,date1);
		ps.setString(3,date2);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			System.out.println(rs.getString(1)+","+rs.getString(2));
			out.concat(rs.getString(1)+","+rs.getString(2)+";");
		}
		conn.close();
		return out;
	}
	public void averageSNRorSINR(String user_id, String date1, String date2, String SNRorSINR) {
		
	}
	
	public static void main(String[] args) throws SQLException{
		//123458 x2
		//12345 x2
		//123456 x1
		//123457 x1
		String cellInformation1 = "Touch,-11,5,-1,4G,20,37100-81937400,2022-11-20,1,12345";
		String cellInformation2 = "Alfa,-12,5,-1,4G,20,37100-81937401,2022-11-12,1,123456";
		String cellInformation3 = "Alfa,-15,5,-1,4G,20,37100-81937402,2022-10-12,2,123457";
		String cellInformation4 = "Alfa,-20,5,-1,3G,20,37100-81937400,2022-10-12,4,123458";
		String cellInformation5 = "Touch,-12,5,-1,3G,20,37100-81937403,2022-11-12,4,123458";
		String cellInformation6 = "RTD,Alfa,-2,5,-1,3G,20,37100-81937400,2022-11-18,2,12345";
		
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
		
  		String[] cellInformationFields = cellInformation6.split(","); //must pass empty or -1 for fields that are not applicable/optional
		
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
		
		
	/*	insertCellInformation( network_operator, signal_power, SINR, SNR, network_type, frequency_band, cell_id, time_stamp, sim_id, user_id);
		insertCellInformation(cellInformation1);
		insertCellInformation(cellInformation2);
		insertCellInformation(cellInformation3);
		insertCellInformation(cellInformation4);
		insertCellInformation(cellInformation5);
		insertCellInformation(cellInformation6); 
		*/
		
		//String user_id ="12345";
		String date1 = "2022-11-18";
		String date2= "2022-11-20";
		String perOperatorOrNetwork = "network_type";
		String perNetworkTypeOrDevice = "sim_id";
	    //averageConnectivityTime(cell_id, "", "", "network_operator");
		//insertCellInformation(cellInformation);
		//averageSignalPower(cell_id, "", "", "network_type"); 
	    //averageConnectivityTime(user_id,  date1,  date2,  perOperatorOrNetwork);
		averageSignalPower(user_id,  date1,  date2,  perNetworkTypeOrDevice);
		

	}
}
