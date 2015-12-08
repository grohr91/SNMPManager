package br.com.unisc.action;

import br.com.unisc.dto.GraphSourceDTO;
import br.com.unisc.dto.SnmpDTO;
import br.com.unisc.snmp.SNMPManager;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Douglas Reinicke
 * @author Guilherme Rohr
 */
public class GraphAction extends ActionSupport {

    private SnmpDTO snmpDTO;
    private Integer refreshTime;
    private Date dtLastUpdate;
    private List<GraphSourceDTO> graphSourceList;
    private String ip;

    public String loadMemGraph() throws Exception {
        try {
            snmpDTO = new SnmpDTO(ip);
            snmpDTO = SNMPManager.loadSNMP(snmpDTO);

            dtLastUpdate = new Date();

            graphSourceList = new ArrayList<GraphSourceDTO>();
            GraphSourceDTO g1 = new GraphSourceDTO("Used", snmpDTO.getMemUsed());
            GraphSourceDTO g2 = new GraphSourceDTO("Free", snmpDTO.getMemFree());
            graphSourceList.add(g2);
            graphSourceList.add(g1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SUCCESS;
    }

    public String loadDiskGraph() throws Exception {
        try {
            snmpDTO = new SnmpDTO(ip);
            snmpDTO = SNMPManager.loadSNMP(snmpDTO);

            dtLastUpdate = new Date();
            graphSourceList = new ArrayList<GraphSourceDTO>();
            GraphSourceDTO g1 = new GraphSourceDTO("Used", snmpDTO.getDiskUsed());
            GraphSourceDTO g2 = new GraphSourceDTO("Free", snmpDTO.getDiskFree());
            graphSourceList.add(g2);
            graphSourceList.add(g1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SUCCESS;
    }

    public SnmpDTO getSnmpDTO() {
        return snmpDTO;
    }

    public void setSnmpDTO(SnmpDTO snmpDTO) {
        this.snmpDTO = snmpDTO;
    }

    public List<GraphSourceDTO> getGraphSourceList() {
        return graphSourceList;
    }

    public void setGraphSourceList(List<GraphSourceDTO> graphSourceList) {
        this.graphSourceList = graphSourceList;
    }

    public Integer getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Integer refreshTime) {
        this.refreshTime = refreshTime;
    }

    public Date getDtLastUpdate() {
        return dtLastUpdate;
    }

    public void setDtLastUpdate(Date dtLastUpdate) {
        this.dtLastUpdate = dtLastUpdate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
