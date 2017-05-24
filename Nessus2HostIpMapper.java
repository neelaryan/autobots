// This will convert multiple XML .nessus files into one XMLTable Report
// java Nessus2HostIpMapper file1.nessus file2.nessus > output.[html/xls]


import java.io.File;
import java.io.IOException;
import java.util.ArrayList; 

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Nessus2HostIpMapper {
	
	public static void main(String args[]) throws IOException, ParserConfigurationException,
	org.xml.sax.SAXException {
		try {
				// Need to convert this to array lists
				String tableA[][] = new String [100000][9];
				String host = null;
				int m = 0;
				int n = 0;
				int u = 0;
				String attr = null;
			
				for (int a = 0; a < args.length; a++)
				{
					File file = new File(args[a]);
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document doc = db.parse(file);
					doc.getDocumentElement().normalize();
					NodeList nodeLst = doc.getElementsByTagName("ReportHost");
			
					for (int s = 0; s < nodeLst.getLength(); s++) 
					{
				
						Node fstNode = nodeLst.item(s);
				
						if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
						{
					
							Element fstElmnt = (Element) fstNode;
							host = fstElmnt.getAttributes().getNamedItem("name").getNodeValue();

							tableA[n][1] = host;
					
							NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("HostProperties");

							for (int i=0; i < fstNmElmntLst.getLength(); i++)
							{
								Element fstNmElmnt = (Element) fstNmElmntLst.item(i);
								
								if(fstNmElmnt.hasChildNodes())
								{
									NodeList riBody = fstNmElmnt.getChildNodes();

									for (int x=0; x < riBody.getLength(); x++)
									{
										if((riBody.item(x)).hasChildNodes())
										{
											if (riBody.item(x).getNodeName()=="tag")
											{
												Element inTag = (Element) riBody.item(x);
												attr = inTag.getAttributes().getNamedItem("name").getNodeValue();
												if(attr.equals("host-ip"))
												{
													tableA[n][0] = inTag.getFirstChild().getNodeValue();
												}
											}
										}
									}
								}
								
								n++;
							}
						} // end of if
					} // end of for loop 2
				} // end of for loop 1
				System.out.println("<table border=1>");
				System.out.println("<tr><th>IP Address</th><th>System Name</th></tr>");
			
				for (int i = 0; i < tableA.length; i++)
				{
					if (tableA[i][0] == null) break;
					
					System.out.println("<tr><td>" + tableA[i][0] + "</td><td>" + tableA[i][1] + "</td></tr>");
				}
			
				System.out.println("</table>");
			
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}