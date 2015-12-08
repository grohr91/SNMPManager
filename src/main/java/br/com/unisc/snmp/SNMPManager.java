/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.unisc.snmp;

import br.com.unisc.dto.SnmpDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

/**
 *
 * @author Douglas Reinicke
 * @author Guilherme Rohr
 */
public class SNMPManager {

    public static final String READ_COMMUNITY = "public";
    public static final String WRITE_COMMUNITY = "private";
    public static final int mSNMPVersion = 0; // 0 represents SNMP version=1
    public static final String SNMP_PORT = "161";

    // OIDs
    public static final String OID_SYS_DESCR = ".1.3.6.1.2.1.1.1.0";
    public static final String OID_SYS_UPTIME = ".1.3.6.1.2.1.1.3.0";
    public static final String OID_MEMORY_TOTAL = ".1.3.6.1.4.1.2021.4.5.0";
    public static final String OID_MEMORY_FREE = ".1.3.6.1.4.1.2021.4.11.0";

    public static final String OIO_SW_INSTALL = ".1.3.6.1.2.1.25.6.3.1.2";

    public static final String OIO_DISK_TOTAL = ".1.3.6.1.4.1.2021.9.1.6.1";
    public static final String OIO_DISK_USED = ".1.3.6.1.4.1.2021.9.1.8.1";

    public static SnmpDTO loadSNMP(SnmpDTO snmpDTO) {
        try {
            String strIPAddress = snmpDTO.getHostIp();
            SNMPManager objSNMP = new SNMPManager();

            String sysDescr = objSNMP.snmpGet(strIPAddress,
                    READ_COMMUNITY, OID_SYS_DESCR);

            String sysUpTime = objSNMP.snmpGet(strIPAddress,
                    READ_COMMUNITY, OID_SYS_UPTIME);

            String memTotal = objSNMP.snmpGet(strIPAddress,
                    READ_COMMUNITY, OID_MEMORY_TOTAL);

            String memFree = objSNMP.snmpGet(strIPAddress,
                    READ_COMMUNITY, OID_MEMORY_FREE);

            ArrayList<String> sws = objSNMP.doSnmpwalk(strIPAddress,
                    READ_COMMUNITY, OIO_SW_INSTALL);

            String diskTotal = objSNMP.snmpGet(strIPAddress,
                    READ_COMMUNITY,
                    OIO_DISK_TOTAL);

            String diskUsed = objSNMP.snmpGet(strIPAddress,
                    READ_COMMUNITY,
                    OIO_DISK_USED);

            snmpDTO.setSysDescr(sysDescr);
            snmpDTO.setSysUpTime(sysUpTime);
            snmpDTO.setMemTotal(Double.valueOf(memTotal));
            snmpDTO.setMemFree(Double.valueOf(memFree));
            snmpDTO.setSoftwares(sws);
            snmpDTO.setDiskTotal(Double.valueOf(diskTotal));
            snmpDTO.setDiskUsed(Double.valueOf(diskUsed));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return snmpDTO;
    }
    /*
     * The following code valid only SNMP version1. This
     * method is very useful to set a parameter on remote device.
     */

    public void snmpSet(String strAddress, String community, String strOID, int Value) {
        strAddress = strAddress + "/" + SNMP_PORT;
        Address targetAddress = GenericAddress.parse(strAddress);
        Snmp snmp;
        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(5000);
            target.setVersion(SnmpConstants.version2c);

            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(strOID), new Integer32(Value)));
            pdu.setType(PDU.SET);

            ResponseListener listener = new ResponseListener() {
                public void onResponse(ResponseEvent event) {
                    // Always cancel async request when response has been received
                    // otherwise a memory leak is created! Not canceling a request
                    // immediately can be useful when sending a request to a broadcast
                    // address.
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    System.out.println("Set Status is: " + event.getResponse().getErrorStatusText());
                }
            };
            snmp.send(pdu, target, null, listener);
            snmp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * The code is valid only SNMP version1. SnmpGet method
     * return Response for given OID from the Device.
     */

    public String snmpGet(String strAddress, String communityStr, String strOID) {
        String str = "";
        try {
            OctetString community = new OctetString(communityStr);
            strAddress = strAddress + "/" + SNMP_PORT;
            Address targetaddress = new UdpAddress(strAddress);
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(community);
            comtarget.setVersion(SnmpConstants.version1);
            comtarget.setAddress(targetaddress);
            comtarget.setRetries(2);
            comtarget.setTimeout(5000);

            PDU pdu = new PDU();
            ResponseEvent response;
            Snmp snmp;
            pdu.add(new VariableBinding(new OID(strOID)));
            pdu.setType(PDU.GET);
            snmp = new Snmp(transport);

            response = snmp.get(pdu, comtarget);
            if (response != null) {
                if (response.getResponse().getErrorStatusText().
                        equalsIgnoreCase("Success")) {
                    PDU pduresponse = response.getResponse();
                    str = pduresponse.getVariableBindings().firstElement().toString();
                    if (str.contains("=")) {
                        int len = str.indexOf("=");
                        str = str.substring(len + 1, str.length());
                    }
                }
            } else {
                System.out.println("Feeling like a TimeOut occured ");
            }
            snmp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(
                "Response=" + str);
        return str;
    }

    public ArrayList<String> doSnmpwalk(String strAddress, String communityStr, String strOID) throws IOException {
        OctetString community = new OctetString(communityStr);
        strAddress = strAddress + "/" + SNMP_PORT;
        Address targetaddress = new UdpAddress(strAddress);
        TransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();

        // setting up target
        CommunityTarget comtarget = new CommunityTarget();
        comtarget.setCommunity(community);
        comtarget.setVersion(SnmpConstants.version2c);
        comtarget.setAddress(targetaddress);
        comtarget.setRetries(2);
        comtarget.setTimeout(5000);

        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(comtarget, new OID(strOID));
        if (events == null || events.size() == 0) {
            System.out.println("No result returned.");
            return new ArrayList<String>();
        }

        ArrayList<String> results = new ArrayList<String>();
        // Get snmpwalk result.
        for (TreeEvent event : events) {
            if (event != null) {
                if (event.isError()) {
                    System.err.println("oid [" + strOID + "] " + event.getErrorMessage());
                }

                VariableBinding[] varBindings = event.getVariableBindings();
                if (varBindings == null || varBindings.length == 0) {
                    //System.out.println("No result returned.");
                }
                for (VariableBinding varBinding : varBindings) {
                    results.add(varBinding.getVariable().toString());
                }
            }
        }

        snmp.close();

        Collections.sort(results);
        return results;
    }
}
