package com.aws.lambda.online;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.aws.lambda.online.data.RequestDetails;
import com.aws.lambda.online.data.ResponseDetails;

public class MySqlTest implements RequestHandler<RequestDetails, ResponseDetails>
{
   
	public ResponseDetails handleRequest(RequestDetails requestDetails, Context arg1) {
		
		// TODO Auto-generated method stub
		ResponseDetails responseDetails = new ResponseDetails();
				try {
					insertDetails(requestDetails, responseDetails);
				} catch (SQLException sqlException) {
					responseDetails.setMessageID("999");
					responseDetails.setMessageReason("Unable to Registor "+sqlException);
				}
				return responseDetails;
	}

	private void insertDetails(RequestDetails requestDetails, ResponseDetails responseDetails) throws SQLException {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		String query = getquery(requestDetails);
		int responseCode = statement.executeUpdate(query);
		if(1 == responseCode)
		{
			responseDetails.setMessageID(String.valueOf(responseCode));
			responseDetails.setMessageReason("Successfully updated details");
		}
		
	}

	private String getquery(RequestDetails requestDetails) {
		
		String query = "INSERT INTO onlineshoping.employee(empid, empname) VALUES (";
		if (requestDetails != null) {
			query = query.concat("'" + requestDetails.getEmpID() + "','" 
					+ requestDetails.getEmpName() + "')");
		}
		System.out.println("the query is "+query);
		return query;
	}

	private Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
				String url = "jdbc:mysql://mysqltest.cgcwnq6hibqm.us-east-2.rds.amazonaws.com:3306";
				String username = "user2";
				String password = "abkmani01";
				Connection conn = DriverManager.getConnection(url, username, password);
				return conn;
	}

}
