// This will convert multiple XML .nessus files into one XMLTable Report
// java XML2Report file1.nessus file2.nessus > output.[html/xls]


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

public class Nessus2Report {
	
	public static void main(String args[]) throws IOException, ParserConfigurationException,
	org.xml.sax.SAXException {
		try {
				// Need to convert this to array lists
				String tableA[][] = new String [100000][9];
				String host = null;
				int m = 0;
				int n = 0;
				int u = 0;
				String hostSet = "-";
				String portSet = "-";
			
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
					
							NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("ReportItem");

							for (int i=0; i < fstNmElmntLst.getLength(); i++)
							{
								Element fstNmElmnt = (Element) fstNmElmntLst.item(i);
								
								if (fstNmElmnt.getAttributes().getNamedItem("severity").getNodeValue() == "0") break;

								tableA[n][0] = fstNmElmnt.getAttributes().getNamedItem("severity").getNodeValue();
								tableA[n][1] = fstNmElmnt.getAttributes().getNamedItem("pluginName").getNodeValue();
								
								String port = fstNmElmnt.getAttributes().getNamedItem("port").getNodeValue();
								String proto = fstNmElmnt.getAttributes().getNamedItem("protocol").getNodeValue();
								String service = fstNmElmnt.getAttributes().getNamedItem("svc_name").getNodeValue();
								
								tableA[n][2] = port + "/" + proto + "/" + service;

								StringBuilder cve = new StringBuilder();
								
								if(fstNmElmnt.hasChildNodes())
								{
									NodeList riBody = fstNmElmnt.getChildNodes();
							
									for (int x=0; x < riBody.getLength(); x++)
									{
										if((riBody.item(x)).hasChildNodes())
										{
											if (riBody.item(x).getNodeName()=="description")
											{
												tableA[n][3] = riBody.item(x).getFirstChild().getNodeValue();
												tableA[n][3] = tableA[n][3].replaceAll("\\r|\\n", " ");
											}
											if (riBody.item(x).getNodeName()=="solution")
											{
												tableA[n][4] = riBody.item(x).getFirstChild().getNodeValue();
												tableA[n][4] = tableA[n][4].replaceAll("\\r|\\n", " ");
											}
											if (riBody.item(x).getNodeName()=="see_also")
											{
												tableA[n][5] = riBody.item(x).getFirstChild().getNodeValue();
												tableA[n][5] = tableA[n][5].replaceAll("\\r|\\n", " ");
											}
											if (riBody.item(x).getNodeName()=="cve")
											{
												cve.append(riBody.item(x).getFirstChild().getNodeValue());
												cve.append(",");
											}
										}
									}
								}

								tableA[n][6] = host;
								tableA[n][7] = cve.toString();
								
								n++;
							}
						} // end of if
					} // end of for loop 2
				} // end of for loop 1
				System.out.println("<table border=1>");
				System.out.println("<tr><th>Severity</th><th>Vulnerability</th><th>Ports and Service</th><th>Observation</th><th>Recommendation</th><th>References</th><th>Host List</th><th>CVE</th></tr>");

				String[] tracker = new String[n];
			
				for (int i = 0; i < tableA.length; i++)
				{
					ArrayList<String> hostSetIndex = new ArrayList<String>();
					ArrayList<String> portSetIndex = new ArrayList<String>();
					if (tableA[i][1] == null) break;
					for (int y = 0; y < tableA.length; y++)
					{
						if (tableA[y][1] == null) break;
						if (tableA[i][1].compareTo(tableA[y][1])==0)
						{
							if (hostSetIndex.contains(tableA[y][6])==false)
							{
								hostSetIndex.add(tableA[y][6]);
							}
							if (portSetIndex.contains(tableA[y][2])==false)
							{
								portSetIndex.add(tableA[y][2]);
							}
						}
					}
					for (int z = 0; z < tracker.length; z++)
					{
						if (tracker[z] == null) break;
						if (tableA[i][1].compareTo(tracker[z])==0)
						{
							m = 1;
						}	
					}
					if (m == 0)
					{
						tracker[u] = tableA[i][1];
						u++;
							// NOTE: Add Sort To Output
						for (int b = 0; b < hostSetIndex.size(); b++)
						{
							if (hostSet == "-") 
							{
								hostSet = hostSetIndex.get(b);
							} else {
								hostSet = hostSet + ", " + hostSetIndex.get(b);
							}
						}
						for (int b = 0; b < portSetIndex.size(); b++)
						{
							if (portSet == "-") 
							{
								portSet = portSetIndex.get(b);
							} else {
								portSet = portSet + ", " + portSetIndex.get(b);
							}
						}
						System.out.println("<tr><td>" + tableA[i][0] + "</td><td>" + tableA[i][1] + "</td><td>" + portSet + "</td><td>" + tableA[i][3] + "</td><td>" + tableA[i][4] + "</td><td>" + tableA[i][5] + "</td><td>" + hostSet + "</td><td>" + tableA[i][7] + "</td></tr>");
					};
					m = 0;
					hostSet = "-";
					portSet = "-";
				}
			
				System.out.println("</table>");
			
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}