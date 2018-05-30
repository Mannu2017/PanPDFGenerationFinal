package com.mannu;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;

public class GenerationPage {
	
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
					String baseUrl1 = FilenameUtils.getPath("\\\\192.168.78.197\\pan-scan6");
					File fname1=new File(baseUrl1);
					Runtime rt1 = Runtime.getRuntime();
					if(!fname1.exists()) {
						rt1.exec(new String[] {"cmd.exe","/c","net use \\\\192.168.78.197\\pan-scan6 /user:tininward Karvy123$"});
					}
					
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				try {
					new GenerationPage();
				} catch (FileNotFoundException | DocumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GenerationPage() throws DocumentException, IOException {
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
					utility.updatepath(ff+"\\"+ackDetail.getInwardno()+"_"+ackDetail.getAckno()+".pdf",ackDetail.getInwardno(),ackDetail.getAckno());
					Document document=new Document();
					PdfAWriter writer=PdfAWriter.getInstance(document,
							new FileOutputStream(ff+"\\"+ackDetail.getInwardno()+"_"+ackDetail.getAckno()+".pdf"), PdfAConformanceLevel.PDF_A_1A);
					document.addTitle(ackDetail.getAckno());
					document.addAuthor("Author");
					document.addSubject("Karvy-Pan");
					document.addLanguage("nl-nl");
			        document.addCreationDate();
			        document.addCreator("Karvy-Mannu");
			        writer.setTagged();
			        writer.createXmpMetadata();
					document.open();
					for(String str:filepath) {
						Image img=makeGray(str.toString());	
						img.setAccessibleAttribute(PdfName.ALT, new PdfString("Logo"));
						document.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
						document.setMargins(0, 0, 0, 0);
						document.newPage();
						document.add(img);
					}
					document.close();
					writer.close();
				}
				
			} else {
				System.out.println("Image Not found in Smart Server");
			}
			
			
		}
		
		
	}
	
	private Image makeGray(String filepath) throws IOException, DocumentException{
		try {
			String baseUrl = FilenameUtils.getPath(filepath);
			File fname=new File(baseUrl);
			Runtime rt = Runtime.getRuntime();
			if(!fname.exists()) {
				rt.exec(new String[] {"cmd.exe","/c","net use "+filepath+" /user:logicalaccess@karvy.com India@123"});
			}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		BufferedImage bi = ImageIO.read(new File(filepath));
        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        newBi.getGraphics().drawImage(bi, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newBi, "png", baos);
        return Image.getInstance(baos.toByteArray());
	}
	
}
