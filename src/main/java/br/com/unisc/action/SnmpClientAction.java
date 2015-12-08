package br.com.unisc.action;

import br.com.unisc.dto.GraphSourceDTO;
import br.com.unisc.dto.SnmpDTO;
import br.com.unisc.snmp.SNMPManager;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 *
 * @author Douglas Reinicke
 * @author Guilherme Rohr
 */
public class SnmpClientAction extends ActionSupport {

    private SnmpDTO snmpDTO;
    private String currentIp, newIp;
    private Integer refreshTime, operation;
    private Date dtStart, dtLastUpdate;
    private List<GraphSourceDTO> graphSourceList;

    public String main() throws Exception {
        try {
            if (operation == null) {
                if (currentIp == null || currentIp.isEmpty()) {
                    currentIp = "192.168.0.239";
                }
                snmpDTO = new SnmpDTO(currentIp);
                snmpDTO = SNMPManager.loadSNMP(snmpDTO);
                refreshTime = 5000;
                dtStart = dtLastUpdate = new Date();
                graphSourceList = new ArrayList<GraphSourceDTO>();
            } else if (operation.equals(0)) {
                refreshDatas();
            } else {
                saveModifications();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SUCCESS;
    }

    /**
     * atualiza dtLastUpdate mantem o snmpDTO.hostIp
     */
    public void refreshDatas() {
        operation = null;
        dtLastUpdate = new Date();
        snmpDTO = new SnmpDTO(currentIp);
        snmpDTO = SNMPManager.loadSNMP(snmpDTO);
        graphSourceList = new ArrayList<GraphSourceDTO>();
    }

    /**
     * atualiza dtLastUpdate; troca o snmpDTO.hostIp e o dtStart
     */
    public void saveModifications() {
        operation = null;
        dtLastUpdate = new Date();
        if (newIp != null && !newIp.isEmpty()) {
            snmpDTO = new SnmpDTO(newIp);
        } else {
            snmpDTO = new SnmpDTO(currentIp);
        }

        snmpDTO = SNMPManager.loadSNMP(snmpDTO);
        graphSourceList = new ArrayList<GraphSourceDTO>();
    }

    public SnmpDTO getSnmpDTO() {
        return snmpDTO;
    }

    public void setSnmpDTO(SnmpDTO snmpDTO) {
        this.snmpDTO = snmpDTO;
    }

    public Date getDtStart() {
        return dtStart;
    }

    public void setDtStart(Date dtStart) {
        this.dtStart = dtStart;
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

    public String getCurrentIp() {
        return currentIp;
    }

    public void setCurrentIp(String currentIp) {
        this.currentIp = currentIp;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public String getNewIp() {
        return newIp;
    }

    public void setNewIp(String newIp) {
        this.newIp = newIp;
    }

}
