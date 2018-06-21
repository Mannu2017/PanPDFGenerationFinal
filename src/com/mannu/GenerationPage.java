package com.mannu;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class GenerationPage {
	PDDocument doc = null;
	PDPage page =null;
	DataUtility utility=new DataUtility();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					String baseUrl = FilenameUtils.getPath("\\\\srv-kdms-file2\\images");
					File fname=new File(baseUrl);
					Runtime rt = Runtime.getRuntime();
					if(!fname.exists()) {
						rt.exec(new String[] {"cmd.exe","/c","net use \\\\srv-kdms-file2\\images /user:logicalaccess@karvy.com India@123"});
					}
					String baseUrl2 = FilenameUtils.getPath("\\\\srv-kdms-pan5\\images");
					File fname2=new File(baseUrl2);
					Runtime rt2 = Runtime.getRuntime();
					if(!fname2.exists()) {
						rt2.exec(new String[] {"cmd.exe","/c","net use \\\\srv-kdms-pan5\\images /user:logicalaccess@karvy.com India@123"});
					}
					
					String baseUrl1 = FilenameUtils.getPath("\\\\192.168.78.197\\pan-scan6");
					File fname1=new File(baseUrl1);
					Runtime rt1 = Runtime.getRuntime();
					if(!fname1.exists()) {
						rt1.exec(new String[] {"cmd.exe","/c","net use \\\\192.168.78.197\\pan-scan6 /user:tininward Karvy123$"});
					}
					
					} catch (Exception e2) {
						e2.printStackTrace();
					}
			}
		});
	}

	public GenerationPage() throws IOException {
		DateFormat df=new SimpleDateFormat("dd-MM-yyyy");
		File ff=new File("\\\\192.168.78.197\\pan-scan6\\"+df.format(new Date()));
		if(!ff.exists()) {
			ff.mkdir();
		}
		
		List<AckDetail> ackDetails=utility.getAckList();
		for(AckDetail ackDetail:ackDetails) {
			System.out.println("Data Details: "+ackDetail.getAckno()+" ^ "+ackDetail.getInwardno());
			List<String> filepath=utility.getFilePath(ackDetail.getAckno());
			if(filepath.size()>0) {
				File fi=new File(filepath.get(0));
				utility.adminUpdate(ackDetail.getAckno());
				if(fi.exists()) {
					doc = new PDDocument();
					for(String str:filepath) {
						BufferedImage awtImage = BWimage(str.toString());
						page = new PDPage(new PDRectangle( awtImage.getWidth(), awtImage.getHeight() ));
					    doc.addPage(page);
					    PDImageXObject  pdImageXObject = LosslessFactory.createFromImage(doc, awtImage);
					    PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, false);
					    contentStream.drawImage(pdImageXObject, 0, 0, awtImage.getWidth(), awtImage.getHeight());
					    contentStream.close();
					}
					doc.save(ff+"\\"+ackDetail.getInwardno()+"_"+ackDetail.getAckno()+".pdf");
					doc.close();
					utility.updatepath(ff+"\\"+ackDetail.getInwardno()+"_"+ackDetail.getAckno()+".pdf",ackDetail.getInwardno(),ackDetail.getAckno());
					
					
				}
				
			} else {
				System.out.println("Image Not found in Smart Server");
			}
		}	
	}
	private static BufferedImage BWimage(String file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D graphic = result.createGraphics();
        graphic.drawImage(image, 0, 0, Color.WHITE, null);
        graphic.dispose();
        
		return result;
	}
}
