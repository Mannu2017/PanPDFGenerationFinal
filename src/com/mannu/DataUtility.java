package com.mannu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataUtility {
	private Connection conn;
	
	public DataUtility() {
		this.conn=DataConn.getConnection();
	}

	public List<AckDetail> getAckList() {
		List<AckDetail> ackDetails=new ArrayList<AckDetail>();
		AckDetail ackDetail=null;
		try {
			if(conn.isClosed()) {
				conn=DataConn.getConnection();
			}
			PreparedStatement ps=conn.prepareStatement("select w.InwardNo,w.AckNo from PanNSDLWorkAssign w inner join Inward i on w.AckNo=i.Ackno where i.NsdlUploadStatus is null and w.WorkStatis=1 order by w.InwardNo");
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				ackDetail=new AckDetail();
				ackDetail.setInwardno(rs.getString(1));
				ackDetail.setAckno(rs.getString(2));
				ackDetails.add(ackDetail);
			}
			ps.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			return ackDetails;
		}
		return ackDetails;
	}

	
	public List<String> getFilePath(Object ackno) {
		List<String> files=new ArrayList<String>();
		try {
			if(conn.isClosed()) {
				conn=DataConn.getConnection();
			}
			ResultSet rs=null;
			CallableStatement cb=conn.prepareCall("{call getpath (?)}",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			cb.setString(1,  ackno.toString());
			boolean result=cb.execute();
			int roweff=0;
			while (result || roweff !=-1) {
				if(result) {
					rs=cb.getResultSet();
					break;
				} else {
					roweff=cb.getUpdateCount();
				}
				result=cb.getMoreResults();
			}
			while (rs.next()) {
				files.add(rs.getString(1));
			}
			rs.close();
			cb.close();
			conn.close();
			
		} catch (SQLException e) {
			return files;
		}
		return files;
	}
	
	public void adminUpdate(String string) {
		try {
			if(conn.isClosed()) {
				conn=DataConn.getConnection();
			}
			PreparedStatement ps=conn.prepareStatement("update inward set NsdlUploadStatus=1 where Ackno='"+string+"'");
			ps.execute();
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatepath(String filepath, String inwardno, String ackno) {
		try {
			if(conn.isClosed()) {
				conn=DataConn.getConnection();
			}
			PreparedStatement ps=conn.prepareStatement("insert into panpdffiles values ('"+inwardno+"','"+ackno+"',getdate(),'"+filepath+"')");
			ps.execute();
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
