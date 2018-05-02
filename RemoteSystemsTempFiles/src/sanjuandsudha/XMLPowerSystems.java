package sanjuandsudha;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import java.sql.Connection;
import java.sql.DriverManager;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.lang.*;
public class XMLPowerSystems
{

	private static String ss;

	public static void main(String[] args) 
	{
		try {

            DocumentBuilderFactory EQ_1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder EQ_2 = EQ_1.newDocumentBuilder();
            Document EQ = EQ_2.parse("MicroGridTestConfiguration_T1_BE_EQ_V2.xml");
            EQ.getDocumentElement ().normalize();
            NodeList Basevoltage = EQ.getElementsByTagName("cim:BaseVoltage");
           // Element Base = (Element)Basevoltage.item(0);
            System.out.println("Base Voltage");
            for(int s=0; s<Basevoltage.getLength() ; s++)
            {

                Node Base_vol = Basevoltage.item(s);
                
                if(Base_vol.getNodeType() == Node.ELEMENT_NODE)
                {	Element Base=(Element)Base_vol;
                	String BV_RDF_ID = Base.getAttribute("rdf:ID");
                	NodeList Nominal= Base.getElementsByTagName("cim:BaseVoltage.nominalVoltage");
                	for(int i=0; i<Nominal.getLength() ; i++)
                	{ Node ind_vol=Nominal.item(i);
                	
                	if(ind_vol.getNodeType() == Node.ELEMENT_NODE)
                	{ 	Element vol=(Element)ind_vol;
                	    String NominalVoltage = vol.getTextContent();
                	   
                        System.out.println("Reference ID" + BV_RDF_ID);
                        System.out.println("Nominal Voltage : " + NominalVoltage);
                    }
                }
                                    
                }
            
			}
            System.out.println("SubStation");
            NodeList Substation = EQ.getElementsByTagName("cim:Substation");
            for(int s=0; s<Substation.getLength() ; s++)
            {

                Node Sub = Substation.item(s);
                
                if(Sub.getNodeType() == Node.ELEMENT_NODE)
                {	Element Sub_1=(Element)Sub;
                	String Sub_RDF_ID = Sub_1.getAttribute("rdf:ID");
                	NodeList Sub_Name= Sub_1.getElementsByTagName("cim:IdentifiedObject.name");
                	for(int i=0; i<Sub_Name.getLength() ; i++)
                	{ Node Sub_Name_1=Sub_Name.item(i);
                	
                	if(Sub_Name_1.getNodeType() == Node.ELEMENT_NODE)
                	{ 	Element Subs=(Element)Sub_Name_1;
                	    String SubstationName = Subs.getTextContent();
                	    NodeList Sub_Reg= Sub_1.getElementsByTagName("cim:Substation.Region");
                    	for(int p=0; p<Sub_Name.getLength() ; p++)
                    	{ Node Sub_Reg_1=Sub_Reg.item(i);
                    	
                    	if(Sub_Reg_1.getNodeType() == Node.ELEMENT_NODE)
                    	{	Element Subrdf=(Element)Sub_Reg_1;
                    	String Sub_Region_RDF_ID = Subrdf.getAttribute("rdf:resource");
                        System.out.println("Reference ID" + Sub_RDF_ID);
                        System.out.println("Name : " + SubstationName);
                        System.out.println("Rgion RDF_ID : " + Sub_Region_RDF_ID);
                    }
                }
                                    
                }
            
			}
		}
		}
		
		System.out.println("Voltage Level");
        NodeList VoltageLevel = EQ.getElementsByTagName("cim:VoltageLevel");
        for(int s=0; s<VoltageLevel.getLength() ; s++)
        {

            Node VoltageL = VoltageLevel.item(s);
            
            if(VoltageL.getNodeType() == Node.ELEMENT_NODE)
            {	Element VoltageL_1=(Element)VoltageL;
            	String VoltageLevel_RDF_ID = VoltageL_1.getAttribute("rdf:ID");
            	NodeList VoltageL_Name= VoltageL_1.getElementsByTagName("cim:IdentifiedObject.name");
            	for(int i=0; i<VoltageL_Name.getLength() ; i++)
            	{ Node VoltageL_Name_1=VoltageL_Name.item(i);
            	
            	if(VoltageL_Name_1.getNodeType() == Node.ELEMENT_NODE)
            	{ 	Element VoltageLs=(Element)VoltageL_Name_1;
            	    String VoltageLevel_Name = VoltageLs.getTextContent();
            	    NodeList VoltageLevel_Sub= VoltageL_1.getElementsByTagName("cim:VoltageLevel.Substation");
                	for(int p=0; p<VoltageLevel_Sub.getLength() ; p++)
                	{ Node VoltageLevel_Sub_1=VoltageLevel_Sub.item(i);
                	
                	if(VoltageLevel_Sub_1.getNodeType() == Node.ELEMENT_NODE)
                	{	Element VoltageLevel_Subrdf=(Element)VoltageLevel_Sub_1;
                       	String VoltageLevel_Subs_RDF_ID = VoltageLevel_Subrdf.getAttribute("rdf:resource");
                       	NodeList VoltageLevel_base= VoltageL_1.getElementsByTagName("cim:VoltageLevel.BaseVoltage");
                    	for(int h=0; h<VoltageLevel_base.getLength() ; h++)
                    	{ Node VoltageLevel_base_1=VoltageLevel_base.item(i);
                    	
                    	if(VoltageLevel_base_1.getNodeType() == Node.ELEMENT_NODE)
                    	{	Element VoltageLevel_baserdf=(Element)VoltageLevel_base_1;
                           	String VoltageLevel_base_RDF_ID = VoltageLevel_baserdf.getAttribute("rdf:resource");  	
                    System.out.println("Reference ID" + VoltageLevel_RDF_ID);
                    System.out.println("Name : " + VoltageLevel_Name);
                    System.out.println("Substation RDF : " + VoltageLevel_Subs_RDF_ID);
                    System.out.println("Base Voltage RDF : " + VoltageLevel_base_RDF_ID);
                }
            }
                                
            }
        
		}
	}
	}
            }
            }
		
		
		NodeList GeneratingUnit = EQ.getElementsByTagName("cim:GeneratingUnit");
        // Element Base = (Element)Basevoltage.item(0);
         System.out.println("GeneratingUnit");
         for(int s=0; s<GeneratingUnit.getLength() ; s++)
         {

             Node GeneratingUnit_1 = GeneratingUnit.item(s);
             
             if(GeneratingUnit_1.getNodeType() == Node.ELEMENT_NODE)
             {	Element GeneratingUnit_2=(Element)GeneratingUnit_1;
             	String GeneratingUnit_rdf = GeneratingUnit_2.getAttribute("rdf:ID");
             	NodeList GeneratingUnit_n= GeneratingUnit_2.getElementsByTagName("cim:IdentifiedObject.name");
             	for(int i=0; i<GeneratingUnit_n.getLength() ; i++)
             	{ Node GeneratingUnit_n_1=GeneratingUnit_n.item(i);
             	
             	if(GeneratingUnit_n_1.getNodeType() == Node.ELEMENT_NODE)
             	{ 	Element GeneratingUnit_n_2=(Element)GeneratingUnit_n_1;
             	    String GeneratingUnit_Name = GeneratingUnit_n_2.getTextContent();
             	   NodeList GeneratingUnit_max= GeneratingUnit_2.getElementsByTagName("cim:GeneratingUnit.maxOperatingP");
                	for(int is=0; is<GeneratingUnit_max.getLength() ; is++)
                	{ Node GeneratingUnit_max_1=GeneratingUnit_max.item(is);
                	
                	if(GeneratingUnit_max_1.getNodeType() == Node.ELEMENT_NODE)
                	{ 	Element GeneratingUnit_max_2=(Element)GeneratingUnit_max_1;
                	    String GeneratingUnit_Max = GeneratingUnit_max_2.getTextContent();
                	    NodeList GeneratingUnit_min= GeneratingUnit_2.getElementsByTagName("cim:GeneratingUnit.minOperatingP");
                    	for(int ii=0; ii<GeneratingUnit_min.getLength() ; ii++)
                    	{ Node GeneratingUnit_min_1=GeneratingUnit_min.item(ii);
                    	
                    	if(GeneratingUnit_min_1.getNodeType() == Node.ELEMENT_NODE)
                    	{ 	Element GeneratingUnit_min_2=(Element)GeneratingUnit_min_1;
                    	    String GeneratingUnit_Min = GeneratingUnit_min_2.getTextContent();
                    	    NodeList GeneratingUnit_eqc= GeneratingUnit_2.getElementsByTagName("cim:Equipment.EquipmentContainer");
                    	    for(int h=0; h<GeneratingUnit_eqc.getLength() ; h++)
                        	{ Node GeneratingUnit_eqc_1=GeneratingUnit_eqc.item(h);
                        	
                        	if(GeneratingUnit_eqc_1.getNodeType() == Node.ELEMENT_NODE)
                        	{	Element GeneratingUnit_eqc_2=(Element)GeneratingUnit_eqc_1;
                               	String GeneratingUnit_eqcont= GeneratingUnit_eqc_2.getAttribute("rdf:resource");  
                     System.out.println("Reference ID" + GeneratingUnit_rdf);
                     System.out.println("Name : " + GeneratingUnit_Name);
                     System.out.println("GeneratingUnitMax :" + GeneratingUnit_Max);
                     System.out.println("GeneratingUnitMin :" + GeneratingUnit_Min);
                     System.out.println("GeneratingUnitEquipmentContainer" + GeneratingUnit_eqcont);
                    
                 }
             }
                                 
             }
         
			}
                	}
                	}
             	}
             	}
             }
         }
		
         NodeList SynchronousMachine = EQ.getElementsByTagName("cim:SynchronousMachine");
         // Element Base = (Element)Basevoltage.item(0);
          System.out.println("Synchronous Machine");
          for(int s=0; s<SynchronousMachine.getLength() ; s++)
          {

              Node SynchronousMachine_1 = SynchronousMachine.item(s);
              
              if(SynchronousMachine_1.getNodeType() == Node.ELEMENT_NODE)
              {	Element SynchronousMachine_2=(Element)SynchronousMachine_1;
              	String SynchronousMachine_rdf = SynchronousMachine_2.getAttribute("rdf:ID");
              	NodeList SynchronousMachine_n= SynchronousMachine_2.getElementsByTagName("cim:IdentifiedObject.name");
              	for(int i=0; i<SynchronousMachine_n.getLength() ; i++)
              	{ Node SynchronousMachine_n_1=SynchronousMachine_n.item(i);
              	
              	if(SynchronousMachine_n_1.getNodeType() == Node.ELEMENT_NODE)
              	{ 	Element SynchronousMachine_n_2=(Element)SynchronousMachine_n_1;
              	    String SynchronousMachine_Name = SynchronousMachine_n_2.getTextContent();
              	   NodeList SynchronousMachine_rated= SynchronousMachine_2.getElementsByTagName("cim:RotatingMachine.ratedS");
                 	for(int is=0; is<SynchronousMachine_rated.getLength() ; is++)
                 	{ Node SynchronousMachine_rated_1=SynchronousMachine_rated.item(is);
                 	
                 	if(SynchronousMachine_rated_1.getNodeType() == Node.ELEMENT_NODE)
                 	{ 	Element SynchronousMachine_rated_2=(Element)SynchronousMachine_rated_1;
                 	    String SynchronousMachine_ratedS = SynchronousMachine_rated_2.getTextContent();
                 	    NodeList SynchronousMachine_P= SynchronousMachine_2.getElementsByTagName("cim:RotatingMachine.ratedPowerFactor");
                     	for(int ii=0; ii<SynchronousMachine_P.getLength() ; ii++)
                     	{ Node SynchronousMachine_P_1=SynchronousMachine_P.item(ii);
                     	
                     	if(SynchronousMachine_P_1.getNodeType() == Node.ELEMENT_NODE)
                     	{ 	Element SynchronousMachine_P_2=(Element)SynchronousMachine_P_1;
                     	    String SynchronousMachine_PowerFactor = SynchronousMachine_P_2.getTextContent();
                     	   double SynchronousMachine_Power=Double.parseDouble(SynchronousMachine_ratedS) *Double.parseDouble(SynchronousMachine_PowerFactor);
                     	  double SynchronousMachine_Q=Double.parseDouble(SynchronousMachine_ratedS) *Math.sin(Math.acos(Double.parseDouble(SynchronousMachine_PowerFactor)));
                     	 NodeList SynchronousMachine_gen= SynchronousMachine_2.getElementsByTagName("cim:RotatingMachine.GeneratingUnit");
                 	    for(int h=0; h<SynchronousMachine_gen.getLength() ; h++)
                     	{ Node SynchronousMachine_gen_1=SynchronousMachine_gen.item(h);
                     	
                     	if(SynchronousMachine_gen_1.getNodeType() == Node.ELEMENT_NODE)
                     	{	Element SynchronousMachine_gen_2=(Element)SynchronousMachine_gen_1;
                            	String SynchronousMachine_genc= SynchronousMachine_gen_2.getAttribute("rdf:resource");  
                            	 NodeList SynchronousMachine_reg= SynchronousMachine_2.getElementsByTagName("cim:RegulatingCondEq.RegulatingControl");
                          	    for(int hh=0; hh<SynchronousMachine_reg.getLength() ; hh++)
                              	{ Node SynchronousMachine_reg_1=SynchronousMachine_gen.item(hh);
                              	
                              	if(SynchronousMachine_reg_1.getNodeType() == Node.ELEMENT_NODE)
                              	{	Element SynchronousMachine_reg_2=(Element)SynchronousMachine_reg_1;
                                     	String SynchronousMachine_regc= SynchronousMachine_reg_2.getAttribute("rdf:resource");  
                                     	NodeList SynchronousMachine_eqcc= SynchronousMachine_2.getElementsByTagName("cim:Equipment.EquipmentContainer");
                                  	    for(int hhh=0; hhh<SynchronousMachine_eqcc.getLength() ; hhh++)
                                      	{ Node SynchronousMachine_eqcc_1=SynchronousMachine_eqcc.item(hhh);
                                      	
                                      	if(SynchronousMachine_eqcc_1.getNodeType() == Node.ELEMENT_NODE)
                                      	{	Element SynchronousMachine_eqcc_2=(Element)SynchronousMachine_reg_1;
                                             	String SynchronousMachine_eqc= SynchronousMachine_eqcc_2.getAttribute("rdf:resource"); 
                                             	NodeList SynchronousMachine_base= SynchronousMachine_2.getElementsByTagName("cim:SynchronousMachine.type");
                                          	    for(int hhs=0; hhs<SynchronousMachine_base.getLength() ; hhs++)
                                              	{ Node SynchronousMachine_base_1=SynchronousMachine_base.item(hhs);
                                              	
                                              	if(SynchronousMachine_base_1.getNodeType() == Node.ELEMENT_NODE)
                                              	{	Element SynchronousMachine_base_2=(Element)SynchronousMachine_reg_1;
                                                     	String SynchronousMachine_baserdf= SynchronousMachine_eqcc_2.getAttribute("rdf:resource");  
                                                     	System.out.println("Reference ID" + SynchronousMachine_rdf);
                                                        System.out.println("Name : " + SynchronousMachine_Name );
                                                        System.out.println("Rated S :" + SynchronousMachine_ratedS);
                                                        System.out.println("P :" + SynchronousMachine_Power);
                                                        System.out.println("Q :" + SynchronousMachine_Q);
                                                        System.out.println("GeneratingUnit_rdf :" + SynchronousMachine_genc);
                                                        System.out.println("RegulatingControl_rdf :" + SynchronousMachine_regc);
                                                        System.out.println("EquipmentContainer_rdf :" + SynchronousMachine_eqc);
                                                        System.out.println("BaseVoltage_rdf :" + SynchronousMachine_baserdf);
                                              	}
                                              	}
                                      	}
                                      	}
                              	}
                              	}
                     	}
                     	}
                     	}
                     	}
                 	}
                 	}
              	}
              	}
              }
          }
		
          NodeList RegulatingControl = EQ.getElementsByTagName("cim:RegulatingControl");
          // Element Base = (Element)Basevoltage.item(0);
           System.out.println("Regulating Control");
           for(int s=0; s<RegulatingControl.getLength() ; s++)
           {

               Node RegulatingControl_1 = RegulatingControl.item(s);
               
               if(RegulatingControl_1.getNodeType() == Node.ELEMENT_NODE)
               {	Element RegulatingControl_2=(Element)RegulatingControl_1;
               	String REGC_RDF_ID = RegulatingControl_2.getAttribute("rdf:ID");
               	NodeList RegulatingControl_name= RegulatingControl_2.getElementsByTagName("cim:IdentifiedObject.name");
               	for(int i=0; i<RegulatingControl_name.getLength() ; i++)
               	{ Node RegulatingControl_name_1=RegulatingControl_name.item(i);
               	
               	if(RegulatingControl_name_1.getNodeType() == Node.ELEMENT_NODE)
               	{ 	Element RegulatingControl_name_2=(Element)RegulatingControl_name_1;
               	    String RegulatingControl_name_3 = RegulatingControl_name_2.getTextContent();
               	 
                       System.out.println("Reference ID" + REGC_RDF_ID );
                      // System.out.println("Reference ID" + BV_RDF_ID);
                       System.out.println("Name : " + RegulatingControl_name_3);
                   }
               }
                                   
               }
           
			}  
           //getConnection();
		}
	
	catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
	}
		 /*public static Connection getConnection() throws Exception{
			  try{
			   String driver = "com.mysql.jdbc.Driver";
			   String url = "";
			   String username = "";
			   String password = "";
			   Class.forName(driver);
			   
			   Connection conn = DriverManager.getConnection(url,username,password);
			   System.out.println("");
			   return conn;
			  } catch(Exception e){System.out.println(e);}
			  
			  
			  return null;
		 }*/
			 
		


}