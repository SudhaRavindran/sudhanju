package triall;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
//import java.Complex;


public class xml
{	

	public static void main(String[] args) throws Exception
	{
		try {
			double Sbase = 1000,base_impedance, R_perunit, X_perunit, B_perunit, G_perunit;
			

            DocumentBuilderFactory EQ_1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder EQ_2 = EQ_1.newDocumentBuilder();
            Document EQ = EQ_2.parse("Assignment_EQ_reduced.xml");
            EQ.getDocumentElement ().normalize();
            
            DocumentBuilderFactory SSH_1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder SSH_2 = SSH_1.newDocumentBuilder();
            Document SSH = SSH_2.parse("Assignment_SSH_reduced.xml");
            SSH.getDocumentElement ().normalize();
            Connection conn=getConnection();
            NodeList Busbar = EQ.getElementsByTagName("cim:BusbarSection");
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS busbar");
		    stmt.executeUpdate("CREATE TABLE busbar (RDFID varchar(255),Name varchar(255))");
            for(int s=0; s<Busbar.getLength() ; s++)
            {

                Node Bus = Busbar.item(s);
                
                if(Bus.getNodeType() == Node.ELEMENT_NODE)
                {	Element Bus1=(Element)Bus;
                	String Bus_RDF_ID = Bus1.getAttribute("rdf:ID");
                	
                	NodeList Busn= Bus1.getElementsByTagName("cim:IdentifiedObject.name");
                	
                	for(int i=0; i<Busn.getLength() ; i++)
                	{ Node Busn2=Busn.item(i);
                	
                	if(Busn2.getNodeType() == Node.ELEMENT_NODE)
                	{ 	Element Busn3=(Element)Busn2;
                	  String Busname = Busn3.getTextContent();
                	  stmt.executeUpdate( "INSERT INTO busbar (RDFID, Name) VALUES ('"+Bus_RDF_ID+"','"+Busname+"');");
                        
                    }
                }
                                    
                }
                
               
            }
            
      		Complex Complete_Y_bus[][] = new Complex[Busbar.getLength()][Busbar.getLength()];
            
            NodeList Basevoltage = EQ.getElementsByTagName("cim:BaseVoltage");
           
            System.out.println("Base Voltage");

            
            stmt.executeUpdate("DROP TABLE IF EXISTS basevoltage");
		    stmt.executeUpdate("CREATE TABLE basevoltage (RDFID varchar(255),NominalValue varchar(255))");
            for(int s=0; s<Basevoltage.getLength() ; s++)
            {

                Node Base_vol = Basevoltage.item(s);
                
                if(Base_vol.getNodeType() == Node.ELEMENT_NODE)
                {	Element Base=(Element)Base_vol;
                	String BV_RDF_ID = Base.getAttribute("rdf:ID");
                	NodeList Nominal= Base.getElementsByTagName("cim:BaseVoltage.nominalVoltage");
                	Double [] basevolt= new Double[Nominal.getLength()];
                	for(int i=0; i<Nominal.getLength() ; i++)
                	{ Node ind_vol=Nominal.item(i);
                	
                	if(ind_vol.getNodeType() == Node.ELEMENT_NODE)
                	{ 	Element vol=(Element)ind_vol;
                	  String NominalVoltage = vol.getTextContent();
                	  basevolt[i]=Double.parseDouble(NominalVoltage);
                        System.out.println("Reference ID" + BV_RDF_ID);
                        System.out.println("Nominal Voltage :" + basevolt[i]);
                        stmt.executeUpdate( "INSERT INTO basevoltage (RDFID, NominalValue) VALUES ('"+BV_RDF_ID+"','"+NominalVoltage+"');");
                        
                    }
                }
                                    
                }
            
			}
            
            
            System.out.println("SubStation");
           
            stmt.executeUpdate("DROP TABLE IF EXISTS substation");
		    stmt.executeUpdate("CREATE TABLE substation (RDFID varchar(255),Name varchar(255),Region_RDF_ID varchar(255))");
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
                        stmt.executeUpdate( "INSERT INTO substation (RDFID, Name,Region_RDF_ID) VALUES ('"+Sub_RDF_ID+"','"+SubstationName+"','"+Sub_Region_RDF_ID+"');");
                        
                    }
                }
                                    
                }
            
			}
		}
		}
		
		System.out.println("Voltage Level");
		stmt.executeUpdate("DROP TABLE IF EXISTS voltagelevel");
	    stmt.executeUpdate("CREATE TABLE voltagelevel (RDFID varchar(255),Name varchar(255),Substation_RDF_ID varchar(255),Basevoltage_RDF_ID varchar(255))");
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
                    stmt.executeUpdate( "INSERT INTO voltagelevel (RDFID, Name,Substation_RDF_ID,BaseVoltage_RDF_ID) VALUES ('"+VoltageLevel_RDF_ID+"','"+VoltageLevel_Name+"','"+VoltageLevel_Subs_RDF_ID+"','"+VoltageLevel_base_RDF_ID+"');");
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
         stmt.executeUpdate("DROP TABLE IF EXISTS generatingunit");
 	    stmt.executeUpdate("CREATE TABLE generatingunit (RDFID varchar(255),Name varchar(255),maxP varchar(255),minP varchar(255),EquipmentContainer_RDF_ID varchar(255))");
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
                    	for(int d=0; d<GeneratingUnit_min.getLength() ; d++)
                    	{ Node GeneratingUnit_min_1=GeneratingUnit_min.item(d);
                    	
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
                     stmt.executeUpdate( "INSERT INTO generatingunit (RDFID, Name,maxP,minP,EquipmentContainer_RDF_ID) VALUES ('"+GeneratingUnit_rdf+"','"+GeneratingUnit_Name+"','"+GeneratingUnit_Max+"','"+GeneratingUnit_Min+"','"+GeneratingUnit_eqcont+"');");
                    
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
         String Basevoltid=null;
		
         NodeList SynchronousMachine = EQ.getElementsByTagName("cim:SynchronousMachine");
         NodeList SynchronousMachineP = SSH.getElementsByTagName("cim:SynchronousMachine");
          System.out.println("Synchronous Machine");
          stmt.executeUpdate("DROP TABLE IF EXISTS synchronousmachine");
   	    stmt.executeUpdate("CREATE TABLE synchronousmachine (RDFID varchar(255),Name varchar(255),RatedS varchar(255),P varchar(255),Q varchar(255),GenUnit_RDF_ID varchar(255),RegControl_RDF_ID varchar(255),EquipmentContainer_RDF_ID varchar(255),Basevoltage_RDF_ID varchar(255))");
          for(int s=0; s<SynchronousMachine.getLength() ; s++)
          {

              Node SynchronousMachine_1 = SynchronousMachine.item(s);
              Node SynchronousMachineP_1 = SynchronousMachineP.item(s); 
              if(SynchronousMachine_1.getNodeType() == Node.ELEMENT_NODE)
              {	Element SynchronousMachine_2=(Element)SynchronousMachine_1;
                Element SynchronousMachineP_2=(Element)SynchronousMachineP_1;
              
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
                 	   NodeList SynchronousMachineP_rated= SynchronousMachineP_2.getElementsByTagName("cim:RotatingMachine.p");
                    	for(int iss=0; iss<SynchronousMachineP_rated.getLength() ; iss++)
                    	{ Node SynchronousMachineP_rated_1=SynchronousMachineP_rated.item(iss);
                    	
                    	if(SynchronousMachineP_rated_1.getNodeType() == Node.ELEMENT_NODE)
                    	{ 	Element SynchronousMachineP_rated_2=(Element)SynchronousMachineP_rated_1;
                    	    String SynchronousMachineP_ratedS = SynchronousMachineP_rated_2.getTextContent();
                 	    NodeList SynchronousMachine_P= SynchronousMachineP_2.getElementsByTagName("cim:RotatingMachine.q");
                     	for(int d=0; d<SynchronousMachine_P.getLength() ; d++)
                     	{ Node SynchronousMachine_P_1=SynchronousMachine_P.item(d);
                     	
                     	if(SynchronousMachine_P_1.getNodeType() == Node.ELEMENT_NODE)
                     	{ 	Element SynchronousMachine_P_2=(Element)SynchronousMachine_P_1;
                     	    String SynchronousMachine_PowerFactor = SynchronousMachine_P_2.getTextContent();
                     	   //double SynchronousMachine_Power=Double.parseDouble(SynchronousMachine_ratedS) *Double.parseDouble(SynchronousMachine_PowerFactor);
                     	  //double SynchronousMachine_Q=Double.parseDouble(SynchronousMachine_ratedS) *Math.sin(Math.acos(Double.parseDouble(SynchronousMachine_PowerFactor)));
                     	 NodeList SynchronousMachine_gen= SynchronousMachine_2.getElementsByTagName("cim:RotatingMachine.GeneratingUnit");
                 	    for(int h=0; h<SynchronousMachine_gen.getLength() ; h++)
                     	{ Node SynchronousMachine_gen_1=SynchronousMachine_gen.item(h);
                     	
                     	if(SynchronousMachine_gen_1.getNodeType() == Node.ELEMENT_NODE)
                     	{	Element SynchronousMachine_gen_2=(Element)SynchronousMachine_gen_1;
                            	String SynchronousMachine_genc= SynchronousMachine_gen_2.getAttribute("rdf:resource");  
                            	 NodeList SynchronousMachine_reg= SynchronousMachine_2.getElementsByTagName("cim:RegulatingCondEq.RegulatingControl");
                          	    for(int hh=0; hh<SynchronousMachine_reg.getLength() ; hh++)
                              	{ Node SynchronousMachine_reg_1=SynchronousMachine_reg.item(hh);
                              	
                              	if(SynchronousMachine_reg_1.getNodeType() == Node.ELEMENT_NODE)
                              	{	Element SynchronousMachine_reg_2=(Element)SynchronousMachine_reg_1;
                                     	String SynchronousMachine_regc= SynchronousMachine_reg_2.getAttribute("rdf:resource");  
                                     	NodeList SynchronousMachine_eqcc= SynchronousMachine_2.getElementsByTagName("cim:Equipment.EquipmentContainer");
                                  	    for(int hhh=0; hhh<SynchronousMachine_eqcc.getLength() ; hhh++)
                                      	{ Node SynchronousMachine_eqcc_1=SynchronousMachine_eqcc.item(hhh);
                                      	
                                      	if(SynchronousMachine_eqcc_1.getNodeType() == Node.ELEMENT_NODE)
                                      	{	Element SynchronousMachine_eqcc_2=(Element)SynchronousMachine_eqcc_1;
                                             	String SynchronousMachine_eqc= SynchronousMachine_eqcc_2.getAttribute("rdf:resource").replaceAll("#",""); 
                                             	NodeList SynchronousMachine_base= SynchronousMachine_2.getElementsByTagName("cim:SynchronousMachine.type");
                                          	    for(int hhs=0; hhs<SynchronousMachine_base.getLength() ; hhs++)
                                              	{ Node SynchronousMachine_base_1=SynchronousMachine_base.item(hhs);
                                              	
                                              	if(SynchronousMachine_base_1.getNodeType() == Node.ELEMENT_NODE)
                                              		
                                              	{	Element SynchronousMachine_base_2=(Element)SynchronousMachine_reg_1;
                                                     	String SynchronousMachine_baserdf= SynchronousMachine_base_2.getAttribute("rdf:resource");  
                                                     	System.out.println("Reference ID" + SynchronousMachine_rdf);
                                                        System.out.println("Name : " + SynchronousMachine_Name );
                                                        System.out.println("Rated S :" + SynchronousMachine_ratedS);
                                                        System.out.println("P :" + SynchronousMachineP_ratedS);
                                                        System.out.println("Q :" + SynchronousMachine_PowerFactor);
                                                        System.out.println("GeneratingUnit_rdf :" + SynchronousMachine_genc);
                                                        System.out.println("RegulatingControl_rdf :" + SynchronousMachine_regc);
                                                        System.out.println("EquipmentContainer_rdf :" + SynchronousMachine_eqc);
                                                        System.out.println("BaseVoltage_rdf :" + SynchronousMachine_baserdf);
                                                        for(int j=0; j<VoltageLevel.getLength(); j++)
                                                        {
                                                    		Element voltq=(Element) VoltageLevel.item(j);
                                                    		String rdf_IDs = voltq.getAttribute("rdf:ID");
                                                    		if(rdf_IDs.equals(SynchronousMachine_eqc) ) 
                                                    		{
                                                    			Node basevolt = voltq.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0);
                                                        		Element basevolt_element = (Element) basevolt;
                                                        		Basevoltid = basevolt_element.getAttribute("rdf:resource").replaceAll("#","");
                                                    		}		
                                                    	}
                                                        
                                                        
                                                       stmt.executeUpdate( "INSERT INTO synchronousmachine (RDFID,Name,RatedS,P,Q,GenUnit_RDF_ID,RegControl_RDF_ID,EquipmentContainer_RDF_ID,BaseVoltage_RDF_ID) VALUES ('"+SynchronousMachine_rdf+"','"+SynchronousMachine_Name+"','"+SynchronousMachine_ratedS+"','"+SynchronousMachineP_ratedS+"','"+SynchronousMachine_PowerFactor+"','"+SynchronousMachine_genc+"','"+SynchronousMachine_regc+"','"+SynchronousMachine_eqc+"','"+Basevoltid+"');");
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
              }
          }
		
          NodeList RegulatingControl = EQ.getElementsByTagName("cim:RegulatingControl");
          NodeList RegulatingControlP = SSH.getElementsByTagName("cim:RegulatingControl");
          // Element Base = (Element)Basevoltage.item(0);
           System.out.println("Regulating Control");
           stmt.executeUpdate("DROP TABLE IF EXISTS regulatingcontrol");
		    stmt.executeUpdate("CREATE TABLE regulatingcontrol (RDFID varchar(255),Name varchar(255),targetvalue varchar(255))");
           for(int s=0; s<RegulatingControl.getLength() ; s++)
           {

               Node RegulatingControl_1 = RegulatingControl.item(s);
               Node RegulatingControlP_1 = RegulatingControlP.item(s);
               if(RegulatingControl_1.getNodeType() == Node.ELEMENT_NODE)
               {	Element RegulatingControl_2=(Element)RegulatingControl_1;
               		Element RegulatingControlP_2=(Element)RegulatingControlP_1;
               
               	String REGC_RDF_ID = RegulatingControl_2.getAttribute("rdf:ID");
               	NodeList RegulatingControl_name= RegulatingControl_2.getElementsByTagName("cim:IdentifiedObject.name");
               	for(int i=0; i<RegulatingControl_name.getLength() ; i++)
               	{ Node RegulatingControl_name_1=RegulatingControl_name.item(i);
               	
               	if(RegulatingControl_name_1.getNodeType() == Node.ELEMENT_NODE)
               	{ 	Element RegulatingControl_name_2=(Element)RegulatingControl_name_1;
               	    String RegulatingControl_name_3 = RegulatingControl_name_2.getTextContent();
               	 NodeList RegulatingControlP_name= RegulatingControlP_2.getElementsByTagName("cim:RegulatingControl.targetValue");
                	for(int ei=0; ei<RegulatingControlP_name.getLength() ; ei++)
                	{ Node RegulatingControlP_name_1=RegulatingControlP_name.item(ei);
                	
                	if(RegulatingControlP_name_1.getNodeType() == Node.ELEMENT_NODE)
                	{ 	Element RegulatingControlP_name_2=(Element)RegulatingControlP_name_1;
                	    String RegulatingControlP_name_3 = RegulatingControlP_name_2.getTextContent();
               	 
                       System.out.println("Reference ID" + REGC_RDF_ID );
                      // System.out.println("Reference ID" + BV_RDF_ID);
                       System.out.println("Name : " + RegulatingControl_name_3);
                       stmt.executeUpdate( "INSERT INTO regulatingcontrol (RDFID, Name,targetvalue) VALUES ('"+REGC_RDF_ID+"','"+RegulatingControl_name_3+"','"+RegulatingControlP_name_3+"');");
                   }
               }
                                   
               }
           
			}  
               }
           }
           //getConnection();
	System.out.println("Power Transformer");
	stmt.executeUpdate("DROP TABLE IF EXISTS powertransformer");
    stmt.executeUpdate("CREATE TABLE powertransformer (RDFID varchar(255),Name varchar(255),Equipmentcontainer_RDF_ID varchar(255))");
    NodeList PT = EQ.getElementsByTagName("cim:PowerTransformer");
    for(int s=0; s<PT.getLength() ; s++)
    {

        Node PT_1 = PT.item(s);
        
        if(PT_1.getNodeType() == Node.ELEMENT_NODE)
        {	Element PT_2=(Element)PT_1;
        	String PT_RDF_ID = PT_2.getAttribute("rdf:ID");
        	NodeList PT_Name= PT_2.getElementsByTagName("cim:IdentifiedObject.name");
        	for(int i=0; i<PT_Name.getLength() ; i++)
        	{ Node PT_Name_1=PT_Name.item(i);
        	
        	if(PT_Name_1.getNodeType() == Node.ELEMENT_NODE)
        	{ 	Element PTs=(Element)PT_Name_1;
        	    String PTName = PTs.getTextContent();
        	    NodeList PT_Reg= PT_2.getElementsByTagName("cim:Equipment.EquipmentContainer");
            	for(int p=0; p<PT_Name.getLength() ; p++)
            	{ Node PT_Reg_1=PT_Reg.item(i);
            	
            	if(PT_Reg_1.getNodeType() == Node.ELEMENT_NODE)
            	{	Element PTrdf=(Element)PT_Reg_1;
            	String PT_eq_RDF_ID = PTrdf.getAttribute("rdf:resource");
                System.out.println("Reference ID :" + PT_RDF_ID);
                System.out.println("Name : " + PTName);
                System.out.println("Equipment Container RDF_ID : " + PT_eq_RDF_ID);
                stmt.executeUpdate( "INSERT INTO powertransformer (RDFID, Name,Equipmentcontainer_RDF_ID) VALUES ('"+PT_RDF_ID+"','"+PTName+"','"+PT_eq_RDF_ID+"');");
                
            }
        
            	}
        	}
        	}
        }
    }
		
    String Basevoltids=null;
		NodeList EnergyConsumer = EQ.getElementsByTagName("cim:EnergyConsumer");
		NodeList EnergyConsumerP = SSH.getElementsByTagName("cim:EnergyConsumer");
		
        // Element Base = (Element)Basevoltage.item(0);
         System.out.println("Energy Consumer");
         stmt.executeUpdate("DROP TABLE IF EXISTS energyconsumer");
    	    stmt.executeUpdate("CREATE TABLE energyconsumer (RDFID varchar(255),Name varchar(255),P varchar(255),Q varchar(255),EquipmentContainer_RDF_ID varchar(255),Basevoltage_RDF_ID varchar(255))");
         for(int s=0; s<EnergyConsumer.getLength() ; s++)
         {

             Node EnergyConsumer_1 = EnergyConsumer.item(s);
             Node EnergyConsumerP_1 = EnergyConsumerP.item(s);
             
             if(EnergyConsumer_1.getNodeType() == Node.ELEMENT_NODE)
             {	Element EnergyConsumer_2=(Element)EnergyConsumer_1;
                Element EnergyConsumerP_2=(Element)EnergyConsumerP_1;             
             	String EnergyConsumer_rdf = EnergyConsumer_2.getAttribute("rdf:ID");
             	NodeList EnergyConsumer_n= EnergyConsumer_2.getElementsByTagName("cim:IdentifiedObject.name");
             	for(int i=0; i<EnergyConsumer_n.getLength() ; i++)
             	{ Node EnergyConsumer_n_1=EnergyConsumer_n.item(i);
             	
             	if(EnergyConsumer_n_1.getNodeType() == Node.ELEMENT_NODE)
             	{ 	Element EnergyConsumer_n_2=(Element)EnergyConsumer_n_1;
             	    String EnergyConsumer_Name = EnergyConsumer_n_2.getTextContent();
             	   NodeList EnergyConsumerP_max= EnergyConsumerP_2.getElementsByTagName("cim:EnergyConsumer.p");
                	for(int is=0; is<EnergyConsumerP_max.getLength() ; is++)
                	{ Node EnergyConsumerP_max_1=EnergyConsumerP_max.item(is);
                	
                	if(EnergyConsumerP_max_1.getNodeType() == Node.ELEMENT_NODE)
                	{ 	Element EnergyConsumerP_max_2=(Element)EnergyConsumerP_max_1;
                	    String EnergyConsumerP_Max = EnergyConsumerP_max_2.getTextContent();
                	    NodeList EnergyConsumerP_min= EnergyConsumerP_2.getElementsByTagName("cim:EnergyConsumer.q");
                    	for(int d=0; d<EnergyConsumerP_min.getLength() ; d++)
                    	{ Node EnergyConsumerP_min_1=EnergyConsumerP_min.item(d);
                    	
                    	if(EnergyConsumerP_min_1.getNodeType() == Node.ELEMENT_NODE)
                    	{ 	Element EnergyConsumerP_min_2=(Element)EnergyConsumerP_min_1;
                    	    String EnergyConsumerP_Min = EnergyConsumerP_min_2.getTextContent();
                    	    NodeList EnergyConsumer_eqc= EnergyConsumer_2.getElementsByTagName("cim:Equipment.EquipmentContainer");
                    	    for(int h=0; h<EnergyConsumer_eqc.getLength() ; h++)
                        	{ Node EnergyConsumer_eqc_1=EnergyConsumer_eqc.item(h);
                        	
                        	if(EnergyConsumer_eqc_1.getNodeType() == Node.ELEMENT_NODE)
                        	{	Element EnergyConsumer_eqc_2=(Element)EnergyConsumer_eqc_1;
                               	String EnergyConsumer_eqcont= EnergyConsumer_eqc_2.getAttribute("rdf:resource").replaceAll("#","");;  
                     System.out.println("Reference ID" + EnergyConsumer_rdf);
                     System.out.println("Name : " + EnergyConsumer_Name);
                     System.out.println("P :" + EnergyConsumerP_Max);
                     System.out.println("Q :" + EnergyConsumerP_Min);
                     System.out.println("EquipmentContainer RDF ID :" + EnergyConsumer_eqcont);
                     for(int j=0; j<VoltageLevel.getLength(); j++)
                     {
                 		Element voltqs=(Element) VoltageLevel.item(j);
                 		String rdf_IDss = voltqs.getAttribute("rdf:ID");
                 		if(rdf_IDss.equals(EnergyConsumer_eqcont) ) 
                 		{
                 			Node basevolts = voltqs.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0);
                     		Element basevolt_elements = (Element) basevolts;
                     		Basevoltids = basevolt_elements.getAttribute("rdf:resource").replaceAll("#","");
                 		}		
                 	}
                     stmt.executeUpdate( "INSERT INTO energyconsumer (RDFID, Name,P,Q,EquipmentContainer_RDF_ID,BaseVoltage_RDF_ID) VALUES ('"+EnergyConsumer_rdf+"','"+EnergyConsumer_Name+"','"+EnergyConsumerP_Max+"','"+EnergyConsumerP_Min+"','"+EnergyConsumer_eqcont+"','"+Basevoltids +"');");
                    
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
         NodeList PTE = EQ.getElementsByTagName("cim:PowerTransformerEnd");
         // Element Base = (Element)Basevoltage.item(0);
          System.out.println("Power Transformer End");
          stmt.executeUpdate("DROP TABLE IF EXISTS powertransformerend");
  	    stmt.executeUpdate("CREATE TABLE powertransformerend (RDFID varchar(255),Name varchar(255),Transformer_r varchar(255),Transformer_x varchar(255),Transformer_b varchar(255),Transformer_g varchar(255),Transformer_RDF_ID varchar(255),Basevoltage_RDF_ID varchar(255))");
          for(int s=0; s<PTE.getLength() ; s++)
          {

              Node PTE_1 = PTE.item(s);
              
              if(PTE_1.getNodeType() == Node.ELEMENT_NODE)
              {	Element PTE_2=(Element)PTE_1;
              	String PTE_rdf = PTE_2.getAttribute("rdf:ID");
              	NodeList PTE_n= PTE_2.getElementsByTagName("cim:IdentifiedObject.name");
              	for(int i=0; i<PTE_n.getLength() ; i++)
              	{ Node PTE_n_1=PTE_n.item(i);
              	
              	if(PTE_n_1.getNodeType() == Node.ELEMENT_NODE)
              	{ 	Element PTE_n_2=(Element)PTE_n_1;
              	    String PTE_Name = PTE_n_2.getTextContent();
              	   NodeList PTE_rated= PTE_2.getElementsByTagName("cim:PowerTransformerEnd.r");
                 	for(int is=0; is<PTE_rated.getLength() ; is++)
                 	{ Node PTE_rated_1=PTE_rated.item(is);
                 	
                 	if(PTE_rated_1.getNodeType() == Node.ELEMENT_NODE)
                 	{ 	Element PTE_rated_2=(Element)PTE_rated_1;
                 	    String PTE_r = PTE_rated_2.getTextContent();
                 	    NodeList PTE_P= PTE_2.getElementsByTagName("cim:PowerTransformerEnd.x");
                     	for(int d=0; d<PTE_P.getLength() ; d++)
                     	{ Node PTE_P_1=PTE_P.item(d);
                     	
                     	if(PTE_P_1.getNodeType() == Node.ELEMENT_NODE)
                     	{ 	Element PTE_P_2=(Element)PTE_P_1;
                     	    String PTE_x = PTE_P_2.getTextContent();
                     	    NodeList PTE_gen= PTE_2.getElementsByTagName("cim:PowerTransformerEnd.PowerTransformer");
                 	    for(int h=0; h<PTE_gen.getLength() ; h++)
                     	{ Node PTE_gen_1=PTE_gen.item(h);
                     	
                     	if(PTE_gen_1.getNodeType() == Node.ELEMENT_NODE)
                     	{	Element PTE_gen_2=(Element)PTE_gen_1;
                            	String PTE_t= PTE_gen_2.getAttribute("rdf:resource");  
                            	 NodeList PTE_reg= PTE_2.getElementsByTagName("cim:TransformerEnd.BaseVoltage");
                          	    for(int hh=0; hh<PTE_reg.getLength() ; hh++)
                              	{ Node PTE_reg_1=PTE_reg.item(hh);
                              	
                              	if(PTE_reg_1.getNodeType() == Node.ELEMENT_NODE)
                              	{	Element PTE_reg_2=(Element)PTE_reg_1;
                                     	String PTE_b= PTE_reg_2.getAttribute("rdf:resource");  
                                     	NodeList PTE_regb= PTE_2.getElementsByTagName("cim:PowerTransformerEnd.b");
                                  	    for(int hhb=0; hhb<PTE_regb.getLength() ; hhb++)
                                      	{ Node PTE_reg_1b=PTE_regb.item(hhb);
                                      	
                                      	if(PTE_reg_1b.getNodeType() == Node.ELEMENT_NODE)
                                      	{	Element PTE_reg_2b=(Element)PTE_reg_1b;
                                             	String PTE_bb= PTE_reg_2b.getTextContent();  
                                             	NodeList PTE_regg= PTE_2.getElementsByTagName("cim:PowerTransformerEnd.g");
                                          	    for(int hhg=0; hhg<PTE_regg.getLength() ; hhg++)
                                              	{ Node PTE_reg_1g=PTE_regg.item(hhg);
                                              	
                                              	if(PTE_reg_1g.getNodeType() == Node.ELEMENT_NODE)
                                              	{	Element PTE_reg_2g=(Element)PTE_reg_1g;
                                                     	String PTE_g= PTE_reg_2g.getTextContent(); 
                                     	System.out.println("Reference ID" + PTE_rdf);
                                        System.out.println("Name : " + PTE_Name );
                                        System.out.println("Transformer.r :" + PTE_r );
                                        System.out.println("Transformer.x :" + PTE_x);
                                        System.out.println("Transformer_rdf :" + PTE_t);
                                        System.out.println("BaseVoltage_rdf :" + PTE_g);
                                        
                                        stmt.executeUpdate( "INSERT INTO powertransformerend (RDFID,Name,Transformer_r,Transformer_x,Transformer_b,Transformer_g,Transformer_RDF_ID,BaseVoltage_RDF_ID) VALUES ('"+PTE_rdf+"','"+PTE_Name+"','"+PTE_r+"','"+PTE_x+"','"+PTE_bb+"','"+PTE_g+"','"+PTE_t+"','"+PTE_b+"');");
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
          String Basevoltidss=null;
          NodeList Breaker = EQ.getElementsByTagName("cim:Breaker");
          NodeList BreakerP = SSH.getElementsByTagName("cim:Breaker");
          // Element Base = (Element)Basevoltage.item(0);
           System.out.println("Breaker");
           stmt.executeUpdate("DROP TABLE IF EXISTS breaker");
     	    stmt.executeUpdate("CREATE TABLE breaker (RDFID varchar(255),Name varchar(255),state varchar(255),EquipmentContainer_RDF_ID varchar(255),Basevoltage_RDF_ID varchar(255))");
           for(int s=0; s<Breaker.getLength() ; s++)
           {

               Node Breaker_1 = Breaker.item(s);
               
               if(Breaker_1.getNodeType() == Node.ELEMENT_NODE)
               {	Element Breaker_2=(Element)Breaker_1;
               	String Breaker_rdf = Breaker_2.getAttribute("rdf:ID");
               	NodeList Breaker_n= Breaker_2.getElementsByTagName("cim:IdentifiedObject.name");
               	for(int i=0; i<Breaker_n.getLength() ; i++)
               	{ Node Breaker_n_1=Breaker_n.item(i);
               	
               	if(Breaker_n_1.getNodeType() == Node.ELEMENT_NODE)
               	{ 	Element Breaker_n_2=(Element)Breaker_n_1;
               	    String Breaker_Name =Breaker_n_2.getTextContent();
               	   NodeList Breaker_rated= Breaker_2.getElementsByTagName("cim:Switch.normalOpen");
                  	for(int is=0; is<Breaker_rated.getLength() ; is++)
                  	{ Node Breaker_rated_1=Breaker_rated.item(is);
                  	
                  	if(Breaker_rated_1.getNodeType() == Node.ELEMENT_NODE)
                  	{ 	Element Breaker_rated_2=(Element)Breaker_rated_1;
                  	    String Breaker_r = Breaker_rated_2.getTextContent();
                  	    NodeList Breaker_gen= Breaker_2.getElementsByTagName("cim:Equipment.EquipmentContainer");
                  	    for(int h=0; h<Breaker_gen.getLength() ; h++)
                      	{ Node Breaker_gen_1=Breaker_gen.item(h);
                      	
                      	if(Breaker_gen_1.getNodeType() == Node.ELEMENT_NODE)
                      	{	Element Breaker_gen_2=(Element)Breaker_gen_1;
                             	String Breaker_t= Breaker_gen_2.getAttribute("rdf:resource").replaceAll("#","");
                             	 
                               	{	  
                                      	System.out.println("Reference ID" + Breaker_rdf);
                                         System.out.println("Name : " + Breaker_Name );
                                         System.out.println("State :" + Breaker_r );
                                         System.out.println("Equipment Container_rdf :" + Breaker_t);
                                       //  System.out.println("BaseVoltage_rdf :" + PTE_b);
                                         for(int j=0; j<VoltageLevel.getLength(); j++)
                                         {
                                     		Element voltqss=(Element) VoltageLevel.item(j);
                                     		String rdf_IDsss = voltqss.getAttribute("rdf:ID");
                                     		if(rdf_IDsss.equals(Breaker_t) ) 
                                     		{
                                     			Node basevoltss = voltqss.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0);
                                         		Element basevolt_elementss = (Element) basevoltss;
                                         		Basevoltidss = basevolt_elementss.getAttribute("rdf:resource").replaceAll("#","");
                                     		}		
                                     	}
                                         stmt.executeUpdate( "INSERT INTO breaker (RDFID, Name,state,Equipmentcontainer_RDF_ID,BaseVoltage_RDF_ID) VALUES ('"+Breaker_rdf+"','"+Breaker_Name+"','"+Breaker_r+"','"+Breaker_t+"','"+Basevoltidss+"');");
                               	}
                               	}
                      	}
                      	}
                      	}
                      	}
                  	}
                  	}
               	}
               	
               
           NodeList RatioTapChanger = EQ.getElementsByTagName("cim:RatioTapChanger");
   		NodeList RatioTapChangerP = SSH.getElementsByTagName("cim:RatioTapChanger");
   		Double step=(double) 0;
           // Element Base = (Element)Basevoltage.item(0);
            System.out.println("Ratio Tap Changer");
            stmt.executeUpdate("DROP TABLE IF EXISTS ratiotapchanger");
     	    stmt.executeUpdate("CREATE TABLE ratiotapchanger (RDFID varchar(255),Name varchar(255),step varchar(255))");
            for(int s=0; s<RatioTapChanger.getLength(); s++)
            {

                Node RatioTapChanger_1 = RatioTapChanger.item(s);
                Node RatioTapChangerP_1 = RatioTapChangerP.item(s);
                
                if(RatioTapChanger_1.getNodeType() == Node.ELEMENT_NODE)
                {	Element RatioTapChanger_2=(Element)RatioTapChanger_1;
                   Element RatioTapChangerP_2=(Element)RatioTapChangerP_1;             
                	String RatioTapChanger_rdf = RatioTapChanger_2.getAttribute("rdf:ID");
                	NodeList RatioTapChanger_n= RatioTapChanger_2.getElementsByTagName("cim:IdentifiedObject.name");
                	for(int i=0; i<RatioTapChanger_n.getLength() ; i++)
                	{ Node RatioTapChanger_n_1=RatioTapChanger_n.item(i);
                	
                	if(RatioTapChanger_n_1.getNodeType() == Node.ELEMENT_NODE)
                	{ 	Element RatioTapChanger_n_2=(Element)RatioTapChanger_n_1;
                	    String RatioTapChanger_Name = RatioTapChanger_n_2.getTextContent();
                	    
                        System.out.println("Reference ID" + RatioTapChanger_rdf);
                        System.out.println("Name : " + RatioTapChanger_Name);
                        //System.out.println("Step :" + RatioTapChangerP_step);
                        
                        for(int j=0; j<RatioTapChangerP.getLength(); j++) {
                    		Element ratio=(Element) RatioTapChangerP.item(j);
                    		String rdf_ID = ratio.getAttribute("rdf:about").replaceAll("#", "");
                    		if(rdf_ID.equals(RatioTapChanger_rdf) ) {
                    			step = Double.parseDouble(ratio.getElementsByTagName("cim:TapChanger.step").item(0).getTextContent());
                    		}		
                    	}
                        System.out.println("Step :" + step);
                        stmt.executeUpdate( "INSERT INTO ratiotapchanger (RDFID, Name,step) VALUES ('"+RatioTapChanger_rdf+"','"+RatioTapChanger_Name+"','"+step+"');");
                                               
                    }
                }
                                    
                            
   			
            
                   
                }
            }
 		
		
		
		         
		NodeList ac = EQ.getElementsByTagName("cim:ACLineSegment");
        
         //System.out.println("Power Transformer End");
         stmt.executeUpdate("DROP TABLE IF EXISTS acsegment");
 	    stmt.executeUpdate("CREATE TABLE acsegment (RDFID varchar(255),Name varchar(255),R varchar(255),X varchar(255),B varchar(255),G varchar(255),CoeqBase varchar(255))");
         for(int s=0; s<ac.getLength() ; s++)
         {

             Node ac_1 = ac.item(s);
             
             if(ac_1.getNodeType() == Node.ELEMENT_NODE)
             {	Element ac_2=(Element)ac_1;
             	String ac_rdf = ac_2.getAttribute("rdf:ID");
             	NodeList ac_n= ac_2.getElementsByTagName("cim:IdentifiedObject.name");
             	for(int i=0; i<ac_n.getLength() ; i++)
             	{ Node ac_n_1=ac_n.item(i);
             	
             	if(ac_n_1.getNodeType() == Node.ELEMENT_NODE)
             	{ 	Element ac_n_2=(Element)ac_n_1;
             	    String ac_Name = ac_n_2.getTextContent();
             	   NodeList ac_rated= ac_2.getElementsByTagName("cim:ACLineSegment.r");
                	for(int is=0; is<ac_rated.getLength() ; is++)
                	{ Node ac_rated_1=ac_rated.item(is);
                	
                	if(ac_rated_1.getNodeType() == Node.ELEMENT_NODE)
                	{ 	Element ac_rated_2=(Element)ac_rated_1;
                	    String ac_r = ac_rated_2.getTextContent();
                	    NodeList ac_P= ac_2.getElementsByTagName("cim:ACLineSegment.x");
                    	for(int d=0; d<ac_P.getLength() ; d++)
                    	{ Node ac_P_1=ac_P.item(d);
                    	
                    	if(ac_P_1.getNodeType() == Node.ELEMENT_NODE)
                    	{ 	Element ac_P_2=(Element)ac_P_1;
                    	    String ac_x = ac_P_2.getTextContent();
                    	              	NodeList ac_regb= ac_2.getElementsByTagName("cim:ACLineSegment.bch");
                                 	    for(int hhb=0; hhb<ac_regb.getLength() ; hhb++)
                                     	{ Node ac_reg_1b=ac_regb.item(hhb);
                                     	
                                     	if(ac_reg_1b.getNodeType() == Node.ELEMENT_NODE)
                                     	{	Element ac_reg_2b=(Element)ac_reg_1b;
                                            	String ac_bb= ac_reg_2b.getTextContent();  
                                            	NodeList ac_regg= ac_2.getElementsByTagName("cim:ACLineSegment.gch");
                                         	    for(int hhg=0; hhg<ac_regg.getLength() ; hhg++)
                                             	{ Node ac_reg_1g=ac_regg.item(hhg);
                                             	
                                             	if(ac_reg_1g.getNodeType() == Node.ELEMENT_NODE)
                                             	{	Element ac_reg_2g=(Element)ac_reg_1g;
                                                    	String ac_g= ac_reg_2g.getTextContent(); 
                                                    	NodeList ac_reggb= ac_2.getElementsByTagName("cim:ConductingEquipment.BaseVoltage");
                                                 	    for(int hhgb=0; hhgb<ac_regg.getLength() ; hhgb++)
                                                     	{ Node ac_reg_1gb=ac_reggb.item(hhgb);
                                                     	
                                                     	if(ac_reg_1gb.getNodeType() == Node.ELEMENT_NODE)
                                                     	{	Element ac_reg_2gb=(Element)ac_reg_1gb;
                                                            	String ac_gb= ac_reg_2gb.getAttribute("rdf:resource"); 
                                    	//System.out.println("Reference ID" + ac_rdf);
                                       //System.out.println("Name : " + ac_Name );
                                       //System.out.println("R :" + ac_r );
                                       //System.out.println("X :" + ac_x);
                                       //System.out.println("B :" + ac_bb);
                                       //System.out.println("G :" + ac_g);
                                       stmt.executeUpdate( "INSERT INTO acsegment (RDFID,Name,R,X,B,G,CoeqBase)VALUES ('"+ac_rdf+"','"+ac_Name+"','"+ac_r+"','"+ac_x+"','"+ac_bb+"','"+ac_g+"','"+ac_gb+"');");
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
         //System.out.println("Shunt Compensator");
            stmt.executeUpdate("DROP TABLE IF EXISTS shuntcompensator");
		    stmt.executeUpdate("CREATE TABLE shuntcompensator (RDFID varchar(255),Name varchar(255),B varchar(255),G varchar(255))");
         NodeList Sc = EQ.getElementsByTagName("cim:LinearShuntCompensator");
         for(int s=0; s<Sc.getLength() ; s++)
         {

             Node Sc1 = Sc.item(s);
             
             if(Sc1.getNodeType() == Node.ELEMENT_NODE)
             {	Element Sc_1=(Element)Sc1;
             	String Sc_RDF_ID = Sc_1.getAttribute("rdf:ID");
             	NodeList Sc_Name= Sc_1.getElementsByTagName("cim:IdentifiedObject.name");
             	for(int i=0; i<Sc_Name.getLength() ; i++)
             	{ Node Sc_Name_1=Sc_Name.item(i);
             	
             	if(Sc_Name_1.getNodeType() == Node.ELEMENT_NODE)
             	{ 	Element Subs1=(Element)Sc_Name_1;
             	    String Scn = Subs1.getTextContent();
             	    NodeList Sc_Reg= Sc_1.getElementsByTagName("cim:LinearShuntCompensator.bPerSection");
                 	for(int p=0; p<Sc_Reg.getLength() ; p++)
                 	{ Node Sc_Reg_1=Sc_Reg.item(p);
                 	
                 	if(Sc_Reg_1.getNodeType() == Node.ELEMENT_NODE)
                 	{	Element Scrdf2=(Element)Sc_Reg_1;
                 	String Sc_Region_RDF_ID2 = Scrdf2.getTextContent();
                 	NodeList Sc_Reg2= Sc_1.getElementsByTagName("cim:LinearShuntCompensator.gPerSection");
                 	for(int pp=0; pp<Sc_Reg2.getLength() ; pp++)
                 	{ Node Sc_Reg_2=Sc_Reg2.item(p);
                 	
                 	if(Sc_Reg_2.getNodeType() == Node.ELEMENT_NODE)
                 	{	Element Scrdf3=(Element)Sc_Reg_2;
                 	String Sc_Region_RDF_ID3 = Scrdf3.getTextContent();
                     stmt.executeUpdate( "INSERT INTO shuntcompensator (RDFID, Name,B,G) VALUES ('"+Sc_RDF_ID+"','"+Scn+"','"+Sc_Region_RDF_ID2+"','"+Sc_Region_RDF_ID3+"');");
                     
                 }
             }
                                 
             }
         
			}
		}
		}
		
		}
         }
         NodeList Terminal = EQ.getElementsByTagName("cim:Terminal");
 		NodeList TerminalP = SSH.getElementsByTagName("cim:Terminal");
 		
         
          //System.out.println("Terminal");
          stmt.executeUpdate("DROP TABLE IF EXISTS terminal");
     	    stmt.executeUpdate("CREATE TABLE terminal (RDFID varchar(255),Name varchar(255),RDFIDA varchar(255),State varchar(255),CoRDFID varchar(255),CoEqRDFID varchar(255))");
          for(int s=0; s<Terminal.getLength(); s++)
          {

              Node Terminal_1 = Terminal.item(s);
              Node TerminalP_1 = TerminalP.item(s);
              
              if(Terminal_1.getNodeType() == Node.ELEMENT_NODE)
              {	Element Terminal_2=(Element)Terminal_1;
                 Element TerminalP_2=(Element)TerminalP_1;             
              	String Terminal_rdf = Terminal_2.getAttribute("rdf:ID");
              	
              	NodeList Terminal_n= Terminal_2.getElementsByTagName("cim:IdentifiedObject.name");
              	for(int i=0; i<Terminal_n.getLength() ; i++)
              	{ Node Terminal_n_1=Terminal_n.item(i);
              	
              	if(Terminal_n_1.getNodeType() == Node.ELEMENT_NODE)
              	{ 	Element Terminal_n_2=(Element)Terminal_n_1;
              	    String Terminal_Name =Terminal_n_2.getTextContent();
              	    
     
                 	if(TerminalP_1.getNodeType() == Node.ELEMENT_NODE)
                 	{ 	Element TerminalP_max_2=(Element)TerminalP_1;
                 	 
                 	    String TerminalP_rdf = TerminalP_max_2.getAttribute("rdf:about");
                 	   
                 	   NodeList TerminalP_state= TerminalP_2.getElementsByTagName("cim:ACDCTerminal.connected");
                    	for(int iss=0; iss<TerminalP_state.getLength() ; iss++)
                    	{ Node TerminalP_state_1=TerminalP_state.item(iss);
                    	
                    	
                    	if(TerminalP_state_1.getNodeType() == Node.ELEMENT_NODE)
                    	{ 	Element TerminalP_state_2=(Element)TerminalP_state_1;
                    	
                    	    String TerminalP_state2= TerminalP_state_2.getTextContent();
                    	    
                 	    NodeList Terminal_min= Terminal_2.getElementsByTagName("cim:Terminal.ConnectivityNode");
                     	for(int d=0; d<Terminal_min.getLength() ; d++)
                     	{ Node Terminal_min_1=Terminal_min.item(d);
                     	 
                     	
                     	if(Terminal_min_1.getNodeType() == Node.ELEMENT_NODE)
                     	{ 	Element Terminal_min_2=(Element)Terminal_min_1;
                     	    String Terminal_rdf1 = Terminal_min_2.getAttribute("rdf:resource");
                     	    NodeList Terminal_eqc= Terminal_2.getElementsByTagName("cim:Terminal.ConductingEquipment");
                     	    for(int h=0; h<Terminal_eqc.getLength() ; h++)
                         	{ Node Terminal_eqc_1=Terminal_eqc.item(h);
                         	
                         	
                         	if(Terminal_eqc_1.getNodeType() == Node.ELEMENT_NODE)
                         	{	Element Terminal_eqc_2=(Element)Terminal_eqc_1;
                                	String Terminal_rdf2= Terminal_eqc_2.getAttribute("rdf:resource");  
                                
                      
                      stmt.executeUpdate( "INSERT INTO terminal (RDFID, Name,RDFIDA,State,CoRDFID,CoEqRDFID) VALUES ('"+Terminal_rdf+"','"+Terminal_Name+"','"+TerminalP_rdf+"','"+TerminalP_state2+"','"+Terminal_rdf1+"','"+Terminal_rdf2+"');");
                     
                     
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

      
// Complete_Ybus for the reduced version
              String Bus[]= new String[Busbar.getLength()]; 
    		Complex Complete_Ybus[][] = new Complex[Busbar.getLength()][Busbar.getLength()];
  		for (int i = 0; i < Busbar.getLength(); i++){
  			
  			  for (int j = 0; j < Busbar.getLength(); j++){
  			      Complete_Ybus[i][j] = new Complex(0,0);
  			  }
  			  
  			  
  		}  
 for (int i = 0; i < Busbar.getLength(); i++){
  			
              Element element = (Element) Busbar.item(i);
  		    
  			String rdfID = element.getAttribute("rdf:ID");
  			Bus[i] = rdfID;
  			
  		}  
  		
  		
  		
  		double Z_base;
		for (int i = 0; i < ac.getLength(); i++){  
  			
  			List <String> LineArrayList = new ArrayList<String>();
  			
              Element element = (Element) ac.item(i);
  	    
  		    String Line_rdfid = element.getAttribute("rdf:ID");
  		    
  		    LineArrayList.add(Line_rdfid);
  		  String R_1=element.getElementsByTagName("cim:ACLineSegment.r").item(0).getTextContent();
		    String X_1=element.getElementsByTagName("cim:ACLineSegment.x").item(0).getTextContent();
		    String G_1=element.getElementsByTagName("cim:ACLineSegment.gch").item(0).getTextContent();
		    String B_1=element.getElementsByTagName("cim:ACLineSegment.bch").item(0).getTextContent();
  		    
		    Double R = Double.parseDouble(R_1);
  		    Double X = Double.parseDouble(X_1);
  		    Double G = Double.parseDouble(G_1);
  		    Double B = Double.parseDouble(B_1);
  		    
  		    Node node = element.getElementsByTagName("cim:ConductingEquipment.BaseVoltage").item(0);
			Element elementid = (Element) node;
			String RDF_ID = elementid.getAttribute("rdf:resource");
			String Line_basevoltage = RDF_ID.replaceAll("[#]+", "");
  	
  		    for (int j = 0; j < Terminal.getLength(); j++) {
  		 Element elements = (Element) Terminal.item(j);				
 		    	 Node nodes = elements.getElementsByTagName("cim:Terminal.ConductingEquipment").item(0);
 				Element elementids = (Element) nodes;
 				String RDF_IDs = elementids.getAttribute("rdf:resource");
 				String condeq = RDF_IDs.replaceAll("[#]+", "");
 				 Node nodess = elements.getElementsByTagName("cim:Terminal.ConnectivityNode").item(0);
 				Element elementidss = (Element) nodess;
 				String RDF_IDss = elementidss.getAttribute("rdf:resource");
 				String coneq = RDF_IDss.replaceAll("[#]+", "");
 	  		  
 		 	if (Line_rdfid.equals(condeq)) 
 				{
 					LineArrayList.add(coneq); 
 				}
 			}  
  		    
  boolean[] status = {true,true,false,false};
 for (int k = 0; k < Breaker.getLength(); k++) {
			List <String> BreakerArrayList = new ArrayList<String>();
		    Element elementBreaker = (Element) Breaker.item(k);
		    Element elementSSH = (Element) BreakerP.item(k);
			
			boolean Breaker_NC = Boolean.valueOf(elementSSH.getElementsByTagName("cim:Switch.open").item(0).getTextContent());
		    
			String rdfID_Breaker = elementBreaker.getAttribute("rdf:ID");
			BreakerArrayList.add(rdfID_Breaker);
			
			 for (int j = 0; j < Terminal.getLength(); j++) {
				    		
				    Element elements = (Element) Terminal.item(j);				
	 		    	 Node nodes = elements.getElementsByTagName("cim:Terminal.ConductingEquipment").item(0);
	 				Element elementids = (Element) nodes;
	 				String RDF_IDs = elementids.getAttribute("rdf:resource");
	 				String condeq = RDF_IDs.replaceAll("[#]+", "");
	 				 Node nodess = elements.getElementsByTagName("cim:Terminal.ConnectivityNode").item(0);
	 				Element elementidss = (Element) nodess;
	 				String RDF_IDss = elementidss.getAttribute("rdf:resource");
	 				String coneq = RDF_IDss.replaceAll("[#]+", "");
	if (rdfID_Breaker.equals(condeq)) {
						BreakerArrayList.add(coneq);
					}  
			 }
  				if (status[0] == true){
  				if(LineArrayList.get(1).equals(BreakerArrayList.get(1))){
  					status[0] = false;
  					LineArrayList.set(1,BreakerArrayList.get(2));
  					status[2] = Breaker_NC;
  			}else if(LineArrayList.get(1).equals(BreakerArrayList.get(2))){
  					status[0]=false;
  					LineArrayList.set(1,BreakerArrayList.get(1));
  					status[2] = Breaker_NC;
  				}
  				}
  				
  				if (status[1] == true){	
  				if(LineArrayList.get(2).equals(BreakerArrayList.get(1))){
  					status[1] = false;
  					LineArrayList.set(2,BreakerArrayList.get(2));
  					status[3] = Breaker_NC;
  				}else if(LineArrayList.get(2).equals(BreakerArrayList.get(2))){
  					status[1]=false;
  					LineArrayList.set(2,BreakerArrayList.get(1));	
  					status[3] = Breaker_NC;
  				}
  				}
  		    } 
  				if ( status[2] == false && status[3] == false){
  				for (int m = 0; m < Busbar.getLength(); m++){
  					List <String> BusArrayList = new ArrayList<String>();
                      Element elementbus = (Element) Busbar.item(m);
  				    
  					String rdfID_Bus = elementbus.getAttribute("rdf:ID");
  					BusArrayList.add(rdfID_Bus);
  					
  					for (int j = 0; j < Terminal.getLength(); j++) {
  					    
  					  Element elements = (Element) Terminal.item(j);				
  	 		    	 Node nodes = elements.getElementsByTagName("cim:Terminal.ConductingEquipment").item(0);
  	 				Element elementids = (Element) nodes;
  	 				String RDF_IDs = elementids.getAttribute("rdf:resource");
  	 				String condeq = RDF_IDs.replaceAll("[#]+", "");
  	 				 Node nodess = elements.getElementsByTagName("cim:Terminal.ConnectivityNode").item(0);
  	 				Element elementidss = (Element) nodess;
  	 				String RDF_IDss = elementidss.getAttribute("rdf:resource");
  	 				String coneq = RDF_IDss.replaceAll("[#]+", "");
  	 	  				if (rdfID_Bus.equals(condeq)) {
  							BusArrayList.add(coneq); 
  							
  						    }
  					    }  
  					if(LineArrayList.get(1).equals(BusArrayList.get(1))){
  						LineArrayList.set(1,BusArrayList.get(0));
  					}else if(LineArrayList.get(2).equals(BusArrayList.get(1))){
  						LineArrayList.set(2,BusArrayList.get(0));						
  					}
  				} 
  				for (int j = 0; j < Basevoltage.getLength(); j++) {
  					Element elementBase = (Element) Basevoltage.item(j);				
  					String rdfID_BaseVoltage = elementBase.getAttribute("rdf:ID");
  					if (Line_basevoltage.equals(rdfID_BaseVoltage)){
  						Double Line_basevoltageValue = Double.parseDouble(elementBase.getElementsByTagName("cim:BaseVoltage.nominalVoltage").item(0).getTextContent());
  						Z_base = Math.pow(Line_basevoltageValue, 2)/Sbase;
  						R_perunit= R/Z_base;
  						X_perunit= X/Z_base;
  						B_perunit= (B*Z_base)/2.0;
  						G_perunit= (G*Z_base)/2.0;
  						LineArrayList.add(Double.toString(R_perunit));
  						LineArrayList.add(Double.toString(X_perunit));
  						LineArrayList.add(Double.toString(G_perunit));
  						LineArrayList.add(Double.toString(B_perunit));
  						
  					} 
  				}
  				
  				for (int d=0; d <Busbar.getLength(); d++) {
  					if(LineArrayList.get(1).equals(Bus[d])){
  						Complex Z1= new Complex(Double.parseDouble(LineArrayList.get(3)),Double.parseDouble(LineArrayList.get(4))); 
  						Complex BG1= new Complex(Double.parseDouble(LineArrayList.get(5)),Double.parseDouble(LineArrayList.get(6))); 
  						Complex one = new Complex(1.0, 0.0);
  						Complex y1= one.divides(Z1);
  						Complex y11= y1.plus(BG1);
  						Complete_Ybus[d][d]= y11.plus(Complete_Ybus[d][d]); 
  						for(int e=0; e < Busbar.getLength(); e++){
  							if(LineArrayList.get(2).equals(Bus[e])){
  								Complex Z2= new Complex(Double.parseDouble(LineArrayList.get(3)),Double.parseDouble(LineArrayList.get(4)));
  								Complex BG2= new Complex(Double.parseDouble(LineArrayList.get(5)),Double.parseDouble(LineArrayList.get(6)));
  								Complex y2= one.divides(Z2);
  								Complex y22= y2.plus(BG2);
  								Complete_Ybus[e][e]= y22.plus(Complete_Ybus[e][e]);
  								Complex neg= new Complex(-1.0, 0.0);
  								Complex negy= y2.times(neg);
  								Complete_Ybus[d][e]= negy.plus(Complete_Ybus[d][e]); 
  								Complete_Ybus[e][d]= negy.plus(Complete_Ybus[e][d]); 
  							}
  						}
  					}
  				}
 
  				 }  
  		}	
          for (int i = 0; i < PT.getLength(); i++){  
  			
  			List <String> TransArrayList = new ArrayList<String>();
  			
              Element element = (Element) PT.item(i);
  	    
  		    String rdfID_Transformer = element.getAttribute("rdf:ID");
  		    
  		    TransArrayList.add(rdfID_Transformer);
     for (int j = 0; j < Terminal.getLength(); j++) {
  		    	Element elements = (Element) Terminal.item(j);				
		    	 Node nodes = elements.getElementsByTagName("cim:Terminal.ConductingEquipment").item(0);
				Element elementids = (Element) nodes;
				String RDF_IDs = elementids.getAttribute("rdf:resource");
				String condeq = RDF_IDs.replaceAll("[#]+", "");
				 Node nodess = elements.getElementsByTagName("cim:Terminal.ConnectivityNode").item(0);
				Element elementidss = (Element) nodess;
				String RDF_IDss = elementidss.getAttribute("rdf:resource");
				String coneq = RDF_IDss.replaceAll("[#]+", "");
	  		  
  				if (rdfID_Transformer.equals(condeq)) {
  					TransArrayList.add(coneq); 
  				}
  			}  
  		boolean[] status = {true,true,false,false};
  		   
  				for (int k = 0; k < Breaker.getLength(); k++) {
  					List <String> BreakerArrayList = new ArrayList<String>();
  				    Element elementBreaker = (Element) Breaker.item(k);
  				    
  				    Element elementSSH = (Element) BreakerP.item(k);
  					
  					boolean Breaker_NC = Boolean.valueOf(elementSSH.getElementsByTagName("cim:Switch.open").item(0).getTextContent());
  				    
  					String rdfID_Breaker = elementBreaker.getAttribute("rdf:ID");
  					BreakerArrayList.add(rdfID_Breaker);
  					
  					 for (int j = 0; j < Terminal.getLength(); j++) {
  						Element elements = (Element) Terminal.item(j);				
  		 		    	 Node nodes = elements.getElementsByTagName("cim:Terminal.ConductingEquipment").item(0);
  		 				Element elementids = (Element) nodes;
  		 				String RDF_IDs = elementids.getAttribute("rdf:resource");
  		 				String condeq = RDF_IDs.replaceAll("[#]+", "");
  		 				 Node nodess = elements.getElementsByTagName("cim:Terminal.ConnectivityNode").item(0);
  		 				Element elementidss = (Element) nodess;
  		 				String RDF_IDss = elementidss.getAttribute("rdf:resource");
  		 				String coneq = RDF_IDss.replaceAll("[#]+", "");
  		 	  		  
  							
  							
  							if (rdfID_Breaker.equals(condeq)) {
  								BreakerArrayList.add(coneq);
  							    }
  						    }  
  				if (status[0] == true){
  				if(TransArrayList.get(1).equals(BreakerArrayList.get(1))){
  					status[0] = false;
  					TransArrayList.set(1,BreakerArrayList.get(2));
  					status[2] = Breaker_NC;
  				}else if(TransArrayList.get(1).equals(BreakerArrayList.get(2))){
  					status[0]=false;
  					TransArrayList.set(1,BreakerArrayList.get(1));
  					status[2] = Breaker_NC;
  				}
  				}
  				
  				if (status[1] == true){	
  				if(TransArrayList.get(2).equals(BreakerArrayList.get(1))){
  					status[1] = false;
  					TransArrayList.set(2,BreakerArrayList.get(2));
  					status[3] = Breaker_NC;
  				}else if(TransArrayList.get(2).equals(BreakerArrayList.get(2))){
  					status[1]=false;
  					TransArrayList.set(2,BreakerArrayList.get(1));	
  					status[3] = Breaker_NC;
  				}
  				}
  		    } 
  		if ( status[2] == false && status[3] == false){
  				for (int m = 0; m < Busbar.getLength(); m++){
  					List <String> BusArrayList = new ArrayList<String>();
                      Element elementbus = (Element) Busbar.item(m);
  				    
  					String rdfID_Bus = elementbus.getAttribute("rdf:ID");
  					BusArrayList.add(rdfID_Bus);
  					
  					for (int j = 0; j < Terminal.getLength(); j++) {
  						Element elements = (Element) Terminal.item(j);				
  		 		    	 Node nodes = elements.getElementsByTagName("cim:Terminal.ConductingEquipment").item(0);
  		 				Element elementids = (Element) nodes;
  		 				String RDF_IDs = elementids.getAttribute("rdf:resource");
  		 				String condeq = RDF_IDs.replaceAll("[#]+", "");
  		 				 Node nodess = elements.getElementsByTagName("cim:Terminal.ConnectivityNode").item(0);
  		 				Element elementidss = (Element) nodess;
  		 				String RDF_IDss = elementidss.getAttribute("rdf:resource");
  		 				String coneq = RDF_IDss.replaceAll("[#]+", "");
  		 				if (rdfID_Bus.equals(condeq)) {
  							BusArrayList.add(coneq); 
  							
  						    }
  					    } 
  					if(TransArrayList.get(1).equals(BusArrayList.get(1))){
  						TransArrayList.set(1,BusArrayList.get(0));
  					}else if(TransArrayList.get(2).equals(BusArrayList.get(1))){
  						TransArrayList.set(2,BusArrayList.get(0));						
  					}
  				} 
  				for (int j = 0; j < PTE.getLength(); j++) {
  					
  					 Element elementss= (Element) PTE.item(j);
  					 Node nodes = elementss.getElementsByTagName("cim:PowerTransformerEnd.PowerTransformer").item(0);
  	 				Element elementids = (Element) nodes;
  	 				String RDF_IDs = elementids.getAttribute("rdf:resource");
  	 				String condeq = RDF_IDs.replaceAll("[#]+", "");
  					
  					
  					if (condeq.equals(rdfID_Transformer)){
  						Double Transformer_r = Double.parseDouble(elementss.getElementsByTagName("cim:PowerTransformerEnd.r").item(0).getTextContent());
  						if (Transformer_r != 0){
  							Transformer_r = Double.parseDouble(elementss.getElementsByTagName("cim:PowerTransformerEnd.r").item(0).getTextContent());
  							Double Transformer_x = Double.parseDouble(elementss.getElementsByTagName("cim:PowerTransformerEnd.x").item(0).getTextContent());
  							Double Transformer_g = Double.parseDouble(elementss.getElementsByTagName("cim:PowerTransformerEnd.g").item(0).getTextContent());
  							Double Transformer_b = Double.parseDouble(elementss.getElementsByTagName("cim:PowerTransformerEnd.b").item(0).getTextContent());
  							Node node = elementss.getElementsByTagName("cim:TransformerEnd.BaseVoltage").item(0);
  			  				Element elementid = (Element) node;
  			  				String rdf_id = elementid.getAttribute("rdf:resource");
  			  				String rdf_ID = rdf_id.replaceAll("[#]+", "");
  			  	 for (int n = 0; n < Basevoltage.getLength(); n++) {
  					Element elementBase = (Element) Basevoltage.item(n);				
  					String rdfID_BaseVoltage = elementBase.getAttribute("rdf:ID");
  					if (rdfID_BaseVoltage.equals(rdf_ID)){
  						Double Transformer_basevoltageValue = Double.parseDouble(elementBase.getElementsByTagName("cim:BaseVoltage.nominalVoltage").item(0).getTextContent());
  						Z_base = Math.pow(Transformer_basevoltageValue, 2)/Sbase;
  						
  						R_perunit= Transformer_r/Z_base;
  						X_perunit= Transformer_x/Z_base;
  						B_perunit= Transformer_b*Z_base;
  						G_perunit= Transformer_g*Z_base;
  						TransArrayList.add(Double.toString(R_perunit));
  						TransArrayList.add(Double.toString(X_perunit));
  						TransArrayList.add(Double.toString(G_perunit));
  						TransArrayList.add(Double.toString(B_perunit));
  						
  					} 
  				}
  				}	
  				}
  				}
  				
  				
  				
  				for (int d=0; d <Busbar.getLength(); d++) {
  					if(TransArrayList.get(1).equals(Bus[d])){
  						Complex Z1= new Complex(Double.parseDouble(TransArrayList.get(3)),Double.parseDouble(TransArrayList.get(4))); 
  						Complex BG1= new Complex(Double.parseDouble(TransArrayList.get(5)),Double.parseDouble(TransArrayList.get(6))); 
  						Complex one = new Complex(1.0, 0.0);
  						Complex y1= one.divides(Z1);
  						Complex y11= y1.plus(BG1);
  						Complete_Ybus[d][d]= y11.plus(Complete_Ybus[d][d]); 
  						for(int e=0; e < Busbar.getLength(); e++){
  							if(TransArrayList.get(2).equals(Bus[e])){
  								Complex Z2= new Complex(Double.parseDouble(TransArrayList.get(3)),Double.parseDouble(TransArrayList.get(4)));
  								Complex BG2= new Complex(Double.parseDouble(TransArrayList.get(5)),Double.parseDouble(TransArrayList.get(6)));
  								Complex y2= one.divides(Z2);
  								Complex y22= y2.plus(BG2);
  								Complete_Ybus[e][e]= y22.plus(Complete_Ybus[e][e]); 
  								Complex neg= new Complex(-1.0, 0.0);
  								Complex negy= y2.times(neg);
  								Complete_Ybus[d][e]= negy.plus(Complete_Ybus[d][e]); 
  								Complete_Ybus[e][d]= negy.plus(Complete_Ybus[e][d]); 
  							}
  						}
  					}
  				}
  			
  				
  				
  				
  				 } 
  				
  		}	
  		for (int d=0; d <Sc.getLength(); d++) {
  			Element elementShunt = (Element) Sc.item(d);
  			List <String> ShuntArrayList = new ArrayList<String>();
  			Double Shunt_b = Double.parseDouble(elementShunt.getElementsByTagName("cim:LinearShuntCompensator.bPerSection").item(0).getTextContent());
  			Double Shunt_g = Double.parseDouble(elementShunt.getElementsByTagName("cim:LinearShuntCompensator.gPerSection").item(0).getTextContent());
  			
  			
  			Double shunt_basevoltageValue = Double.parseDouble(elementShunt.getElementsByTagName("cim:ShuntCompensator.nomU").item(0).getTextContent());
  			String rdfID_Shunt = elementShunt.getAttribute("rdf:ID");			
  			ShuntArrayList.add(rdfID_Shunt);
  			Node node = elementShunt.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0);
				Element elementid = (Element) node;
				String rdf_id = elementid.getAttribute("rdf:resource");
				String Shunt_EqID = rdf_id.replaceAll("[#]+", "");
				
  			
  			
  			for (int n=0; n < Busbar.getLength(); n++) {
  				Element elementBus = (Element) Busbar.item(n);
  				String rdfID_Bus = elementBus.getAttribute("rdf:ID");
  				Node nodes = elementBus.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0);
  				Element elementids = (Element) nodes;
  				String rdf_ids = elementids.getAttribute("rdf:resource");
  				String rdf_IDs = rdf_ids.replaceAll("[#]+", "");
  				
  				if(Shunt_EqID.equals(rdf_IDs)){
  					ShuntArrayList.add(rdfID_Bus);
  				}
  			}
  			
  			
  			Z_base = Math.pow(shunt_basevoltageValue, 2)/Sbase;
  			B_perunit= Shunt_b*Z_base; 
  			G_perunit= Shunt_g*Z_base; 
  			B_perunit= B_perunit*4; 
  			G_perunit= G_perunit*4; 
  			ShuntArrayList.add(Double.toString(G_perunit));
  			ShuntArrayList.add(Double.toString(B_perunit));
  	
  }
  		System.out.println("\n"+"Complete_Ybus");
 		
 		for (int di=0; di< Busbar.getLength();di++){
 			for(int ej=0; ej< Busbar.getLength(); ej++)
 			{
 				System.out.print(Complete_Ybus[di][ej]+"\t");	
 			}
 			System.out.println("\n");
  		
  		 
      } 
          
         
  		 }
    				
	
    catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();}
        }

public static Connection getConnection() throws Exception{
			  try{
			   String driver = "com.mysql.cj.jdbc.Driver";
			   String url = "jdbc:mysql://localhost:3306/powersystem?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			   Connection conn = DriverManager.getConnection(url,"root","");
			   System.out.println("Connected to Our Power System Database");
			   
			   return conn;
			  } catch(Exception e){System.out.println(e);}
			  
			    return null;
		 }



}

