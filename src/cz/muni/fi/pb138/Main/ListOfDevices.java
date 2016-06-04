package cz.muni.fi.pb138.Main;

import cz.muni.fi.pb138.Devices.Device;
import cz.muni.fi.pb138.Devices.Port;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @version 30.5.2016
 * @author Petr Beran
 * @author Kristýna Leknerová
 * @author Jakub Mičuda
 */
public class ListOfDevices {

    private final List<Device> listOfDevices = new ArrayList<>();

    public List<Device> getListOfDevices() {
        return listOfDevices;
    }
    
    /**
     * Method for exporting listOfDevices to XML.
     */
    public void exportXML() {
        try {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// Root element masterRouter
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("masterRouter");
		doc.appendChild(rootElement);
                
                // Main modem device
                Element mainDevice = doc.createElement("mainDevice");
		rootElement.appendChild(mainDevice);
                
                Attr mainDeviceDid = doc.createAttribute("did");
		mainDeviceDid.setValue("0");
		mainDevice.setAttributeNode(mainDeviceDid);
                
                Attr mainDeviceType = doc.createAttribute("type");
		mainDeviceType.setValue("modem");
		mainDevice.setAttributeNode(mainDeviceType);
                
                Element mainDeviceAddress = doc.createElement("address");
                mainDeviceAddress.appendChild(doc.createTextNode("00:00:00:00:00:00"));
                mainDevice.appendChild(mainDeviceAddress);
                
                Element mainDeviceParentPort = doc.createElement("parentPort");
		mainDevice.appendChild(mainDeviceParentPort);
                
                //Element mainDevicePort = doc.createElement("port");
		//mainDeviceParentPort.appendChild(mainDevicePort);
                
                // Devices
                Element devicesElement = doc.createElement("devices");
		rootElement.appendChild(devicesElement);
                
                for (Device device : listOfDevices) {
                    Element deviceElement = doc.createElement("devices");
                    devicesElement.appendChild(deviceElement);
                    
                    // Did
                    Attr deviceDid = doc.createAttribute("did");
                    deviceDid.setValue(device.getDid().toString());
                    deviceElement.setAttributeNode(deviceDid);
                
                    // Type
                    Attr deviceType = doc.createAttribute("type");
                    deviceType.setValue(getDeviceTypeFromDevice(device));
                    deviceElement.setAttributeNode(deviceType);
                    
                    // TODO: Maximální čáslo portů
                
                    // Address
                    Element deviceAddress = doc.createElement("address");
                    deviceAddress.appendChild(doc.createTextNode(device.getAddress()));
                    deviceElement.appendChild(deviceAddress);
                    
                    // Name
                    if (device.getName() != null) {
                        Element deviceName = doc.createElement("name");
                        deviceName.appendChild(doc.createTextNode(device.getName()));
                        deviceElement.appendChild(deviceName);
                    }
                    
                    Element deviceParentPort = doc.createElement("parentPort");
                    deviceElement.appendChild(deviceParentPort);
                    
                    for (int i = 0; i < device.getArrayOfPorts().size(); i++) {
                        Element devicePort = doc.createElement("port");
                        devicePort.appendChild(doc.createTextNode(device.getName()));
                        
                        Attr portNumber = doc.createAttribute("number");
                        portNumber.setValue(String.valueOf(i));
                        devicePort.setAttributeNode(portNumber);
                        
                        deviceParentPort.appendChild(devicePort);
                    }
                }
                
                // Device types declaration
                Element types = doc.createElement("types");
		rootElement.appendChild(types);
                
                Element typeComputer = doc.createElement("type");
                Attr typeComputerTid = doc.createAttribute("tid");
		typeComputerTid.setValue("computer");
		typeComputer.setAttributeNode(typeComputerTid);
                Element typeComputerName = doc.createElement("name");
                typeComputerName.appendChild(doc.createTextNode("Computer"));
                typeComputer.appendChild(typeComputerName);
		types.appendChild(typeComputer);
                
                Element typeHub = doc.createElement("type");
                Attr typeHubTid = doc.createAttribute("tid");
		typeHubTid.setValue("hub");
		typeHub.setAttributeNode(typeHubTid);
                Element typeHubName = doc.createElement("name");
                typeHubName.appendChild(doc.createTextNode("HUB"));
                typeHub.appendChild(typeHubName);
		types.appendChild(typeHub);
                
                Element typeRouter = doc.createElement("type");
                Attr typeRouterTid = doc.createAttribute("tid");
		typeRouterTid.setValue("router");
		typeRouter.setAttributeNode(typeRouterTid);
                Attr typeRouterMaxPorts = doc.createAttribute("max_ports");
		typeRouterMaxPorts.setValue(String.valueOf(8));
		typeRouter.setAttributeNode(typeRouterMaxPorts);
                Element typeRouterName = doc.createElement("name");
                typeRouterName.appendChild(doc.createTextNode("Router"));
                typeRouter.appendChild(typeRouterName);
		types.appendChild(typeRouter);
                
                Element typeSwitch12 = doc.createElement("type");
                Attr typeSwitch12Tid = doc.createAttribute("tid");
		typeSwitch12Tid.setValue("switch12");
		typeSwitch12.setAttributeNode(typeSwitch12Tid);
                Attr typeSwitch12MaxPorts = doc.createAttribute("max_ports");
		typeSwitch12MaxPorts.setValue(String.valueOf(12));
		typeSwitch12.setAttributeNode(typeSwitch12MaxPorts);
                Element typeSwitch12Name = doc.createElement("name");
                typeSwitch12Name.appendChild(doc.createTextNode("Switch 12"));
                typeSwitch12.appendChild(typeSwitch12Name);
		types.appendChild(typeSwitch12);
                
                Element typeSwitch24 = doc.createElement("type");
                Attr typeSwitch24Tid = doc.createAttribute("tid");
		typeSwitch24Tid.setValue("switch24");
		typeSwitch24.setAttributeNode(typeSwitch24Tid);
                Attr typeSwitch24MaxPorts = doc.createAttribute("max_ports");
		typeSwitch24MaxPorts.setValue(String.valueOf(24));
		typeSwitch24.setAttributeNode(typeSwitch24MaxPorts);
                Element typeSwitch24Name = doc.createElement("name");
                typeSwitch24Name.appendChild(doc.createTextNode("Switch 24"));
                typeSwitch24.appendChild(typeSwitch24Name);
		types.appendChild(typeSwitch24);
                
                Element typeSwitch48 = doc.createElement("type");
                Attr typeSwitch48Tid = doc.createAttribute("tid");
		typeSwitch48Tid.setValue("switch48");
		typeSwitch48.setAttributeNode(typeSwitch48Tid);
                Attr typeSwitch48MaxPorts = doc.createAttribute("max_ports");
		typeSwitch48MaxPorts.setValue(String.valueOf(48));
		typeSwitch48.setAttributeNode(typeSwitch48MaxPorts);
                Element typeSwitch48Name = doc.createElement("name");
                typeSwitch48Name.appendChild(doc.createTextNode("Switch 48"));
                typeSwitch48.appendChild(typeSwitch48Name);
		types.appendChild(typeSwitch48);
                
                // Write the content into XML file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("D:\\Dokumenty\\resultfile.xml"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		System.out.println("File saved!");

                
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ListOfDevices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ListOfDevices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ListOfDevices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String getDeviceTypeFromDevice(Device device) {
        switch (device.getDeviceType()) {
            case COMPUTER:
                return "computer";
            case HUB:
                return "hub";
            case MODEM:
                return "modem";
            case ROUTER:
                return "router";
            case SWITCH12:
                return "switch12";
            case SWITCH24:
                return "switch24";
            case SWITCH48:
                return "switch48";
            default:
                throw new IllegalArgumentException("Device type not recognized");
        }
    }
    
    /**
     * Compares addresses of devices in the port and returns address of the other device.
     * @param port Port containing two different devices.
     * @param thisDevice Device with valid address. 
     * @return 
     */
    private String getAddressOfTheOtherDevice(Port port, Device thisDevice) {
        
        if (thisDevice.getAddress().equals(port.getDeviceA().getAddress())) {
            if (thisDevice.getAddress().equals(port.getDeviceB().getAddress())) {
                throw new IllegalArgumentException("Device has port with itself!");
            }
            else {
                return port.getDeviceB().getAddress();
            }
        }
        else {
            if (thisDevice.getAddress().equals(port.getDeviceB().getAddress())) {
                throw new IllegalArgumentException("Port doesn't contain given device! (comparison by address)");
            }
            else {
                return port.getDeviceA().getAddress();
            }
        }
    }

    /**
     * Method for importing XML into listOfDevices.
     */
    public void importXML() {
        return;
    }

}
